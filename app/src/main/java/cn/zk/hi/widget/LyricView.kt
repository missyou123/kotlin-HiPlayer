package cn.zk.hi.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import cn.zk.hi.R
import cn.zk.hi.model.LyricBean
import cn.zk.hi.utils.LyricUtil
import org.jetbrains.anko.doAsync
import java.io.File

/**
 * 作者: zhukun on $(DATE)$(TIME).
 * 歌词自定义view
 */
class LyricView : View {
    //容器的宽高
    private var screenW = 0
    private var screenH = 0
    //居中文本的字体大小和颜色
    private var bigSize = 0f
    private var white = 0
    //其他文本的字体大小和颜色
    private var normalSize = 0f
    private var gray = 0
    private var centerLine = 0//当前居中行数
    private var lineHeight = 0//行高
    private var isUpdate = true//是否根据progress更新view
    private var offsetY = 0f//剧中行y方向的偏移量
    private var progress = 0//当前进度
    private var durtion = 0//当前总进度
    //歌词 集合
    private var list = ArrayList<LyricBean>()
    private val paint by lazy { Paint(Paint.ANTI_ALIAS_FLAG) }//通过惰性加载创建画笔Paint

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    /**
     * 初始化
     */
    init {
        bigSize = resources.getDimension(R.dimen.sp40)
        normalSize = resources.getDimension(R.dimen.sp37)
        white = resources.getColor(R.color.colorLyricBig)
        gray = resources.getColor(R.color.colorLyricNormal)
        lineHeight = resources.getDimensionPixelOffset(R.dimen.px80)
        paint.textAlign = Paint.Align.CENTER
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            //判断歌词是否加载好
            if (list.size > 1) drawLyrics(it) else drawPrompt(it)
        }
    }

    /**
     * 渲染提示
     */
    private fun drawPrompt(canvas: Canvas) {
        var text = "歌词加载中..."
        if(list.size > 0) text = list[0].content
        //初始化画笔
        paint.textSize = bigSize
        paint.color = white
        //计算文本的高度
        var rect = Rect()
        paint.getTextBounds(text, 0, text.length, rect)
        var textH = rect.height()
        //计算居中行的坐标
        var x = (screenW / 2).toFloat()
        var y = (screenW / 2 + textH / 2).toFloat()
        //使用画笔画出提示
        canvas.drawText(text, x, y, paint)
    }

    /**
     * 渲染歌词
     */
    private fun drawLyrics(canvas: Canvas) {
        if (isUpdate) {
            //根据当前时间获取偏移量
            //是否是最后一行
            var lineTime = 0
            if (centerLine == list.size - 1) {
                lineTime = durtion - list[centerLine].startTime
            } else {
                var currentTime = list[centerLine].startTime
                var nextTime = list[centerLine + 1].startTime
                lineTime = nextTime - currentTime
            }
            //偏移时长 = 当前progress - 行可用时长
            var offsetTime = progress - list[centerLine].startTime
            //偏移百分比 = 偏移时间/行可用时间
            var offsetPercent = offsetTime / (lineTime.toFloat())
            //偏移量 = 偏移百分比 * 行高
            this.offsetY = offsetPercent * lineHeight
        }
        //获取据中行的坐标
        var centerText = list[centerLine].content
        var rect = Rect()
        paint.getTextBounds(centerText, 0, centerText.length, rect)
        //居中行的文本的高
        var centerLineH = rect.height()
        //居中行的y坐标
        var centerLineY = screenH / 2 + centerLineH / 2 - offsetY

        //遍历歌词列表, 获取坐标渲染
        for ((index, value) in list.withIndex()) {
            var content = value.content
            //判断是否是居中行
            if (index == centerLine) {
                paint.textSize = bigSize
                paint.color = white
            } else {
                paint.textSize = normalSize
                paint.color = gray
            }
            var x = screenW / 2
            var y = centerLineY + (index - centerLine) * lineHeight
            //判断y坐标是否超出界限
            if (y < 0) continue
            if (y > screenH) break
            canvas.drawText(content, x.toFloat(), y, paint)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        screenW = w
        screenH = h
    }

    /**
     * 根据当前progress判断居中行
     */
    fun updateLineView(progress: Int) {
        if (!isUpdate) return
        if (list.size == 0) return
        this.progress = progress
        //判断是否是最后一行
        var lastLineTime = list[list.size - 1].startTime
        if (progress >= lastLineTime) {
            centerLine = list.size - 1
        } else {
            //遍历歌词
            for (index in list.indices) {
                //index行的startTime
                var currentStartTime = list[index].startTime
                var nextStartTime = list[index + 1].startTime
                //当前进度大于等于 index 的time   同时 小于 next的time
                if (progress in currentStartTime..(nextStartTime - 1)) {
                    centerLine = index
                    break
                }
            }
        }
        invalidate()
    }

    /**
     * 获取歌词
     */
    fun getLyrics() {
        doAsync {
            this@LyricView.list.clear()
            this@LyricView.list.addAll(LyricUtil.loadLyric())
        }
    }

    /**
     * 总进度
     */
    fun setDurtion(durtion: Int) {
        this.durtion = durtion
    }

    /**
     * 开启自更新的标识
     */
    fun changeUpdateSate(flag: Boolean) {
        isUpdate = flag
    }

    //手放下时的y坐标
    private var downY = 0f
    private var markY = 0f//y方向的偏移量 替代  换算用
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (list.size <= 1) return true
        event?.let {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    isUpdate = false
                    downY = event.y
                    markY = this.offsetY
                    //通知界面将居中线的定时任务remove
                    listener?.invoke(-2)
                }
                MotionEvent.ACTION_MOVE -> {
                    var moveY = event.y
                    //移动距离
                    this.offsetY = downY - moveY + markY
                    //判断offsetY是否超过行高
                    if (Math.abs(offsetY) >= lineHeight) {
                        var offsetNum = (this.offsetY / lineHeight).toInt()
                        //给居中行重新赋值
                        centerLine += offsetNum
                        //判断centerLine是否超出边界
                        if (centerLine < 0) centerLine = 0
                        else if (centerLine > list.size - 1) centerLine = list.size - 1
                        //重新赋值downY , 偏移量 . 替代值
                        this.downY = moveY
                        this.offsetY = this.offsetY % lineHeight
                        this.markY = this.offsetY
                        //更新centerLine进度
                        listener?.invoke(list.get(centerLine).startTime)
                    }
                    invalidate()
                }
                MotionEvent.ACTION_UP -> {
                    this.offsetY = 0f
                    invalidate()
                    //延时 通知界面自己更新, 并隐藏居中线
                    listener?.invoke(-1)
                }
                else -> {
                }
            }
        }
        return true
    }

    //进度回调函数
    private var listener: ((progress: Int) -> Unit)? = null

    //设置进度回调函数函数
    fun setProgressListener(listener: (progress: Int) -> Unit) {
        this.listener = listener
    }
}