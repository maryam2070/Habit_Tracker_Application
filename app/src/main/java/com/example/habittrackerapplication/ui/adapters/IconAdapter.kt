package com.example.habittrackerapplication.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.allViews
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.habittrackerapplication.R

class IconAdapter(val context: Context, val list: ArrayList<Int>, val iconRv: RecyclerView):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var selectedItem=0

    private var selectedView: RecyclerView.ViewHolder? =null
    public fun getSelectedItem():Int{
        return selectedItem
    }

    private class ViewHolder(view: View): RecyclerView.ViewHolder(view){
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.icon_item,parent,false))
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Glide.with(context)
            .load(list.get(position))
            .into(holder.itemView.findViewById(R.id.icon_item_iv))

        holder.itemView.setOnClickListener {

            for (i in iconRv.allViews) {
                i.setBackgroundResource(R.drawable.transparent_text_view)
            }
            holder.itemView.setBackgroundResource(R.drawable.text_view_background)

            selectedView=holder
            selectedItem=position
        }

    }


    override fun getItemCount(): Int {
        return list.size
    }


}