package com.example.getactivetask.Views

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.getactive.Base.BaseActivity
import com.example.getactive.Views.ViewModels.PostActivityViewModel
import com.example.getactivetask.DI.Models.DataModel
import com.example.getactivetask.DI.Network.NetworkState
import com.example.getactivetask.R
import kotlinx.android.synthetic.main.activity_post.*
import javax.inject.Inject

class PostActivity : BaseActivity<PostActivityViewModel>() {
    lateinit var userId:String
     var key:String = ""
    lateinit var dataModel: DataModel
    @Inject
    lateinit var postActivityViewModel: PostActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userId = intent.getStringExtra("id")
        delete.hide()

        if(intent.hasExtra("model")){
            textView.text = "Update Post"
            key = intent.getStringExtra("key")
            dataModel = intent.getSerializableExtra("model") as DataModel
            if(dataModel.imageUrl.isNullOrEmpty()){
                imageurledittext.visibility = View.GONE
            }else{

                imageurledittext.setText(dataModel.imageUrl)
                imageurledittext.isEnabled = false
            }
            titleedittext.setText(dataModel.title)
            delete.show()
        }

        setUpObservers()
        setUpListeners()
    }

    private fun setUpListeners() {
        submit.setOnClickListener {

            if(!titleedittext.text.toString().isEmpty()){
                val dataModel:DataModel = if(imageurledittext.text.isNullOrEmpty()){
                    DataModel(title = titleedittext.text.toString(),imageUrl = "",time = System.currentTimeMillis().toString()
                        ,type = 1,createdby = userId)
                }else{
                    DataModel(title = titleedittext.text.toString(),imageUrl = imageurledittext.text.toString()
                        ,time = System.currentTimeMillis().toString()
                        ,type = 2,createdby = userId)
                }
                if(key.isEmpty()){
                    getViewModel().postData(dataModel)

                }else{
                    getViewModel().updateData(dataModel,key)
                }
            }else
                Toast.makeText(this,"Title cannot be empty",Toast.LENGTH_SHORT).show()





        }

        delete.setOnClickListener {
            if(dataModel.imageUrl.isNullOrEmpty()){
                getViewModel().deletePost(key)
            }else{
                Toast.makeText(this,"Image Posts cant be deleted",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setUpObservers() {
        getViewModel().netWorkResponse.observe(this, Observer {
            if (it.status == NetworkState.SUCESS){
                Toast.makeText(this,"Data saved successfully",Toast.LENGTH_LONG).show()
                finish()
            }else{
                Toast.makeText(this,"Some error occurred,Try Again",Toast.LENGTH_LONG).show()

            }
        })
    }

    override fun layoutRes(): Int {
        return R.layout.activity_post
    }

    override fun getViewModel(): PostActivityViewModel {
        return postActivityViewModel
    }
}
