package com.example.ybebrightapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.ybebrightapp.databinding.ActivityRegisterBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = Firebase.firestore

        binding.btnRegister.setOnClickListener {
            with(binding) {
                val id = edtId.text.toString().trim()
                val name = edtName.text.toString()
                val nik = edtNik.text.toString().trim()
                val rek = edtRek.text.toString().trim()
                val phone = edtPhone.text.toString().trim()
                val email = edtEmail.text.toString().trim()
                val address = edtAddress.text.toString()

                if (id.isEmpty()) {
                    edtId.error = "Field ini harus diisi!"
                    return@setOnClickListener
                }
                if (name.isEmpty()) {
                    edtName.error = "Field ini harus diisi!"
                    return@setOnClickListener
                }
                if (nik.isEmpty()) {
                    edtNik.error = "Field ini harus diisi!"
                    return@setOnClickListener
                }
                if (rek.isEmpty()) {
                    edtRek.error = "Field ini harus diisi!"
                    return@setOnClickListener
                }
                if (phone.isEmpty()) {
                    edtPhone.error = "Field ini harus diisi!"
                    return@setOnClickListener
                }
                if (email.isEmpty()) {
                    edtEmail.error = "Field ini harus diisi!"
                    return@setOnClickListener
                }
                if (address.isEmpty()) {
                    edtAddress.error = "Field ini harus diisi!"
                    return@setOnClickListener
                }

                val data = hashMapOf(
                    "ID Referensi" to id,
                    "Nama" to name,
                    "NIK" to nik,
                    "Rek" to rek,
                    "Phone" to phone,
                    "Email" to email,
                    "Alamat" to address)

                db.collection("member")
                    .add(data)
                    .addOnSuccessListener {
                        Toast.makeText(this@RegisterActivity, "Anda berhasil mendaftar!", Toast.LENGTH_SHORT).show()
                        finish()
                    }.addOnFailureListener {
                        Toast.makeText(this@RegisterActivity, "Gagal mendaftar", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}