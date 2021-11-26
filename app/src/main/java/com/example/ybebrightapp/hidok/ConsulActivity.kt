package com.example.ybebrightapp.hidok

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.ybebrightapp.R
import com.example.ybebrightapp.databinding.ActivityConsulBinding
import com.example.ybebrightapp.model.Agent
import com.example.ybebrightapp.model.Consul
import com.example.ybebrightapp.utils.hideKeyboard
import com.example.ybebrightapp.viewmodel.ViewModelFactory

class ConsulActivity : AppCompatActivity() {

    companion object {
        const val IMG_FRONT = "front"
        const val IMG_RIGHT = "right"
        const val IMG_LEFT = "left"
        private const val ERROR_MSG = "Field ini harus diisi!"
    }

    private lateinit var binding: ActivityConsulBinding
    private lateinit var preferences: SharedPreferences
    private var member: Agent? = null
    private val frag = CameraFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConsulBinding.inflate(layoutInflater)
        setContentView(binding.root)
        member = intent.getParcelableExtra("data")

        preferences = getSharedPreferences("myShared", MODE_PRIVATE)

        val factory = ViewModelFactory.getInstance(this)
        val viewModel = ViewModelProvider(this, factory)[HiDokViewModel::class.java]

        viewModel.uriFront.observe(this) {
            if (it != null) {
                Glide.with(this)
                    .load(it)
                    .centerCrop()
                    .into(binding.imgFront)
            }
        }

        viewModel.uriRight.observe(this) {
            if (it != null) {
                Glide.with(this)
                    .load(it)
                    .centerCrop()
                    .into(binding.imgRight)
            }
        }

        viewModel.uriLeft.observe(this) {
            if (it != null) {
                Glide.with(this)
                    .load(it)
                    .centerCrop()
                    .into(binding.imgLeft)
            }
        }

        binding.imgFront.setOnClickListener {
            openCamera(IMG_FRONT)
        }

        binding.imgRight.setOnClickListener {
            openCamera(IMG_RIGHT)
        }

        binding.imgLeft.setOnClickListener {
            openCamera(IMG_LEFT)
        }

        binding.btnSkip.setOnClickListener {
            val intent = Intent(this@ConsulActivity, HiDokActivity::class.java)
            intent.putExtra("data", member)
            startActivity(intent)
        }

        binding.btnSend.setOnClickListener {
            hideKeyboard()
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
                val alergi = when(rgAlergi.checkedRadioButtonId) {
                    R.id.rb_yes -> "Ya"
                    R.id.rb_no -> "Tidak"
                    else -> ""
                }

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

                if (alergi.isEmpty()) {
                    return@setOnClickListener
                }

                val text = "Nama: $name Umur: $age\n" +
                        "No. WA: $noWa\nNo. Wa Agen: $noWaAgen\nKeluhan: \n$keluhan\nBerapa lama: $since\n" +
                        "Pernah Berobat: $berobat\nTipe Kulit: $skinType\nRiwayat KB/suntik/implant:\n$riwayatKb\n" +
                        "Pola hidup/aktifitas sehari hari: $aktifitas"

                val data = Consul(
                    text,
                    viewModel.uriFront.value,
                    viewModel.uriRight.value,
                    viewModel.uriLeft.value)

                val intent = Intent(this@ConsulActivity, HiDokActivity::class.java)
                if (member == null) {
                    intent.putExtra("name", name)
                } else {
                    intent.putExtra("data", member)
                }
                intent.putExtra("consul", data)
                startActivity(intent)
            }
        }
    }

    private fun openCamera(req: String) {
        hideKeyboard()
        val bundle = Bundle()
        bundle.putString("req", req)
        binding.mainView.visibility = View.GONE
        frag.arguments = bundle
            supportFragmentManager.beginTransaction()
                .add(R.id.frame_cam, frag, CameraFragment::class.java.simpleName)
                .addToBackStack(null)
                .commit()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        frag.onDestroy()
        binding.mainView.visibility = View.VISIBLE
    }
}