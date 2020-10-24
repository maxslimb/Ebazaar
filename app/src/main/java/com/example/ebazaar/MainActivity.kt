package com.example.ebazaar

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.main.*
import kotlinx.android.synthetic.main.maind.*


class MainActivity : AppCompatActivity(){
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var database: DatabaseReference
    private val uid = FirebaseAuth.getInstance().uid
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progressBar?.visibility = View.VISIBLE
        VERIFYUSERLOGGEDIN()
        setContentView(R.layout.main)
        toggle= ActionBarDrawerToggle(this,drawer_layout,toolbar,R.string.open_draw,R.string.close_draw)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        nav_view.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.signout -> {
                    AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener {
                            Toast.makeText(applicationContext,"User Signed Out",Toast.LENGTH_SHORT).show()
                            val myi1 = Intent(this@MainActivity, Signin::class.java)
                            myi1.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                            this@MainActivity.startActivity(myi1)
                        }
                }

                R.id.action_home -> Toast.makeText(applicationContext,"clicked on home",Toast.LENGTH_SHORT).show()
            }
            it.isChecked = true
            true
        }
        database = FirebaseDatabase.getInstance().getReference("product")

        val products = arrayListOf<Product>()
        // Read from the database
        /*for (i in 0..2){
            products.add(Product("Organic Apple","https://via.placeholder.com/300/ffff00",45.0))}
        */database.addValueEventListener(object : ValueEventListener {
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
                    progressBar.visibility = View.GONE
                }
            }
        })



    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.actionCart1)
        {
            startActivity(Intent(this,Cart::class.java))
            return true
        }
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater:MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_toolbar,menu)
        return true
    }
    private fun VERIFYUSERLOGGEDIN(){

        if(uid==null)
        {
            val intent = Intent(this@MainActivity, signup::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            this@MainActivity.startActivity(intent)
        }
    }
}
