package com.gemicle.locationkeeper.ui.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.gemicle.locationkeeper.BuildConfig
import com.gemicle.locationkeeper.LocationKeeperApplication
import com.gemicle.locationkeeper.R
import com.gemicle.locationkeeper.service.LocationUpdatesService
import com.gemicle.locationkeeper.utils.Utils.isServiceRunning
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var locationPresenter: LocationPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        locationPresenter = (application as LocationKeeperApplication).locationPresenter
        locationPresenter.tryUploadDataFromDb()
        if (!checkPermissions()) {
            requestPermissions()
        }
        setupButtonListeners()
        setButtonsState(isServiceRunning(LocationUpdatesService::class.java))
    }

    private fun setupButtonListeners() {
        start?.setOnClickListener {
            if (!checkPermissions()) {
                requestPermissions()
            } else {
                startService(Intent(this, LocationUpdatesService::class.java))
                setButtonsState(true)
            }
        }

        stop?.setOnClickListener {
            stopService(Intent(this, LocationUpdatesService::class.java))
            setButtonsState(false)
        }
    }

    private fun checkPermissions(): Boolean {
        return hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                && hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    private fun hasPermission(permission: String): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this@MainActivity,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_PERMISSIONS_REQUEST_CODE
        )
    }

    private fun showSettingsSnack() {
        Snackbar.make(
            findViewById(R.id.activity_main),
            R.string.permission_denied_explanation,
            Snackbar.LENGTH_INDEFINITE
        )
            .setAction(R.string.settings) {
                goToSettings()
            }
            .show()
    }

    private fun goToSettings() {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri = Uri.fromParts(
            "package",
            BuildConfig.APPLICATION_ID, null
        )
        intent.data = uri
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)    }

    private fun setButtonsState(isLocationUpdates: Boolean) {
        if (isLocationUpdates) {
            start?.isEnabled = false
            stop?.isEnabled = true
        } else {
            start?.isEnabled = true
            stop?.isEnabled = false
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE && grantResults.getOrNull(0) != PackageManager.PERMISSION_GRANTED) {
            showSettingsSnack()
        }
    }

    companion object {
        // Used in checking for runtime permissions.
        private const val REQUEST_PERMISSIONS_REQUEST_CODE = 34
    }
}
