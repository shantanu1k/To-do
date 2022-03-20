package com.cowday.todo.screens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.cowday.todo.databinding.ActivityEditNoteBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class EditNoteActivity : AppCompatActivity(){
    private lateinit var binding: ActivityEditNoteBinding
    private lateinit var fStore: FirebaseFirestore
    private lateinit var user: FirebaseUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        user = FirebaseAuth.getInstance().currentUser!!
        fStore = FirebaseFirestore.getInstance()
        val prevText = intent?.extras?.getString("content")
        val id = intent?.extras?.getString("id")
        //Setting the editText as the recently saved note
        binding.editText.setText(prevText)
        binding.updateButton.setOnClickListener {
            val updateNote = binding.editText.text
            if(updateNote.isEmpty()){
                Toast.makeText(this, "Note should not be empty", Toast.LENGTH_SHORT).show()
            }
            val noteToUpdate = updateNote.toString()
            val map = HashMap<String,Any>()
            map["note"] = noteToUpdate
            val documentReference: DocumentReference = fStore.collection("notes").document(user.uid).collection("userNotes").document(id!!)
            documentReference.update(map).addOnSuccessListener {
                Toast.makeText(this, "Note saved!", Toast.LENGTH_SHORT).show()
                onBackPressed()
            }
            documentReference.set(map).addOnFailureListener {
                Toast.makeText(this, "Couldn't save the note!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}