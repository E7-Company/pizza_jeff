package com.jeff.pizzas.ui.details

import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.jeff.pizzas.R
import com.jeff.pizzas.databinding.FragmentPizzaDetailsBinding
import com.jeff.pizzas.model.*
import com.jeff.pizzas.ui.order.OrderFragment.Companion.BUNDLE_ORDER_ID
import com.jeff.pizzas.utils.*
import com.jeff.pizzas.utils.Constants.USERSTATUS
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PizzaDetailsFragment : Fragment() {

    companion object {
        const val BUNDLE_KEY_ID = "BUNDLE_KEY_ID"
    }

    private var binding: FragmentPizzaDetailsBinding by autoCleared()
    private val viewModel: PizzaDetailsViewModel by viewModels()

    private var order: Order? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPizzaDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getInt(BUNDLE_KEY_ID)?.let { viewModel.start(it) }
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.pizza.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Event.Status.SUCCESS -> {
                    bindPizza(it.data)
                    binding.progressBar.visibility = View.GONE
                    binding.details.visibility = View.VISIBLE
                }

                Event.Status.ERROR ->
                    it.message?.toast(requireContext())

                Event.Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.details.visibility = View.GONE
                }
            }
        })

        viewModel.pendingOrder.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Event.Status.SUCCESS -> {
                    val pendingOrder = it.data
                    order = pendingOrder
                        ?: Order(
                            lines = mutableListOf(),
                            status = OrderStatus.PENDING,
                            time = 0
                        )

                    binding.buttonsLayout.visibility = View.VISIBLE
                }

                Event.Status.ERROR -> it.message?.toast(requireContext())

                else -> {}
            }
        })

        viewModel.request.observe(viewLifecycleOwner, Observer { event ->
            event.getContentIfNotHandled()?.let {
                when (event.status) {
                    Event.Status.SUCCESS -> {
                        order = it
                        callButton()
                        binding.progressBar.visibility = View.GONE
                        binding.details.visibility = View.VISIBLE
                    }

                    Event.Status.ERROR ->
                        event.message?.toast(requireContext())

                    Event.Status.LOADING -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.details.visibility = View.GONE
                    }
                }
            }
        })
    }

    private fun bindPizza(pizza: Pizza?) {
        pizza?.let {
            binding.name.text = it.name
            binding.professions.text = it.content

            binding.image.loadCircularImage(it.imageUrl, binding.root)

            if(binding.buttonsLayout.size == 0)
                pizzaToButtons(it)
        }
    }

    private fun pizzaToButtons(pizza: Pizza) {
        pizza.prices.forEach {
            binding.buttonsLayout.addView(createButton(pizza, it))
        }
    }

    private fun createButton(pizza: Pizza, price: Price): Button {
        val button = Button(context)
        button.text = "${price.size} - ${price.price}€"

        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val userStatus = prefs.getString(USERSTATUS, UserStatus.SINGLE.name)

        if (userStatus == UserStatus.MARRIED.name && Constants.MARRIEDPIZZAS.contains(price.size) ||
            userStatus == UserStatus.SINGLE.name && Constants.SINGLEPIZZAS.contains(price.size)
        ) {
            button.setBackgroundResource(R.color.purple_200)
            button.setTextColor(Color.WHITE)
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(10, 10, 10, 10)
            button.layoutParams = params
        }

        button.setOnClickListener {
            val orderLine = OrderLine(title = pizza.name, price = price)
            order?.lines?.add(orderLine)
            viewModel.registerOrderLine(order)
        }
        return button
    }

    private fun callButton() {
        val builder = context?.let { AlertDialog.Builder(it) }
        builder?.let {
            it.setMessage(resources.getString(R.string.do_you_want_continue))
                .setCancelable(false)
                .setPositiveButton(resources.getString(R.string.yes)) { dialog, _ ->
                    dialog.dismiss()
                }
                .setNegativeButton(resources.getString(R.string.no)) { _, _ ->
                    goToOrder()
                }
            val alert = builder.create()
            alert.show()
        }
    }

    private fun goToOrder() {
        findNavController().navigate(
            R.id.action_pizzaDetailFragment_to_orderFragment,
            bundleOf(BUNDLE_ORDER_ID to order?.id)
        )
    }

}