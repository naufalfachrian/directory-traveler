package id.sugarknife.hibikihagyu.activity

import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.Switch
import android.widget.Toast
import id.sugarknife.hibikihagyu.R
import id.sugarknife.hibikihagyu.extension.askStoragePermission
import id.sugarknife.hibikihagyu.extension.hasStoragePermission
import id.sugarknife.hibikihagyu.extension.preferences
import id.sugarknife.hibikihagyu.extension.showHiddenFiles
import id.sugarknife.hibikihagyu.fragment.DirectoryFragment
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class DirectoryActivity : AppCompatActivity() {

    private val askStoragePermissionRequestCode = 431

    private val fragmentTag = "DirectoryFragment"

    private var shouldExit = false

    var currentDirectory: File? = null
        set(value) {
            field = value
            refreshDirectoryFragment()
            title = value?.name
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbarWidget)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp)

        setupMenuItems()

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

    private fun setupMenuItems() {
        val toggleShowHiddenFiles = navigationView.menu.findItem(R.id.toggleShowHiddenFiles)
        val toggleShowHiddenFilesSwitch = toggleShowHiddenFiles.actionView as? Switch
        toggleShowHiddenFilesSwitch?.setOnCheckedChangeListener { _, checked ->
            preferences.showHiddenFiles = checked
            refreshDirectoryFragment()
        }
        toggleShowHiddenFiles.setOnMenuItemClickListener { menuItem ->
            val switch = menuItem.actionView as? Switch ?: return@setOnMenuItemClickListener false
            switch.isChecked = !switch.isChecked
            return@setOnMenuItemClickListener true
        }
        toggleShowHiddenFilesSwitch?.isChecked = preferences.showHiddenFiles
    }

    private fun showDirectoryFragment() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.content, DirectoryFragment(), fragmentTag)
                .commit()
    }

    private fun refreshDirectoryFragment() {
        (supportFragmentManager.findFragmentByTag(fragmentTag) as? DirectoryFragment)?.refresh()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            if (currentDirectory == DirectoryActivity.rootDirectory) {
                pressBackButtonTwiceToExit()
            } else {
                currentDirectory = currentDirectory?.parentFile
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        super.onOptionsItemSelected(item)
        when (item?.itemId) {
            android.R.id.home -> {
                drawerLayout.openDrawer(GravityCompat.START, true)
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
