package com.annie.slidetounlock

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.annie.slidetounlock.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {



    //用一个数组保存所有的模型对象
    private val modelsArray = arrayListOf<ViewModel>()


    @SuppressLint("ClickableViewAccessibility")
    //View并不一定需要被点解 需要加这个注解
    //Suppress镇压抑制 Lint线头（可点击 视图 可访问性）
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //(2)创建绑定类对象
        val binding = ActivityMainBinding.inflate(layoutInflater)
        //(3)将绑定类里面对应的view设置给activity
        setContentView(binding.root)

       //9个点的视图保存到数组
        val dotArray = arrayOf(binding.dot1,binding.dot2,binding.dot3,binding.dot4,binding.dot5,binding.dot6,binding.dot7,binding.dot8,binding.dot9)
        //遍历数组
        dotArray.forEach {
            modelsArray.add(ViewModel(it,R.drawable.dot_normal,R.drawable.dot_selected))
        }

        //竖线
        val verticalLineArray = arrayOf(binding.line14,binding.line25,binding.line36,binding.line47,binding.line58,binding.line69)
        verticalLineArray.forEach {
            modelsArray.add(ViewModel(it,R.drawable.line_1_normal,R.drawable.line_1_error))
        }

        //横线
        val landscapeLineArray = arrayOf(binding.line12,binding.line23,binding.line45,binding.line56,binding.line78,binding.line89)
        landscapeLineArray.forEach {
            modelsArray.add(ViewModel(it,R.drawable.line_2_normal,R.drawable.line_2_error))
        }

        //左斜
        val leftSlashArray = arrayOf(binding.line24,binding.line35,binding.line57,binding.line68)
        leftSlashArray.forEach {
            modelsArray.add(ViewModel(it,R.drawable.line_4_normal,R.drawable.line_4_error))
        }

        //右斜
        val rightSlashArray = arrayOf(binding.line15,binding.line26,binding.line48,binding.line59)
        rightSlashArray.forEach {
            modelsArray.add(ViewModel(it,R.drawable.line_3_normal,R.drawable.line_3_error))
        }}}


