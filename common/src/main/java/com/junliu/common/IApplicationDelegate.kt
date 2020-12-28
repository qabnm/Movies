package com.junliu.common

/**
 * @author: jun.liu
 * @date: 2020/10/29 17:31
 * @des: 代理接口回调类 用于业务组件实现该接口
 */
interface IApplicationDelegate {
    fun onCreate()

    fun onTerminate()

    fun onLowMemory()

    fun onTrimMemory(level: Int)
}