package com.example.myanimator

import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_head_anim.*

/**
 * 多个头像动画（跳动 + 轮播）
 * @author ssq
 */
class HeadAnimActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_head_anim)
        // 三个头像
        val image3 = arrayListOf<Int>()
        image3.add(R.drawable.pic_1)
        image3.add(R.drawable.pic_2)
        image3.add(R.drawable.pic_3)
        val animSet3 = arrayListOf<ObjectAnimator>()
        val handler3 = Handler()
        initAnim(this, rlHead3, image3, animSet3, handler3)
        // 五个头像
        val image5 = arrayListOf<Int>()
        image5.addAll(image3)
        image5.add(R.drawable.pic_4)
        image5.add(R.drawable.pic_5)
        val animSet5 = arrayListOf<ObjectAnimator>()
        val handler5 = Handler()
        initAnim(this, rlHead5, image5, animSet5, handler5)
    }

    /**
     * 初始化动画
     */
    private fun initAnim(
        context: Context,
        layout: RelativeLayout,
        images: ArrayList<Int>,
        animSet: ArrayList<ObjectAnimator>,
        handler: Handler
    ) {
        layout.removeAllViews()
        animSet.clear()
        val ivList = arrayListOf<ImageView>()
        val isJumpAnim = images.size <= 3
        for (i in images.lastIndex downTo 0) {
            val iv = CircleImageView(context)
            val width = ConvertUtils.dp2px(30f)
            val lp = RelativeLayout.LayoutParams(width, width)
            // 是否为跳动 动画
            if (isJumpAnim) {
                iv.alpha = 1f
                when (i) {
                    0 -> iv.translationX = AnimatorUtil.animPetPositionLeft//-150f
                    1 -> iv.translationX = AnimatorUtil.animPetPositionMiddle//-80f
                    2 -> iv.translationX = AnimatorUtil.animPetPositionRight//-10f
                }
            } else {
                iv.alpha = 0f
            }
            iv.layoutParams = lp
            iv.scaleType = ImageView.ScaleType.CENTER_CROP
            iv.borderColor = ContextCompat.getColor(context, R.color.colorPrimaryDark)
            iv.borderWidth = ConvertUtils.dp2px(1f)
            iv.setImageResource(images[i])
//            ImageLoaderUtil.with(itemView).displayHeadImage(images[i], iv)
            layout.addView(iv)
            ivList.add(iv)
        }
        AnimatorUtil.startPetHeadAnim(animSet, ivList, handler)
    }

    // 如果动画用于RecycleView 列表，可以增加以下代码，通过handler和animSet控制动画的开启和取消。
    /*override fun onViewAttachedToWindow(holder: MyViewHolder) {
        super.onViewAttachedToWindow(holder)
        for (anim in holder.animSet) {
            anim.start()// 启动动画
        }
    }

    override fun onViewDetachedFromWindow(holder: MyViewHolder) {
        clearAnimSet(holder)
    }

    override fun onViewRecycled(holder: MyViewHolder) {
        // 清除动画
        clearAnimSet(holder)
        super.onViewRecycled(holder)
    }

    *//*
     * 清空动画集
     *//*
    private fun clearAnimSet(holder: MyViewHolder) {
        holder.handler.removeCallbacksAndMessages(null)
        for (anim in holder.animSet) {
            anim.pause()
        }
    }*/
}
