package com.example.ebazaar

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.main.*
import kotlinx.android.synthetic.main.maind.*
import kotlinx.android.synthetic.main.seller.*
import kotlinx.android.synthetic.main.seller_main.*
import java.util.*

class seller: AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var toggle: ActionBarDrawerToggle
    private val uid = FirebaseAuth.getInstance().uid
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.seller_main)
        toggle= ActionBarDrawerToggle(this,drawer_layout1,toolbar_seller,R.string.open_draw,R.string.close_draw)
        drawer_layout1.addDrawerListener(toggle)
        toggle.syncState()
        setSupportActionBar(toolbar_seller)
        nav_view1.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.signout -> {
                    AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener {
                            Toast.makeText(applicationContext,"User Signed Out",Toast.LENGTH_SHORT).show()
                            val myi1 = Intent(this@seller, signup::class.java)
                            myi1.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                            this@seller.startActivity(myi1)
                        }
                }

                R.id.action_home1 -> closeContextMenu()
                R.id.youroder -> startActivity(Intent(applicationContext, yorder::class.java))
            }
           // it.isChecked = true
            true
        }
        database = FirebaseDatabase.getInstance().reference
        val submit:Button ?= findViewById(R.id.sbp)
        val hh:Button ?= findViewById(R.id.home)
        photoselect.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type="image/*"
            startActivityForResult(intent,0)
        }
        submit?.setOnClickListener {
                    uploadimagetofirebaseStorage()
            Toast.makeText(applicationContext,"${pname.text} Successfully Added",Toast.LENGTH_SHORT).show()

        }
        hh?.setOnClickListener {
            val myi = Intent(this@seller, MainActivity::class.java)
            myi.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            this@seller.startActivity(myi)  }
        signout.setOnClickListener {
            signOut()
        }
    }
  var selectphotouri : Uri? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==0 && resultCode==Activity.RESULT_OK && data !=null){
            Log.d("seller","photo  selected")
            selectphotouri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,selectphotouri)
            val bitmapdrawable = BitmapDrawable(bitmap)
            photoselect.setBackgroundDrawable(bitmapdrawable)
            photoselect.text=""
        }
    }
    private fun uploadimagetofirebaseStorage(){
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        ref.putFile(selectphotouri!!)
            .addOnSuccessListener {
        Log.d("seller","success uploaded image: ${it.metadata?.path}")
                ref.downloadUrl.addOnSuccessListener {
                    if (pname != null) {
                        if (pprice != null) {
                            writepdata(pname.text.toString(),it.toString(),pprice.text.toString().toDouble())
                        }
                    }
            }

        }

    }
    private fun writepdata(title:String, photoUrl: String,price:Double){

        val key = database.child("product").push().key
        if (key == null) {
            Log.w(TAG, "Couldn't get push key for posts")
            return
        }

        val prodat = Product(title,photoUrl,price)
        val productValues = prodat.toMap()

        val childUpdates = hashMapOf<String, Any>(
            "/product/$key" to productValues,
            "/user-products/$uid/$key" to productValues
        )
        pname.setText("")
        pprice.setText("")
        pprice.clearFocus()
        photoselect.setBackgroundResource(R.drawable.rounded_ph)
        photoselect.setText(R.string.select_photo)
        database.updateChildren(childUpdates)
        Toast.makeText(applicationContext,"Successfully Added Item $title",Toast.LENGTH_SHORT).show()
    }
    private fun signOut() {
        AuthUI.getInstance()
            .signOut(this)
            .addOnCompleteListener {
                Toast.makeText(this@seller,"User Signed Out",Toast.LENGTH_SHORT).show()
                val myi1 = Intent(this@seller, signup::class.java)
                myi1.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                this@seller.startActivity(myi1)
            }
    }

    companion object {
        private const val TAG = "SELLER"
    }

}
