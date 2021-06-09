package com.duoduovv.common.download;

import android.content.Context;
import android.net.Uri;
import android.os.Build;

import com.bumptech.glide.load.resource.bitmap.Downsampler;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerLibraryInfo;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.database.DatabaseProvider;
import com.google.android.exoplayer2.database.ExoDatabaseProvider;
import com.google.android.exoplayer2.offline.DefaultDownloadIndex;
import com.google.android.exoplayer2.offline.Download;
import com.google.android.exoplayer2.offline.DownloadManager;
import com.google.android.exoplayer2.offline.DownloadRequest;
import com.google.android.exoplayer2.offline.DownloadService;
import com.google.android.exoplayer2.scheduler.Requirements;
import com.google.android.exoplayer2.ui.DownloadNotificationHelper;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.upstream.cache.Cache;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
import com.google.android.exoplayer2.upstream.cache.NoOpCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;

import org.checkerframework.checker.nullness.qual.MonotonicNonNull;

import java.io.File;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.concurrent.Executors;

/**
 * @author: jun.liu
 * @date: 2021/4/22 18:19
 * @des: 获取downloadManager对象
 * 可以参考官方文档：https://exoplayer.dev/downloading-media.html
 */
public class DownloadManagerHelper {
    private static final boolean USE_CRONET_FOR_NETWORKING = true;
    public static final String DOWNLOAD_NOTIFICATION_CHANNEL_ID = "download_channel";
    private static final String USER_AGENT =
            "ExoPlayerDemo/"
                    + ExoPlayerLibraryInfo.VERSION
                    + " (Linux; Android "
                    + Build.VERSION.RELEASE
                    + ") "
                    + ExoPlayerLibraryInfo.VERSION_SLASHY;
    private static @MonotonicNonNull DownloadManager downloadManager;
    private static @MonotonicNonNull DatabaseProvider databaseProvider;
    private static @MonotonicNonNull Cache downloadCache;
    private static @MonotonicNonNull File downloadDirectory;
    private static DataSource.@MonotonicNonNull Factory dataSourceFactory;
    private static HttpDataSource.@MonotonicNonNull Factory httpDataSourceFactory;
    private static @MonotonicNonNull DownloadNotificationHelper downloadNotificationHelper;

    public static RenderersFactory buildRenderersFactory(Context context, boolean preferExtensionRenderer) {
        @DefaultRenderersFactory.ExtensionRendererMode
        int extensionRendererMode = DefaultRenderersFactory.EXTENSION_RENDERER_MODE_OFF;
        return new DefaultRenderersFactory(context.getApplicationContext()).setExtensionRendererMode(extensionRendererMode);
    }

    /**
     * 获取downloadManager对象
     *
     * @return DownloadManager
     */
    public static synchronized DownloadManager getDownloadManager(Context context) {
        ensureDownloadManagerInitialized(context);
        return downloadManager;
    }

    private static synchronized void ensureDownloadManagerInitialized(Context context) {
        if (downloadManager == null) {
            downloadManager =
                    new DownloadManager(
                            context,
                            getDatabaseProvider(context),
                            getDownloadCache(context),
                            getHttpDataSourceFactory(context),
                            Executors.newFixedThreadPool(/* nThreads= */ 6));
            downloadManager.setMaxParallelDownloads(3);
            downloadManager.setRequirements(DownloadManager.DEFAULT_REQUIREMENTS);
//            downloadTracker =
//                    new DownloadTracker(context, getHttpDataSourceFactory(context), downloadManager);
        }
    }

    public static void setMaxParallelDownloads(int maxParallelDownloads) {
        if (downloadManager != null) {
            downloadManager.setMaxParallelDownloads(maxParallelDownloads);
        }
    }

    private static synchronized DatabaseProvider getDatabaseProvider(Context context) {
        if (databaseProvider == null) {
            databaseProvider = new ExoDatabaseProvider(context);
        }
        return databaseProvider;
    }

    private static synchronized Cache getDownloadCache(Context context) {
        if (downloadCache == null) {
            File downloadContentDirectory = new File(getDownloadDirectory(context), "downloads");
            downloadCache = new SimpleCache(
                    downloadContentDirectory, new NoOpCacheEvictor(), getDatabaseProvider(context));
        }
        return downloadCache;
    }

    private static synchronized File getDownloadDirectory(Context context) {
        if (downloadDirectory == null) {
            downloadDirectory = context.getExternalFilesDir(/* type= */ null);
            if (downloadDirectory == null) {
                downloadDirectory = context.getFilesDir();
            }
        }
        return downloadDirectory;
    }

    /**
     * Returns a {@link DataSource.Factory}.
     */
    public static synchronized DataSource.Factory getDataSourceFactory(Context context) {
        if (dataSourceFactory == null) {
            context = context.getApplicationContext();
            DefaultDataSourceFactory upstreamFactory =
                    new DefaultDataSourceFactory(context, getHttpDataSourceFactory(context));
            dataSourceFactory = buildReadOnlyCacheDataSource(upstreamFactory, getDownloadCache(context));
        }
        return dataSourceFactory;
    }

    public static synchronized HttpDataSource.Factory getHttpDataSourceFactory(Context context) {
        if (httpDataSourceFactory == null) {
            if (USE_CRONET_FOR_NETWORKING) {
                context = context.getApplicationContext();
                CronetEngineWrapper cronetEngineWrapper =
                        new CronetEngineWrapper(context, USER_AGENT, /* preferGMSCoreCronet= */ false);
                httpDataSourceFactory =
                        new CronetDataSource.Factory(cronetEngineWrapper, Executors.newSingleThreadExecutor());
            } else {
                CookieManager cookieManager = new CookieManager();
                cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER);
                CookieHandler.setDefault(cookieManager);
                httpDataSourceFactory = new DefaultHttpDataSource.Factory().setUserAgent(USER_AGENT);
            }
        }
        return httpDataSourceFactory;
    }

    private static CacheDataSource.Factory buildReadOnlyCacheDataSource(DataSource.Factory upstreamFactory, Cache cache) {
        return new CacheDataSource.Factory()
                .setCache(cache)
                .setUpstreamDataSourceFactory(upstreamFactory)
                .setCacheWriteDataSinkFactory(null)
                .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR);
    }

    public static synchronized DownloadNotificationHelper getDownloadNotificationHelper(
            Context context) {
        if (downloadNotificationHelper == null) {
            downloadNotificationHelper =
                    new DownloadNotificationHelper(context, DOWNLOAD_NOTIFICATION_CHANNEL_ID);
        }
        return downloadNotificationHelper;
    }


    /**
     * 恢复所有的下载任务
     *
     * @param context
     */
    public static void resumeDownload(Context context) {
        DownloadService.sendResumeDownloads(context, ExoDownloadService.class, false);
    }

    /**
     * 暂停所有的下载任务
     *
     * @param context
     */
    public static void pauseDownload(Context context) {
        DownloadService.sendPauseDownloads(context, ExoDownloadService.class, false);
    }

    /**
     * Clear the stop reason for a single download
     *
     * @param context   Context
     * @param contentId String
     */
    public static void clearStopReason(Context context, String contentId) {
        DownloadService.sendSetStopReason(context, ExoDownloadService.class, contentId, Download.STOP_REASON_NONE, false);
    }

    /**
     * Set the stop reason for a single download
     * Setting a stop reason does not remove a download. The partial download will be retained,
     * and clearing the stop reason will cause the download to continue.
     *
     * @param context
     * @param stopReason
     * @param contentId
     */
    public static void setStopReason(Context context, int stopReason, String contentId) {
        DownloadService.sendSetStopReason(context, ExoDownloadService.class, contentId, stopReason, false);
    }

    /**
     * 移除所有下载任务
     *
     * @param context
     */
    public static void removeAllDownloads(Context context) {
        DownloadService.sendRemoveAllDownloads(context, ExoDownloadService.class, false);
    }

    /**
     * 移除下载任务
     *
     * @param context
     * @param contentId
     */
    public static void removeDownload(Context context, String contentId) {
        DownloadService.sendRemoveDownload(context, ExoDownloadService.class, contentId, false);
    }

    /**
     * 添加一个下载任务
     *
     * @param contentId
     * @param contentUri
     */
    public static void addDownload(Context context, String contentId, Uri contentUri) {
        DownloadRequest downloadRequest = new DownloadRequest.Builder(contentId, contentUri).build();
        DownloadService.sendAddDownload(context, ExoDownloadService.class, downloadRequest, false);
    }
}
