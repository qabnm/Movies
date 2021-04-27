package com.duoduovv.common.download;

import android.app.Notification;
import android.content.Context;

import androidx.annotation.Nullable;

import com.duoduovv.common.R;
import com.google.android.exoplayer2.offline.Download;
import com.google.android.exoplayer2.offline.DownloadManager;
import com.google.android.exoplayer2.offline.DownloadService;
import com.google.android.exoplayer2.scheduler.PlatformScheduler;
import com.google.android.exoplayer2.scheduler.Scheduler;
import com.google.android.exoplayer2.ui.DownloadNotificationHelper;
import com.google.android.exoplayer2.util.NotificationUtil;
import com.google.android.exoplayer2.util.Util;

import java.util.List;

import static com.duoduovv.common.download.DownloadManagerHelper.DOWNLOAD_NOTIFICATION_CHANNEL_ID;

/**
 * @author: jun.liu
 * @date: 2021/4/23 9:59
 * @des:
 */
public class ExoDownloadService extends DownloadService {
    private static final int JOB_ID = 1;
    private static final int FOREGROUND_NOTIFICATION_ID = 1;

    protected ExoDownloadService() {
        super(FOREGROUND_NOTIFICATION_ID,
                DEFAULT_FOREGROUND_NOTIFICATION_UPDATE_INTERVAL,
                DOWNLOAD_NOTIFICATION_CHANNEL_ID,
                R.string.exo_download,
                0);
    }

    @Override
    protected DownloadManager getDownloadManager() {
        DownloadManager downloadManager = DownloadManagerHelper.getDownloadManager(this);
        DownloadNotificationHelper downloadNotificationHelper = DownloadManagerHelper.getDownloadNotificationHelper(this);
        downloadManager.addListener(new TerminalStateNotificationHelper(
                this,
                downloadNotificationHelper,
                FOREGROUND_NOTIFICATION_ID + 1));
        return downloadManager;
    }

    @Nullable
    @Override
    protected Scheduler getScheduler() {
        return new PlatformScheduler(this, JOB_ID);
    }

    @Override
    protected Notification getForegroundNotification(List<Download> downloads) {
        return DownloadManagerHelper.getDownloadNotificationHelper(this).buildProgressNotification(
                /* context= */ this,
                R.drawable.ic_download,
                /* contentIntent= */ null,
                /* message= */ null,
                downloads);
    }

    /**
     * Creates and displays notifications for downloads when they complete or fail.
     *
     * <p>This helper will outlive the lifespan of a single instance of {@link ExoDownloadService}.
     * It is static to avoid leaking the first {@link ExoDownloadService} instance.
     */
    private static final class TerminalStateNotificationHelper implements DownloadManager.Listener {

        private final Context context;
        private final DownloadNotificationHelper notificationHelper;

        private int nextNotificationId;

        public TerminalStateNotificationHelper(Context context, DownloadNotificationHelper notificationHelper, int firstNotificationId) {
            this.context = context.getApplicationContext();
            this.notificationHelper = notificationHelper;
            nextNotificationId = firstNotificationId;
        }

        @Override
        public void onDownloadChanged(DownloadManager downloadManager, Download download, @Nullable Exception finalException) {
            Notification notification;
            if (download.state == Download.STATE_COMPLETED) {
                notification = notificationHelper.buildDownloadCompletedNotification(
                        context,
                        R.drawable.ic_download_done,
                        /* contentIntent= */ null,
                        Util.fromUtf8Bytes(download.request.data));
            } else if (download.state == Download.STATE_FAILED) {
                notification = notificationHelper.buildDownloadFailedNotification(
                        context,
                        R.drawable.ic_download_done,
                        /* contentIntent= */ null,
                        Util.fromUtf8Bytes(download.request.data));
            } else {
                return;
            }
            NotificationUtil.setNotification(context, nextNotificationId++, notification);
        }
    }
}
