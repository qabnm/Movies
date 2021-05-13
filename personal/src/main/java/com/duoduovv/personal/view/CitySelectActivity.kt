package com.duoduovv.personal.view

import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.duoduovv.common.util.FileUtils
import com.duoduovv.common.util.RouterPath
import com.duoduovv.common.util.SharedPreferencesHelper
import com.duoduovv.personal.R
import com.duoduovv.personal.adapter.AreaAdapter
import com.duoduovv.personal.adapter.CityAdapter
import com.duoduovv.personal.adapter.ProvinceAdapter
import com.duoduovv.personal.bean.CityBean
import com.duoduovv.personal.databinding.ActivityCitySelectBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dc.android.bridge.BridgeContext.Companion.AREA
import dc.android.bridge.BridgeContext.Companion.CITY
import dc.android.bridge.BridgeContext.Companion.PROVINCE
import dc.android.bridge.util.AndroidUtils
import dc.android.bridge.view.BridgeActivity

/**
 * @author: jun.liu
 * @date: 2021/4/26 11:27
 * @des:选择城市功能
 */
@Route(path = RouterPath.PATH_CITY_SELECT)
class CitySelectActivity : BridgeActivity() {
    override fun getLayoutId() = R.layout.activity_city_select
    private lateinit var mBind: ActivityCitySelectBinding
    private var provinceAdapter: ProvinceAdapter? = null
    private var cityAdapter: CityAdapter? = null
    private var areaAdapter: AreaAdapter? = null
    private var province = ""
    private var city = ""
    private var area = ""
    override fun initView() {
        mBind = ActivityCitySelectBinding.bind(layoutView)
        mBind.rvProvince.layoutManager = LinearLayoutManager(this)
        mBind.rvCity.layoutManager = LinearLayoutManager(this)
        mBind.rvArea.layoutManager = LinearLayoutManager(this)
        provinceAdapter = ProvinceAdapter()
        cityAdapter = CityAdapter()
        areaAdapter = AreaAdapter()
        mBind.rvProvince.adapter = provinceAdapter
        mBind.rvCity.adapter = cityAdapter
        mBind.rvArea.adapter = areaAdapter
        mBind.tvClear.setOnClickListener {
            SharedPreferencesHelper.helper.remove(PROVINCE)
            SharedPreferencesHelper.helper.remove(CITY)
            SharedPreferencesHelper.helper.remove(AREA)
            AndroidUtils.toast("默认位置清除成功,请重新进入", this)
        }
        mBind.tvSure.setOnClickListener {
            SharedPreferencesHelper.helper.setValue(PROVINCE, province)
            SharedPreferencesHelper.helper.setValue(CITY, city)
            SharedPreferencesHelper.helper.setValue(AREA, area)
            AndroidUtils.toast("设置成功,请重新进入", this)
        }
    }

    override fun initData() {
        val jsonResult = FileUtils.getFromAssets("city.json", this)
        val gson = Gson()
        val dataList: CityBean = gson.fromJson(jsonResult, object : TypeToken<CityBean>() {}.type)
        Log.d("city", "$dataList")
        provinceAdapter?.setList(dataList)
        provinceAdapter?.setOnItemClickListener { _, _, position ->
            val cityList = dataList[position].city
            province = dataList[position].name
            mBind.tvSelect.text = province
            cityAdapter?.setList(cityList)
        }
        cityAdapter?.setOnItemClickListener { adapter, _, position ->
            val areaList = (adapter as CityAdapter).data[position].area
            city = adapter.data[position].name
            areaAdapter?.setList(areaList)
            mBind.tvSelect.text = "$province$city"
        }

        areaAdapter?.setOnItemClickListener { adapter, _, position ->
            area = (adapter as AreaAdapter).data[position]
            mBind.tvSelect.text = "$province$city$area"
        }
    }

}