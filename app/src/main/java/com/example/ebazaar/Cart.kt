package com.example.ebazaar

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultListener
import kotlinx.android.synthetic.main.activity_cart.*
import org.json.JSONObject


class Cart : AppCompatActivity(), PaymentResultListener{
    private lateinit var database: DatabaseReference
    private val uid = FirebaseAuth.getInstance().uid
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        database = FirebaseDatabase.getInstance().getReference("Cart/$uid")
        if (intent.hasExtra("product_name")&&intent.hasExtra("product_price")&&intent.hasExtra("product_photo")){
            val title = intent.getStringExtra("product_name")?:""
            val price = intent.getDoubleExtra("product_price", 0.0)
            val purl = intent.getStringExtra("product_photo")?:""
            writepdata(title, purl, price)
        }
        /*val query1 = FirebaseDatabase.getInstance().reference.child("Cart").child("$uid")
        query1.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Couldn't get push key for posts")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val order = snapshot.getValue(Product::class.java)
                val database1: DatabaseReference =
                    FirebaseDatabase.getInstance().getReference("Order/$uid")
                val key = database1.child("Order/$uid").push().key
                if (key == null) {
                    Log.w(TAG, "Couldn't get push key for posts")
                    return
                }
                val productValues = order!!.toMap()

                val childUpdates = hashMapOf<String, Any>(
                    "$key" to productValues
                )
                database.updateChildren(childUpdates)
            }
        })*/
        val query : Query = FirebaseDatabase.getInstance().getReference("Cart/$uid")
        Log.w(TAG, "$uid")
        val options: FirebaseRecyclerOptions<Product> = FirebaseRecyclerOptions.Builder<Product>()
            .setQuery(query, Product::class.java)
            .build()
            val ad = CartAdapter(options)
        ad.startListening()
        productsRecyclerView.apply {
            layoutManager = GridLayoutManager(this@Cart, 1, RecyclerView.VERTICAL, false)
            adapter = ad
            progressBar2?.visibility = View.GONE
        }



       /* database.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(this@Cart,"Something Went Wrong!",Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(p0: DataSnapshot) {

                for(i in p0.children) {
                    val pro = i.getValue(Product::class.java)
                    products.add(pro!!)
                }

            }
        })*/

        submit_order.setOnClickListener {

            FirebaseDatabase.getInstance().reference.child("Cart/$uid")
                .removeValue()
            ad.startListening()
            Checkout.preload(applicationContext)
            startpayment()
            /*Toast.makeText(applicationContext,"Order Placed Successfully!",Toast.LENGTH_SHORT).show()
            val myi = Intent(this@Cart, MainActivity::class.java)
            myi.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            this@Cart.startActivity(myi)*/
        }

    }

   private fun startpayment() {
        val checkout = Checkout()
        checkout.setKeyID("rzp_test_l7x9oScajUO2rC")
        /*try {
            val orderRequest = JSONObject()
            orderRequest.put("amount", 50000) // amount in the smallest currency unit
            orderRequest.put("currency", "INR")
            //orderRequest.put("receipt", "order_rcptid_11")
            val order:  = razorpay.Orders.create(orderRequest)
        } catch (e: RazorpayException) {
            // Handle Exception
            System.out.println(e.getMessage())
        }*/
       val activity: Activity = this
       val co = Checkout()

       try {
           val options = JSONObject()
           options.put("name","Ebazzar")
           options.put("description","Product Charges")
           //You can omit the image option to fetch the image from dashboard
           options.put("image","https://s3.amazonaws.com/rzp-mobile/images/rzp.png")
           options.put("theme.color", "#3399cc")
           options.put("currency","INR")
           //options.put("order_id", "order_DBJOWzybf0sJbb")
           options.put("amount","5000")//pass amount in currency subunits

           val prefill = JSONObject()
           prefill.put("email","gaurav.kumar@example.com")
           prefill.put("contact","9876543210")

           options.put("prefill",prefill)
           co.open(activity,options)
       }catch (e: Exception){
           Toast.makeText(activity,"Error in payment: "+ e.message,Toast.LENGTH_LONG).show()
           e.printStackTrace()
       }

    }





    private fun writepdata(title: String, photoUrl: String, price: Double){

        val key = database.child("Cart/$uid").push().key
        if (key == null) {
            Log.w(TAG, "Couldn't get push key for posts")
            return
        }

        val prodat = Product(title, photoUrl, price)
        val productValues = prodat.toMap()

        val childUpdates = hashMapOf<String, Any>(
            "$key " to productValues
        )

        database.updateChildren(childUpdates)
        Toast.makeText(this@Cart, "Successfully Added Item $title to Cart", Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val TAG = "CART"
    }

    override fun onPaymentSuccess(p0: String?) {
        Toast.makeText(this@Cart, "Payment Successful: $p0 ",Toast.LENGTH_LONG).show()

    }

    override fun onPaymentError(errorCode: Int, errorDescription: String?) {
        Toast.makeText(this@Cart, "Error $errorCode \n $errorDescription",Toast.LENGTH_LONG).show()
    }
}