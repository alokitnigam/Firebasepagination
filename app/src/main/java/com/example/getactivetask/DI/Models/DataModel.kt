package com.example.getactivetask.DI.Models

import java.io.Serializable

data class DataModel (
     var title:String,
     var imageUrl:String,
     var createdby:String,
     var time:String,
     var type:Int // 0 for normal, 1 for image
):Serializable{

     constructor():this("","","","",0)



}