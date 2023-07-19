package com.example.white_rabbit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Half.toFloat
import android.widget.Toast
import com.example.white_rabbit.DataClasses.Feedback
import com.example.white_rabbit.databinding.ActivityViewFeedbackBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ViewFeedback : AppCompatActivity() {
    private lateinit var binding: ActivityViewFeedbackBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseRef: DatabaseReference
    private lateinit var uid: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewFeedbackBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var uid = 100.toString()
        var pid = 20.toString()
        var ratingScore = 0
        var currentFedId = ""

        //initialize variables
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        uid = auth.currentUser?.uid.toString()
        databaseRef = FirebaseDatabase.getInstance().reference.child("feedback")
        val id = intent.getStringExtra("id").toString()
        val des = intent.getStringExtra("des").toString()
        val rating = intent.getStringExtra("rating").toString()

                binding.etNewDescription.setText(des)
                binding.ratingBar.rating = rating.toFloat()
                currentFedId = id

        binding.saveBtn.setOnClickListener {
            val dis = binding.etNewDescription.text.toString()

            if (dis.isEmpty()) {
                if (dis.isEmpty()) {
                    binding.etNewDescription.error = "Enter feedback"
                }
                Toast.makeText(this, "Enter valid details", Toast.LENGTH_SHORT).show()
            } else {
                val map = HashMap<String, Any>()

                //add data to hashMap
                map["dis"] = dis
                map["ratingScore"] = ratingScore.toString()


                //update database from hashMap
                databaseRef.child(currentFedId).updateChildren(map).addOnCompleteListener {
                    if (it.isSuccessful) {
                        intent = Intent(applicationContext, ViewAllFeedbacks::class.java)
                        startActivity(intent)
                        Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(this, it.exception?.message, Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }


        binding.ratingBar.setOnRatingBarChangeListener { bar, fl, b ->

            when (bar.rating.toInt()) {
                1 -> ratingScore = 1
                2 -> ratingScore = 2
                3 -> ratingScore = 3
                4 -> ratingScore = 4
                5 -> ratingScore = 5
                else -> ratingScore = 0
            }
        }

        binding.deleteBtn.setOnClickListener {
            databaseRef.child(currentFedId).removeValue().addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Item deleted", Toast.LENGTH_SHORT).show()
                    intent = Intent(applicationContext, ViewAllFeedbacks::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}