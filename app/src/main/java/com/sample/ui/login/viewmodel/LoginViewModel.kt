package com.sample.ui.login.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.constant.Constants
import com.sample.data.MainRepository
import com.sample.data.model.Login
import com.sample.data.network.LoginApiRequest
import com.sample.util.NetworkHelper
import com.sample.util.Resource
import com.sample.util.extensions.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
    ) : ViewModel() {
    val status = SingleLiveEvent<Constants.Status>()
    val events = MutableLiveData<Resource<Login>>()
    var login = LoginApiRequest()

    fun setUserName(username: String?) {
        login.username = username
    }
    fun getUserName():String? {
        return login.username
    }

    fun setPassword(password: String?) {
        login.password = password
    }
    fun getPassword():String?{
        return login.password
    }
    fun getStatus(): LiveData<Constants.Status> {
        return status
    }
    fun loginPost() {
            validateData().takeIf { it }?.let {
                viewModelScope.launch {
                    events.postValue(Resource.loading(null))
                    if (networkHelper.isNetworkConnected()) {
                        mainRepository.login(login).let {
                            if (it.isSuccessful) {
                                events.postValue(Resource.success(it.body()))
                                mainRepository.insertAll(it.body()?.user)
                            } else events.postValue(Resource.error(it.errorBody().toString(), null))
                        }
                    } else events.postValue(Resource.error("No internet connection", null))
                }
            }
    }
    private fun validateData(): Boolean {
        if (getUserName().isNullOrBlank()) {
               status.value = Constants.Status.EMPTY_USERNAME
               return false
        }
        if (getPassword().isNullOrBlank()) {
            status.value=Constants.Status.EMPTY_PASSWORD
            return false
        }
        return true
    }

    override fun onCleared() {
        super.onCleared()
    }
}
