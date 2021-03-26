package com.sample.data

import com.sample.data.local.UserDao
import com.sample.data.model.User
import com.sample.data.network.ApiHelper
import com.sample.data.network.LoginApiRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * All the network and database communication
 * should be done here when asked by a ViewModel
 */

class MainRepository @Inject constructor(private val apiHelper: ApiHelper,private val userDao : UserDao) {

     suspend fun insertAll(user: User?) = withContext(Dispatchers.IO) {  user?.let { userDao.insertUser(it) }}
     suspend fun login(loginApiRequest: LoginApiRequest) =  apiHelper.login(loginApiRequest)
}