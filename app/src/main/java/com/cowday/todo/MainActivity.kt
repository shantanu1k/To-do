package com.cowday.todo

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.cowday.todo.adapter.NoteAdapter
import com.cowday.todo.data.Note
import com.cowday.todo.databinding.ActivityMainBinding
import com.cowday.todo.screens.AddNoteActivity
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class MainActivity : AppCompatActivity(),NoteAdapter.OnItemClickListener{
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var recyclerView: RecyclerView
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var fStore: FirebaseFirestore
    private lateinit var user: FirebaseUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fStore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        user = auth.currentUser!!
        val query: Query = fStore.collection("notes").document(user.uid).collection("userNotes").orderBy("note",Query.Direction.DESCENDING)
        val getNotes = FirestoreRecyclerOptions.Builder<Note>()
            .setQuery(query,Note::class.java)
            .build()
        recyclerView = binding.noteList
        noteAdapter = NoteAdapter(getNotes,this)
        recyclerView.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        recyclerView.adapter = noteAdapter
        binding.addNewNote.setOnClickListener {
            val i = Intent(this,AddNoteActivity::class.java)
            startActivity(i)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.sign_out){
            auth.signOut()
            val logoutIntent = Intent(this,LoginActivity::class.java)
            //User cannot go back to main page after signing out
            logoutIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(logoutIntent)

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()
        noteAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        noteAdapter.stopListening()
    }

    override fun onDelete(doc: DocumentSnapshot, position: Int) {
        //Position should never be -1
        if(position!=-1){
            doc.reference.delete()
        }
    }
}
