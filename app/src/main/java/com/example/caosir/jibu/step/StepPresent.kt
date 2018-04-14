package com.example.caosir.jibu.step

import com.example.caosir.jibu.base.BasePresenter
import com.example.caosir.jibu.base.BaseUI
import com.example.caosir.jibu.base.StepMode
import com.example.caosir.jibu.config.Constant
import com.example.caosir.jibu.pojo.Exercise
import com.example.caosir.jibu.utils.DbUtils
import com.wise.common.baserx.RxJavaDemoUtil
import com.wise.common.commonutils.LogUtil
import com.wise.common.commonutils.TimeUtil
import rx.Subscriber

import java.util.ArrayList

/**
 * 创建人: caosir
 * 创建时间：2018/3/15
 * 修改人：
 * 修改时间：
 * 类说明：
 */

class StepPresent : BasePresenter<StepPresent.StepUI>() {

    fun get7Day() {
        val date = ArrayList<Int>()
        for (i in 0..6) {
            DbUtils.createDb(activity, DbUtils.DB_NAME)
            val list = DbUtils.getQueryByWhere(Exercise::class.java, "date", arrayOf(TimeUtil.getOldDate(-6 + i)))
            LogUtil.d("數據庫" + list.size)
            if (list.size == 0 || list.isEmpty()) {
                date.add(0)
            } else {
                //2018-04-08
                date.add(list[0].step)
            }

        }
        ui.show7Day(date)
        ui.showToday(date[6])

    }


    fun getStep(){
        RxJavaDemoUtil.whlieTime(object : Subscriber<Long>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {

            }

            override fun onNext(aLong: Long?) {
                val list = DbUtils.getQueryByWhere(Exercise::class.java, "date", arrayOf(TimeUtil.getOldDate(0)))
                if (list.size == 0 || list.isEmpty()) {
                    ui.showToday(0)
                } else {
                    //2018-04-08
                    ui.showToday(list[0].step)
                }
            }
        }, 500)
    }


    fun getMostLast() {
        val list = DbUtils.getQueryByTop(Exercise::class.java, "step")
        if (list.size == 0) {
            ui.showLast(0)
            ui.showMost(0)
        } else {
            ui.showLast(list[0].step)
            ui.showMost(list[list.size - 1].step)
        }

    }

    interface StepUI : BaseUI {
        fun show7Day(date: List<*>)
        fun showToday(date: Int)
        fun showLast(date: Int)
        fun showMost(date: Int)
    }
}
