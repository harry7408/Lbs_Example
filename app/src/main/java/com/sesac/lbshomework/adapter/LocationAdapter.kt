package com.sesac.lbshomework.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sesac.lbshomework.databinding.ItemLocationBinding
import com.sesac.lbshomework.model.LatLang
import java.util.Locale

class LocationAdapter(private val items: MutableList<LatLang>) :
    RecyclerView.Adapter<LocationAdapter.LocationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        return LocationViewHolder(
            ItemLocationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun addItem(item : LatLang) {
        items.add(item)
        notifyItemInserted(items.size-1)
    }

    inner class LocationViewHolder(private val binding: ItemLocationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: LatLang) {
            with(binding) {
                latLangTextView.text = String.format(
                    Locale.KOREA,
                    "${item.latitude} : ${item.longitude}"
                )
            }
        }
    }
}