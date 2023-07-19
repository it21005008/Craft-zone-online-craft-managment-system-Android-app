package com.example.white_rabbit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.white_rabbit.DataClasses.Feedback
import com.example.white_rabbit.databinding.ActivityAddFeedbackBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddFeedback : AppCompatActivity() {
    private lateinit var binding: ActivityAddFeedbackBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseRef: DatabaseReference
    private lateinit var uid: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddFeedbackBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var ratingScore = 0

        database = FirebaseDatabase.getInstance()
        //uid = auth.currentUser?.uid.toString()
        var uid=100.toString()
        var pid=20.toString()
        databaseRef = FirebaseDatabase.getInstance().reference.child("feedback")

        binding.submitFeedbackBtn.setOnClickListener {
            val dis = binding.etNewDescription.text.toString()

            if (dis.isEmpty()) {
                if (dis.isEmpty()) {
                    binding.etNewDescription.error = "Enter feedback"
                }
                Toast.makeText(this, "Enter valid details", Toast.LENGTH_SHORT).show()
            }else {
                var id = databaseRef.push().key!!
                val feedback: Feedback = Feedback(dis, ratingScore.toString(),uid,id,pid)
                databaseRef.child(id).setValue(feedback).addOnCompleteListener {
                    if (it.isSuccessful) {
                        intent = Intent(applicationContext, ViewAllFeedbacks::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(
                            this,
                            "Something went wrong, try again",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
        binding.ratingBar.setOnRatingBarChangeListener { bar, fl, b ->

            when(bar.rating.toInt()){
                1-> ratingScore=1
                2-> ratingScore=2
                3-> ratingScore=3
                4-> ratingScore=4
                5-> ratingScore=5
                else -> ratingScore=0
            }
        }

    }
}