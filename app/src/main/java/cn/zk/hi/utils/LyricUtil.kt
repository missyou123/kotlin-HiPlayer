package cn.zk.hi.utils

import android.util.Log
import cn.zk.hi.model.LyricBean
import java.io.File
import java.nio.charset.Charset

/**
 * 作者: zhukun on $(DATE)$(TIME).
 * 单词获取工具类
 */
object LyricUtil {
    //加载歌词
    fun loadLyric(): ArrayList<LyricBean> {
        val list = java.util.ArrayList<LyricBean>()
        var file = File("/mnt/sdcard/1.lrc")
        //判断是否存在
        Log.e("LyricUtil", file.exists().toString())
        if (!file.exists()) {
            list.add(LyricBean(0, "歌词加载失败..."))
            return list
        }
        //读取每一行歌词
        val linesList = file.readLines(Charset.forName("gbk"))
        //遍历解析歌词
        for (line in linesList) {
            if (line.contains(']'))
                list.add(parseLine(line))
        }
        return list
    }

    /**
     * 解析每行歌词
     */
    private fun parseLine(line: String): LyricBean {
        //通过"["切割歌词
        var arr = line.split("]")
        //文本
        var content = arr[arr.size - 1]
        //开始时间
        var startTime = parseTime(arr[0])

        return LyricBean(startTime, content)
    }

    /**
     * 解决时间
     */
    private fun parseTime(timeStr: String): Int {
        var time = timeStr.substring(1)
        //按照:切割
        val list = time.split(":")
        var hour = 0
        var min = 0
        var sec = 0f
        if (list.size == 3) {
            //小时
            hour = (list[0].toInt()) * 60 * 60 * 1000
            min = (list[1].toInt()) * 60 * 1000
            sec = (list[2].toFloat()) * 1000
        } else {
            //没有小时
            min = (list[0].toInt()) * 60 * 1000
            sec = (list[1].toFloat()) * 1000
        }
        return (hour + min + sec).toInt()
    }
}