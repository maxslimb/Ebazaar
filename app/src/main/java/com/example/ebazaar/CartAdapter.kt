package com.example.ebazaar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso


class CartAdapter(options: FirebaseRecyclerOptions<Product>) : FirebaseRecyclerAdapter<Product, CartAdapter.ViewHolder>(options) {
    private val uid = FirebaseAuth.getInstance().uid
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_row,parent,false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: Product) {

        Picasso.get().load(model.geturl()).into(holder.image)
        holder.title.text = model.getname()
        holder.price.text = model.getPprice().toString()

        holder.cancel.setOnClickListener {
            AlertDialog.Builder(holder.image.context)
                .setMessage("Are you sure you want to remove this item from your cart?")
                .setPositiveButton("Yes") { p0, p1 ->
                    FirebaseDatabase.getInstance().reference.child("Cart/$uid")
                        .child(getRef(position).key!!).removeValue()
                }
                .setNegativeButton("No"){p0,p1 -> }
                .create()
                .show()

        }
    }

    class ViewHolder(itemView:View): RecyclerView.ViewHolder(itemView) {
        val image : ImageView = itemView.findViewById(R.id.c_product_photo)
        val title : TextView = itemView.findViewById(R.id.c_title)
        val price : TextView = itemView.findViewById(R.id.c_price)
        val cancel : ImageView = itemView.findViewById(R.id.remove)
    }



}