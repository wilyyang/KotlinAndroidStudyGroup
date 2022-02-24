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
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
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

        // Chapter 21
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
//        val colRef: CollectionReference = db.collection("users")
//
//        val docRef: Task<DocumentReference> = colRef.add(
//            mapOf("name" to "kkang", "email" to "a@a.com", "avg" to 10))
//
//        docRef.addOnSuccessListener { Log.d("wly9", "DocumentSnapshot added with ID: ${it.id}") }
//        docRef.addOnFailureListener { Log.w("wly9", "Error adding document", it) }

        db.collection("users").add(mapOf("name" to "wily", "email" to "b@b.com", "avg" to 31))
            .addOnSuccessListener { Log.d("wly9", "DocumentSnapshot added with ID: ${it.id}") }
            .addOnFailureListener { Log.w("wly9", "Error adding document", it)  }

        class User(val name:String, val email:String, val avg: Int, @JvmField val isAdmin: Boolean, val isTop: Boolean)
        val user = User("kim", "kim@a.com", 20, true, true)
        db.collection("users").add(user)

        val user2 = User("lee", "lee@a.com", 30, false, false)
        db.collection("users").document("ID01").set(user2)

        db.collection("users").document("ID01")
    }
}