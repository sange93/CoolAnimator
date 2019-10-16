package com.example.myanimator

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Handler
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView

/**
 * 动画工具类
 * @author ssq
 */
object AnimatorUtil {
    val animPetPositionLeft by lazy { ConvertUtils.dp2px(-51f).toFloat() }
    val animPetPositionMiddle by lazy { ConvertUtils.dp2px(-28f).toFloat() }
    val animPetPositionRight by lazy { ConvertUtils.dp2px(-5f).toFloat() }
    private val animPetPositionEnd by lazy { ConvertUtils.dp2px(-60f).toFloat() }

    /**
     * 启动放大的心跳动画
     */
    fun startEnlargeHeart(imageView: ImageView) {
        val anim = imageView.animate()
        anim.scaleX(2.0f).scaleY(2.0f).setInterpolator(AccelerateInterpolator()).setDuration(200)
                .setListener(object : Animator.AnimatorListener {
                    var tag = true
                    override fun onAnimationRepeat(animation: Animator?) {}

                    override fun onAnimationEnd(animation: Animator?) {
                        if (tag) {
                            anim.scaleX(1.0f).scaleY(1.0f).setInterpolator(DecelerateInterpolator()).setDuration(400).startDelay = 0
                        } else {
                            anim.scaleX(2.0f).scaleY(2.0f).setInterpolator(AccelerateInterpolator()).setDuration(400).startDelay = 2000
                        }
                        tag = !tag
                    }

                    override fun onAnimationCancel(animation: Animator?) {}

                    override fun onAnimationStart(animation: Animator?) {}
                }).startDelay = 1000
    }

    /**
     * 启动宠物多头像动画
     */
    fun startPetHeadAnim(animSet: ArrayList<ObjectAnimator>, imageViews: ArrayList<ImageView>, handler: Handler) {
        if (imageViews.size <= 3) {
            startPetHeadJumpAnim(animSet, imageViews, handler)
        } else {
            startPetHeadAlphaAnim(animSet, imageViews, handler)
        }
    }

    /**
     * 启动宠物多头像跳动动画
     */
    private fun startPetHeadJumpAnim(animSet: ArrayList<ObjectAnimator>, imageViews: ArrayList<ImageView>, handler: Handler) {
        for (i in imageViews.lastIndex downTo 0) {
            val anim = ObjectAnimator.ofFloat(imageViews[i], "translationY", 0f, -30f, 0f)
            animSet.add(anim)
            anim.duration = 600
            when (i) {
                imageViews.lastIndex - 1 -> {
                    anim.startDelay = 200
                }
                imageViews.lastIndex - 2 -> {
                    anim.startDelay = 400
                }
            }
            if (i == imageViews.lastIndex) {
                anim.addListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(animation: Animator?) {}

                    override fun onAnimationEnd(animation: Animator?) {
                        handler.postDelayed({
                            for (an in animSet) {
                                an.start()
                            }
                        }, 2000)
                    }

                    override fun onAnimationCancel(animation: Animator?) {}

                    override fun onAnimationStart(animation: Animator?) {}
                })
            }
            anim.start()
        }
    }

    /**
     * 启动宠物多头像位移动画
     */
    private fun startPetHeadAlphaAnim(animSet: ArrayList<ObjectAnimator>, imageViews: ArrayList<ImageView>, handler: Handler) {
        var startX = 0f
        var endX = 0f
        for ((i, v) in (imageViews.lastIndex downTo 0).withIndex()) {
            // 第4个以后,把（显示的）第一个隐藏，后两个向前移动
            if (i > 2) {
                for (j in v + 3 downTo v + 1) {
                    val anim = when (j) {
                        v + 3 -> {
                            startX = animPetPositionLeft//-150f
                            endX = animPetPositionEnd//-180f
                            val xA = PropertyValuesHolder.ofFloat("translationX", startX, endX)
                            val alphaA = PropertyValuesHolder.ofFloat("alpha", 1f, 0f)
                            ObjectAnimator.ofPropertyValuesHolder(imageViews[j], xA, alphaA)
                        }
                        v + 2 -> {
                            startX = animPetPositionMiddle//-80f
                            endX = animPetPositionLeft//-150f
                            ObjectAnimator.ofFloat(imageViews[j], "translationX", startX, endX)
                        }
                        v + 1 -> {
                            startX = animPetPositionRight//-10f
                            endX = animPetPositionMiddle//-80f
                            ObjectAnimator.ofFloat(imageViews[j], "translationX", startX, endX)
                        }
                        else -> null
                    }
                    if (anim != null) {
                        animSet.add(anim)
                        anim.duration = 900
                        anim.startDelay = i * 600L
                        anim.start()
                    }
                }
            }
            // 依次显示View
            when (i) {
                0 -> {
                    startX = animPetPositionMiddle//-80f
                    endX = animPetPositionLeft//-150f
                }
                1 -> {
                    startX = animPetPositionRight//-10f
                    endX = animPetPositionMiddle//-80f
                }
                in 2..imageViews.lastIndex -> {
                    startX = 0f
                    endX = animPetPositionRight//-10f
                }
            }
            val xA = PropertyValuesHolder.ofFloat("translationX", startX, endX)
            val alphaA = PropertyValuesHolder.ofFloat("alpha", 0f, 1f)
            val anim = ObjectAnimator.ofPropertyValuesHolder(imageViews[v], xA, alphaA)
            animSet.add(anim)
            anim.duration = 900//300
            anim.startDelay = i * 600L// 200
            anim.start()
            if (v == 0) {
                anim.addListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(animation: Animator?) {}

                    override fun onAnimationEnd(animation: Animator?) {
                        handler.postDelayed({
                            for (view in imageViews) {
                                view.alpha = 0f
                            }
                            for (an in animSet) {
                                an.start()
                            }
                        }, 2000)
                    }

                    override fun onAnimationCancel(animation: Animator?) {}

                    override fun onAnimationStart(animation: Animator?) {}
                })
            }
        }
    }

    /**
     * 启动抖动提示动画（如 EditText为空时 抖动效果）
     */
    fun startErrorAnim(view: View){
        view.startAnimation(AnimationUtils.loadAnimation(App.instance, R.anim.shake_anim))
    }

}