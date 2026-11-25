package com.example.easedroppartner.easedroppartner.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.easedroppartner.R
import com.example.easedroppartner.easedroppartner.Model.Instant_model
import com.example.easedroppartner.easedroppartner.activity.Map_track_Activity

class Instant_delevery_Adapter{

//    (private val list: List<Instant_model>, private val context: android.content.Context) :
//    RecyclerView.Adapter<Instant_delevery_Adapter.Instant_delivery_Holder>()
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Instant_delivery_Holder {
//        val context: android.content.Context = parent.context
//        val inflater = LayoutInflater.from(context)
//        val view = inflater.inflate(R.layout.row_instant_order, parent, false)
//        return Instant_delivery_Holder(view)
//    }
//
//    override fun getItemCount(): Int {
//        return list.size
//    }
//
//    override fun onBindViewHolder(holder: Instant_delivery_Holder, position: Int) {
//        val model: Instant_model = list[position]
//        holder.bind(model)
//    }
//
//    inner class Instant_delivery_Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//
//        private val active_price: TextView = itemView.findViewById(R.id.active_price)
//        private val active_d2: TextView = itemView.findViewById(R.id.active_d2)
//        private val active_pcname: TextView = itemView.findViewById(R.id.active_pcname)
//        private val active_dcname: TextView = itemView.findViewById(R.id.active_dcname)
//
//        fun bind(model: Instant_model) {
//            //    active_d1.text = model.active_d1
//            active_price.text = model.total_amt
//            active_pcname.text = model.sender_name
//            active_dcname.text = model.rec_name
//            if (model.d_status.equals("0")){
//                active_d2.text="Pending..."
//
//            }else  if (model.d_status.equals("1")){
//                active_d2.text="Assign..."
//
//            }else  if (model.d_status.equals("2")){
//                active_d2.text="Delivered..."
//
//            }
//            itemView.setOnClickListener {
//                val intent = Intent(context,Map_track_Activity::class.java).apply {
//                    // Put extra data here
//                    putExtra("sender_name", model.sender_name)
//                    putExtra("receiver_name",model.rec_name)
//                }
//                context.startActivity(intent)
//            }
//        }
//
//    }
//

}