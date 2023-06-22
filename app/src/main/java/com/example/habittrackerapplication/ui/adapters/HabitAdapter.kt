package com.example.habittrackerapplication.ui.adapters

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.habittrackerapplication.R
import com.example.habittrackerapplication.common.getIconArray
import com.example.habittrackerapplication.databinding.HabitItemBinding
import com.example.habittrackerapplication.models.Habit
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.karumi.dexter.listener.OnDialogButtonClickListener
import kotlinx.coroutines.CoroutineScope
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class HabitAdapter(
    val context: Context, val list:ArrayList<Habit>, val isRadioBtnEnabled:Boolean=true,
    private val itemClickListener: ItemClickListener?
):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    interface ItemClickListener {
        fun onItemClick(position: Int)
        fun onRadioButtonClick(position: Int)
    }
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        init {
            view.setOnClickListener {
                if (it.verticalScrollbarPosition>= 0) {
                    itemClickListener!!.onItemClick(it.verticalScrollbarPosition)
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.habit_item,parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        if (holder is ViewHolder) {
            val binding = HabitItemBinding.bind(holder.itemView)
            binding.habitNameTv.text = model.name
            binding.habitDaysTv.text = "Total ${model.days} completed"

            Glide.with(context)
                .load(getIconArray()[model.iconPosition!!])
                .placeholder(R.drawable.user_avatar)
                .centerCrop()
                .into(holder.itemView.findViewById(R.id.icon_iv))

            if (isRadioBtnEnabled) {
                if (model.isChecked) {
                    binding.completeRb.visibility = View.INVISIBLE
                    binding.completeIv.visibility = View.VISIBLE
                } else {
                    binding.completeRb.visibility = View.VISIBLE
                    binding.completeIv.visibility = View.INVISIBLE
                }
            } else {
                binding.completeRb.visibility = View.INVISIBLE
                binding.completeIv.visibility = View.INVISIBLE
            }
            holder.itemView.findViewById<RadioButton>(R.id.complete_rb).setOnClickListener {
                    itemClickListener!!.onRadioButtonClick(position)

            }

        }
    }


    override fun getItemCount(): Int {
        return list.size
    }
}
