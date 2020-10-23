package com.android.codingtask.view.petinfo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PetInfoViewModel : ViewModel() {

    val webViewErrorOccurred = MutableLiveData<Boolean>()

}