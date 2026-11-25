package com.example.easedroppartner.easedroppartner.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.easedroppartner.R
import com.example.easedroppartner.easedroppartner.Api.All_India_delivery_Api
import com.example.easedroppartner.easedroppartner.Model.All_India_Delivery_Model
import com.example.easedroppartner.easedroppartner.fragment.DeliveredFragment
import com.example.easedroppartner.easedroppartner.fragment.Delivered_all_india_Fragment

class All_india_Delivered_Adapter (private val list: ArrayList<All_India_Delivery_Model>,private val context: Context):
RecyclerView.Adapter<All_india_Delivered_Adapter.All_India_delivered_Holder>() {

    inner class All_India_delivered_Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv_sender_name: TextView = itemView.findViewById(R.id.tv_sender_name)
        val tv_sender_phone: TextView = itemView.findViewById(R.id.tv_sender_phone)
        val tv_pickup_add: TextView = itemView.findViewById(R.id.tv_pickup_add)
        val tv_resiver_name: TextView = itemView.findViewById(R.id.tv_resiver_name)
        val tv_resiver_phone: TextView = itemView.findViewById(R.id.tv_resiver_phone)
        val tv_drop_add: TextView = itemView.findViewById(R.id.tv_drop_add)
        val tv_package_desc: TextView = itemView.findViewById(R.id.tv_desc)
        val tv_package_weight: TextView = itemView.findViewById(R.id.tv_package_waight)
        val tv_payment_type: TextView = itemView.findViewById(R.id.tv_payment_type)
        val tv_total_amt: TextView = itemView.findViewById(R.id.tv_total_amt)
//        val tv_sender_lat_lang: TextView = itemView.findViewById(R.id.sender_lat_lng)
//        val accept: Button = itemView.findViewById(R.id.accept)
//        val cancel: Button = itemView.findViewById(R.id.cancel)

        //val tv_receiver_lat_lang: TextView = itemView.findViewById(R.id.receiver_lat_lng)

//        val delivered: TextView = itemView.findViewById(R.id.btn_delivered)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): All_India_delivered_Holder {
        val context: Context = parent.context
        val inflater = LayoutInflater.from(context)

        val view = inflater.inflate(R.layout.row_delivered, parent, false)

        return All_India_delivered_Holder(view)
    }

    override fun getItemCount(): Int {
        return list.size

    }
    override fun onBindViewHolder(holder: All_India_delivered_Holder, position: Int) {
        val model: All_India_Delivery_Model = list[position]

        holder.tv_sender_name.text = model.sender_name
        holder.tv_pickup_add.text = model.sender_add
        holder.tv_drop_add.text = model.receiver_add
        holder.tv_sender_phone.text = model.sender_ph
        holder.tv_payment_type.text = model.payment_type
        holder.tv_total_amt.text = model.total_amt
        holder.tv_resiver_name.text = model.receiver_name
        holder.tv_resiver_phone.text = model.receiver_ph
        holder.tv_package_desc.text = model.package_desc
        holder.tv_package_weight.text = model.package_weight
//        if (model.d_status == "2" ||model.d_status == "3"){
//            holder.accept.visibility=View.GONE
//            holder.cancel.visibility=View.GONE
//
//        }

     //   holder.tv_sender_lat_lang.text = "(${model.sender_lat_lang})"


     //   holder.delivered.text="Delivered"

//        holder.delivered.setOnClickListener {
//
//            val allIndiaDeliveryApi = All_India_delivery_Api()
//
//          //  allIndiaDeliveryApi.AcceptPackage(context, model.id)
//
//            val intent = Intent(context, Delivered_all_india_Fragment::class.java).apply {
//                putExtra("package", model)
//            }
//            context.startActivity(intent)
//        }

    }
}
