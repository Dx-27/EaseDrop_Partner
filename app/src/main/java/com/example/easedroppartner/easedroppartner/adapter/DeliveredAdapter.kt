package com.example.easedroppartner.easedroppartner.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.easedroppartner.R
import com.example.easedroppartner.easedroppartner.Api.Package_Api
import com.example.easedroppartner.easedroppartner.Model.Instant_model
import com.example.easedroppartner.easedroppartner.fragment.DeliveredFragment

class DeliveredAdapter(private val list: ArrayList<Instant_model>, private val context: Context) :
    RecyclerView.Adapter<DeliveredAdapter.DeliveredAdapter_Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeliveredAdapter_Holder {
        val context: Context = parent.context
        val inflater = LayoutInflater.from(context)

        val view = inflater.inflate(R.layout.row_delivered, parent, false)

        return DeliveredAdapter_Holder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: DeliveredAdapter_Holder, position: Int) {
        val model: Instant_model = list[position]

        holder.tv_sender_name.text = model.sender_name
        holder.tv_pickup_add.text = model.sender_add
        holder.tv_drop_add.text = model.rec_add
        holder.tv_sender_phone.text = model.sender_phn
        holder.tv_payment_type.text = model.payment_mode
        holder.tv_total_amt.text = model.total_amt
        holder.tv_resiver_name.text = model.rec_name
        holder.tv_resiver_phone.text = model.rec_phn
        holder.tv_package_desc.text = model.package_content
        holder.tv_package_weight.text = model.package_size
        //holder.tv_is_fragile.text = model.is_fragile

        holder.tv_sender_lat_lang.setText("(" + model.sender_lat + ", " + model.sender_lang + ")")
        holder.tv_receiver_lat_lang.setText("(" + model.rec_lat + ", " + model.rec_lang + ")")

        holder.delivered.text="Delivered"

        holder.delivered.setOnClickListener {

            val packageApi = Package_Api()

          //  packageApi.AcceptPackage(context, model.id)

            val intent = Intent(context, DeliveredFragment::class.java).apply {
                putExtra("package", model)
            }
            context.startActivity(intent)
        }

    }

    inner class DeliveredAdapter_Holder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
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
        val tv_sender_lat_lang: TextView = itemView.findViewById(R.id.sender_lat_lng)
        val tv_receiver_lat_lang: TextView = itemView.findViewById(R.id.receiver_lat_lng)
        val delivered: TextView = itemView.findViewById(R.id.btn_delivered)
    }
}