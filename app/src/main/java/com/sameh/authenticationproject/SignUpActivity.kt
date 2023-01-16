package com.sameh.authenticationproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sameh.authenticationproject.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        binding.btnRegister.setOnClickListener {
            val email = binding.edEmail.text.toString()
            val password = binding.edPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                binding.progressBar.visibility = View.VISIBLE
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) {
                        if (it.isSuccessful) {
                            Toast.makeText(this, "You are created account", Toast.LENGTH_SHORT)
                                .show()
                            binding.progressBar.visibility = View.GONE

                            sendEmailVerification()

                        } else {
                            Toast.makeText(this, "${it.exception!!.message}", Toast.LENGTH_SHORT)
                                .show()
                            binding.progressBar.visibility = View.GONE
                        }
                    }
            } else {
                Toast.makeText(this, "please fill out all fields", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun sendEmailVerification() {
        val currentUser = auth.currentUser
        currentUser!!.sendEmailVerification()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "${it.exception!!.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}