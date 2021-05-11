package com.example.comicstore

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ComicsAdapter(private val data: List<Comic>?) : RecyclerView.Adapter<ComicsAdapter.ViewHolder>() {

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(property: Comic) {
            var database: FirebaseDatabase
            var reference: DatabaseReference
            val title = view.findViewById<TextView>(R.id.titulo)
            val number = view.findViewById<TextView>(R.id.issue_num)
            val pageCount = view.findViewById<TextView>(R.id.page_count)
            val price = view.findViewById<TextView>(R.id.price)
            val imageView = view.findViewById<ImageView>(R.id.photo)
            val add_btn = view.findViewById<Button>(R.id.add_Cart)

            database=FirebaseDatabase.getInstance()
            reference=database.getReference("Comics")
            add_btn.setOnClickListener {

                val comic = Comic(property.title,property.issueNumber,property.description,property.pageCount,property.prices,property.thumbnail)
                val id = reference.push().key
                reference.child(id!!).setValue(comic)
            }

            title.text = property.title
            number.text = ("Número " + property.issueNumber.toString())
            pageCount.text = ("Número de páginas " + property.pageCount.toString())
            price.text = ("$" + property.prices.get(0).price.toString())
            Glide.with(view.context)
                    .load(property.thumbnail.path + "." + property.thumbnail.extension)
                    .fitCenter()
                    .error(R.drawable.ic_launcher_background)
                    .into(imageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.comics_renglon,parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data!![position])
        val comic :Comic = data!![position]

    }

    override fun getItemCount(): Int {
        return data!!.size
    }
}