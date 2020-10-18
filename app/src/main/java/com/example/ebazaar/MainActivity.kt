package com.example.ebazaar

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.main.*
import kotlinx.android.synthetic.main.maind.*


class MainActivity : AppCompatActivity(){
    private lateinit var database: DatabaseReference
    private val uid = FirebaseAuth.getInstance().uid
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        VERIFYUSERLOGGEDIN()
        setContentView(R.layout.main)
        val toggle= ActionBarDrawerToggle(this,drawer_layout,toolbar,R.string.open_draw,R.string.close_draw)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        database = FirebaseDatabase.getInstance().getReference("product")

        val products = arrayListOf<Product>()
        val addp: Button = findViewById(R.id.addp)
        addp.setOnClickListener {
            val myi = Intent(this@MainActivity, seller::class.java)
            this@MainActivity.startActivity(myi)  }
        // Read from the database
        for (i in 0..2){
            products.add(Product("Organic Apple","https://via.placeholder.com/300/ffff00",45.0))}
        database.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(p0: DataSnapshot) {
                for(i in p0.children){
                   val pro = i.getValue(Product::class.java)
                    products.add(pro!!)

                }
                recycleview.apply {
                    layoutManager = GridLayoutManager(this@MainActivity, 2)
                    adapter = ProductsAdapter(products)
                }
            }
        })



    }
    private fun VERIFYUSERLOGGEDIN(){

        if(uid==null)
        {
            val intent = Intent(this@MainActivity, Signin::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            this@MainActivity.startActivity(intent)
        }
    }
}
