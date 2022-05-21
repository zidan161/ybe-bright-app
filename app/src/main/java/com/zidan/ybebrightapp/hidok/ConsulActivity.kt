package com.zidan.ybebrightapp.hidok

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.CheckBox
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.zidan.ybebrightapp.R
import com.zidan.ybebrightapp.databinding.ActivityConsulBinding
import com.zidan.ybebrightapp.model.Agent
import com.zidan.ybebrightapp.model.Consul
import com.zidan.ybebrightapp.utils.hideKeyboard
import com.zidan.ybebrightapp.viewmodel.ViewModelFactory

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

        if (member == null) {
            binding.mainView.visibility = View.GONE
            binding.sorryView.visibility = View.VISIBLE
        }

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

        val adapterKulit = createDropdown(listOf("Normal", "Kering", "Berminyak", "Sensitif", "Kombinasi"))
        val adapterKb = createDropdown(listOf("Pil", "Suntik", "Implant", "Tidak KB"))
        val genderAdapter = createDropdown(listOf("Laki laki", "Perempuan"))
        val mensAdapter = createDropdown(listOf("Ya, Lancar", "Tidak Lancar"))
        var isMan = false

        binding.edtSkinTypeVal.setAdapter(adapterKulit)

        binding.edtRiwayatKbVal.setAdapter(adapterKb)

        binding.edtGenderVal.setAdapter(genderAdapter)
        binding.edtGenderVal.setOnItemClickListener { _, _, i, _ ->
            if (i == 0) {
                binding.edtMens.visibility = View.GONE
                binding.edtRiwayatKb.visibility = View.GONE
                isMan = true
            } else {
                binding.edtMens.visibility = View.VISIBLE
                binding.edtRiwayatKb.visibility = View.VISIBLE
                isMan = false
            }
        }

        binding.edtMensVal.setAdapter(mensAdapter)

        binding.btnSend.setOnClickListener {
            hideKeyboard()
            with(binding) {
                val name = edtName.editText?.text.toString()
                val age = edtAge.editText?.text.toString()
                val gender = edtGender.editText?.text.toString()
                val noWa = edtWa.editText?.text.toString().trim()
                val noWaAgen = edtWaAgen.editText?.text.toString().trim()
                val keluhan = edtKeluhan.editText?.text.toString()
                val since = edtSince.editText?.text.toString()
                val berobat = edtSdhBerobat.editText?.text.toString()
                val skinType = edtSkinTypeVal.text.toString()
                val riwayatKb = edtRiwayatKbVal.text.toString()
                val mens = edtMensVal.text.toString()

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

                if (gender.isEmpty()) {
                    edtGender.editText?.error = ERROR_MSG
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

                if (riwayatKb.isEmpty() && !isMan) {
                    edtRiwayatKb.editText?.error = ERROR_MSG
                    return@setOnClickListener
                }

                if (mens.isEmpty() && !isMan) {
                    edtMens.editText?.error = ERROR_MSG
                    return@setOnClickListener
                }

                if (alergi.isEmpty()) {
                    return@setOnClickListener
                }

                val polaHidup: String = isChecked(listOf(cbSun, cbBegadang, cbDrugs))
                val polaMakan: String = isChecked(listOf(cbSmoke, cbAlcohol, cbInstant, cbGorengan, cbNuts, cbSantan, cbJeroan, cbSeafood))

                var kb = "Riwayat KB/suntik/implant:$riwayatKb\n"
                var mensText = "$mens\n"

                if (isMan) {
                    kb = ""
                    mensText = ""
                }

                val text = "Nama: $name Umur: $age\nJenis Kelamin: $gender\n" +
                        "No. WA: $noWa\nNo. Wa Agen: $noWaAgen\nKeluhan: \n$keluhan\nBerapa lama: $since\n" +
                        "Pernah Berobat: $berobat\nTipe Kulit: $skinType\n$kb" +
                        "Pola hidup:\n$polaHidup Pola Makan:\n$polaMakan$mensText"

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

    private fun createDropdown(data: List<String>): ArrayAdapter<String> {
        return ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            data
        )
    }

    private fun isChecked(view: List<CheckBox>): String {
        var text = ""
        view.forEach {
            if (it.isChecked) {
                text += "- ${it.text}\n"
            }
        }
        return text
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