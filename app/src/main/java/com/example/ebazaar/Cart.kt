package com.example.ebazaar

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_cart.*

class Cart : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private val uid = FirebaseAuth.getInstance().uid
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        val products = arrayListOf<Product>()
        for (i in 0..2){
            products.add(Product("Organic Apple","https://via.placeholder.com/300/ffff00",45.0))}
        database = FirebaseDatabase.getInstance().getReference("Cart")
        database.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(this@Cart,"Something Went Wrong!",Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(p0: DataSnapshot) {
                for(i in p0.children){
                    val pro = i.getValue(Product::class.java)
                    products.add(pro!!)

                }
                productsRecyclerView.apply {
                    layoutManager = GridLayoutManager(this@Cart,1,RecyclerView.VERTICAL,false)
                    adapter = CartAdapter(products)
                    progressBar2?.visibility = View.GONE
                }
            }
        })
        if (intent.hasExtra("product_name")&&intent.hasExtra("product_price")&&intent.hasExtra("product_photo")){
        val title = intent.getStringExtra("product_name")?:""
        val price = intent.getDoubleExtra("product_price",0.0)
        val purl = intent.getStringExtra("product_photo")?:""
            writepdata(title,purl,price)
        }


    }



    private fun writepdata(title:String,photoUrl: String,price:Double){

        val key = database.child("Cart").push().key
        if (key == null) {
            Log.w(TAG, "Couldn't get push key for posts")
            return
        }

        val prodat = Product(title,photoUrl,price)
        val productValues = prodat.toMap()

        val childUpdates = hashMapOf<String, Any>(
            "/$uid/$key " to productValues
        )

        database.updateChildren(childUpdates)
        Toast.makeText(this@Cart,"Successfully Added Item $title to Cart", Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val TAG = "CART"
    }
}