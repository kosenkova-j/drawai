package com.example.drawai.domain

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.drawai.databinding.ItemArtBinding
import com.example.drawai.domain.Art
import com.example.drawai.common.OnArtItemClickListener

class ArtAdapter(
    private val onItemClick: OnArtItemClickListener,
    private val onDeleteClick: OnArtItemClickListener
) : ListAdapter<Art, ArtAdapter.ArtViewHolder>(ArtDiffCallback()) {

    // 1. ViewHolder с ViewBinding
    inner class ArtViewHolder(
        private val binding: ItemArtBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(art: Art) {
            binding.art = art
            binding.executePendingBindings()
        }
    }

    // 2. DiffUtil для оптимизации обновлений списка
    private class ArtDiffCallback : DiffUtil.ItemCallback<Art>() {
        override fun areItemsTheSame(oldItem: Art, newItem: Art): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Art, newItem: Art): Boolean {
            return oldItem == newItem
        }
    }

    // 3. Создание ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtViewHolder {
        val binding = ItemArtBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ).apply {
            // Передаем листенеры в binding
            this.onItemClick = this@ArtAdapter.onItemClick
            this.onDeleteClick = this@ArtAdapter.onDeleteClick
        }
        return ArtViewHolder(binding)
    }

    // 4. Привязка данных
    override fun onBindViewHolder(holder: ArtViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    // 5. Дополнительные методы (опционально)
    fun getArtAtPosition(position: Int): Art? {
        return if (position in 0 until itemCount) getItem(position) else null
    }
}