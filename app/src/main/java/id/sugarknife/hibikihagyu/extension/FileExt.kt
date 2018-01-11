package id.sugarknife.hibikihagyu.extension

import java.io.File
import java.io.FileFilter

val File.listFilesWithoutHiddenFiles: Array<out File>
    get() {
        return listFiles(FileFilter { !it.isHidden })
    }