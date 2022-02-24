package com.example.ch20_firebase

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.ch20_firebase.databinding.ActivityAuthBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider

private const val MODE_LOGIN = "login"
private const val MODE_LOGOUT = "logout"
private const val MODE_SIGNIN = "signin"
class AuthActivity : AppCompatActivity() {
    lateinit var binding: ActivityAuthBinding

    private val googleLoginForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result?.data)
                try {
                    val account = task.getResult(ApiException::class.java)!!

                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    MyApplication.auth.signInWithCredential(credential)
                        .addOnCompleteListener(this) { task->
                            if(task.isSuccessful){
                                MyApplication.email = account.email
                                changeVisibility(MODE_LOGIN)
                            }else{
                                changeVisibility(MODE_LOGOUT)
                            }
                        }
                }catch(e: ApiException){
                    changeVisibility(MODE_LOGOUT)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(MyApplication.checkAuth()){
            changeVisibility(MODE_LOGIN)
        }else{
            changeVisibility(MODE_LOGOUT)
        }

        binding.logoutBtn.setOnClickListener {
            MyApplication.auth.signOut()
            MyApplication.email = null
            changeVisibility(MODE_LOGOUT)
        }

        binding.goSignInBtn.setOnClickListener {
            changeVisibility(MODE_SIGNIN)
        }

        binding.googleLoginBtn.setOnClickListener {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
            val signInIntent = GoogleSignIn.getClient(this, gso).signInIntent
            googleLoginForResult.launch(signInIntent)
        }

        binding.signBtn.setOnClickListener {
            val email: String = binding.authEmailEditView.text.toString()
            val password: String = binding.authPasswordEditView.text.toString()

            MyApplication.auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    binding.authEmailEditView.text.clear()
                    binding.authPasswordEditView.text.clear()
                    if(task.isSuccessful){
                        MyApplication.auth.currentUser?.sendEmailVerification()
                            ?.addOnCompleteListener { sendTask->
                                if(sendTask.isSuccessful){
                                    Toast.makeText(baseContext, "회원가입에 성공하였습니다. 전송된 메일을 확인해 주세요.", Toast.LENGTH_SHORT).show()
                                    changeVisibility(MODE_LOGOUT)
                                }else{
                                    Toast.makeText(baseContext, "메일 전송 실패", Toast.LENGTH_SHORT).show()
                                    changeVisibility(MODE_LOGOUT)
                                }
                            }
                    } else {
                        Toast.makeText(baseContext, "회원가입 실패", Toast.LENGTH_SHORT).show()
                        changeVisibility(MODE_LOGOUT)
                    }
                }
        }

        binding.loginBtn.setOnClickListener {
            val email: String = binding.authEmailEditView.text.toString()
            val password: String = binding.authPasswordEditView.text.toString()

            MyApplication.auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    binding.authEmailEditView.text.clear()
                    binding.authPasswordEditView.text.clear()
                    if(task.isSuccessful){
                        if(MyApplication.checkAuth()){
                            MyApplication.email = email
                            changeVisibility(MODE_LOGIN)
                        }else{
                            Toast.makeText(baseContext, "전송된 메일로 이메일 인증이 되지 않았습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        Toast.makeText(baseContext, "로그인 실패", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun changeVisibility(mode: String){
        if(mode == MODE_LOGIN){
            binding.run {
                authMainTextView.text = "${MyApplication.email} 님 반갑습니다."
                logoutBtn.visibility = View.VISIBLE
                goSignInBtn.visibility = View.GONE
                googleLoginBtn.visibility = View.GONE
                authEmailEditView.visibility = View.GONE
                authPasswordEditView.visibility = View.GONE
                signBtn.visibility = View.GONE
                loginBtn.visibility = View.GONE
            }
        }else if(mode == MODE_LOGOUT){
            binding.run {
                authMainTextView.text = "로그인 하거나 회원가입 해주세요."
                logoutBtn.visibility = View.GONE
                goSignInBtn.visibility = View.VISIBLE
                googleLoginBtn.visibility = View.VISIBLE
                authEmailEditView.visibility = View.VISIBLE
                authPasswordEditView.visibility = View.VISIBLE
                signBtn.visibility = View.GONE
                loginBtn.visibility = View.VISIBLE
            }
        }else if(mode == MODE_SIGNIN){
            binding.run {
                logoutBtn.visibility = View.GONE
                goSignInBtn.visibility = View.GONE
                googleLoginBtn.visibility = View.GONE
                authEmailEditView.visibility = View.VISIBLE
                authPasswordEditView.visibility = View.VISIBLE
                signBtn.visibility = View.VISIBLE
                loginBtn.visibility = View.GONE
            }
        }
    }
}