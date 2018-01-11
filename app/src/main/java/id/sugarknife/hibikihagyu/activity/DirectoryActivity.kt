package id.sugarknife.hibikihagyu.activity

import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import id.sugarknife.hibikihagyu.R
import id.sugarknife.hibikihagyu.extension.askStoragePermission
import id.sugarknife.hibikihagyu.extension.hasStoragePermission
import id.sugarknife.hibikihagyu.extension.preferences
import id.sugarknife.hibikihagyu.extension.showHiddenFiles
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

        replaceDirectoryFragment()

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

    private fun replaceDirectoryFragment() {
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        super.onPrepareOptionsMenu(menu)
        menu?.findItem(R.id.toggleShowHiddenFiles)?.isChecked = preferences.showHiddenFiles
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        super.onOptionsItemSelected(item)
        when (item?.itemId) {
            R.id.toggleShowHiddenFiles -> {
                item.isChecked = !item.isChecked
                preferences.showHiddenFiles = item.isChecked
                replaceDirectoryFragment()
            }
        }
        return true
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
