package com.cowday.todo.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.cowday.todo.R
import com.cowday.todo.data.Note
import com.cowday.todo.screens.EditNoteActivity
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.DocumentSnapshot

class NoteAdapter(getNotes: FirestoreRecyclerOptions<Note>,val listener: OnItemClickListener): FirestoreRecyclerAdapter<Note, NoteAdapter.NoteViewHolder>(getNotes) {
    class NoteViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val note = itemView.findViewById<TextView>(R.id.note)
        val noteCard = itemView.findViewById<CardView>(R.id.note_card)
        val deleteButton = itemView.findViewById<ImageView>(R.id.delete_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_note,parent,false)
        return NoteViewHolder(itemView)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onDataChanged() {
        super.onDataChanged()
        notifyDataSetChanged()
    }
    override fun onBindViewHolder(holder: NoteViewHolder, position: Int, model: Note) {
        holder.note.text = model.note
        val id = snapshots.getSnapshot(position).id
        holder.noteCard.setOnClickListener {
            val i = Intent(it.context,EditNoteActivity::class.java)
            i.putExtra("content",model.note)
            i.putExtra("id",id)
            i.putExtra("position",position)
            it.context.startActivity(i)
        }
        holder.deleteButton.setOnClickListener {
            listener.onDelete(snapshots.getSnapshot(position),position)
        }
    }
    interface OnItemClickListener{
        fun onDelete(doc: DocumentSnapshot, position: Int)
    }
}