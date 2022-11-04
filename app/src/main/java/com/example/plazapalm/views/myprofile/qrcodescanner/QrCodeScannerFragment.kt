package com.example.plazapalm.views.myprofile.qrcodescanner

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.budiyev.android.codescanner.*
import com.example.plazapalm.R
import com.example.plazapalm.databinding.QrCodeScannerFragmentBinding
import com.example.plazapalm.utils.CommonMethods
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class QrCodeScannerFragment : Fragment(R.layout.qr_code_scanner_fragment) {
    private lateinit var codeScanner: CodeScanner
    private var binding: QrCodeScannerFragmentBinding? = null
    val viewModel: QrCodeScannerVM by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = QrCodeScannerFragmentBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.vm = viewModel
        codeScanner = CodeScanner(requireContext(), binding?.scannerView!!)
        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false // Whether to enable flash or not
        codeScanner.decodeCallback = DecodeCallback { barCodeResult ->
            activity?.runOnUiThread {
                if (!barCodeResult.equals("")) {
                    CommonMethods.showToast(requireContext(), "Code Scanned .. ")
                    Log.d("qrCodeResult-->", "barCodeResult" + barCodeResult.text.toString())
                    findNavController().navigateUp()
                } else {
                    CommonMethods.showToast(requireContext(), "Code not scanned")
                }
            }
        }
        codeScanner.errorCallback = ErrorCallback { barCodeError ->
            // or ErrorCallback.SUPPRESS
            activity?.runOnUiThread {

                Log.d("qrCodeError-->", "Error in Scanned Code.." + barCodeError.message)
                CommonMethods.showToast(requireContext(), "Error in Scanned Code..")

            }

        }
        binding?.scannerView?.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onStop() {
        super.onStop()
        codeScanner.stopPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        codeScanner.stopPreview()
    }
}