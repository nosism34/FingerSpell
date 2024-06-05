package com.example.fingerspell.View

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fingerspell.R
import com.example.fingerspell.data.Finger
import com.example.fingerspell.databinding.ActivityHomeBinding
import com.example.fingerspell.getImageUri
import com.example.fingerspell.model.ReviewAdapter

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var adapter: ReviewAdapter
    private val list = ArrayList<Finger>()
    private var currentImageUri: Uri? = null

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adapter = ReviewAdapter(list)
        binding.rvFinger.layoutManager = GridLayoutManager(this, 2)
        binding.rvFinger.setHasFixedSize(true)
        binding.rvFinger.adapter = adapter
        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        list.addAll(getListFinger())
        adapter.notifyDataSetChanged()

        binding.Camera.setOnClickListener { startCamera() }
    }

    private fun getListFinger(): ArrayList<Finger> {
        val dataName = resources.getStringArray(R.array.data_name)
        val listFinger = ArrayList<Finger>()
        for (name in dataName) {
            val finger = Finger(name)
            listFinger.add(finger)
        }
        return listFinger
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri!!)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }


    private fun showImage() {
        currentImageUri?.let { uri ->
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }

}
