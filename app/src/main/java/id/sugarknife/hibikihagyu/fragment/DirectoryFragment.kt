package id.sugarknife.hibikihagyu.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import id.sugarknife.hibikihagyu.R
import id.sugarknife.hibikihagyu.activity.DirectoryActivity
import id.sugarknife.hibikihagyu.adapter.DirectoryListAdapter
import id.sugarknife.hibikihagyu.delegate.FileSelectedDelegate
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_directory.*
import java.io.File


class DirectoryFragment : Fragment(), FileSelectedDelegate {

    var adapter: DirectoryListAdapter? = null

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
        val directoryActivity = activity as? DirectoryActivity
        if (directoryActivity != null) {
            directoryActivity.currentDirectory = file
        }
    }

    override fun fileSelected(file: File) {
        //
    }

    fun refresh() {
        val directoryActivity = activity as? DirectoryActivity

        directoryListView.visibility = View.GONE

        if (directoryActivity != null) {
            val currentDirectory = directoryActivity.currentDirectory
            if (currentDirectory != null) {
                Schedulers.io().createWorker().schedule {
                    if (adapter == null) {
                        adapter = DirectoryListAdapter(context, currentDirectory.listFiles(), this@DirectoryFragment)
                        AndroidSchedulers.mainThread().createWorker().schedule {
                            directoryListView.adapter = adapter
                            directoryListView.visibility = View.VISIBLE
                            runLayoutAnimation(directoryListView)
                        }
                    } else {
                        adapter?.directoryItems = currentDirectory.listFiles()
                        AndroidSchedulers.mainThread().createWorker().schedule {
                            adapter?.notifyDataSetChanged()
                            directoryListView.visibility = View.VISIBLE
                            runLayoutAnimation(directoryListView)
                        }
                    }
                }
            }
        }
    }

    private fun runLayoutAnimation(recyclerView: RecyclerView) {
        val context = recyclerView.context
        val controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down)

        recyclerView.layoutAnimation = controller
        recyclerView.scheduleLayoutAnimation()
    }

}
