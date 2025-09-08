package com.example.myapitest.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapitest.R
import com.example.myapitest.ui.loadUrl
import com.example.myapitest.model.Car

class CarAdapter (
    private val items: List<Car>

) : RecyclerView.Adapter<CarAdapter.CarViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_car_layout, parent, false)
        return CarViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        val item = items[position]
        holder.model.text = item.name
        holder.year.text = item.year
        holder.license.text = item.licence
        holder.imageView.loadUrl(item.imageUrl)

    }

    override fun getItemCount(): Int = items.size

    class CarViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val imageView: ImageView = view.findViewById(R.id.image)
        val model: TextView = view.findViewById(R.id.model)
        val year: TextView = view.findViewById(R.id.year)
        val license: TextView = view.findViewById(R.id.license)
    }

}