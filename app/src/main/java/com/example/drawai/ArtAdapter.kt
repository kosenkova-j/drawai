package com.example.drawai

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ArtAdapter(
    private val onItemClick: (ArtEntity) -> Unit
) : RecyclerView.Adapter<ArtAdapter.ArtViewHolder>() {

    private var arts = emptyList<ArtEntity>()

    inner class ArtViewHolder(private val binding: ItemArtBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(art: ArtEntity) {
            binding.originalImage.setImageBitmap(
                BitmapFactory.decodeByteArray(art.originalImage, 0, art.originalImage.size)
            )
            binding.generatedImage.setImageBitmap(
                BitmapFactory.decodeByteArray(art.generatedImage, 0, art.generatedImage.size)
            )
            binding.root.setOnClickListener { onItemClick(art) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtViewHolder {
        val binding = ItemArtBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ArtViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArtViewHolder, position: Int) {
        holder.bind(arts[position])
    }

    override fun getItemCount(): Int = arts.size

    fun submitList(newList: List<ArtEntity>) {
        arts = newList
        notifyDataSetChanged()
    }
}