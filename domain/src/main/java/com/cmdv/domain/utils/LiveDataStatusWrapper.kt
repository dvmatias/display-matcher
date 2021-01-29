package com.cmdv.domain.utils

class LiveDataStatusWrapper<out T>(val status: Status, val data: T?, val message: String?) {

    enum class Status {
        LOADING,
        ERROR,
        SUCCESS
    }

    companion object {
        fun <T> success(data: T?): LiveDataStatusWrapper<T> {
            return LiveDataStatusWrapper(Status.SUCCESS, data, null)
        }

        fun <T> error(msg: String, data: T?): LiveDataStatusWrapper<T> {
            return LiveDataStatusWrapper(Status.ERROR, data, msg)
        }

        fun <T> loading(data: T?): LiveDataStatusWrapper<T> {
            return LiveDataStatusWrapper(Status.LOADING, data, null)
        }
    }
}