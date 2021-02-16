package com.jeff.pizzas.ui.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jeff.pizzas.databinding.ItemProfileOrderBinding
import com.jeff.pizzas.model.Order
import com.jeff.pizzas.model.User
import com.jeff.pizzas.utils.format
import com.jeff.pizzas.utils.timestampToDate

class ProfileAdapter: RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder>() {

    private var data = listOf<Order?>()

    override fun getItemCount() = data.size
    override fun getItemId(position: Int) = position.toLong()
    override fun getItemViewType(position: Int) = position

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val binding: ItemProfileOrderBinding = ItemProfileOrderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProfileViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) = with(holder) {
        val order: Order? = data[position]
        holder.bind(order)
    }

    internal fun setData(data: User) {
        this.data = data.orders.sortedByDescending { it.time }
        notifyDataSetChanged()
    }

    class ProfileViewHolder(private val itemProfileOrderBinding: ItemProfileOrderBinding) : RecyclerView.ViewHolder(
        itemProfileOrderBinding.root
    ) {
        fun bind(item: Order?) {
            itemProfileOrderBinding.statusTextView.text = "${item?.time?.timestampToDate()} (${item?.status?.name})"
            val total = item?.lines?.sumByDouble { it.price.price } ?: 0.0
            itemProfileOrderBinding.priceTextView.text = "TOTAL: ${total.format()}"
        }
    }

}