package com.example.getactivetask.Views.Adapter

import android.content.Intent
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.getactivetask.DI.Models.DataModel
import com.example.getactivetask.R
import com.example.getactivetask.Views.PostActivity
import com.google.firebase.database.DataSnapshot
import java.text.DateFormatSymbols
import java.util.*
import kotlin.collections.ArrayList

class PostAdapter   : RecyclerView.Adapter<PostAdapter.ViewHolder>() {
    private lateinit var userId1: String
    var postslist :ArrayList<DataSnapshot> = ArrayList()
    lateinit var editClickListener: EditClickListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.post_view, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(postslist[position])
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {

        fun bind(dataSnapshot: DataSnapshot){
            val dataModel = dataSnapshot.getValue(DataModel::class.java)
            if(dataModel!!.type == 2){
                Glide.with(itemView.context).load(dataModel.imageUrl).into(image)
                image.visibility = View.VISIBLE


            }else{
                image.visibility = View.GONE
            }
            if(userId1 == dataModel.createdby){
                editOrDelete.visibility = View.VISIBLE
            }else{
                editOrDelete.visibility = View.GONE
            }

            title.setText(dataModel!!.title)
            time.setText(getFormattedDate(dataModel.time.toLong()))
            createdBy.setText(dataModel.createdby)

            editOrDelete.setOnClickListener {
                itemView.context.startActivity(Intent(itemView.context,PostActivity::class.java)
                    .putExtra("id",userId1)
                    .putExtra("model",dataModel)
                    .putExtra("key",dataSnapshot.key))
//                editClickListener.onEditClicked(dataSnapshot)
            }
        }
        val title = itemView.findViewById(R.id.title) as TextView
        val image  = itemView.findViewById(R.id.image) as ImageView
        val editOrDelete  = itemView.findViewById(R.id.edit) as ImageView
        val time  = itemView.findViewById(R.id.time) as TextView
        val createdBy  = itemView.findViewById(R.id.created_by) as TextView
    }

    override fun getItemCount(): Int {
        return postslist.size
    }

    fun getLastItemId(): Pair<String,String> {
        val dataModel = postslist.get(postslist.size-1).getValue(DataModel::class.java)

        return Pair(postslist.get(postslist.size-1).key!!,dataModel!!.time)
    }

    fun setData(list: List<DataSnapshot>?) {
        val initialSize: Int = postslist.size
        postslist.addAll(list!!)
        notifyItemRangeInserted(initialSize, postslist.size)

    }

    fun addAtTop(first: DataSnapshot) {
        postslist.add(0,first)
        notifyItemInserted(0)

    }

    fun deleteItem(position:Int) {

        postslist.removeAt(position)
        notifyItemRemoved(position)

    }

    fun changedItem(first: DataSnapshot, position: Int) {
        postslist.removeAt(position)
        postslist.add(position,first)
        notifyItemChanged(position)

    }

    fun setOnEditClickListener(editClickListener: EditClickListener){
        this.editClickListener = editClickListener
    }

    fun setUserId(userId: String) {
        this.userId1 = userId


    }

    interface EditClickListener{
        fun onEditClicked(dataSnapshot: DataSnapshot)
    }

    fun getFormattedDate(smsTimeInMilis: Long): String? {
        val postTime = Calendar.getInstance()
        postTime.timeInMillis = smsTimeInMilis
        val now = Calendar.getInstance()
        val symbols =
            DateFormatSymbols(Locale.getDefault())
        // OVERRIDE SOME symbols WHILE RETAINING OTHERS
        symbols.amPmStrings = arrayOf("am", "pm")
        val timeFormatString = "h:mm aa"
        return if (now[Calendar.DATE] == postTime[Calendar.DATE]) {
            "Today, " + DateFormat.format(
                "h:mm aa",
                postTime
            ).toString().replace("AM", "am").replace("PM", "pm")
        } else if (now[Calendar.DATE] - postTime[Calendar.DATE] == 1) {
            "Yesterday, " + DateFormat.format(
                "h:mm aa",
                postTime
            ).toString().replace("AM", "am").replace("PM", "pm")
        } else {
            DateFormat.format("MMMM dd , h:mm aa", postTime).toString()
                .replace("AM", "am").replace("PM", "pm")
        }
    }


}