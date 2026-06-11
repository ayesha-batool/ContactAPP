package com.example.contact_app_recycler_view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.widget.Filter
import android.widget.Filterable
import java.util.*

class ContactAdapter(
    private var contactList: MutableList<Contact>,
    private val listener: OnContactActionListener
) : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>(), Filterable {

    private var contactListFull: List<Contact> = ArrayList(contactList)

    interface OnContactActionListener {
        fun onItemClick(position: Int)
        fun onEditClick(position: Int)
        fun onDeleteClick(position: Int)
    }

    class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvContactName: TextView = itemView.findViewById(R.id.tvContactName)
        val tvContactPhone: TextView = itemView.findViewById(R.id.tvContactPhone)
        val ivContactImage: ImageView = itemView.findViewById(R.id.ivContactImage)
        val btnEdit: Button = itemView.findViewById(R.id.btnEdit)
        val btnDelete: Button = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_item_contact, parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val currentContact = contactList[position]

        holder.tvContactName.text = currentContact.name
        holder.tvContactPhone.text = currentContact.phone
        
        if (currentContact.imageUri != null) {
            holder.ivContactImage.setImageURI(currentContact.imageUri)
        } else {
            holder.ivContactImage.setImageResource(android.R.drawable.ic_menu_report_image)
        }

        holder.itemView.setOnClickListener {
            listener.onItemClick(position)
        }

        holder.btnEdit.setOnClickListener {
            listener.onEditClick(position)
        }

        holder.btnDelete.setOnClickListener {
            listener.onDeleteClick(position)
        }
    }

    override fun getItemCount(): Int {
        return contactList.size
    }

    fun updateList(newList: MutableList<Contact>) {
        contactList = newList
        contactListFull = ArrayList(newList)
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return contactFilter
    }

    private val contactFilter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList = mutableListOf<Contact>()

            if (constraint == null || constraint.isEmpty()) {
                filteredList.addAll(contactListFull)
            } else {
                val filterPattern = constraint.toString().lowercase(Locale.ROOT).trim()
                for (item in contactListFull) {
                    if (item.name.lowercase(Locale.ROOT).contains(filterPattern)) {
                        filteredList.add(item)
                    }
                }
            }

            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            contactList.clear()
            contactList.addAll(results?.values as List<Contact>)
            notifyDataSetChanged()
        }
    }
}