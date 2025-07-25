package com.heldermarques.appreceitas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ReceitaAdapter(
    private val meals: MutableList<Meal>
) : RecyclerView.Adapter<ReceitaAdapter.MealViewHolder>() {

    // Interface de clique no item
    private var onItemClickListener: ((Meal) -> Unit)? = null

    fun setOnItemClickListener(listener: (Meal) -> Unit) {
        onItemClickListener = listener
    }

    class MealViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tituloText: TextView = itemView.findViewById(R.id.textTitulo)
        val imagemView: ImageView = itemView.findViewById(R.id.imgReceita)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_receita, parent, false)
        return MealViewHolder(view)
    }

    override fun getItemCount(): Int = meals.size

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        val meal = meals[position]
        holder.tituloText.text = meal.nome

        Glide.with(holder.itemView.context)
            .load(meal.linkImagem)
            .into(holder.imagemView)

        // Define clique no item
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(meal)
        }
    }
}
