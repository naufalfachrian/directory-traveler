package id.sugarknife.hibikihagyu.adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import id.sugarknife.hibikihagyu.R
import id.sugarknife.hibikihagyu.delegate.FileSelectedDelegate
import id.sugarknife.hibikihagyu.util.ByteSizeHumanReadable
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class DirectoryListAdapter(
        private val context: Context,
        private val directoryItems: List<File>,
        private val delegate: FileSelectedDelegate)
    : RecyclerView.Adapter<DirectoryListAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return directoryItems.count()
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.displayFileProperties(directoryItems[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_directory_item, parent, false))
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun displayFileProperties(file: File) {
            val filenameLabel = itemView.findViewById<TextView>(R.id.filenameLabel)
            val sizeLabel = itemView.findViewById<TextView>(R.id.sizeLabel)
            val timestampLabel = itemView.findViewById<TextView>(R.id.timestampLabel)
            val iconView = itemView.findViewById<ImageView>(R.id.iconView)

            if (file.isDirectory) {
                iconView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_folder))
                val subItemCount = file.listFiles().count()
                sizeLabel.text = context.resources.getQuantityString(R.plurals.count_files, subItemCount, subItemCount)
            }
            if (file.isFile) {
                iconView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_file_octet_stream))
                sizeLabel.text = ByteSizeHumanReadable.format(file.length())
            }

            filenameLabel.text = file.name
            timestampLabel.text = SimpleDateFormat("MMM dd, yyyy, HH:mm:ss", Locale.US).format(Date(file.lastModified()))

            itemView.setOnClickListener {
                if (file.isDirectory)
                    delegate.directorySelected(file)
                if (file.isFile)
                    delegate.fileSelected(file)
            }
        }
    }

}