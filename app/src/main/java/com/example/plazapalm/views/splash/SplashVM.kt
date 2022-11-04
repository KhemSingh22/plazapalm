package com.example.plazapalm.views.splash

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.example.plazapalm.models.CategoriesResponseModel
import com.example.plazapalm.networkcalls.*
import com.example.plazapalm.pref.PreferenceFile
import com.example.plazapalm.utils.CommonMethods
import dagger.hilt.android.lifecycle.HiltViewModel
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class SplashVM @Inject constructor(
    private var repository: Repository,
    private var cacheUtil: CacheUtil,
    pref: PreferenceFile,
) : ViewModel() {
    var token = ObservableField("")

    init {
        //  token.set(pref.retrieveKey("token"))
    }

    /**call isPremium Api..**/
    fun isPremiumStatusApi() {
        val body = JSONObject()
        body.put("Authorization", token.get())
        repository.makeCall(
            apiKey = ApiEnums.GET_PREMIUM_STATUS,
            loader = true,
            saveInCache = false,
            getFromCache = false,
            requestProcessor = object : ApiProcessor<Response<CategoriesResponseModel>> {
                override suspend fun sendRequest(retrofitApi: RetrofitApi): Response<CategoriesResponseModel> {
                    return retrofitApi.isPremiumStatus(token.get().toString())
                }

                override fun onResponse(res: Response<CategoriesResponseModel>) {
                    CommonMethods.showToast(CommonMethods.context, res.body()?.message.toString())
                }
            }
        )
    }
}