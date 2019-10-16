package com.example.myanimator

/**
 * 转换 工具类
 * @author ssq
 */
object ConvertUtils {

    /**
     * Value of dp to value of px.
     *
     * @param dpValue The value of dp.
     * @return value of px
     */
    fun dp2px(dpValue: Float): Int {
        val scale = App.instance.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }
}