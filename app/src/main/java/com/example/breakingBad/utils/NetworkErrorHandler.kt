package com.example.breakingBad.utils

import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

open class NetworkErrorHandler(private val uiComponent: UiErrorInterface) {

    fun handle(e: Throwable) {
        when (e) {
            is IOException -> {
                uiComponent.onNoInternet()
            }
            is SocketTimeoutException -> {
                uiComponent.onNoInternet()
            }
            is HttpException -> {
                when (e.code()) {
                    401, 403 -> uiComponent.onUnauthorized()
                    else -> uiComponent.onServerError(message = e.message())
                }
            }
        }
    }

}

interface UiErrorInterface {

    fun onNoInternet()

    fun onServerError(message: String)

    fun onUnauthorized()

}

fun UiErrorInterface.handleNetworkError(e: Throwable) {
    NetworkErrorHandler(this).handle(e)
}
