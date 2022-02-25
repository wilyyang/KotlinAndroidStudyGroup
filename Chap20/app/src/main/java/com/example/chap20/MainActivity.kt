package com.example.chap20

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.example.chap20.databinding.ActivityMainBinding
import com.firebase.ui.storage.images.FirebaseImageLoader
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.InputStream

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

    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        db.collection("users").document("ID01").update("email", "lee@b.com")
        db.collection("users").document("ID01").update(mapOf("name" to "lee01", "email" to "lee@c.com"))
        db.collection("users").document("ID01").update(mapOf("avg" to FieldValue.delete()))
        db.collection("users").document("ID01").delete()

        db.collection("users").get().addOnSuccessListener {
            result ->
            for(document in result){
                Log.d("wily9", "${document.id} => ${document.data}")
            }
        }.addOnFailureListener { exception ->
            Log.d("wily9", "Error getting documents: ", exception)
        }

        val docRef = db.collection("users").document("ID01")
        docRef.get().addOnSuccessListener { document ->
            if(document != null){
                Log.d("wily9", "DocumentSnapshot data: ${document.data}")
            }else{
                Log.d("wily9", "No such document")
            }
        }.addOnFailureListener { exception ->
            Log.d("wily9", "get failed with ", exception)
        }

        class User2{
            var name: String? = null
            var email: String? = null
            var avg: Int = 0
        }

        val docRef2 = db.collection("users").document("ID01")
        docRef2.get().addOnSuccessListener { documentSnapshot ->
            val selectUser = documentSnapshot.toObject(User2::class.java)
            Log.d("wily9", "name: ${selectUser?.name}")
        }

        db.collection("users")
            .whereEqualTo("name", "lee")
            .get()
            .addOnSuccessListener { documents ->
                for(document in documents){
                    Log.d("wily9", "${document.id} => ${document.data}")
                }
            }.addOnFailureListener { exception ->
                Log.d("wily9", "Error getting documents: ", exception)
            }
    }

    private fun temp(){
        val bitmap = getBitmapFromView(binding.addPicImageView)
        val baos = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        val storage: FirebaseStorage = Firebase.storage
        val storageRef: StorageReference = storage.reference
        val imgRef: StorageReference = storageRef.child("images/a.jpg")

        var uploadTask = imgRef.putBytes(data)
        uploadTask.addOnFailureListener{
            Log.d("wily9", "upload fail......")
        }.addOnCompleteListener { taskSnapshot ->
            Log.d("wily9", "upload success......")
        }

        val urlTask = uploadTask.continueWithTask { task ->
            if(!task.isSuccessful){
                task.exception?.let {
                    throw it
                }
            }
            imgRef.downloadUrl
        }.addOnCompleteListener { task ->
            if(task.isSuccessful){
                val downloadUri = task.result
                Log.d("wily9", "upload url ...$downloadUri")
            }else{

            }
        }

        val stream = FileInputStream(File("/storage/emulated/0/temp.jpg"))
        val streamTask = imgRef.putStream(stream)

        var file = Uri.fromFile(File("/storage/emulated/0/temp.jpg"))
        val uriTask = imgRef.putFile(file)

        val deleteRef: StorageReference = storageRef.child("images/a.jpg")
        deleteRef.delete()
            .addOnFailureListener {
                Log.d("wily9", "failure.........")
            }
            .addOnCompleteListener {
                Log.d("wily9", "success.........")
            }

        val ONE_MEGABYTE: Long = 1024 * 1024
        imgRef.getBytes(ONE_MEGABYTE).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
            binding.addPicImageView.setImageBitmap(bitmap)
        }.addOnFailureListener {
            Log.d("wily9", "failure.............")
        }

        val localFile = File.createTempFile("images", "jpg")
        imgRef.getFile(localFile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            binding.addPicImageView.setImageBitmap(bitmap)
        }.addOnFailureListener{

        }

        imgRef.downloadUrl.addOnSuccessListener {
            Log.d("wily9", "download uri : $it")
        }.addOnFailureListener {  }

        Glide.with(this).load(imgRef).into(binding.addPicImageView)
    }

    private fun getBitmapFromView(view: View): Bitmap? {
        var bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        var canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }
}

@GlideModule
class MyAppGlideModule : AppGlideModule() {
    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        registry.append(
            StorageReference::class.java, InputStream::class.java,
            FirebaseImageLoader.Factory()
        )
    }
}