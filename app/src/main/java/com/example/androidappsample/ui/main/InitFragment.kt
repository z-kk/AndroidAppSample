package com.example.androidappsample.ui.main

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.content.pm.verify.domain.DomainVerificationManager
import android.content.pm.verify.domain.DomainVerificationUserState
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.androidappsample.BluetoothUtil
import com.example.androidappsample.R

class InitFragment : Fragment() {
    companion object {
        fun newInstance() = InitFragment()
    }

    interface InitFragmentListener {
        fun onInitFragmentFinish()
    }

    private lateinit var viewModel: InitViewModel
    private lateinit var listener: InitFragmentListener
    private lateinit var btUtil: BluetoothUtil
    private var showingDialog = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is InitFragmentListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement InitFragmentListener")
        }

        btUtil = BluetoothUtil(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[InitViewModel::class.java]

        checkState()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_init, container, false)
    }

    private fun checkState() {
        if (showingDialog || viewModel.launchingActivity) {
            return
        } else if (!viewModel.defaultSettingsOk) {
            defaultSettings()
        } else if (!viewModel.bluetoothEnabled) {
            if (btUtil.adapter == null) {
                finish()
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                bluetoothPermissionResult.launch(android.Manifest.permission.BLUETOOTH_CONNECT)
            } else {
                bluetoothPermissionResult.launch(android.Manifest.permission.BLUETOOTH)
            }
            viewModel.launchingActivity = true
        } else {
            finish()
        }
    }

    private fun defaultSettings() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val context = requireContext()
            context.getSystemService(DomainVerificationManager::class.java)
                .getDomainVerificationUserState(context.packageName)?.let { userState ->
                    val host = getString(R.string.app_links_host)
                    if (userState.hostToStateMap[host] == DomainVerificationUserState.DOMAIN_STATE_NONE) {
                        AlertDialog.Builder(context)
                            .setMessage("Add link [$host]")
                            .setPositiveButton("OK") { _, _ ->
                                defaultSettingsResult.launch(
                                    Intent(
                                        Settings.ACTION_APP_OPEN_BY_DEFAULT_SETTINGS,
                                        Uri.parse("package:${context.packageName}")
                                    )
                                )
                                viewModel.launchingActivity = true
                                showingDialog = false
                            }
                            .setCancelable(false)
                            .show()
                        showingDialog = true
                    } else {
                        viewModel.defaultSettingsOk = true
                    }
                }
        } else {
            viewModel.defaultSettingsOk = true
        }

        checkState()
    }

    private val defaultSettingsResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        viewModel.launchingActivity = false
        checkState()
    }

    private val bluetoothPermissionResult = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        viewModel.launchingActivity = false
        if (granted) {
            if (btUtil.adapter?.isEnabled == false) {
                enableBluetoothResult.launch(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
                viewModel.launchingActivity = true
            } else {
                viewModel.bluetoothEnabled = true
            }
            checkState()
        } else {
            AlertDialog.Builder(requireContext())
                .setTitle("Error")
                .setMessage("Bluetooth not granted!")
                .setOnCancelListener {
                    finish()
                }
                .show()
        }
    }

    private val enableBluetoothResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        viewModel.launchingActivity = false
        if (it.resultCode == RESULT_OK) {
            viewModel.bluetoothEnabled = true
        }
        checkState()
    }

    private fun finish() {
        listener.onInitFragmentFinish()
    }
}