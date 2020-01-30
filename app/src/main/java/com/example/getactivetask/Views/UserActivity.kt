package com.example.getactivetask.Views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.getactivetask.R
import kotlinx.android.synthetic.main.activity_user.*

class UserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        login.setOnClickListener {
            if(editText.text.isNullOrEmpty()){
                Toast.makeText(this,"Enter an Id",Toast.LENGTH_LONG).show()
            }else{
                startActivity( Intent(this,MainActivity::class.java)
                    .putExtra("id",editText.text.toString()))
            }
        }
    }
}
