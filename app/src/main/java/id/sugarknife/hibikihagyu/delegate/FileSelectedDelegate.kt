package id.sugarknife.hibikihagyu.delegate

import java.io.File

interface FileSelectedDelegate {

    fun directorySelected(file: File)
    fun fileSelected(file: File)

}