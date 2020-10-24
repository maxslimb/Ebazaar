package com.example.ebazaar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class CartAdapter(private val products:ArrayList<Product>) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_row,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount()= products.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = products[position]
        Picasso.get().load(product.photoUrl).into(holder.image)
        holder.title.text = product.title
        holder.price.text = product.price.toString()
    }
    class ViewHolder(itemView:View): RecyclerView.ViewHolder(itemView) {
        val image : ImageView = itemView.findViewById(R.id.c_product_photo)
        val title : TextView = itemView.findViewById(R.id.c_title)
        val price : TextView = itemView.findViewById(R.id.c_price)
    }

}