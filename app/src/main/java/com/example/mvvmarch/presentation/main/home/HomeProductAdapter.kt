package com.example.mvvmarch.presentation.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmarch.databinding.ItemLayoutBinding
import com.example.mvvmarch.domain.product.entity.ProductEntity

class HomeProductAdapter :
    ListAdapter<ProductEntity, HomeProductAdapter.HomeProductViewHolder>(DiffCallBack()) {

    lateinit var onItemClickListener: (ProductEntity) -> Unit

    private class DiffCallBack : DiffUtil.ItemCallback<ProductEntity>() {
        override fun areItemsTheSame(oldItem: ProductEntity, newItem: ProductEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProductEntity, newItem: ProductEntity): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeProductViewHolder {
        return HomeProductViewHolder(
            ItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: HomeProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class HomeProductViewHolder(private val binding: ItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: ProductEntity) {
            binding.productNameTextView.text = product.name

            itemView.setOnClickListener {
                onItemClickListener.invoke(product)
            }
        }
    }
}