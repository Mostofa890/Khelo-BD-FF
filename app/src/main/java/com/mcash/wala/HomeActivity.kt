package com.mcash.wala

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class HomeActivity : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance("https://mcash-7b610-default-rtdb.firebaseio.com").reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val tvName = findViewById<TextView>(R.id.tvName)
        val tvBalance = findViewById<TextView>(R.id.tvBalance)
        val btnLogout = findViewById<TextView>(R.id.btnLogout)

        val uid = auth.currentUser?.uid ?: run {
            startActivity(Intent(this, LoginActivity::class.java))
            finish(); return
        }

        db.child("users").child(uid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snap: DataSnapshot) {
                val name = snap.child("username").getValue(String::class.java) ?: ""
                val bal = snap.child("balance").getValue(Int::class.java) ?: 0
                tvName.text = "হ্যালো, $name"
                tvBalance.text = "৳ $bal"
            }
            override fun onCancelled(e: DatabaseError) {}
        })

        btnLogout.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finishAffinity()
        }
    }
}
