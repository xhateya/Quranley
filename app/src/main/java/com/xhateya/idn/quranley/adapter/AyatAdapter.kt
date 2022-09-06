package com.xhateya.idn.quranley.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.xhateya.idn.quranley.R
import com.xhateya.idn.quranley.model.ModelAyat


class AyatAdapter(private val items: List<ModelAyat>) :
    RecyclerView.Adapter<AyatAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val a = LayoutInflater.from(parent.context).inflate(R.layout.list_item_ayat, parent, false)
        return ViewHolder(a)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = items[position]
        holder.tvNomorAyat.text = data.nomor
        holder.tvArabic.text = data.arab
        holder.tvTerjemahan.text = data.indo
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvNomorAyat: TextView = itemView.findViewById(R.id.tvNomorAyat)
        var tvArabic: TextView = itemView.findViewById(R.id.tvArabic)
        var tvTerjemahan: TextView = itemView.findViewById(R.id.tvTerjemahan)

    }
}