package com.example.ybebrightapp.hidok

import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.example.ybebrightapp.databinding.ActivityConsulBinding
import com.example.ybebrightapp.model.Agent
import com.example.ybebrightapp.model.Consul
import com.github.drjacky.imagepicker.ImagePicker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ConsulActivity : AppCompatActivity() {

    companion object {
        private const val IMG_FRONT = "front"
        private const val IMG_RIGHT = "right"
        private const val IMG_LEFT = "left"
        private const val ERROR_MSG = "Field ini harus diisi!"
    }

    private lateinit var binding: ActivityConsulBinding
    private lateinit var preferences: SharedPreferences

    private val activityResultLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions())
        { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConsulBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val member = intent.getParcelableExtra<Agent>("data")

        preferences = getSharedPreferences("myShared", MODE_PRIVATE)

        activityResultLauncher.launch(
            arrayOf(Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE)
        )

        val capture = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                when (preferences.getString("key", "")) {
                    IMG_FRONT -> {
                        preferences.edit().putString(IMG_FRONT, it.data?.data.toString()).apply()
                    }
                    IMG_RIGHT -> {
                        preferences.edit().putString(IMG_RIGHT, it.data?.data.toString()).apply()
                    }
                    IMG_LEFT -> {
                        preferences.edit().putString(IMG_LEFT, it.data?.data.toString()).apply()
                    }
                }
            }
        }

        binding.imgFront.setOnClickListener {
            preferences.edit().putString("key", IMG_FRONT).apply()
            CoroutineScope(Dispatchers.Main).launch {
                capture.launch(
                    ImagePicker.with(this@ConsulActivity)
                        .cameraOnly()
                        .cropSquare()
                        .createIntent()
                )
            }
        }

        binding.imgRight.setOnClickListener {
            preferences.edit().putString("key", IMG_RIGHT).apply()
            CoroutineScope(Dispatchers.Main).launch {
                capture.launch(
                    ImagePicker.with(this@ConsulActivity)
                    .cameraOnly()
                    .cropSquare()
                    .createIntent()
                )
            }
        }

        binding.imgLeft.setOnClickListener {
            preferences.edit().putString("key", IMG_LEFT).apply()
            CoroutineScope(Dispatchers.Main).launch {
                capture.launch(
                    ImagePicker.with(this@ConsulActivity)
                        .cameraOnly()
                        .cropSquare()
                        .createIntent()
                )
            }
        }

        binding.btnSend.setOnClickListener {
            with(binding) {
                val name = edtName.editText?.text.toString()
                val age = edtAge.editText?.text.toString()
                val noWa = edtWa.editText?.text.toString().trim()
                val noWaAgen = edtWaAgen.editText?.text.toString().trim()
                val keluhan = edtKeluhan.editText?.text.toString()
                val since = edtSince.editText?.text.toString()
                val berobat = edtSdhBerobat.editText?.text.toString()
                val skinType = edtSkinType.editText?.text.toString()
                val riwayatKb = edtRiwayatKb.editText?.text.toString()
                val aktifitas = edtAktifitas.editText?.text.toString()

                if (name.isEmpty()) {
                    edtName.editText?.error = ERROR_MSG
                    return@setOnClickListener
                }

                if (age.isEmpty()) {
                    edtAge.editText?.error = ERROR_MSG
                    return@setOnClickListener
                }

                if (noWa.isEmpty()) {
                    edtWa.editText?.error = ERROR_MSG
                    return@setOnClickListener
                }

                if (noWaAgen.isEmpty()) {
                    edtWaAgen.editText?.error = ERROR_MSG
                    return@setOnClickListener
                }

                if (keluhan.isEmpty()) {
                    edtKeluhan.editText?.error = ERROR_MSG
                    return@setOnClickListener
                }

                if (since.isEmpty()) {
                    edtSince.editText?.error = ERROR_MSG
                    return@setOnClickListener
                }

                if (berobat.isEmpty()) {
                    edtSdhBerobat.editText?.error = ERROR_MSG
                    return@setOnClickListener
                }

                if (skinType.isEmpty()) {
                    edtSkinType.editText?.error = ERROR_MSG
                    return@setOnClickListener
                }

                if (riwayatKb.isEmpty()) {
                    edtRiwayatKb.editText?.error = ERROR_MSG
                    return@setOnClickListener
                }

                if (aktifitas.isEmpty()) {
                    edtAktifitas.editText?.error = ERROR_MSG
                    return@setOnClickListener
                }

                val text = "Nama: $name Umur: $age\n" +
                        "No. WA: $noWa\nNo. Wa Agen: $noWaAgen\nKeluhan: \n$keluhan\nBerapa lama: $since\n" +
                        "Pernah Berobat: $berobat\nTipe Kulit: $skinType\nRiwayat KB/suntik/implant:\n$riwayatKb\n" +
                        "Pola hidup/aktifitas sehari hari: $aktifitas"

                val data = Consul(
                    text,
                    preferences.getString(IMG_FRONT, "")?.toUri(),
                    preferences.getString(IMG_RIGHT, "")?.toUri(),
                    preferences.getString(IMG_LEFT, "")?.toUri())

                val intent = Intent(this@ConsulActivity, HiDokActivity::class.java)
                intent.putExtra("data", member)
                intent.putExtra("consul", data)
                startActivity(intent)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        CoroutineScope(Dispatchers.Main).launch {
            if (preferences.getString(IMG_FRONT, null) != null) {
                Glide.with(this@ConsulActivity)
                    .load(preferences.getString(IMG_FRONT, ""))
                    .centerCrop()
                    .into(binding.imgFront)
            }
            if (preferences.getString(IMG_RIGHT, null) != null) {
                Glide.with(this@ConsulActivity)
                    .load(preferences.getString(IMG_RIGHT, ""))
                    .centerCrop()
                    .into(binding.imgRight)
            }
            if (preferences.getString(IMG_LEFT, null) != null) {
                Glide.with(this@ConsulActivity)
                    .load(preferences.getString(IMG_LEFT, ""))
                    .centerCrop()
                    .into(binding.imgLeft)
            }
        }
    }
}