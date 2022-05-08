package com.zidan.ybebrightapp.login

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.zidan.ybebrightapp.model.Agent
import com.zidan.ybebrightapp.RegisterActivity
import com.zidan.ybebrightapp.databinding.ActivityLoginBinding
import com.zidan.ybebrightapp.main.MainActivity
import com.zidan.ybebrightapp.utils.hideKeyboard
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var preferences: SharedPreferences
    private lateinit var mDatabase: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferences = getSharedPreferences("myShared", MODE_PRIVATE)

        mDatabase = Firebase.database("https://ybebright-app-default-rtdb.asia-southeast1.firebasedatabase.app/")

        val idSimpan = preferences.getString("id", null)

        var fixedData: Agent? = null

        val id = MutableLiveData<String>()

        binding.btnSkip.setOnClickListener {
            hideKeyboard()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.edtId.setOnFocusChangeListener { _, hasFocus ->
            binding.imgCheck.visibility = View.GONE
            if (!hasFocus) {
                fixedData = null
                val idText = binding.edtId.text.toString()
                if (idSimpan != null) {
                    if (idText != idSimpan) {
                        binding.edtId.error = "Aplikasi hanya boleh satu akun"
                        return@setOnFocusChangeListener
                    }
                }
                binding.pgId.visibility = View.VISIBLE

                id.value = idText
                println(id.value)
            }
        }

        id.observe(this) { idMember ->

            mDatabase.getReference("member").addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    val root = snapshot.children

                    main@ for (place in root) {
                        for (agent in place.children) {
                            if (agent.key == idMember) {
                                fixedData = agent.getValue(Agent::class.java)
                                break@main
                            }
                        }
                    }

                    if (fixedData == null) {
                        binding.pgId.visibility = View.GONE
                        binding.edtId.error = "ID tidak terdaftar"
                    } else {
                        binding.pgId.visibility = View.GONE
                        binding.imgCheck.visibility = View.VISIBLE
                        binding.edtId.error = null
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }

        binding.btnLogin.setOnClickListener {
            hideKeyboard()
            CoroutineScope(Dispatchers.Main).launch {
                if (fixedData != null) {
                    if (binding.edtPhone.text.toString() == fixedData?.no_hp) {
                        if (idSimpan == null) {
                            preferences.edit().putString("id", fixedData?.idMember).apply()
                        }
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        intent.putExtra(MainActivity.DATA_KEY, fixedData)
                        startActivity(intent)
                        finish()
                    } else {
                        binding.edtPhone.error = "Nomor telepon tidak sesuai!"
                    }
                }
            }
        }

        binding.btnRegister.setOnClickListener {
            hideKeyboard()
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}