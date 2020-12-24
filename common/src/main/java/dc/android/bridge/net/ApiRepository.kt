package dc.android.bridge.net

/**
 * @author: jun.liu
 * @date: 2020/9/27 : 10:58
 * 该类下放到实际的项目中去
 */
open class ApiRepository : BaseRepository() {
    protected val apiService: IApiService by lazy {
        RetrofitFactory.instance.createRetrofit(IApiService::class.java)
    }
}