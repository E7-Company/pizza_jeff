package com.jeff.pizzas.ui.pizzas

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jeff.pizzas.databinding.ItemPizzaBinding
import com.jeff.pizzas.model.Pizza
import com.jeff.pizzas.utils.loadCircularImage

class PizzasAdapter internal constructor(
    private val listener: (Int) -> Unit
) : RecyclerView.Adapter<PizzasAdapter.PizzaViewHolder>() {

    private var data = emptyList<Pizza>()

    override fun getItemCount() = data.size
    override fun getItemId(position: Int) = position.toLong()
    override fun getItemViewType(position: Int) = position

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PizzaViewHolder {
        val binding: ItemPizzaBinding = ItemPizzaBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PizzaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PizzaViewHolder, position: Int) = with(holder) {
        val pizza: Pizza = data[position]
        holder.bind(pizza)
        holder.itemView.setOnClickListener { listener(pizza.id) }
    }

    internal fun setData(data: List<Pizza>) {
        this.data = data
        notifyDataSetChanged()
    }

    class PizzaViewHolder(private val itemPizzaBinding: ItemPizzaBinding) : RecyclerView.ViewHolder(
            itemPizzaBinding.root
    ) {
        fun bind(item: Pizza) {
            itemPizzaBinding.nameTextView.text = item.name
            itemPizzaBinding.pizzaImage.loadCircularImage(item.imageUrl, itemPizzaBinding.root)
        }
    }

}