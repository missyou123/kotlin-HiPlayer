package cn.zk.hi.utils

/**
 * 作者: zhukun on $(DATE)$(TIME).
 */
object ProgressUtil {
    val HOUR = 60 * 60 * 1000
    val MIN = 60 * 1000
    val SEC = 1000
    fun parseDuration(progress: Int): String {
        val hour = progress / HOUR
        val min = progress % HOUR / MIN
        val sec = progress % MIN / SEC
        var result:String = ""
        if (hour == 0) {
            //不足1小时 不显示小时
            result = String.format("%02d:%02d",min,sec)
        } else {
            result = String.format("%02d:%02d%02d",hour,min,sec)
        }
        return result
    }
}