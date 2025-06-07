package com.example.drawai.gallery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.drawai.databinding.ItemArtBinding
import com.example.drawai.domain.Art
import com.example.drawai.common.OnArtItemClickListener

class GalleryAdapter(
    private val OnItemClick: OnArtItemClickListener? = null,
    private val OnDeleteClick: OnArtItemClickListener? = null
) : ListAdapter<Art, GalleryAdapter.ArtViewHolder>(ArtDiffCallback()) {

    inner class ArtViewHolder(
        private val binding: ItemArtBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(art: Art) {
            binding.apply {
                this.art = art

                // Основной клик по элементу
                root.setOnClickListener {
                    OnItemClick?.onClick(art)
                }

                // Обработка кнопки удаления (если есть)
                if (OnDeleteClick != null) {
                    deleteButton.visibility = View.VISIBLE
                    deleteButton.setOnClickListener {
                        OnDeleteClick.onClick(art)
                    }
                } else {
                    deleteButton.visibility = View.GONE
                }

                executePendingBindings()
            }
        }
    }

    // 2. DiffUtil для оптимизации обновлений
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
            // Установка обработчиков кликов
            root.setOnClickListener {
                art?.let { item -> OnItemClick?.onClick(item) }
            }

            // Опциональная кнопка удаления
            onDeleteClick?.let { listener ->
                deleteButton.setOnClickListener {
                    art?.let { item -> listener.onClick(item) }
                }
            }
        }
        return ArtViewHolder(binding)
    }

    // 4. Привязка данных
    override fun onBindViewHolder(holder: ArtViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}