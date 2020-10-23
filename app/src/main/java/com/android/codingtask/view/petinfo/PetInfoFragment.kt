package com.android.codingtask.view.petinfo

import android.annotation.SuppressLint
import android.net.http.SslError
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.ProgressBar
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.android.codingtask.databinding.PetInfoFragmentBinding

class PetInfoFragment : Fragment() {

    companion object {
        const val MAX_PROGRESS = 100
    }

    private val petInfoViewModel by viewModels<PetInfoViewModel>()
    private lateinit var binding: PetInfoFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Check if there's history
                if (binding.webviewPetInfo.canGoBack()) {
                    binding.webviewPetInfo.goBack()
                } else if (isEnabled) {
                    isEnabled = false
                    // If there's no web page history, exit the activity)
                    requireActivity().onBackPressed()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = PetInfoFragmentBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = petInfoViewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initWebView()

        configureWebClient()

        arguments?.let {
            requireActivity().title = PetInfoFragmentArgs.fromBundle(it).title
            loadContentUrl(PetInfoFragmentArgs.fromBundle(it).contentUtl)
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        petInfoViewModel.webViewErrorOccurred.value = false

        binding.webviewPetInfo.apply {
            settings.javaScriptEnabled = true
            settings.loadWithOverviewMode = true
            settings.useWideViewPort = true
            webViewClient = object : WebViewClient() {
                override
                fun onReceivedSslError(
                    view: WebView?,
                    handler: SslErrorHandler?,
                    error: SslError?
                ) {
                    handler?.proceed()
                }

                override fun onReceivedError(
                    view: WebView?,
                    request: WebResourceRequest?,
                    error: WebResourceError?
                ) {
                    super.onReceivedError(view, request, error)
                    petInfoViewModel.webViewErrorOccurred.value = true
                }
            }
        }

    }

    private fun configureWebClient() {
        binding.webviewPetInfo.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(
                view: WebView,
                newProgress: Int
            ) {
                super.onProgressChanged(view, newProgress)
                binding.webviewProgressBar.progress = newProgress
                if (newProgress < MAX_PROGRESS && binding.webviewProgressBar.visibility == ProgressBar.GONE) {
                    binding.webviewProgressBar.visibility = ProgressBar.VISIBLE
                }
                if (newProgress == MAX_PROGRESS) {
                    binding.webviewProgressBar.visibility = ProgressBar.GONE
                }
            }
        }
    }

    private fun loadContentUrl(pageUrl: String) {
        binding.webviewPetInfo.loadUrl(pageUrl)
    }

}