package com.annie.slidetounlock

import android.view.View
import android.widget.ImageView

class UnlockPresenter (
    private val mView:IUnlockView, //可以找到View的接口
    private val mDotViewList:List<ImageView>,//存储九个点
    private val mImageViewModelsList:List<ImageViewModel>,//存九个点和线 都在里面 即拥有所有的视图控件
    private val mLineTagList:List<Int>//线的tag值
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
            highlightDotView(dotView)
        }else{
        //不是第一个点 判断路径是否可达 另起一方法单独判断是否有路径
            val lineView = hasLine(dotView)?:return
            //点亮第二个点 和他们之间的线
            highlightDotView(dotView)
            highlightLineView(lineView)


        }


    }


    fun endTouch(){

    }

    /**
     * 判断是否有路径
     * 参数为传入的第二个点
     * 有就返回线的 tag值
     * 且不一定有 ？
     */
    private fun hasLine(view: ImageView):ImageView?{
        //获取两个点的tag值
        val last = (lastSelectedDotView!!.tag as String).toInt()
        val current = (view.tag as String).toInt()
        //获取线条的tag值
        val lineTag = if (last < current) last * 10 + current else current * 10 + last
        //首先要确定有这条线
        if (mLineTagList.contains(lineTag)){
            //如果有这个就找到对应的视图 通过tag值找线 另起一个方法
            return findLineWithTag(lineTag)
        }else{
            return null //没有就返回空

        }
    }

    /**
     * 通过tag值找线
     */
    private fun findLineWithTag(lineTag:Int):ImageView?{
        mImageViewModelsList.forEach {
            if (it.view.tag == "$lineTag"){
                return it.view
            }
        }
        return null
    }


    //点亮
    private fun highlightDotView(view: ImageView){
        view.visibility = View.VISIBLE
        lastSelectedDotView = view
    }
    private fun highlightLineView(view: ImageView){
        view.visibility = View.VISIBLE
    }


    //判断是否在某一个View（点）里 如果在就得到这个View并对它进行操作
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