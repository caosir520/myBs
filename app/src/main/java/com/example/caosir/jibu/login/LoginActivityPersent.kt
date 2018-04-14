package com.example.caosir.jibu.login


import com.example.caosir.jibu.base.BasePresenter
import com.example.caosir.jibu.base.BaseUI
import com.example.caosir.jibu.pojo.User
import com.example.caosir.jibu.utils.DbUtils

/**
 * Created by Administrator on 2017/11/27/027.
 */

class LoginActivityPersent : BasePresenter<LoginActivityPersent.Ui>() {

    fun login(name: String, passWord: String) {
        val user = DbUtils.getQueryByWhere(User::class.java, "name", arrayOf(name))
        if (user.isEmpty()) {
            ui.loginError("用户不存在")
        } else if (user[0].password == passWord) {
            ui.loginSuccess()
            DbUtils.name = name
        } else {
            ui.loginError("用户密码错误")
        }
    }

    fun zhuc(name: String, passWord: String) {
        val user = User()
        user.name = name
        user.password = passWord
        DbUtils.insert(user)
        ui.zhucSuccess()
    }


    interface Ui : BaseUI {
        fun loginError(error: String)
        fun loginSuccess()
        fun zhucError(error: String)
        fun zhucSuccess()
    }

}
