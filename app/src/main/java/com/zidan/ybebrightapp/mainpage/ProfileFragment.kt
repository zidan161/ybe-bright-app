package com.zidan.ybebrightapp.mainpage

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
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.github.drjacky.imagepicker.ImagePicker
import com.zidan.ybebrightapp.ProfileViewModel
import com.zidan.ybebrightapp.checkout.CityAdapter
import com.zidan.ybebrightapp.checkout.ProvinceAdapter
import com.zidan.ybebrightapp.databinding.EditAddressBinding
import com.zidan.ybebrightapp.model.Agent
import com.zidan.ybebrightapp.databinding.FragmentProfileBinding
import com.zidan.ybebrightapp.databinding.PoinLayoutBinding
import com.zidan.ybebrightapp.hidok.FriendlyMessageAdapter.Companion.TAG
import com.zidan.ybebrightapp.main.MainActivity
import com.zidan.ybebrightapp.model.Poin
import com.zidan.ybebrightapp.product.ProductViewModel
import com.zidan.ybebrightapp.utils.hideKeyboard
import com.zidan.ybebrightapp.utils.showAlert
import com.zidan.ybebrightapp.utils.showLoading
import com.zidan.ybebrightapp.viewmodel.ViewModelFactory
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.zidan.ybebrightapp.R
import com.zidan.ybebrightapp.login.LoginActivity

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
                tvStatus.text = data?.keagenan?.uppercase()
                if (data?.keagenan != "customer") {
                    database =
                        Firebase.database("https://ybebright-app-default-rtdb.asia-southeast1.firebasedatabase.app/")
                        database.getReference(PATH_PROFILE).child(data?.idMember.toString())
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
                    tvName.text = data?.nama
                    tvValId.text = data?.idMember
                    tvValPoin.text = data?.point.toString()
                    tvNik.text = data?.nik
                    tvPhone.text = data?.no_hp
                    tvEmail.text = data?.email
                    tvAddress.text = "${data?.alamat}, ${data?.kota}"
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
                    val list = viewModel.getPoin(data?.point!!.toInt())
                    setRadio(list)
                }
                imgProfile.setOnClickListener {
                    getPhoto.launch(
                        ImagePicker.with(requireActivity())
                            .galleryOnly()
                            .cropSquare()
                            .createIntent()
                    )
                }
                tvEditAddress.setOnClickListener {
                    editAddress()
                }

                btnLogout.setOnClickListener {
                    logout("logout")
                }
            }
        }
    }

    private fun editAddress(){
        val loading = showLoading()
        loading.show()
        val bind = EditAddressBinding.inflate(layoutInflater)

        val factory = ViewModelFactory.getInstance(requireActivity())
        val viewModel = ViewModelProvider(requireActivity(), factory)[ProductViewModel::class.java]
        val (province, error) = viewModel.getProvinces()
        val dialog = showAlert(requireContext(), bind.root, false)

        if (error != null) {
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
            activity?.onBackPressed()
        }

        province.observe(viewLifecycleOwner) {
            loading.hide()
            val adapter = ProvinceAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line, it
            )
            bind.edtProvince.setAdapter(adapter)

            dialog.show()
            bind.btnOk.setOnClickListener {
                val pro = bind.edtProvince.text.toString()
                val city = bind.edtCity.text.toString()
                val address = bind.edtAddress.text.toString()

                if (pro.isEmpty()) {
                    bind.edtProvince.error = "Field ini harus diisi!"
                    return@setOnClickListener
                }
                if (city.isEmpty()) {
                    bind.edtCity.error = "Field ini harus diisi!"
                    return@setOnClickListener
                }
                if (address.isEmpty()) {
                    bind.edtAddress.error = "Field ini harus diisi!"
                    return@setOnClickListener
                }

                if (data != null) {
                    val ref = database.getReference("member").child(data?.kota!!.lowercase()).child(data?.idMember!!)

                    val member = data
                    member?.kota = city.uppercase()
                    member?.alamat = address

                    ref.updateChildren(member?.toMap()!!)

                    if (data?.kota != city.lowercase()) {
                        database.getReference("member").child(data?.kota!!.lowercase()).child(data?.idMember!!).removeValue()
                    }
                    logout("address")
                }

                hideKeyboard()
                dialog.dismiss()
            }

            bind.btnCancel.setOnClickListener {
                dialog.dismiss()
            }
        }

        bind.edtProvince.setOnItemClickListener { _, _, _, id ->
            viewModel.setId(id.toString())
            dialog.hide()
            loading.show()
        }

        viewModel.cities.observe(viewLifecycleOwner) {
            loading.hide()
            dialog.show()
            val adapter = CityAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line, it
            )
            bind.edtCity.setAdapter(adapter)
        }
    }

    private fun setPhoto(uri: Uri) {
        val storageReference = Firebase.storage
            .getReference("img_profile")
            .child(data?.idMember ?: "")
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
                            .child("${data?.idMember}")
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

    private fun logout(request: String) {
        val activity = requireActivity() as MainActivity
        if (request == "address") {
            activity.finishSetAddress()
        } else {
            activity.logout()
        }
    }
}