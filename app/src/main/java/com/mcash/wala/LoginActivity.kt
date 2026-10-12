package com.mcash.wala

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etUsername = findViewById<android.widget.EditText>(R.id.etUsername)
        val etPassword = findViewById<android.widget.EditText>(R.id.etPassword)
        val btnSignIn  = findViewById<android.widget.Button>(R.id.btnSignIn)
        val tvRegister = findViewById<android.widget.TextView>(R.id.tvRegister)
        val tvForgot   = findViewById<android.widget.TextView>(R.id.tvForgot)

        btnSignIn.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val pass = etPassword.text.toString().trim()
            if (username.isEmpty() || pass.length < 6) {
                Toast.makeText(this, "সঠিক তথ্য দিন", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            auth.signInWithEmailAndPassword("$username@khelobd.app", pass)
                .addOnSuccessListener { result ->
                    if (result.user!!.isEmailVerified) {
                        startActivity(Intent(this, HomeActivity::class.java))
                    } else {
                        startActivity(Intent(this, VerifyEmailActivity::class.java))
                    }
                    finishAffinity()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "ভুল: ${it.message}", Toast.LENGTH_LONG).show()
                }
        }

        tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        tvForgot.setOnClickListener {
            Toast.makeText(this, "Forgot password - coming soon", Toast.LENGTH_SHORT).show()
        }
    }
}
