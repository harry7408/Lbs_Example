package com.sesac.lbshomework

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.sesac.lbshomework.adapter.LocationAdapter
import com.sesac.lbshomework.common.REQUEST_CODE
import com.sesac.lbshomework.databinding.ActivityMainBinding
import com.sesac.lbshomework.model.LatLang

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val locationService: LocationService = LocationService()
    private val locationAdapter = LocationAdapter(mutableListOf())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }

        with(binding) {
            serviceButton.setOnClickListener {
                checkPermission()
            }
            locationRecyclerView.apply {
                layoutManager =
                    LinearLayoutManager(
                        this@MainActivity,
                        LinearLayoutManager.VERTICAL,
                        false
                    )
                    adapter=locationAdapter
            }
        }
    }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            when {
                (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED) -> {
                    val intent = Intent(this, LocationService::class.java)
                    startService(intent)
                }

                shouldShowRequestPermissionRationale(
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) && shouldShowRequestPermissionRationale(
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) -> {
                    showDialog()
                }

                else -> {
                    requestPermission()
                }
            }
        }
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ), REQUEST_CODE
            )
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                ), REQUEST_CODE
            )
        }
    }

    private fun showDialog() {
        AlertDialog.Builder(this)
            .setMessage("위치 정보르 가져오기 위해선 권한이 필요합니다")
            .setNegativeButton("Cancel", null)
            .setPositiveButton("OK") { _, _ ->
                requestPermission()
            }.show()
    }
}

