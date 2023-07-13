package com.annie.slidetounlock

import android.widget.ImageView

class UnlockPresenter (
    val mView:IUnlockView,
    val mDotViewList:List<ImageView>,
    val mImageViewModelsList:List<ImageViewModel>,
    val mLineTag:List<Int>
    ) {
    private lateinit var user: User

    fun loadUserinfo(){

        //..
        if (user.isLogin){
            mView.showAlertText("请输入密码")
        }else{
            mView.showAlertText("请设置密码")
        }

    }


    fun userTouch(x:Float,y:Float){

    }


    fun endTouch(){

    }
}