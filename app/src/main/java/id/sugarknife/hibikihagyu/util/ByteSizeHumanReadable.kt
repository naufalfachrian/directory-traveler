package id.sugarknife.hibikihagyu.util

object ByteSizeHumanReadable {

    fun format(bytes: Long): String {
        var b = bytes
        val dictionary = arrayOf("bytes", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB")
        var index = 0
        index = 0
        while (index < dictionary.size) {
            if (b < 1024) {
                break
            }
            b /= 1024
            index++
        }
        return b.toString() + " " + dictionary[index]
    }

}