package com.example.ebazaar

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.startActivityForResult
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth

class Signin : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signin)
        buildui()
    }
   private fun buildui()
    {val providers = arrayListOf(
        AuthUI.IdpConfig.EmailBuilder().build(),
        AuthUI.IdpConfig.GoogleBuilder().build())

// Create and launch sign-in intent
        startActivityForResult(
            AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
                .setTheme(R.style.AppTheme)
            .build(),
            RC_SIGN_IN)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                if(intent.getStringExtra("user") == "customer"){
                val intent = Intent(this@Signin,MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                    Toast.makeText(this@Signin,"Sign In Successfully",Toast.LENGTH_SHORT).show()
                }
                if(intent.getStringExtra("user") == "seller"){
                    val intent = Intent(this@Signin,seller::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    Toast.makeText(this@Signin,"Sign In Successfully",Toast.LENGTH_SHORT).show()
                }

            }else {
                if (response == null){
                    Toast.makeText(this@Signin,"SignInCancelled",Toast.LENGTH_SHORT).show()
                }
                else{
                    Log.e("signinfailed","${response.error?.errorCode}")
                }
                // ...
            }
        }
    }

    companion object {

        private const val RC_SIGN_IN = 123
    }
}