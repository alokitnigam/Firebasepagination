package com.example.getactivetask.Views

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.getactive.Base.BaseActivity
import com.example.getactive.Views.ViewModels.PostActivityViewModel
import com.example.getactivetask.DI.Models.DataModel
import com.example.getactivetask.DI.Network.NetworkState
import com.example.getactivetask.R
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_post.*
import java.io.IOException
import javax.inject.Inject


class PostActivity : BaseActivity<PostActivityViewModel>() {
    lateinit var storageReference: StorageReference
    private val PICK_IMAGE_REQUEST: Int = 22
    lateinit var userId:String

    private var filePath: Uri? = null

    var key:String = ""
    lateinit var dataModel: DataModel
    @Inject
    lateinit var postActivityViewModel: PostActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userId = intent.getStringExtra("id")
        storageReference = FirebaseStorage.getInstance().reference
        delete.hide()
        imageurledittext.isFocusable = false

        if(intent.hasExtra("model")){
            textView.text = "Update Post"
            key = intent.getStringExtra("key")
            dataModel = intent.getSerializableExtra("model") as DataModel
            if(dataModel.imageUrl.isNullOrEmpty()){
                imageurledittext.visibility = View.GONE
            }else{

                imageurledittext.setText(dataModel.imageUrl)
                Glide.with(this).load(dataModel.imageUrl).into(imageView)
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


            if(key.isEmpty()){
                uploadFile()

            }else{
                val dataModel:DataModel = if(imageurledittext.text.isNullOrEmpty()){
                    DataModel(title = titleedittext.text.toString(),imageUrl = "",time = System.currentTimeMillis().toString()
                        ,type = 1,createdby = userId)
                }else{
                    DataModel(title = titleedittext.text.toString(),imageUrl = imageurledittext.text.toString()
                        ,time = System.currentTimeMillis().toString()
                        ,type = 2,createdby = userId)
                }

                getViewModel().updateData(dataModel,key)
            }
        }

        delete.setOnClickListener {
            if(dataModel.imageUrl.isNullOrEmpty()){
                getViewModel().deletePost(key)
            }else{
                Toast.makeText(this,"Image Posts cant be deleted",Toast.LENGTH_SHORT).show()
            }
        }

        imageurledittext.setOnClickListener {
            showFileChooser()
        }
    }

    private fun submitData() {
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

    private fun showFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }


    override fun layoutRes(): Int {
        return R.layout.activity_post
    }

    override fun getViewModel(): PostActivityViewModel {
        return postActivityViewModel
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            filePath = data.data
            try {
                val bitmap =
                    MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                imageView.setImageBitmap(bitmap)


            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun uploadFile() {
        if (filePath != null) {
            val progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Uploading")
            progressDialog.show()

            val riversRef: StorageReference = storageReference.child("images/{${System.currentTimeMillis()}}")
            riversRef.putFile(filePath!!)
                .addOnSuccessListener {
                    progressDialog.dismiss()
                    riversRef.downloadUrl.addOnSuccessListener {
                        Log.d("", "onSuccess: uri= "+ it.toString());

                        imageurledittext.setText(it.toString())
                        submitData()

                    }
                    Toast.makeText(applicationContext, "File Uploaded ", Toast.LENGTH_LONG)
                        .show()

                   // submitData(it)
                }
                .addOnFailureListener { exception ->
                    progressDialog.dismiss()
                    Toast.makeText(
                        applicationContext,
                        exception.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
                .addOnProgressListener { taskSnapshot ->
                    //calculating progress percentage
                    val progress =
                        100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
                    //displaying percentage in progress dialog
                    progressDialog.setMessage("Uploaded " + progress.toInt() + "%...")
                }
        } else { //you can display an error toast
        }
    }
}
