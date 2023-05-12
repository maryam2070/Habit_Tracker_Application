package com.example.habittrackerapplication.ui.adapters

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.habittrackerapplication.R
import com.example.habittrackerapplication.databinding.HabitItemBinding
import com.example.habittrackerapplication.models.Habit
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class HabitAdapter(val context: Context ,val list:ArrayList<Habit>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private class ViewHolder(view: View): RecyclerView.ViewHolder(view) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.habit_item,parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model=list[position]
        if(holder is ViewHolder)
        {
            val binding=HabitItemBinding.bind(holder.itemView)
            binding.habitNameTv.text = model.name
            binding.habitDaysTv.text = "Total ${model.days} completed"
            //holder.itemView.findViewById<ImageView>(R.id.iv_child_profile).setImageBitmap()
            /*Glide.with(fragment.requireActivity())
                .load(iconlist[model.iconPosition!!])
                //    .placeholder(R.drawable.student_avatar)
                .centerCrop()
                .into(holder.itemView.findViewById(R.id.icon_iv))*/

            //holder.itemView.findViewById<RadioButton>(R.id.complete_rb).isEnabled=isEnabled
            if(model.isChecked)
            {
                binding.completeRb.visibility=View.INVISIBLE
                binding.completeIv.visibility=View.VISIBLE
            }else{
                binding.completeRb.visibility=View.VISIBLE
                binding.completeIv.visibility=View.INVISIBLE
            }
            holder.itemView.findViewById<RadioButton>(R.id.complete_rb).setOnCheckedChangeListener { compoundButton, b ->
               /* if(model.isChecked==(false) && isEnabled==true){

                    var calendar= Calendar.getInstance()
                    val formatter = SimpleDateFormat("dd-MM-yyyy")
                    val inFormat = SimpleDateFormat("dd-MM-yyyy")
                    val date: Date = inFormat.parse(formatter.format(calendar.time))
                    val outFormat = SimpleDateFormat("dd-MM-yyyy")
                    val goal= outFormat.format(date)


                    FirebaseDatabase.getInstance().reference.child("accounts")
                        .child((FirebaseAuth.getInstance().currentUser)?.uid!!)
                        .child("Habits").child(model.id!!).child("days").setValue(model.days!!+1)

                    val key= FirebaseDatabase.getInstance().reference.child("accounts")
                        .child((FirebaseAuth.getInstance().currentUser)?.uid!!)
                        .child("Habits").child(model.id!!).child("completedDays").push().getKey()

                    FirebaseDatabase.getInstance().reference.child("accounts")
                        .child((FirebaseAuth.getInstance().currentUser)?.uid!!)
                        .child("Habits").child(model.id!!).child("completedDays").child(key.toString())
                        .child("dayOfMonth").setValue(calendar.get(Calendar.DAY_OF_MONTH))

                    FirebaseDatabase.getInstance().reference.child("accounts")
                        .child((FirebaseAuth.getInstance().currentUser)?.uid!!)
                        .child("Habits").child(model.id!!).child("completedDays").child(key.toString())
                        .child("month").setValue(calendar.get(Calendar.MONTH)+1)

                    FirebaseDatabase.getInstance().reference.child("accounts")
                        .child((FirebaseAuth.getInstance().currentUser)?.uid!!)
                        .child("Habits").child(model.id!!).child("completedDays").child(key.toString())
                        .child("year").setValue(calendar.get(Calendar.YEAR)).addOnSuccessListener {

                        }

                    println("AAAA Before     "+model.completedDays)
                    model.completedDays.add(goal)

                    viewModel.updateHabit(model)
                    println("AAAA After    "+model.completedDays)

                    holder.itemView.findViewById<TextView>(R.id.habit_days_tv).text = "Total ${model.days+1} completed"

                    holder.itemView.findViewById<RadioButton>(R.id.complete_rb).visibility=
                        View.GONE
                    holder.itemView.findViewById<ImageView>(R.id.complete_iv).visibility=
                        View.VISIBLE


                    var d: Dialog = Dialog(fragment.requireContext())
                    d.setContentView(R.layout.check_habit_dialog)
                    d.show()*/
                }

            }


        }


    override fun getItemCount(): Int {
        return list.size
    }
}
