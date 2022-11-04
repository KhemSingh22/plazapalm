package com.example.plazapalm.views.chat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.plazapalm.databinding.ChatReceiverBinding
import com.example.plazapalm.databinding.ChatSenderBinding

class ChatAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val receiver = 0
    private val sender = 1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layInflater = LayoutInflater.from(parent.context)
        var viewHolder: RecyclerView.ViewHolder? = null

        when (viewType) {
            receiver -> {
                viewHolder = ReceiverVH(ChatReceiverBinding.inflate(layInflater, parent, false))
            }
            sender -> {
                viewHolder = SenderVH(ChatSenderBinding.inflate(layInflater, parent, false))
            }
        }

        return viewHolder!!
    }
    inner class SenderVH(val binding: ChatSenderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setData() {
            binding.executePendingBindings()
        }
    }

    inner class ReceiverVH(val binding: ChatReceiverBinding) : RecyclerView.ViewHolder(binding.root) {

        fun setData() {
            binding.executePendingBindings()
        }

    }

    override fun getItemViewType(position: Int): Int {
        return if (position % 2 == 0) sender else receiver
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SenderVH -> holder.setData()
            is ReceiverVH -> holder.setData()
        }
    }

    override fun getItemCount(): Int = 20
}