package com.mcash.wala

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class VerifyEmailActivity : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_email)

        val btnContinue = findViewById<android.widget.Button>(R.id.btnContinue)
        val tvNoOther   = findViewById<android.widget.TextView>(R.id.tvNoOther)

        btnContinue.setOnClickListener {
            val user = auth.currentUser ?: run { finish(); return@setOnClickListener }
            user.reload().addOnSuccessListener {
                if (user.isEmailVerified) {
                    startActivity(Intent(this, HomeActivity::class.java))
                    finishAffinity()
                } else {
                    user.sendEmailVerification()
                    Toast.makeText(this, "Email আবার পাঠানো হয়েছে", Toast.LENGTH_SHORT).show()
                }
            }
        }

        tvNoOther.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finishAffinity()
        }
    }
}
