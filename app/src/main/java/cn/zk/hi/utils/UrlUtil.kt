package cn.zk.hi.utils

object UrlUtil {

    /**
     * 获取推荐url
     */
    fun getFavoritesUrl(offset: Int, size: Int): String {
        val url = ("http://mapi.yinyuetai.com/suggestions/front_page.json?deviceinfo="
                + "{\"aid\":\"10201036\",\"os\":\"Android\","
                + "\"ov\":" + "\"" + getSystemVersion() + "\"" + ","
                + "\"rn\":\"480*800\","
                + "\"dn\":" + "\"" + getPhoneModel() + "\"" + ","
                + "\"cr\":\"46000\","
                + "\"as\":"
                + "\"WIFI\","
                + "\"uid\":"
                + "\"dbcaa6c4482bc05ecb0bf39dabf207d2\","
                + "\"clid\":110025000}"
                + "&offset=" + offset
                + "&size=" + size
                + "&v=4&rn=640*540")
        return url
    }


    fun getSystemVersion(): String {
        return android.os.Build.VERSION.RELEASE
    }

    fun getPhoneModel(): String {
        return android.os.Build.MODEL;
    }
}