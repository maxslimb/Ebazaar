package com.example.ebazaar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_yorder.*

class yorder : AppCompatActivity() {
    private val uid = FirebaseAuth.getInstance().uid
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_yorder)
        val query : Query = FirebaseDatabase.getInstance().getReference("Seller-Orders/$uid")
        val options: FirebaseRecyclerOptions<Product> = FirebaseRecyclerOptions.Builder<Product>()
            .setQuery(query,Product::class.java)
            .build()
        val ad = CartAdapter(options)
        ad.startListening()
        productsRe.apply {
            layoutManager = GridLayoutManager(this@yorder, 1, RecyclerView.VERTICAL, false)
            adapter = ad
            prb?.visibility = View.INVISIBLE
        }

        val query1 = FirebaseDatabase.getInstance().reference.child("Order")
            .orderByChild("title")

        query1.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.w("yorder", "Couldn't get push key for posts")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    val order = it.getValue(Product::class.java)
                    Log.d("yorder",order.toString())
                    checkquey(order,it.key)
                    query1.removeEventListener(this)

                }

            }
        })

    }


    fun checkquey(order: Product?, key: String?) {
        val query = FirebaseDatabase.getInstance().reference.child("user-products/$uid")
            .orderByChild("title").equalTo(order?.title)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.w("yorder", "Couldn't get push key for posts")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    val order1 = it.getValue(Product::class.java)
                    Log.d("yorder",order1.toString())
                    writedata(order1)
                    query.removeEventListener(this)
                    key?.let { it1 ->
                        FirebaseDatabase.getInstance().reference.child("Order").child(
                            it1
                        ).removeValue()
                    }
                }

            }
        })

    }

    private fun writedata(order: Product?) {
        val key = FirebaseDatabase.getInstance().getReference("Seller-Orders").push().key
        if (key == null) {
            Log.w("yorder", "Couldn't get push key for posts")
            return
        }
        //val productValues = order!!.toMap()

        val childUpdates = hashMapOf<String, Any>(
            "$key" to (order!!.toMap())
        )
        FirebaseDatabase.getInstance().getReference("Seller-Orders/$uid").updateChildren(childUpdates)


    }


}