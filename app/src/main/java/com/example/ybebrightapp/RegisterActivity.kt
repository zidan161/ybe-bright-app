package com.example.ybebrightapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ybebrightapp.databinding.ActivityRegisterBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import android.widget.ArrayAdapter

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val status = arrayOf("Reseller", "Agen", "Agen Tunggal")

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            status
        )

        val dropdown = binding.edtDaftar

        dropdown.setAdapter(adapter)

        val db = Firebase.database("https://ybebright-app-default-rtdb.asia-southeast1.firebasedatabase.app/")

        binding.btnRegister.setOnClickListener {
            with(binding) {
                val id = edtId.text.toString().trim()
                val name = edtName.text.toString()
                val nik = edtNik.text.toString().trim()
                val rek = edtRek.text.toString().trim()
                val namaRek = edtNameRek.text.toString()
                val bank = edtBank.text.toString()
                val cabangBank = edtCabangBank.text.toString()
                val phone = edtPhone.text.toString().trim()
                val email = edtEmail.text.toString().trim()
                val city = edtCity.text.toString().trim()
                val address = edtAddress.text.toString()

                var isDone = true

                if (name.isEmpty()) {
                    edtName.error = "Field ini harus diisi!"
                    isDone = false
                }
                if (nik.isEmpty()) {
                    edtNik.error = "Field ini harus diisi!"
                    isDone = false
                }
                if (rek.isEmpty()) {
                    edtRek.error = "Field ini harus diisi!"
                    isDone = false
                }
                if (bank.isEmpty()) {
                    edtBank.error = "Field ini harus diisi!"
                    isDone = false
                }
                if (cabangBank.isEmpty()) {
                    edtCabangBank.error = "Field ini harus diisi!"
                    isDone = false
                }
                if (phone.isEmpty()) {
                    edtPhone.error = "Field ini harus diisi!"
                    isDone = false
                }
                if (email.isEmpty()) {
                    edtEmail.error = "Field ini harus diisi!"
                    isDone = false
                }
                if (city.isEmpty()) {
                    edtCity.error = "Field ini harus diisi!"
                    isDone = false
                }
                if (address.isEmpty()) {
                    edtAddress.error = "Field ini harus diisi!"
                    isDone = false
                }

                if (isDone) {
                    val data = hashMapOf(
                        "idMember" to id,
                        "nama" to name,
                        "keagenan" to dropdown.text.toString(),
                        "nik" to nik,
                        "no_rek" to rek,
                        "nama_rek" to namaRek,
                        "nama_bank" to bank,
                        "cabang_bank" to cabangBank,
                        "no_hp" to phone,
                        "email" to email,
                        "kota" to city,
                        "alamat" to address
                    )

                    db.getReference("member_baru")
                        .child(name).setValue(data).addOnSuccessListener {
                            Snackbar.make(binding.root, "Anda sudah mendaftar, harap tunggu konfirmasi", Snackbar.LENGTH_LONG).show()
                            finish()
                        }
                }
            }
        }
    }
}