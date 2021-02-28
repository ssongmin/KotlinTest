package com.example.kotlintest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    var auth: FirebaseAuth? = null
    var googleSignInClient : GoogleSignInClient? = null
    val GOOGLE_LOGIN_CODE = 9001
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        btn_email_login.setOnClickListener(View.OnClickListener { 
            v: View? ->
                signigAndSignup()
        })
        btn_google_login.setOnClickListener {
            googleLogin()
        }
        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    fun googleLogin() {
        var signInintent = googleSignInClient?.signInIntent
        startActivityForResult(signInintent, GOOGLE_LOGIN_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == GOOGLE_LOGIN_CODE){
            Log.e("ssong", "111")
            var result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result != null) {
                Log.e("ssong", result.isSuccess.toString())
                Log.e("ssong", result.status.statusMessage+"")
                Log.e("ssong", "12")
                if(result?.isSuccess){
                    Log.e("ssong", "222")
                    var account = result.signInAccount
                    firebaseAuthWithGoogle(account)
                }
            }
        }
    }

    fun firebaseAuthWithGoogle(account: GoogleSignInAccount?){
        var credential = GoogleAuthProvider.getCredential(account?.idToken, null)

        auth?.signInWithCredential(credential)
            ?.addOnCompleteListener {
                    task ->
                if(task.isSuccessful) {
                    //로그인
                    moveMainPage(task.result?.user)

                } else {
                    //에러
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun signigAndSignup() {
        auth?.createUserWithEmailAndPassword(email_edit_text.text.toString(), pwd_edit_text.text.toString())
            ?.addOnCompleteListener {
                task ->
                    Log.e("ssong", task.toString())
                Log.e("ssong", task.exception.toString())
                Log.e("ssong", task.isSuccessful.toString())
                    if(task.isSuccessful) {
                        //회원가입
                        moveMainPage(task.result?.user)
                    }else {
                        //로그인
                        Log.e("ssong", "111")
                        siginEmail()
                    }
        }
    }

    override fun onStart() {
        super.onStart()
        moveMainPage(auth?.currentUser)
    }

    fun siginEmail() {
        Log.e("ssong", "222")
        auth?.signInWithEmailAndPassword(email_edit_text.text.toString(), pwd_edit_text.text.toString())
            ?.addOnCompleteListener {
                    task ->
                Log.e("ssong", "333")
                if(task.isSuccessful) {
                    //로그인
                    moveMainPage(task.result?.user)

                } else {
                    //에러
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun moveMainPage(user: FirebaseUser?){
        if(user != null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}