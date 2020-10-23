package com.android.codingtask.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.codingtask.model.Settings
import com.android.codingtask.testing.OpenForTesting
import com.android.codingtask.model.Pet
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

@OpenForTesting
class PetRepository(private val okHttpClient: OkHttpClient) {

    fun getSettings(): LiveData<Resource<Settings>> {
        val settings = MutableLiveData<Resource<Settings>>()

        val request: Request = Request.Builder()
            .url("https://raw.githubusercontent.com/JKKhanpara/Android-Coding-Task/master/responses/settings.json")
            .build()

        settings.value = Resource.loading(null)

        okHttpClient.newCall(request)
            .enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val responseObject = JSONObject(response.body!!.string())
                        settings.postValue(
                            Resource.success(
                                convertSettings(responseObject.getJSONObject("settings"))
                            )
                        )
                    } else {
                        settings.postValue(Resource.error("", null))
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    settings.postValue(Resource.error("", null))
                }
            })
        return settings
    }

    fun convertSettings(jsonObject: JSONObject): Settings = Settings(
        jsonObject.getBoolean("isChatEnabled"),
        jsonObject.getBoolean("isCallEnabled"),
        jsonObject.getString("workHours")
    )

    fun getPets(): LiveData<Resource<List<Pet>>> {
        val pets = MutableLiveData<Resource<List<Pet>>>()

        val request: Request = Request.Builder()
            .url("https://raw.githubusercontent.com/JKKhanpara/Android-Coding-Task/master/responses/pets.json")
            .build()

        pets.value = Resource.loading(null)
        okHttpClient.newCall(request)
            .enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val responseObject = JSONObject(response.body!!.string())
                        val petsJsonArray = responseObject.getJSONArray("pets")
                        pets.postValue(Resource.success(convertPetList(petsJsonArray)))
                    } else {
                        pets.postValue(Resource.error("", null))
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    pets.postValue(Resource.error("", null))
                }
            })
        return pets
    }

    fun convertPetList(jsonArray: JSONArray): List<Pet> {
        val list = ArrayList<Pet>()
        for (i in 0 until jsonArray.length()) {
            val petObject: JSONObject = jsonArray.getJSONObject(i)
            list.add(
                Pet(
                    petObject.getString("title"),
                    petObject.getString("image_url"),
                    petObject.getString("content_url"),
                    petObject.getString("date_added")
                )
            )
        }
        return list
    }
}
