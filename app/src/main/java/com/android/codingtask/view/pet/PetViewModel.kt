package com.android.codingtask.view.pet

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.android.codingtask.model.Settings
import com.android.codingtask.model.Pet
import com.android.codingtask.repository.PetRepository
import com.android.codingtask.repository.Resource
import com.android.codingtask.testing.OpenForTesting
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

@OpenForTesting
class PetViewModel(petRepository: PetRepository) : ViewModel() {

    val settings: LiveData<Resource<Settings>> = petRepository.getSettings()

    val isChatEnabled: LiveData<Boolean> =
        Transformations.map(settings) { settings ->
            settings.status == Resource.Status.SUCCESS && settings.data?.isChatEnabled ?: false
        }

    val isCallEnabled: LiveData<Boolean> =
        Transformations.map(settings) { settings ->
            settings.status == Resource.Status.SUCCESS && settings.data?.isCallEnabled ?: false
        }

    val workHours: LiveData<String> =
        Transformations.map(settings) { settings ->
            settings.data?.workHours ?: ""
        }

    var pets: LiveData<Resource<List<Pet>>> = petRepository.getPets()

    /**
     * Check if the current time is within working hours
     *
     * @return true current time is within working hours, false otherwise.
     */
    fun checkForAvailability(): Boolean {
        val delimiter = " "
        val parts = workHours.value?.split(delimiter)
        parts?.let {
            if (it.size > 3) {
                val startTime = it[1]
                val endTime = it[3]
                return isTimeBetween(startTime, endTime)
            }
        }
        return false
    }


    /**
     * Returns whether current time lies between two times.
     *
     * @return true current time lies between two times, false otherwise.
     */
    private fun isTimeBetween(startTime: String?, endTime: String?): Boolean {
        try {
            val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("H:m", Locale.US)
            val start = LocalTime.parse(startTime, formatter)
            val end = LocalTime.parse(endTime, formatter)
            val now = LocalTime.now()
            if (start.isBefore(end)) return now.isAfter(start) && now.isBefore(end)
            return if (now.isBefore(start)) now.isBefore(start) && now.isBefore(end) else now.isAfter(
                start
            ) && now.isAfter(end)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false

    }
}

