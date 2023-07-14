package com.annie.slidetounlock

import android.widget.ImageView

class UnlockPresenter (
    val mView:IUnlockView,
    val mDotViewList:List<ImageView>,
    val mImageViewModelsList:List<ImageViewModel>,
    val mLineTag:List<Int>
    ) {
    private lateinit var user: User

    //加载用户信息
    fun loadUserinfo(){
        user = User("zhangsan","",false)
        if (user.isLogin){
            mView.showAlertText("请设置密码图案")
        }else{
            mView.showAlertText("请绘制密码图案")
        }

    }


    fun userTouch(x:Float,y:Float){

    }


    fun endTouch(){

    }
}