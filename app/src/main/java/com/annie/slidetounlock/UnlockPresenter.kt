package com.annie.slidetounlock

import android.view.View
import android.widget.ImageView

class UnlockPresenter (
    private val mView:IUnlockView,
    private val mDotViewList:List<ImageView>,
    private val mImageViewModelsList:List<ImageViewModel>,
    private val mLineTag:List<Int>
    ) {
    private lateinit var user: User
    private var lastSelectedDotView:ImageView? = null


    //加载用户信息
    fun loadUserinfo(){
        user = User("zhangsan","",false)
        if (user.isLogin){ //登录
            mView.showAlertText("请绘制密码图案")
        }else{  //没有登录
            mView.showAlertText("请设置密码图案")
        }

    }


    fun userTouch(x:Float,y:Float){
        //判断触摸点是否在某个区域内部 如果没有提前return 即提前结束
        val dotView = isInView(x,y) ?: return //不为空则赋值给dotView 否则提前return
        //判断是否为第一个触摸点
        if (lastSelectedDotView == null){
            //点亮点 记录密码
            highlightView(dotView)
        }


    }


    fun endTouch(){

    }

    //点亮
    fun highlightView(view: ImageView){
        view.visibility = View.VISIBLE
    }


    //判断是否在某一个View里 如果在就得到这个View并对它进行操作
    private fun isInView(x:Float,y:Float):ImageView?{
       // for (dotView in mDotViewList)
        mDotViewList.forEach{
            if ((x>=it.x && x<=it.x+it.width) && (y>=it.y && y<it.y+it.height)){
                return it
            }
        }
        return null
    }


}