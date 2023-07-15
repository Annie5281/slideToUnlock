package com.annie.slidetounlock

import android.os.Handler
import android.provider.ContactsContract.CommonDataKinds.Im
import android.view.View
import android.widget.ImageView
import javax.net.ssl.SSLEngineResult.Status

class UnlockPresenter (
    private val mView:IUnlockView, //可以找到View的接口
    private val mDotViewList:List<ImageView>,//存储九个点
    private val mImageViewModelsList:List<ImageViewModel>,//存九个点和线 都在里面 即拥有所有的视图控件
    private val mLineTagList:List<Int>//线的tag值
    ) {
    private lateinit var user: User
    private var lastSelectedDotView:ImageView? = null
    private val passwordBuilder= StringBuilder()
    private var firstPassword = "" //用于记录用户第一次登录时设置的密码
    private val selectedViewArray = arrayListOf<ImageView>()//所有点亮的视图


    //加载用户信息
    fun loadUserinfo(){
        user = User("zhangsan","123",true)
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



    //一抬手 ①滑动结束之后记录密码 判断密码 ②清空图案
    fun endTouch(){
        if (user.isLogin){ //true已经登录过了
            if (user.password == passwordBuilder.toString()){
                mView.showAlertText("解锁成功")
                mView.switchActivity()
            }else{
                mView.showAlertText("解锁失败 请重新绘制密码图案")
                changeImageWithStatus(ImageStatus.STATUS_ERROR)
            }
            clear()
        }else{//isLogin = false 第一次登录
            if (firstPassword.isEmpty()){ //第一次设置的密码
                firstPassword = passwordBuilder.toString()
                mView.showAlertText("请确认密码图案")
                lastSelectedDotView = null //为第二次密码输入做准备
                passwordBuilder.clear()
            }else{//第二次确认密码
                if (firstPassword == passwordBuilder.toString()){
                    mView.showAlertText("密码设置成功")
                    user.password = firstPassword
                    user.isLogin = true
                    mView.switchActivity()
                    lastSelectedDotView = null
                    passwordBuilder.clear()
                }else{ //密码两次不同 设置密码失败
                    mView.showAlertText("两次密码不一致 请重新设置")
                    //清除之前设置的
                    clear()
                    changeImageWithStatus(ImageStatus.STATUS_ERROR)
                }
            }
        }
        hideView()
    }

    private fun changeImageWithStatus(status: Int){
        selectedViewArray.forEach {
            mImageViewModelsList.forEach { model->
                if (model.view == it){
                    if (status == ImageStatus.STATUS_ERROR){
                        model.view.setImageResource(model.errorResId)
                    }else{
                        model.view.setImageResource(model.normalResId)
                    }
                }
            }
        }
    }

    //②清空
    private fun clear(){
        firstPassword = ""
        lastSelectedDotView = null
        passwordBuilder.clear()

    }
    /**
     * 隐藏已经点亮的ImageView
     */
    private fun hideView(){
        Handler().postDelayed({
            selectedViewArray.forEach { it.visibility = View.INVISIBLE }
            changeImageWithStatus(ImageStatus.STATUS_NORMAL)
            selectedViewArray.clear()

        },500)
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


    /**
     * 点亮
     */
    private fun highlightDotView(view: ImageView){
        view.visibility = View.VISIBLE
        lastSelectedDotView = view //更新记录上一个点
        passwordBuilder.append(view.tag as String)//记录密码
        selectedViewArray.add(view)


    }
    private fun highlightLineView(view: ImageView){
        view.visibility = View.VISIBLE
        selectedViewArray.add(view)
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