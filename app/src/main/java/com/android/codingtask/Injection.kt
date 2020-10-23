package com.android.codingtask

import com.android.codingtask.repository.PetRepository
import com.android.codingtask.view.pet.PetViewModelFactory
import okhttp3.OkHttpClient

object Injection {
    private val okHttpClient: OkHttpClient by lazy { OkHttpClient() }

    private val repository: PetRepository by lazy { PetRepository(okHttpClient) }

    val petViewModelFactory: PetViewModelFactory by lazy {
        PetViewModelFactory(repository)
    }
}