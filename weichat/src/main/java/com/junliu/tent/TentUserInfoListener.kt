package com.junliu.tent

import com.tencent.open.utils.HttpUtils
import com.tencent.tauth.IRequestListener
import org.apache.http.conn.ConnectTimeoutException
import org.json.JSONException
import org.json.JSONObject
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
    }

    override fun onMalformedURLException(p0: MalformedURLException?) {
    }

    override fun onJSONException(p0: JSONException?) {
    }

    override fun onConnectTimeoutException(p0: ConnectTimeoutException?) {
    }

    override fun onSocketTimeoutException(p0: SocketTimeoutException?) {
    }

    override fun onNetworkUnavailableException(p0: HttpUtils.NetworkUnavailableException?) {
    }

    override fun onHttpStatusException(p0: HttpUtils.HttpStatusException?) {
    }

    override fun onUnknowException(p0: Exception?) {
    }
}