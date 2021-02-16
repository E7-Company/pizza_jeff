package com.jeff.pizzas.ui.profile

import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeff.pizzas.R
import com.jeff.pizzas.databinding.FragmentProfileBinding
import com.jeff.pizzas.model.*
import com.jeff.pizzas.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var binding: FragmentProfileBinding by autoCleared()
    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var adapter: ProfileAdapter

    companion object {
        val TAG = ProfileFragment::class.java.simpleName
        fun newInstance() = ProfileFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupObservers()
        setupOnClickButton()
    }

    private fun setupRecyclerView() {
        adapter = ProfileAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.user.observe(viewLifecycleOwner, Observer { it ->
            when (it.status) {
                Event.Status.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                    bindProfile(it.data)
                    it.data?.let { data -> adapter.setData(data) }
                }

                Event.Status.ERROR ->
                    it.message?.toast(requireContext())

                Event.Status.LOADING ->
                    binding.progressBar.visibility = View.VISIBLE

            }
        })

        viewModel.updateEvent.observe(viewLifecycleOwner, Observer { it ->
            when (it.status) {
                Event.Status.SUCCESS -> {
                    bindProfile(it.data)
                    binding.progressBar.visibility = View.GONE
                    val prefs = PreferenceManager.getDefaultSharedPreferences(context)
                    prefs.putUserStatus(it.data?.status)
                    resources.getString(R.string.user_updated).toast(requireContext())
                }

                Event.Status.ERROR ->
                    it.message?.toast(requireContext())

                Event.Status.LOADING ->
                    binding.progressBar.visibility = View.VISIBLE

            }
        })

        viewModel.couponEvent.observe(viewLifecycleOwner, Observer { it ->
            bindCoupon(it)
        })
    }

    private fun bindProfile(user: User?) {
        user?.let {
            binding.userName.setText(it.name)
            binding.userEmail.setText(it.email)

            if (it.isMarried()) {
                binding.marriedRadioButton.isChecked = true
            } else {
                binding.singleRadioButton.isChecked = true
            }

            viewModel.setCoupon(it)
        }
    }

    private fun bindCoupon(sizes: Set<String>) {
        binding.couponView.visibility = View.VISIBLE
        binding.couponText.text = "XXXXX\n${resources.getString(R.string.discount_pizzas)} ${sizes}"
    }

    private fun setupOnClickButton() {
        binding.buttonSave.setOnClickListener {
            if (binding.userEmail.text.isEmpty() || !binding.userEmail.text.toString().isEmail())
                resources.getString(R.string.email_incorrect).toast(requireContext())
            else if (binding.userName.text.isEmpty())
                resources.getString(R.string.username_incorrect).toast(requireContext())
            else {
                val user = viewModel.user.value?.data
                user?.let {
                    it.name = binding.userName.text.toString()
                    it.email = binding.userEmail.text.toString()
                    it.status =
                        if (binding.marriedRadioButton.isChecked) UserStatus.MARRIED else UserStatus.SINGLE
                    viewModel.updateUser(it)
                }
            }
        }

        binding.buttonLogout.setOnClickListener {
            exitApp()
        }
    }

    private fun exitApp() {
        context?.deleteDatabase(Constants.DATABASE)
        this.activity?.triggerRestart()
    }

}