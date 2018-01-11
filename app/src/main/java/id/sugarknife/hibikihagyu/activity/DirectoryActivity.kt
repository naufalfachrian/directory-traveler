package id.sugarknife.hibikihagyu.activity

import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import id.sugarknife.hibikihagyu.R
import id.sugarknife.hibikihagyu.extension.askStoragePermission
import id.sugarknife.hibikihagyu.extension.hasStoragePermission
import id.sugarknife.hibikihagyu.fragment.DirectoryFragment
import java.io.File

class DirectoryActivity : AppCompatActivity() {

    private val askStoragePermissionRequestCode = 431

    private val fragmentTag = "DirectoryFragment"

    private var shouldExit = false

    var currentDirectory: File? = null
        set(value) {
            field = value
            displayCurrentDirectory()
            title = value?.name
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showDirectoryFragment()

        if (!application.hasStoragePermission()) {
            askStoragePermission(askStoragePermissionRequestCode)
        } else {
            currentDirectory = DirectoryActivity.rootDirectory
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == askStoragePermissionRequestCode) {
            if (grantResults.count() > 0 && grantResults.first() == PackageManager.PERMISSION_GRANTED) {
                currentDirectory = DirectoryActivity.rootDirectory
            } else {
                askStoragePermission(askStoragePermissionRequestCode)
            }
        }
    }

    private fun showDirectoryFragment() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.content, DirectoryFragment(), fragmentTag)
                .commit()
    }

    private fun displayCurrentDirectory() {
        (supportFragmentManager.findFragmentByTag(fragmentTag) as? DirectoryFragment)?.refresh()
    }

    override fun onBackPressed() {
        if (currentDirectory == DirectoryActivity.rootDirectory) {
            pressBackButtonTwiceToExit()
        } else {
            currentDirectory = currentDirectory?.parentFile
        }
    }

    private fun pressBackButtonTwiceToExit() {
        if (shouldExit) {
            super.onBackPressed()
        } else {
            Toast.makeText(this, getString(R.string.exit_toast), Toast.LENGTH_SHORT).show()
            shouldExit = true
            Handler().postDelayed({
                shouldExit = false
            }, 1000)
        }
    }

    companion object {
        val rootDirectory = Environment.getExternalStorageDirectory()!!
    }
}
