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
    //保存9个点  private val dotArray1 = arrayListOf<ImageView>()
    private lateinit var dotArray : Array<ImageView> //不需要更改增删
    //private val dotArray = arrayListOf<ImageView>()
    // 现在是空的 可往里面添加  因为不用初始化所以不用这种

    //记录上一个被点亮的点  一开始没有就是空
    private var lastSelectedDot:ImageView? = null

    //后期判断密码是否正确 正确下一个界面 错误报错变成红色
    //记录密码  一个容器 拼接密码
    private val passwordBuilder = StringBuilder()
    //模拟密码
    private val password = "123"

    //记录所有点亮的控件  把点亮的控件全部扔进去 方便后续还原
    private val selectedArray = arrayListOf<ImageView>()


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
        dotArray = arrayOf(binding.dot1,binding.dot2,binding.dot3,binding.dot4,binding.dot5,binding.dot6,binding.dot7,binding.dot8,binding.dot9)
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
        }

        //给容器添加触摸事件
        binding.container.setOnTouchListener { v, event ->
            //处理触摸事件
            //判断某个触摸点是否在某个范围内
            when (event.action) {
                MotionEvent.ACTION_DOWN -> { //按下 ①坐标位置②判断范围
                    //判断触摸点是否在圆点内部
                    val dotView = isInView(event.x,event.y)  //得到的点or空
                    if (dotView != null){ //不为空 即在圆点上
                        //在圆点上就让它点亮 默认情况下是暗色的
                        dotView.visibility = View.VISIBLE
                        //记录下来
                        lastSelectedDot = dotView


                        //记录密码
                        passwordBuilder.append(dotView.tag as String)
                        //保存点亮的控件
                        selectedArray.add(dotView)
                    }
                }


                MotionEvent.ACTION_MOVE -> {  //移动
                    //判断触摸点是否在圆点内部
                    val dotView = isInView(event.x,event.y)
                    //处理在点亮的点内部移动触发move事件  在点的内部移动
                    if (lastSelectedDot != dotView){//这一个和上一个点不一样 不能出现11 11 22 ...
                    if (dotView != null){   //移动过程中不为空 则点到点里面了
                        //判断是不是第一个点  ①第一个点就直接点亮 ②不是第一个点就要判断路线有没有
                        if (lastSelectedDot == null){ //没有上一个点就是第一个点
                            //点亮
                            dotView.visibility = View.VISIBLE
                            //记录下来
                            lastSelectedDot = dotView

                            //记录密码
                            passwordBuilder.append(dotView.tag as String)
                            //保存点亮控件
                            selectedArray.add(dotView)
                        }else{//不是第一个点 是下一个点
                            //判断路线是否有 用tag值判断
                            //获取上一个点和当前点的tag值
                            val lastTag = (lastSelectedDot!!.tag as String).toInt()
                            val currentTag = (dotView.tag as String).toInt()
                            //形成线的tag值  小的值×10+大的值
                            val lineTag =
                                if (lastTag < currentTag) lastTag*10+currentTag else currentTag*10+lastTag
                            //获取lineTag对应的控件
                            val lineView =
                                binding.container.findViewWithTag<ImageView>("$lineTag")
                            if (lineView != null){ //有路线
                                dotView.visibility = View.VISIBLE
                                lineView.visibility = View.VISIBLE
                                lastSelectedDot = dotView
                                //记录密码
                                passwordBuilder.append(dotView.tag as String)

                                //保存
                                selectedArray.add(dotView) //存点
                                selectedArray.add(lineView) //存线
                            }
                        }
                    }
                }
            }


                MotionEvent.ACTION_UP -> {  //放手抬起
                    binding.alertText.text = passwordBuilder.toString()
                    //判断密码是否正确
                    if (passwordBuilder.toString() == password){
                        //密码正确
                        binding.alertText.text = "密码解锁成功"
                    }else{
                        //密码错误
                        binding.alertText.text = "密码解锁失败"

                        //换成红色
                        //切换图片 先遍历出所有选中的图片
                        selectedArray.forEach {
                            //找到这个控件对应的model
                            for (model in modelsArray) {
                                if (model.view == it) {
                                    model.changeImage(false)
                                    break
                                }
                            }
                        }
                    }

                    //延迟一下再恢复(隐藏)
                    //Thread.sleep(1000) 影响主线程
                    Handler().postDelayed({
                        selectedArray.forEach {
                            it.visibility = View.INVISIBLE
                            //找到这个控件对应的model
                            for (model in modelsArray) {
                                if (model.view == it) {
                                    model.changeImage(true)
                                    break
                                }
                            } //再换成正常的图片
                        }
                        },500)
                }
            }
            true
            //返回值：需不需要对这个触摸事件消费
        }
        }




    /**
     * 判断触摸点是否在某个圆点内部
     * 在：返回这个圆点对象
     * 不在：返回空 null
     * forEach 只关心内容
     */
    private fun isInView(x:Float,y:Float):ImageView?{
        dotArray.forEach {
            if ((x >= it.left && x <= it.right) && (y >= it.top && y <= it.bottom )){
                return it
            }
        }
        //遍历完了都没有就返回空
        return null
    }
}
