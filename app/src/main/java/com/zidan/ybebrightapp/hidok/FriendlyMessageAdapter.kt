package com.zidan.ybebrightapp.hidok

import android.app.Activity
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView.*
import com.bumptech.glide.Glide
import com.zidan.ybebrightapp.R
import com.zidan.ybebrightapp.databinding.DialogImageBinding
import com.zidan.ybebrightapp.databinding.MessageMeBinding
import com.zidan.ybebrightapp.databinding.MessageYouBinding
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.zidan.ybebrightapp.hidok.model.FriendlyMessage
import com.zidan.ybebrightapp.utils.showAlert
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class FriendlyMessageAdapter(private val activity: Activity,
    private val options: FirebaseRecyclerOptions<FriendlyMessage>) : FirebaseRecyclerAdapter<FriendlyMessage, ViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == VIEW_TYPE_ME) {
            val view = inflater.inflate(R.layout.message_me, parent, false)
            val binding = MessageMeBinding.bind(view)
            MessageViewHolder(binding)
        } else {
            val view = inflater.inflate(R.layout.message_you, parent, false)
            val binding = MessageYouBinding.bind(view)
            MessageYouViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: FriendlyMessage) {
        holder.setIsRecyclable(false)
        if (options.snapshots[position].isMe) {
            (holder as MessageViewHolder).bind(model)
        } else {
            (holder as MessageYouViewHolder).bind(model)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (options.snapshots[position].isMe) VIEW_TYPE_ME else VIEW_TYPE_ADMIN
    }

    inner class MessageViewHolder(private val binding: MessageMeBinding) : ViewHolder(binding.root) {
        fun bind(item: FriendlyMessage) {
            if (item.imageUrl != null) {
                binding.messageTextView.visibility = View.GONE
                binding.messageImageView.visibility = View.VISIBLE
                loadImageIntoView(binding.messageImageView, item.imageUrl!!)

                val bind = DialogImageBinding.inflate(activity.layoutInflater)
                binding.messageImageView.setOnClickListener {
                    Glide.with(activity).load(item.imageUrl).into(bind.imgView)
                    showAlert(activity, bind.root, true)
                        .setOnKeyListener { dialog, keyCode, _ ->
                            if (keyCode == KeyEvent.KEYCODE_BACK) {
                                dialog.cancel()
                            }
                           true
                        }
                }
            } else {
                binding.messageTextView.text = item.text
            }
        }
    }

    inner class MessageYouViewHolder(private val binding: MessageYouBinding) :
        ViewHolder(binding.root) {
        fun bind(item: FriendlyMessage) {
            if (item.imageUrl != null) {
                binding.messageTextView.visibility = View.GONE
                binding.messageImageView.visibility = View.VISIBLE
                loadImageIntoView(binding.messageImageView, item.imageUrl!!)

                val bind = DialogImageBinding.inflate(activity.layoutInflater)
                binding.messageImageView.setOnClickListener {
                    Glide.with(activity).load(item.imageUrl).into(bind.imgView)
                    showAlert(activity, bind.root, true)
                        .setOnKeyListener { dialog, keyCode, _ ->
                            if (keyCode == KeyEvent.KEYCODE_BACK) {
                                dialog.cancel()
                            }
                            true
                        }
                }
            } else {
                binding.messageTextView.text = item.text
            }
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
                        .centerCrop()
                        .into(view)
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Getting download url was not successful.", e)
                }
        } else {
            Glide.with(view.context).load(url).into(view)
        }
    }

    companion object {
        const val TAG = "MessageAdapter"
        const val VIEW_TYPE_ME = 1
        const val VIEW_TYPE_ADMIN = 2
    }
}
