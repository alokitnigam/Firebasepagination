package com.example.getactivetask.DI.Models

import io.reactivex.Observable


data class RealtimeData(
    val id: String,
    val record: Observable<DataModel>
)