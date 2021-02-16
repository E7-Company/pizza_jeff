package com.jeff.pizzas.ui.register

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.jeff.pizzas.R
import com.jeff.pizzas.databinding.FragmentRegisterBinding
import com.jeff.pizzas.model.User
import com.jeff.pizzas.model.UserStatus
import com.jeff.pizzas.ui.MainActivity
import com.jeff.pizzas.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment: Fragment() {

    private var binding: FragmentRegisterBinding by autoCleared()
    private val viewModel: RegisterViewModel by viewModels()

    private var user: User? = null
    private var userStatus = UserStatus.SINGLE

    companion object {
        val TAG = RegisterFragment::class.java.simpleName
        fun newInstance() = RegisterFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        setupOnClickButtons()
    }

    private fun setupObservers() {
        viewModel.user.observe(viewLifecycleOwner, Observer { event ->
            event.getContentIfNotHandled()?.let {
                when (event.status) {
                    Event.Status.SUCCESS -> {
                        user = event.data
                        if (user != null) {
                            val prefs = PreferenceManager.getDefaultSharedPreferences(context)
                            prefs.putUserStatus(user?.status)
                            goToPizzas()
                        } else
                            showLoading(false)
                    }

                    Event.Status.ERROR -> {
                        showLoading(false)
                        event.message?.toast(requireContext())
                    }

                    Event.Status.LOADING -> showLoading(true)
                }
            }
        })

        viewModel.registerEvent.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Event.Status.SUCCESS -> {
                    goToPizzas()
                }

                Event.Status.ERROR -> {
                    it.message?.toast(requireContext())
                    showLoading(false)
                }

                Event.Status.LOADING -> showLoading(true)
            }
        })
    }

    private fun setupOnClickButtons() {
        binding.buttonRegister.setOnClickListener {
            if (isCorrectRegister()) {
                viewModel.registerUser(
                    User(
                        email = binding.enterEmail.text.toString(),
                        name = binding.userName.text.toString(),
                        status = userStatus,
                        orders = mutableListOf()
                    )
                )
            }
        }

        binding.statusRadioGroup.setOnCheckedChangeListener { _, radioButton ->
            when (radioButton) {
                binding.marriedRadioButton.id -> userStatus = UserStatus.MARRIED
                binding.singleRadioButton.id -> userStatus = UserStatus.SINGLE
            }
        }
    }

    private fun isCorrectRegister(): Boolean {
        if (binding.userName.text.isEmpty()) {
            binding.nameError.error = resources.getString(R.string.username_incorrect)
            return false
        } else
            binding.nameError.isErrorEnabled = false

        if (!binding.enterEmail.text.toString().isEmail()) {
            binding.emailError.error = resources.getString(R.string.email_incorrect)
            return false
        } else
            binding.emailError.isErrorEnabled = false

        return true
    }

    private fun showLoading(show: Boolean) {
        if (show) {
            binding.progressBar.visibility = View.VISIBLE
            binding.registerLayout.visibility = View.GONE
            binding.buttonRegister.visibility = View.GONE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.registerLayout.visibility = View.VISIBLE
            binding.buttonRegister.visibility = View.VISIBLE
        }
    }

    private fun goToPizzas() {
        activity?.finish()
        startActivity(Intent(context, MainActivity::class.java))
    }

}