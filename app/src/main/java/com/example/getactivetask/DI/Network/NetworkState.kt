package com.example.getactivetask.DI.Network

class NetworkState(val status: Int, val message: String) {
    companion object {
        val FAILED = -1
        val SUCESS = 1
        val INITIAL_LOADING = 2
        var LOADING = 3
    }
 }
