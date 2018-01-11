package id.sugarknife.hibikihagyu.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import id.sugarknife.hibikihagyu.R
import id.sugarknife.hibikihagyu.activity.DirectoryActivity
import id.sugarknife.hibikihagyu.adapter.DirectoryListAdapter
import id.sugarknife.hibikihagyu.delegate.FileSelectedDelegate
import id.sugarknife.hibikihagyu.extension.listFilesWithoutHiddenFiles
import id.sugarknife.hibikihagyu.extension.preferences
import id.sugarknife.hibikihagyu.extension.runAnimation
import id.sugarknife.hibikihagyu.extension.showHiddenFiles
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_directory.*
import java.io.File

class DirectoryFragment : Fragment(), FileSelectedDelegate {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_directory, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        directoryListView.layoutManager = LinearLayoutManager(context)
        refresh()
    }

    override fun directorySelected(file: File) {
        val directoryActivity = activity as? DirectoryActivity ?: return
        directoryActivity.currentDirectory = file
    }

    override fun fileSelected(file: File) {
        //
    }

    fun refresh() {
        directoryListView.visibility = View.GONE
        val directoryActivity = activity as? DirectoryActivity ?: return
        val currentDirectory = directoryActivity.currentDirectory ?: return
        Schedulers.io().createWorker().schedule {
            val items: Array<out File> = if (preferences.showHiddenFiles) {
                currentDirectory.listFiles()
            } else {
                currentDirectory.listFilesWithoutHiddenFiles
            }
            val adapter = DirectoryListAdapter(context, items, this@DirectoryFragment)
            AndroidSchedulers.mainThread().createWorker().schedule {
                if (adapter.itemCount == 0) {
                    noItemsLayout.visibility = View.VISIBLE
                } else {
                    noItemsLayout.visibility = View.GONE
                    directoryListView.adapter = adapter
                    refreshRecyclerView()
                }
            }
        }
    }

    private fun refreshRecyclerView() {
        directoryListView.visibility = View.VISIBLE
        directoryListView.runAnimation(R.anim.layout_animation_fall_down)
    }

}
