package com.example.ybebrightapp.mainpage

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.ybebrightapp.ProfileViewModel
import com.example.ybebrightapp.R
import com.example.ybebrightapp.model.Agent
import com.example.ybebrightapp.databinding.FragmentProfileBinding
import com.example.ybebrightapp.databinding.PoinLayoutBinding
import com.example.ybebrightapp.hidok.FriendlyMessageAdapter.Companion.TAG
import com.example.ybebrightapp.hidok.MyOpenDocumentContract
import com.example.ybebrightapp.login.LoginActivity
import com.example.ybebrightapp.main.MainActivity
import com.example.ybebrightapp.model.Poin
import com.example.ybebrightapp.utils.showAlert
import com.example.ybebrightapp.viewmodel.ViewModelFactory
import com.github.drjacky.imagepicker.ImagePicker
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

class ProfileFragment : Fragment() {

    companion object {
        private const val PATH_PROFILE = "profile"
    }

    private lateinit var fragmentProfileBinding: FragmentProfileBinding
    private var data: Agent? = null
    private lateinit var database: FirebaseDatabase

    private val getPhoto = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val uri = it.data?.data!!
            setPhoto(uri)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        fragmentProfileBinding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return fragmentProfileBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity != null) {
            data = arguments?.getParcelable(MainActivity.DATA_KEY)

            val factory = ViewModelFactory.getInstance(requireContext())
            val viewModel = ViewModelProvider(this, factory)[ProfileViewModel::class.java]

            with(fragmentProfileBinding) {
                tvStatus.text = data?.status?.uppercase()
                if (data?.status != "customer") {
                    database =
                        Firebase.database("https://ybebright-app-default-rtdb.asia-southeast1.firebasedatabase.app/")
                        database.getReference(PATH_PROFILE).child(data?.id.toString())
                            .addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    if (snapshot.value != null) {
                                        Glide.with(this@ProfileFragment).load(snapshot.value)
                                            .placeholder(R.drawable.ic_account_circle)
                                            .into(imgProfile)
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    TODO("Not yet implemented")
                                }
                            })
                }
                if (data != null) {
                    tvName.text = data?.name
                    tvValId.text = data?.id
                    tvValPoin.text = data?.poin.toString()
                    tvNik.text = data?.nik
                    tvPhone.text = data?.phone
                    tvEmail.text = data?.email
                    tvAddress.text = "${data?.address}, ${data?.city}"
                } else {
                    btnLogin.visibility = View.VISIBLE
                    cvInfo.visibility = View.INVISIBLE
                    tvValId.visibility = View.INVISIBLE
                    tvValPoin.visibility = View.INVISIBLE
                    tvId.visibility = View.INVISIBLE
                    tvPoin.visibility = View.INVISIBLE
                    btnLogout.visibility = View.GONE
                }
                btnLogin.setOnClickListener {
                    val intent = Intent(context, LoginActivity::class.java)
                    startActivity(intent)
                }
                tvValPoin.setOnClickListener {
                    val list = viewModel.getPoin(data?.poin!!)
                    setRadio(list)
                }
                imgProfile.setOnClickListener {
                    getPhoto.launch(
                        ImagePicker.with(requireActivity())
                            .galleryOnly()
                            .cropOval()
                            .createIntent()
                    )
                }
            }
        }
    }

    private fun setPhoto(uri: Uri) {
        val storageReference = Firebase.storage
            .getReference("img_profile")
            .child(data?.id ?: "")
        putImageInStorage(storageReference, uri)
    }

    private fun putImageInStorage(storageReference: StorageReference, uri: Uri) {
        // First upload the image to Cloud Storage
        storageReference.putFile(uri)
            .addOnSuccessListener(
                requireActivity()
            ) { taskSnapshot -> // After the image loads, get a public downloadUrl for the image
                // and add it to the message.
                taskSnapshot.metadata!!.reference!!.downloadUrl
                    .addOnSuccessListener { uri ->
                        loadImageIntoView(fragmentProfileBinding.imgProfile, uri.toString())
                        database.getReference(PATH_PROFILE)
                            .child("${data?.id}")
                            .setValue(uri.toString())
                    }
            }
            .addOnFailureListener(requireActivity()) { e ->
                Log.w(
                    TAG,
                    "Image upload task was unsuccessful.",
                    e
                )
            }
    }

    private fun loadImageIntoView(view: ImageView, url: String) {
        if (url.startsWith("gs://")) {
            val storageReference = Firebase.storage.getReferenceFromUrl(url)
            storageReference.downloadUrl
                .addOnSuccessListener { uri ->
                    val downloadUrl = uri.toString()
                    Glide.with(view.context)
                        .load(downloadUrl)
                        .into(view)
                }
                .addOnFailureListener { e ->
                    Log.w(
                        TAG,
                        "Getting download url was not successful.", e
                    )
                }
        } else {
            Glide.with(view.context).load(url).into(view)
        }
    }

    private fun setRadio(data: List<Poin>) {
        val bind = PoinLayoutBinding.inflate(layoutInflater)
        for (i in data) {
            val rb = RadioButton(context)
            rb.text = i.tukar
            bind.rgMain.addView(rb)
        }
        showAlert(requireContext(), bind.root, true).show()
    }
}