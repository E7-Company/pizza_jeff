package com.jeff.pizzas.ui.order

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jeff.pizzas.databinding.ItemOrderBinding
import com.jeff.pizzas.model.Order
import com.jeff.pizzas.model.OrderLine
import com.jeff.pizzas.utils.format

class OrderAdapter internal constructor(
        private val listener: (OrderLine) -> Unit
): RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    private var data = emptyList<OrderLine>()

    override fun getItemCount() = data.size
    override fun getItemId(position: Int) = position.toLong()
    override fun getItemViewType(position: Int) = position

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding: ItemOrderBinding = ItemOrderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
        )
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) = with(holder) {
        val orderLine: OrderLine = data[position]
        holder.bind(orderLine, listener)
    }

    internal fun setData(data: Order) {
        this.data = data.lines
        notifyDataSetChanged()
    }

    class OrderViewHolder(private val itemOrderBinding: ItemOrderBinding) : RecyclerView.ViewHolder(
            itemOrderBinding.root
    ) {
        fun bind(item: OrderLine, listener: (OrderLine) -> Unit) {
            itemOrderBinding.nameTextView.text = item.title
            itemOrderBinding.dataTextView.text = "${item.price.size} - ${item.price.price.format()}"
            itemOrderBinding.orderImageButton.setOnClickListener { listener(item) }
        }
    }

}