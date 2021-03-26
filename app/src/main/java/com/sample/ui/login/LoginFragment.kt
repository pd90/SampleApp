package com.sample.ui.login

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.sample.R
import com.sample.constant.Constants
import com.sample.databinding.FragmentLoginBinding
import com.sample.ui.login.viewmodel.LoginViewModel
import com.sample.util.NetworkHelper
import com.sample.util.Status
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class LoginFragment : Fragment() {
    @Inject
    lateinit var networkHelper: NetworkHelper
    private lateinit var binding: FragmentLoginBinding
    private val loginViewModel : LoginViewModel by viewModels()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        loginViewModel.getStatus().observe(viewLifecycleOwner, Observer { handleStatus(it) })
        setupObserver()
        binding.button2.setOnClickListener {
            if(binding.textusername.text.toString().isNotEmpty()) {
                loginViewModel.setUserName(binding.textusername.text.toString())
            }else{
                binding.textInputusername.isErrorEnabled=true
                loginViewModel.setUserName(null)
            }
            if(binding.textusername.text.toString().isNotEmpty()) {
                loginViewModel.setPassword(binding.textpassword.text.toString())
            }else{
                binding.textInputpassword.isErrorEnabled=true
                loginViewModel.setPassword(null)
            }
            loginViewModel.loginPost()
        }

        //add text watcher
        binding.textusername.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                if (!TextUtils.isEmpty(binding.textusername.text)) {
                    binding.textInputusername.isErrorEnabled=false
                }
            }
        }
        binding.textpassword.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                if (!TextUtils.isEmpty(binding.textpassword.text)) {
                    binding.textInputpassword.isErrorEnabled=false
                }
            }
        }
    }


    private fun handleStatus(status: Constants.Status?) {
        when (status) {
            Constants.Status.EMPTY_USERNAME -> {
                binding.textInputusername.error = getString(R.string.valid_username)
            }
            Constants.Status.EMPTY_PASSWORD -> {
                binding.textInputpassword.error = getString(R.string.valid_password)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        return binding.root
    }
    companion object {
        fun newInstance() = LoginFragment()
    }

    private fun setupObserver() {
        activity?.let { it ->
            loginViewModel.events.observe(it, Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        binding.progressBar2.visibility = View.GONE
                        Toast.makeText(
                            activity,
                            getString(R.string.inserted_success),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    Status.LOADING -> {
                        binding.progressBar2.visibility = View.VISIBLE
                    }
                    Status.ERROR -> {
                        //Handle Error
                        Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show()
                    }
                }
            })
        }
    }
}