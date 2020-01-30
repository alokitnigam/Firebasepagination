package com.example.getactivetask.Views

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.getactive.Base.BaseActivity
import com.example.getactivetask.R
import com.example.getactivetask.Views.Adapter.PostAdapter
import com.example.getactivetask.Views.ViewModels.MainActivityViewModel
import com.google.firebase.database.DataSnapshot
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


class MainActivity : BaseActivity<MainActivityViewModel>() {
    lateinit var userId:String
    private var mTotalItemCount = 0
    private var mLastVisibleItemPosition = 0
    private var mIsLoading = false
    lateinit var loading: ProgressBar

    lateinit var linearLayoutManager:LinearLayoutManager
    var postslist = ArrayList<DataSnapshot>()

    @Inject
    lateinit var mainActivityViewModel: MainActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userId = intent.getStringExtra("id")

        setUpObservers()
        setUpListeners()
        setUpAdapter()

    }
    lateinit var adapter: PostAdapter
    private fun setUpAdapter() {
        adapter = PostAdapter()
        adapter.setUserId(userId)
        linearLayoutManager = LinearLayoutManager(this)
        postsrv.layoutManager = linearLayoutManager
        postsrv.adapter = adapter
        mainActivityViewModel.getInitialPosts()
        postsrv.addOnScrollListener(object :RecyclerView.OnScrollListener(){

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                mTotalItemCount = linearLayoutManager.getItemCount()
                mLastVisibleItemPosition = linearLayoutManager.findLastCompletelyVisibleItemPosition()

                if (!mIsLoading && mTotalItemCount-1 <= mLastVisibleItemPosition ) {
                    getNextPosts(adapter.getLastItemId())
                    mIsLoading = true
                }
            }
        })

        adapter.setOnEditClickListener(object:PostAdapter.EditClickListener{
            override fun onEditClicked(dataSnapshot: DataSnapshot) {
//                startActivity(thi)
            }

        })
    }

    private fun getNextPosts(lastItemId: Pair<String, String>) {
        mainActivityViewModel.getNextPosts(lastItemId.first,lastItemId.second)

    }

    private fun setUpListeners() {
        addpost.setOnClickListener {
            startActivity(Intent(this,PostActivity::class.java).putExtra("id",userId))
        }
    }

    private fun setUpObservers() {
        progressBar.visibility = View.VISIBLE
        mainActivityViewModel.posts.observe(this, Observer {
            Collections.reverse(it)
            adapter.setData(it)
            postslist.addAll(it)
            mainActivityViewModel.getNotifiedOfDataChanges()
            progressBar.visibility = View.GONE


            mIsLoading = false

        })
        mainActivityViewModel.nextposts.observe(this, Observer {
            val list = it as ArrayList<DataSnapshot>
            list.removeAt(list.size - 1)
            list.reverse()
            if(!list.isEmpty()){

                adapter.setData(list)
                postslist.addAll(list)
            }

            mIsLoading = false


        })
        mainActivityViewModel.datachaged.observe(this, Observer {
            when(it.second){
                "Added" ->{
                    var isPresent =false
                    for(i in 0 until  postslist.size){
                        if(postslist[i].key == it.first.key){
                            isPresent = true
                        }
                    }
                    if(!isPresent){

                        adapter.addAtTop(it.first)
                        postslist.add(0,it.first)
                    }
                }
                "Deleted" ->{
                    var position = 0
                    for(i in 0 until  postslist.size){
                        if(postslist[i].key == it.first.key){
                            position = i
                        }
                    }
                    postslist.removeAt(position)
                    adapter.deleteItem(position)

                }
                "Changed" ->{
                    var position = 0
                    for(i in 0 until  postslist.size){
                        if(postslist[i].key == it.first.key){
                            position = i
                        }
                    }

                    adapter.changedItem(it.first,position)

                }
            }
        })
    }

    override fun layoutRes(): Int {
        return R.layout.activity_main
    }

    override fun getViewModel(): MainActivityViewModel {
        return mainActivityViewModel
    }


}
