package com.cowday.todo.screens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.cowday.todo.databinding.ActivityAddNoteBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.collections.HashMap

class AddNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddNoteBinding
    private lateinit var fStore:  FirebaseFirestore
    private lateinit var user: FirebaseUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        user = FirebaseAuth.getInstance().currentUser!!
        fStore = FirebaseFirestore.getInstance()
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        binding.saveTheNote.setOnClickListener {
            val newNote = binding.editText.text
            if(newNote.isEmpty()){
                Toast.makeText(this, "Note should not be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val noteToAdd = newNote.toString()
            val map = HashMap<String, Any>()
            map["note"] = noteToAdd
            val documentReference: DocumentReference = fStore.collection("notes").document(user.uid).collection("userNotes").document()
            documentReference.set(map).addOnSuccessListener {
                Toast.makeText(this, "Note saved!", Toast.LENGTH_SHORT).show()
                onBackPressed()
            }
            documentReference.set(map).addOnFailureListener {
                Toast.makeText(this, "Couldn't save the note!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}