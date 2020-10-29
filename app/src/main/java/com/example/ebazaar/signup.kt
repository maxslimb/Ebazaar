package com.example.ebazaar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_signup.*

class signup : AppCompatActivity(){
    private val uid = FirebaseAuth.getInstance().uid
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        customer.setOnClickListener {
            val intent = Intent(this@signup, Signin::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("user", "customer")
            this@signup.startActivity(intent)
        }
        seller.setOnClickListener {
            val intent = Intent(this@signup, Signin::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("user", "seller")
            this@signup.startActivity(intent)
        }

    }

    override fun onStart() {
        super.onStart()
        if(uid!=null)
        {
            if(uid != "uhXnMzyXbBTQallUtFnAwar78Ch2"){
                val intent = Intent(this@signup, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                this@signup.startActivity(intent)
            }
            if(uid == "uhXnMzyXbBTQallUtFnAwar78Ch2"|| uid == "CAhANbG1FLXzJCgtMCRj3hVE6pF2"){
                val intent = Intent(this@signup, com.example.ebazaar.seller::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                this@signup.startActivity(intent)
            }
        }
        if(uid==null){
            TODO()
        }
    }

}