package com.android.codingtask.view.pet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.codingtask.repository.PetRepository

class PetViewModelFactory constructor(private val repository: PetRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(PetViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            PetViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}