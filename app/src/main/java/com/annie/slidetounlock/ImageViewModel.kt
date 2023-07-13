package com.annie.slidetounlock

import android.widget.ImageView

/**
 * 用一个模型 一个类来封装一个控件
 * 这个控件有两种状态：正常状态、错误状态
 *
 * 把每一个控件都封装成对应的 model
 * 好处：随时随地切换
 * 坏处：有多少个控件就得有多少个 model
 *  没有自定义的前提下比较好的方法
 */
class ImageViewModel (
    val view:ImageView,
    val normalResId:Int,
    val errorResId:Int
){
    fun changeImage(isNormal:Boolean){
        if(isNormal){
            view.setImageResource(normalResId)
        }else{
            view.setImageResource(errorResId)
        }
    }
}