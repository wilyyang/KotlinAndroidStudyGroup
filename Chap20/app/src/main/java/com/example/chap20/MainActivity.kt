package com.example.chap20

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth

    private val googleForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result?.data)
                try {
                    val account = task.getResult(ApiException::class.java)!!
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    auth.signInWithCredential(credential)
                        .addOnCompleteListener(this) { task->
                            if(task.isSuccessful){

                            }else{

                            }
                        }
                }catch(e: ApiException){
                    e.printStackTrace()
                }

            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth
        auth.createUserWithEmailAndPassword("email", "password")
            .addOnCompleteListener(this){
                    task->
                if(task.isSuccessful){
                    auth.currentUser?.sendEmailVerification()
                        ?.addOnCompleteListener { sendTask ->
                            if(sendTask.isSuccessful){

                            }else{

                            }
                        }
                }else{
                    Log.w("wily8", "createUserWithEmailAndPassword", task.exception)
                }

            }
        auth.signInWithEmailAndPassword("email", "password")
            .addOnCompleteListener(this) { task ->
                if(task.isSuccessful){
                    auth.currentUser?.let {
                        val isEmailVerified = auth.currentUser?.isEmailVerified
                        val email = auth.currentUser?.email
                        val uid = auth.currentUser?.uid
                    }

                }else{

                }
            }
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("default_web_client_id")
            .requestEmail()
            .build()
        val signInIntent = GoogleSignIn.getClient(this, gso).signInIntent
        googleForResult.launch(signInIntent)
    }
}