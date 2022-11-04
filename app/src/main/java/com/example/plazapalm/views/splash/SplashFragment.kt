package com.example.plazapalm.views.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.plazapalm.R
import com.example.plazapalm.databinding.SplashFragmentBinding
import com.example.plazapalm.datastore.DataStoreUtil
import com.example.plazapalm.datastore.LOGIN_DATA
import com.example.plazapalm.models.LoginData
import com.example.plazapalm.utils.CommonMethods
import com.example.plazapalm.utils.hideKeyboard
import com.example.plazapalm.utils.navigateWithId
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SplashFragment : Fragment(R.layout.splash_fragment) {
    @Inject
    lateinit var dataStoreUtil: DataStoreUtil
    private var binding: SplashFragmentBinding? = null
    private val viewModel: SplashVM by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SplashFragmentBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.vm = viewModel
        // viewModel.isPremiumStatusApi()
    }
    override fun onResume() {
        super.onResume()
        CommonMethods.statusBar(false)
        Handler(Looper.getMainLooper()).postDelayed(
            {
                CoroutineScope(Dispatchers.Main).launch {
                    dataStoreUtil.readObject(LOGIN_DATA, LoginData::class.java) { loginData ->
                        if (loginData != null) {
                            binding?.root?.navigateWithId(R.id.action_splash_to_dashBoardFragment)
                        } else {
                            binding?.root?.navigateWithId(R.id.action_splashFragment_to_loginFragment)
                        }
                    }
                }
            }, 2000
        )
        CommonMethods.context.hideKeyboard()
    }
}