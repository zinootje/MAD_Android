package com.example.data

//extension on the throwable class to detect if it is a network error
//this expects that that retrofit is used as the network library
fun Throwable.isNetworkError(): Boolean {
    return this is java.net.UnknownHostException || (this is retrofit2.HttpException && this.code() == 503)
}