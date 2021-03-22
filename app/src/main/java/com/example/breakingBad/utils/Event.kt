package com.example.breakingBad.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

class Event<out T>(private val data: T) {
    private var consumed = false

    fun getData(): T? =
        if (consumed) null
        else {
            consumed = true
            data
        }
}

fun <T> LiveData<Event<T>>.observeEvent(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
    observe(lifecycleOwner) {
        it.getData()?.let { data ->
            observer.onChanged(data)
        }
    }
}