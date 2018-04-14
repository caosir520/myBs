package com.example.caosir.jibu.history

import com.example.caosir.jibu.base.BasePresenter
import com.example.caosir.jibu.base.BaseUI
import com.example.caosir.jibu.pojo.Exercise
import com.example.caosir.jibu.utils.DbUtils
import com.wise.common.commonutils.LogUtil
import com.wise.common.commonutils.TimeUtil

/**
 * Created by Administrator on 2017/11/27/027.
 */

class HistoryActivityPersent : BasePresenter<HistoryActivityPersent.Ui>() {


    fun stringDay2Point(day: String): String {
        return day.replace("-", ".")
    }

    fun stringDay2Heng(day: String): String {
        return day.replace(".", "-")
    }

    fun getDayDate(day: String) {
        LogUtil.d("" + day)
        val list = DbUtils.getQueryByWhere(Exercise::class.java, "date", arrayOf(day))
        LogUtil.d("數據庫" + list.size)
        if (list.size == 0 || list.isEmpty()) {
            ui.showDay(Exercise())
        } else {
            //2018-04-08
            ui.showDay(list[0])
        }
    }

    interface Ui : BaseUI {
        fun showDay(exercise: Exercise)

    }

}
