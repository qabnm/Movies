package com.duoduovv.tent

import android.util.Log
import com.tencent.open.utils.HttpUtils
import com.tencent.tauth.IRequestListener
import org.apache.http.conn.ConnectTimeoutException
import org.json.JSONException
import java.io.IOException
import java.lang.Exception
import java.net.MalformedURLException
import java.net.SocketTimeoutException

/**
 * @author: jun.liu
 * @date: 2021/2/18 14:14
 * @des:
 */
abstract class TentUserInfoListener:IRequestListener {
    override fun onIOException(p0: IOException?) {
        Log.i("tent","onIOException${p0?.toString()}")
    }

    override fun onMalformedURLException(p0: MalformedURLException?) {
        Log.i("tent","onMalformedURLException${p0?.toString()}")
    }

    override fun onJSONException(p0: JSONException?) {
        Log.i("tent","onJSONException${p0?.toString()}")
    }

    override fun onConnectTimeoutException(p0: ConnectTimeoutException?) {
        Log.i("tent","onConnectTimeoutException${p0?.toString()}")
    }

    override fun onSocketTimeoutException(p0: SocketTimeoutException?) {
        Log.i("tent","onSocketTimeoutException${p0?.toString()}")
    }

    override fun onNetworkUnavailableException(p0: HttpUtils.NetworkUnavailableException?) {
        Log.i("tent","onNetworkUnavailableException${p0?.toString()}")
    }

    override fun onHttpStatusException(p0: HttpUtils.HttpStatusException?) {
        Log.i("tent","onHttpStatusException${p0?.toString()}")
    }

    override fun onUnknowException(p0: Exception?) {
        Log.i("tent","onUnknowException${p0?.toString()}")
    }
}