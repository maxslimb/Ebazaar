package com.example.ebazaar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.seller.*

class Cart : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private val uid = FirebaseAuth.getInstance().uid
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        val title = intent.getStringExtra("product_name") ?: ""
        val price = intent.getStringExtra("product_price") ?: ""
        //writepdata(title,price.toDouble())
    } // TODO: save everything to database and then retrive everything and then update recycler view
    private fun writepdata(title:String,price:Double){

        val key = database.child("Cart").push().key
        if (key == null) {
            Log.w(Cart.TAG, "Couldn't get push key for posts")
            return
        }

        val prodat = Product(title,"",price)
        val productValues = prodat.toMap()

        val childUpdates = hashMapOf<String, Any>(
            "/product/$key" to productValues,
            "/user-products/$uid/$key" to productValues
        )

        database.updateChildren(childUpdates)
        Toast.makeText(this@Cart,"Successfully Added Item $title", Toast.LENGTH_SHORT).show()
    }
    companion object {
        private const val TAG = "CART"
    }
}