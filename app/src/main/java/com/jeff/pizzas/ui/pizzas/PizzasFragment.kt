package com.jeff.pizzas.ui.pizzas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeff.pizzas.R
import com.jeff.pizzas.databinding.FragmentPizzasBinding
import com.jeff.pizzas.ui.details.PizzaDetailsFragment.Companion.BUNDLE_KEY_ID
import com.jeff.pizzas.utils.Event
import com.jeff.pizzas.utils.autoCleared
import com.jeff.pizzas.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class PizzasFragment: Fragment() {

    companion object {
        const val QUERY_STRING_ID = "QUERY_STRING_ID"
    }

    private var binding: FragmentPizzasBinding by autoCleared()
    private val viewModel: PizzasViewModel by viewModels()
    private lateinit var adapter: PizzasAdapter

    private var lastSearchQuery = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPizzasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupObservers()
        setupSearch()
        restoreInstance(savedInstanceState)
    }

    private fun setupRecyclerView() {
        adapter = PizzasAdapter(::onItemClickListener)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.pizzaList.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Event.Status.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                    if (!it.data.isNullOrEmpty())
                        if (lastSearchQuery.isEmpty())
                            adapter.setData(it.data)
                        else
                            filterList(lastSearchQuery)
                }

                Event.Status.ERROR ->
                    it.message?.toast(requireContext())

                Event.Status.LOADING ->
                    binding.progressBar.visibility = View.VISIBLE
            }
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (lastSearchQuery.isNotEmpty()) {
            outState.putString(QUERY_STRING_ID, lastSearchQuery)
        }
        super.onSaveInstanceState(outState)
    }

    private fun restoreInstance(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            val query = savedInstanceState.getString(QUERY_STRING_ID)
            if (query != null) {
                setLastSearchQuery(query)
            }
        }
    }

    private fun setLastSearchQuery(query: String) {
        lastSearchQuery = query
    }

    private fun setupSearch() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(queryString: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(queryString: String): Boolean {
                filterList(queryString)
                setLastSearchQuery(queryString)
                return true
            }

        })
    }

    private fun filterList(query: String){
        viewModel.pizzaList.value?.data?.filter {
            it.name.toLowerCase(Locale.ROOT)
                .contains(query.toLowerCase(Locale.ROOT))
        }.let {
            if (it != null) {
                adapter.setData(it)
            }
        }
    }

    private fun onItemClickListener(pizzaId: Int) {
        findNavController().navigate(
            R.id.action_pizzasFragment_to_pizzaDetailFragment,
            bundleOf(BUNDLE_KEY_ID to pizzaId)
        )
    }

}