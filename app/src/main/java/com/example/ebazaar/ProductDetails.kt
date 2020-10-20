package com.example.ebazaar

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.product_details.*

class ProductDetails: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product_details)
        val title = intent.getStringExtra("title") ?: ""
        val price = intent.getDoubleExtra("price",0.0)
        val photo = intent.getStringExtra("photo") ?: ""
        Picasso.get().load(photo).resize(300,300).into(product_photo)
        product_name.text = title
        product_price.text = price.toString()
        availability.setOnClickListener {
            AlertDialog.Builder(this)
                .setMessage("in Stock")
                .setPositiveButton("OK") { p0, p1 ->  }
                .create()
                .show()
        }
        addtocart.setOnClickListener {
            val intent = Intent(this,Cart::class.java)
            intent.putExtra("product_name",title)
            intent.putExtra("product_price",price)
            startActivity(intent)
        }
    }
}