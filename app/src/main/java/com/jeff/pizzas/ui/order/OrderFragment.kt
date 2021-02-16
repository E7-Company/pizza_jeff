package com.jeff.pizzas.ui.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeff.pizzas.R
import com.jeff.pizzas.databinding.FragmentOrderListBinding
import com.jeff.pizzas.model.*
import com.jeff.pizzas.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderFragment : Fragment() {

    companion object {
        const val BUNDLE_ORDER_ID = "BUNDLE_ORDER_ID"
    }

    private var binding: FragmentOrderListBinding by autoCleared()
    private val viewModel: OrderViewModel by viewModels()
    private lateinit var adapter: OrderAdapter

    private var total: Double = 0.0

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrderListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getInt(BUNDLE_ORDER_ID)?.let { viewModel.start(it) }
        setupRecyclerView()
        setupObservers()
        setupOnClickButtons()
    }

    private fun setupRecyclerView() {
        adapter = OrderAdapter(::onItemRemoveItemClickListener)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.user.observe(viewLifecycleOwner, Observer { event ->
            event.getContentIfNotHandled()?.let {
                when (event.status) {
                    Event.Status.SUCCESS -> {
                        binding.progressBar.visibility = View.GONE
                        setMovieOption(event.data)
                    }

                    Event.Status.ERROR ->
                        event.message?.toast(requireContext())

                    Event.Status.LOADING ->
                        binding.progressBar.visibility = View.VISIBLE

                }
            }
        })

        viewModel.order.observe(viewLifecycleOwner, Observer { it ->
            when (it.status) {
                Event.Status.SUCCESS -> {
                    total = it.data?.lines?.sumByDouble { it.price.price } ?: 0.0
                    binding.totalText.text = "${resources.getString(R.string.total)} ${total.format()}"
                    binding.progressBar.visibility = View.GONE
                    it.data?.let { data -> adapter.setData(data) }
                    binding.buttonsLayout.visibility = View.VISIBLE
                }

                Event.Status.ERROR ->
                    it.message?.toast(requireContext())

                Event.Status.LOADING ->
                    binding.progressBar.visibility = View.VISIBLE

            }
        })

        viewModel.updateOrder.observe(viewLifecycleOwner, Observer { it ->
            when (it.status) {
                Event.Status.SUCCESS -> {
                    it.data?.let { data ->
                        data.time = System.currentTimeMillis()
                        val user = viewModel.user.value?.data
                        user?.orders?.add(data)
                        if (user != null) {
                            viewModel.updateUser(user)
                        }
                    }
                }

                Event.Status.ERROR -> it.message?.toast(requireContext())

                Event.Status.LOADING -> binding.progressBar.visibility = View.VISIBLE

            }
        })

        viewModel.updateUser.observe(viewLifecycleOwner, Observer { it ->
            when (it.status) {
                Event.Status.SUCCESS ->
                    goToPizzas()

                Event.Status.ERROR -> it.message?.toast(requireContext())

                Event.Status.LOADING ->
                    binding.progressBar.visibility = View.VISIBLE

            }
        })
    }

    private fun setupOnClickButtons() {
        binding.buttonBuy.setOnClickListener {
            if (total > 0)
                viewModel.updateOrderStatus(OrderStatus.COMPLETE)
            else
                resources.getString(R.string.order_empty).toast(requireContext())
        }

        binding.buttonCancel.setOnClickListener {
            viewModel.updateOrderStatus(OrderStatus.CANCEL)
        }
    }

    private fun onItemRemoveItemClickListener(orderLine: OrderLine) {
        viewModel.removeOrderLine(orderLine)
    }

    private fun setMovieOption(user: User?) {
        if (user != null && user.isMarried())
            showMovieOption()
    }

    private fun showMovieOption() {
        val builder = context?.let { AlertDialog.Builder(it) }
        builder?.let {
            it.setMessage(resources.getString(R.string.buy_movie))
                .setCancelable(false)
                .setPositiveButton(resources.getString(R.string.yes)) { _, _ ->
                    viewModel.addOrderLine(
                        OrderLine(
                            title = Constants.MOVIE,
                            price = Price(Constants.MOVIE, 10.0)
                        )
                    )
                }
                .setNegativeButton(resources.getString(R.string.no)) { dialog, _ ->
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()
        }
    }

    private fun goToPizzas() {
        findNavController().navigate(
                R.id.action_orderFragment_to_pizzasFragment
        )
    }

}