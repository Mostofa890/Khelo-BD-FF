package com.mcash.wala

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance("https://mcash-7b610-default-rtdb.firebaseio.com").reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val etUsername = findViewById<android.widget.EditText>(R.id.etUsername)
        val etEmail    = findViewById<android.widget.EditText>(R.id.etEmail)
        val etMobile   = findViewById<android.widget.EditText>(R.id.etMobile)
        val etPassword = findViewById<android.widget.EditText>(R.id.etPassword)
        val cbTerms    = findViewById<android.widget.CheckBox>(R.id.cbTerms)
        val btnCreate  = findViewById<android.widget.Button>(R.id.btnCreate)
        val tvSignIn   = findViewById<android.widget.TextView>(R.id.tvSignIn)

        btnCreate.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val email    = etEmail.text.toString().trim()
            val mobile   = etMobile.text.toString().trim()
            val pass     = etPassword.text.toString().trim()

            when {
                username.isEmpty() -> { toast("Username দিন"); return@setOnClickListener }
                !email.contains("@") -> { toast("সঠিক Email দিন"); return@setOnClickListener }
                mobile.length < 11 -> { toast("সঠিক Mobile দিন"); return@setOnClickListener }
                pass.length < 6 -> { toast("Password কমপক্ষে ৬ ডিজিট"); return@setOnClickListener }
                !cbTerms.isChecked -> { toast("Terms মেনে নিন"); return@setOnClickListener }
            }

            auth.createUserWithEmailAndPassword("$username@khelobd.app", pass)
                .addOnSuccessListener { result ->
                    val uid = result.user!!.uid
                    val user = mapOf(
                        "uid" to uid,
                        "username" to username,
                        "email" to email,
                        "mobile" to mobile,
                        "balance" to 0,
                        "status" to "active",
                        "createdAt" to System.currentTimeMillis()
                    )
                    db.child("users").child(uid).setValue(user).addOnSuccessListener {
                        result.user!!.sendEmailVerification()
                        toast("Verification email পাঠানো হয়েছে")
                        startActivity(Intent(this, VerifyEmailActivity::class.java))
                        finishAffinity()
                    }
                }
                .addOnFailureListener { toast("ব্যর্থ: ${it.message}") }
        }

        tvSignIn.setOnClickListener { finish() }
    }

    private fun toast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}
