package com.example.ybebrightapp.hidok

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ybebrightapp.model.Agent
import com.example.ybebrightapp.databinding.HiDokActivityBinding
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.example.ybebrightapp.hidok.model.FriendlyMessage
import com.example.ybebrightapp.model.Consul
import com.github.drjacky.imagepicker.ImagePicker
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HiDokActivity : AppCompatActivity() {
    private lateinit var binding: HiDokActivityBinding
    private lateinit var manager: LinearLayoutManager

    // Firebase instance variables
    private lateinit var db: FirebaseDatabase
    private lateinit var adapter: FriendlyMessageAdapter
    private var member: Agent? = null

    private val capture = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            onImageSelected(it.data?.data!!)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HiDokActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        member = intent.getParcelableExtra("data")
        val consul = intent.getParcelableExtra<Consul>("consul")

        db = Firebase.database("https://ybebright-app-default-rtdb.asia-southeast1.firebasedatabase.app/")

        if (consul != null) {
            val friendlyMessage = FriendlyMessage(
                consul.text,
                member?.name,
                null,
                null,
                true
            )
            db.getReference("chat").child("${member?.id}").push().setValue(friendlyMessage)
            onImageSelected(consul.frontPhoto!!)
            onImageSelected(consul.rightPhoto!!)
            onImageSelected(consul.leftPhoto!!)
        }

        // Initialize Realtime Database
        val messagesRef = db.getReference(MESSAGES_CHILD).child("${member?.id}")

        // The FirebaseRecyclerAdapter class and options come from the FirebaseUI library
        CoroutineScope(Dispatchers.Main).launch {
            val options = FirebaseRecyclerOptions.Builder<FriendlyMessage>()
                .setQuery(messagesRef, FriendlyMessage::class.java)
                .build()
            adapter = FriendlyMessageAdapter(this@HiDokActivity, options)
            binding.progressBar.visibility = ProgressBar.INVISIBLE
            manager = LinearLayoutManager(this@HiDokActivity)
            manager.stackFromEnd = true
            binding.messageRecyclerView.layoutManager = manager
            binding.messageRecyclerView.adapter = adapter
        }

        adapter.registerAdapterDataObserver(
            MyScrollToBottomObserver(binding.messageRecyclerView, adapter, manager)
        )

        binding.messageEditText.addTextChangedListener(MyButtonObserver(binding.sendButton))

        binding.sendButton.setOnClickListener {
            val lastChat = binding.messageEditText.text.toString()
            val friendlyMessage = FriendlyMessage(
                lastChat,
                member?.name,
                null,
                null,
                true
            )
            db.getReference("chat").child("${member?.id}").push().setValue(friendlyMessage)
            binding.messageEditText.setText("")
        }

        // When the image button is clicked, launch the image picker
        binding.addMessageImageView.setOnClickListener {
            CoroutineScope(Dispatchers.Default).launch {
                capture.launch(
                    ImagePicker.with(this@HiDokActivity)
                        .cameraOnly()
                        .createIntent()
                )
            }
        }
    }

    public override fun onPause() {
        adapter.stopListening()
        super.onPause()
    }

    public override fun onResume() {
        super.onResume()
        adapter.startListening()
    }

    private fun onImageSelected(uri: Uri) {
        Log.d(TAG, "Uri: $uri")
        val tempMessage = FriendlyMessage(null, member?.name, null, LOADING_IMAGE_URL, true)
        CoroutineScope(Dispatchers.IO).launch {
            db.getReference("chat")
                .child("${member?.id}")
                .push()
                .setValue(
                    tempMessage,
                    DatabaseReference.CompletionListener { databaseError, databaseReference ->
                        if (databaseError != null) {
                            Log.w(
                                TAG, "Unable to write message to database.",
                                databaseError.toException()
                            )
                            return@CompletionListener
                        }

                        // Build a StorageReference and then upload the file
                        val key = databaseReference.key
                        val storageReference = Firebase.storage
                            .getReference(member?.nik ?: "")
                            .child(key!!)
                            .child(uri.lastPathSegment!!)
                        putImageInStorage(storageReference, uri, key)
                    })
        }
    }

    private fun putImageInStorage(storageReference: StorageReference, uri: Uri, key: String?) {
        // First upload the image to Cloud Storage
        CoroutineScope(Dispatchers.IO).launch {
            storageReference.putFile(uri)
                .addOnSuccessListener(
                    this@HiDokActivity
                ) { taskSnapshot -> // After the image loads, get a public downloadUrl for the image
                    // and add it to the message.
                    taskSnapshot.metadata!!.reference!!.downloadUrl
                        .addOnSuccessListener { uri ->
                            val friendlyMessage =
                                FriendlyMessage(null, member?.name, null, uri.toString(), true)
                            db.getReference("chat")
                                .child("${member?.id}")
                                .child(key!!)
                                .setValue(friendlyMessage)
                        }
                }
                .addOnFailureListener(this@HiDokActivity) { e ->
                    Log.w(
                        TAG,
                        "Image upload task was unsuccessful.",
                        e
                    )
                }
        }
    }

    companion object {
        private const val TAG = "MainActivity"
        const val MESSAGES_CHILD = "chat"
        const val LOADING_IMAGE_URL = "https://www.google.com/images/spin-32.gif"
    }
}
