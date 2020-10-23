package com.android.codingtask.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.codingtask.repository.Resource

object ApiUtil {
    fun <T : Any> successCall(data: T) = MutableLiveData<Resource<T>>().apply {
        value = Resource.success(data)
    } as LiveData<Resource<T>>
}