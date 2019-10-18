# CoolAnimator
@[TOC]
# 序言
- 仿“某音”APP 多头像跳动动画+轮播动画+心跳动画+输入框抖动动画
实现逻辑比较简单，主要是细微之处调整比较费时间，有需要的童鞋可以拿去用。
# 效果一：多头像动画
## 效果展示
![多头像动画](https://img-blog.csdnimg.cn/20191018091903771.gif)
## 1、跳动动画
```kotlin
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
```
## 2、轮播动画
```kotlin
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

```
## 多头像效果的使用
```kotlin
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
            layout.addView(iv)
            ivList.add(iv)
        }
        AnimatorUtil.startPetHeadAnim(animSet, ivList, handler)
    }
```
 如果动画用于RecycleView 列表，适配item复用机制，可以在Adapter中增加以下代码，通过handler和animSet控制动画的开启和取消。
 ```kotlin
 override fun onViewAttachedToWindow(holder: MyViewHolder) {
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
    /**
     * 清空动画集
     */
    private fun clearAnimSet(holder: MyViewHolder) {
        holder.handler.removeCallbacksAndMessages(null)
        for (anim in holder.animSet) {
            anim.pause()
        }
    }
 ```
# 效果二：(单头像)心跳动画——放大&缩小
## 效果展示
![心跳动画](https://img-blog.csdnimg.cn/20191018091953184.gif)
## Code
```kotlin
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
```
# 效果三：(输入框)抖动动画
## 效果展示
![抖动动画](https://img-blog.csdnimg.cn/20191018092022104.gif)
## Code
```kotlin
	/**
     * 启动抖动提示动画（如 EditText为空时 抖动效果）
     */
    fun startErrorAnim(view: View){
        view.startAnimation(AnimationUtils.loadAnimation(App.instance, R.anim.shake_anim))
    }
```
shake_anim.xml
```xml
<?xml version="1.0" encoding="utf-8"?>

<translate xmlns:android="http://schemas.android.com/apk/res/android"
    android:fromXDelta="0"
    android:toXDelta="10"
    android:duration="300"
    android:interpolator="@anim/cycles_anim" />
```
cycles_anim.xml
```xml
<?xml version="1.0" encoding="utf-8"?>

<cycleInterpolator xmlns:android="http://schemas.android.com/apk/res/android"
    android:cycles="2" />
```