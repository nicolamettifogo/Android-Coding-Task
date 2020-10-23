package com.android.codingtask.view.pet

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.android.codingtask.model.Settings
import com.android.codingtask.repository.PetRepository
import com.android.codingtask.repository.Resource
import com.android.codingtask.util.ApiUtil
import com.android.codingtask.util.mock
import com.android.codingtask.model.Pet
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.*

@RunWith(JUnit4::class)
class PetViewModelTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private val mockRepository = mock<PetRepository>()
    private lateinit var petViewModel: PetViewModel

    @Test
    fun settings() {
        // Mock API response
        val settings = Settings(true, isCallEnabled = true, workHours = "M-F 09:00 - 18:00")
        `when`(mockRepository.getSettings()).thenReturn(ApiUtil.successCall(settings))

        // init ViewModel
        petViewModel = PetViewModel(mockRepository)

        // settings
        val observer = mock<Observer<Resource<Settings>>>()
        petViewModel.settings.observeForever(observer)
        verify(mockRepository, times(1)).getSettings()
        verify(observer).onChanged(Resource.success(settings))

        // chat enabled
        val observerChatEnabled = mock<Observer<Boolean>>()
        petViewModel.isChatEnabled.observeForever(observerChatEnabled)
        verify(observerChatEnabled).onChanged(true)

        // call enabled
        val observerCallEnabled = mock<Observer<Boolean>>()
        petViewModel.isCallEnabled.observeForever(observerCallEnabled)
        verify(observerCallEnabled).onChanged(true)

        petViewModel.settings.removeObserver(observer)
    }

    @Test
    fun pets() {

        `when`(mockRepository.getPets()).thenReturn(ApiUtil.successCall(emptyList()))

        // init ViewModel
        petViewModel = PetViewModel(mockRepository)

        // observer
        val observer = mock<Observer<Resource<List<Pet>>>>()
        petViewModel.pets.observeForever(observer)

        verify(mockRepository, times(1)).getPets()

        petViewModel.pets.removeObserver(observer)
    }
}
