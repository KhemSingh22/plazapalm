package com.example.plazapalm.networkcalls


import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import com.example.plazapalm.MainActivity
import com.example.plazapalm.R
import com.example.plazapalm.datastore.DataStoreUtil
import com.example.plazapalm.utils.CommonMethods
import com.example.plazapalm.utils.CommonMethods.dialog
import com.example.plazapalm.utils.hideProgress
import com.example.plazapalm.utils.isNetworkAvailable
import com.example.plazapalm.utils.showProgress
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.schedule


class Repository @Inject constructor(
    private val retrofitApi: RetrofitApi,
    private val cacheUtil: CacheUtil,
    private val dataStoreUtil: DataStoreUtil
) {
    var aD: AlertDialog.Builder? = null
    fun <T : Any> makeCall(
        apiKey: ApiEnums,
        loader: Boolean,
        saveInCache: Boolean,
        getFromCache: Boolean = false,
        requestProcessor: ApiProcessor<Response<T>>
    ) {
        val activity = MainActivity.context.get()!!
        if (getFromCache && cacheUtil.snapshot().containsKey(apiKey)) {
            Log.d("cacheUtil", "========${cacheUtil[apiKey]}")
            requestProcessor.onResponse(cacheUtil[apiKey] as Response<T>)
            return
        }
        if (!activity.isNetworkAvailable()) {
            CommonMethods.showToast(CommonMethods.context, "You are offline.")

            // activity.showNegativeAlerter(activity.getString(R.string.your_device_offline))
            return
        }
        if (loader) {
            activity.showProgress()
        }

        val dataResponse: Flow<Response<Any>?> = flow {
            val response = requestProcessor.sendRequest(retrofitApi) as Response<Any>
            emit(response)
        }.flowOn(Dispatchers.IO)

        CoroutineScope(Dispatchers.Main).launch {
            dataResponse.catch { exception ->
                exception.printStackTrace()
                Log.d("exception", "errorException$exception")
                // activity.showProgress()
                hideProgress()
                //activity.showNegativeAlerter(exception.message ?: "")
                showErrorDialog()
            }.collect { response ->
                Log.d("resCodeIs", "====${response?.code()}")
                Timer().schedule(2000) {
                    hideProgress()
                }
                when {
                    response?.code() in 100..199 -> {
                        /**Informational*/
                        requestProcessor.onError(
                            activity.resources?.getString(R.string.some_error_occured) ?: ""
                        )
                        CommonMethods.showToast(CommonMethods.context, "Oops! Something went wrong")

                        //    activity.showNegativeAlerter(activity.resources?.getString(R.string.some_error_occured) ?: "")
                    }
                    response?.isSuccessful == true -> {
                        /**Success*/
                        Log.d("successBody", "====${response.body()}")
                        if (saveInCache)
                            cacheUtil.put(apiKey, response)
                        requestProcessor.onResponse(response as Response<T>)
                    }
                    response?.code() in 300..399 -> {
                        /**Redirection*/
                        requestProcessor.onError(
                            activity.resources?.getString(R.string.some_error_occured) ?: ""
                        )

                        // activity.showNegativeAlerter(activity.resources?.getString(R.string.some_error_occured) ?: "")
                    }
                    response?.code() == 401 -> {
                        /**UnAuthorized*/
                        Log.d("errorBody", "====${response.errorBody()?.string()}")
                        CommonMethods.showToast(CommonMethods.context, "Oops! Something went wrong")
                        requestProcessor.onError(activity.resources?.getString(R.string.some_error_occured) ?: "")
                        getRefreshToken()
                        dataStoreUtil.clearDataStore {

                        }
                        //activity.sessionExpired()
                        requestProcessor.onError("unAuthorized")
                    }
                    response?.code() == 404 -> {
                        /**Page Not Found*/
                        requestProcessor.onError(
                            activity.resources?.getString(R.string.some_error_occured) ?: ""
                        )
                        CommonMethods.showToast(CommonMethods.context, "Oops! Something went wrong")

                        // activity.showNegativeAlerter(activity.resources?.getString(R.string.some_error_occured) ?: "")
                    }
                    response?.code() in 500..599 -> {
                        /**ServerErrors*/
                        requestProcessor.onError(
                            activity.resources?.getString(R.string.some_error_occured) ?: ""
                        )
                        CommonMethods.showToast(CommonMethods.context, "Oops! Something went wrong")

                        // activity.showNegativeAlerter(activity.resources?.getString(R.string.some_error_occured) ?: "")
                    }
                    else -> {
                        /**ClientErrors*/
                        val res = response?.errorBody()!!.string()
                        val jsonObject = JSONObject(res)
                        when {
                            jsonObject.has("message") -> {
                                requestProcessor.onError(jsonObject.getString("message"))
                                if (!jsonObject.getString("message").equals("Data not found", true))
                                    Toast.makeText(
                                        MainActivity.context.get(),
                                        jsonObject.getString("message"),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                //activity.showNegativeAlerter(jsonObject.getString("message"))
                            }
                            else -> {
                                requestProcessor.onError(
                                    activity.resources?.getString(R.string.some_error_occured) ?: ""
                                )
                                // activity.showNegativeAlerter(activity.resources?.getString(R.string.some_error_occured) ?: "")
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getRefreshToken() {
    }

    private fun showErrorDialog() {

        /*   if (aD == null) {
               aD = AlertDialog.Builder(MainActivity.context.get())
               aD?.setTitle("Oops! Something went wrong. Please try again later.")
               aD?.setCancelable(false)
               aD?.setPositiveButton("Ok") { dialogInterface, _ ->
                   dialogInterface.cancel()
                   dialogInterface.dismiss()
               }
               aD?.create()

               aD?.show()

           }*/

        if (dialog != null && dialog?.isShowing!!) {
            dialog?.dismiss()
        } else {
            dialog = Dialog(CommonMethods.context, android.R.style.Theme_Dialog)
            dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog?.setContentView(R.layout.repository_alert)
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog?.window?.attributes?.width = ViewGroup.LayoutParams.MATCH_PARENT
            dialog?.setCancelable(false)
            dialog?.findViewById<AppCompatTextView>(R.id.tvErrorOk)?.setOnClickListener {
                dialog?.dismiss()
            }
            dialog?.show()

        }

    }

}
