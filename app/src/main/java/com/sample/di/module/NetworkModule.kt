package com.sample.di.module

import com.sample.BuildConfig
import com.sample.constant.Constants.BASE_URL
import com.sample.data.network.ApiHelper
import com.sample.data.network.ApiHelperImpl
import com.sample.data.network.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.reactivex.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


/**
 * Module which provides all required dependencies for network
 */
@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @Provides
    fun provideBaseUrl() = BuildConfig.BASE_URL

    @Provides
    @Singleton
    fun provideOkHttpClient() = if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    } else OkHttpClient
        .Builder()
        .build()


    @Provides
    @Singleton
    internal fun provideRetrofitInterface(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient())
            .addConverterFactory(GsonConverterFactory.create()).
            addCallAdapterFactory(RxJava2CallAdapterFactory.
            createWithScheduler(Schedulers.io()))
            .build()
    }


    private fun okHttpClient(): OkHttpClient {

        val httpClient = OkHttpClient.Builder()

        val loggingInterceptor = HttpLoggingInterceptor()

        // set your desired log level
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        // add logging as last interceptor
        httpClient.addInterceptor(loggingInterceptor)
        httpClient.addInterceptor { chain ->
            val original = chain.request()

            // Request customization: add request headers
            val requestBuilder = original.newBuilder()
                .header("Content-Type", "application/json")
            val request = requestBuilder.addHeader("X-Acc", BuildConfig.Token).build()
            chain.proceed(request)
        }
        httpClient.retryOnConnectionFailure(true)

        httpClient.connectTimeout(60, TimeUnit.SECONDS)
        httpClient.callTimeout(60, TimeUnit.SECONDS)
        httpClient.readTimeout(60, TimeUnit.SECONDS)
        httpClient.interceptors().add(Interceptor { chain ->
            val request = chain.request()
            var response: Response? = null
            var responseOK = false
            var tryCount = 0

            while (!responseOK && tryCount < 3) {
                try {
                    response = chain.proceed(request)
                    responseOK = response.isSuccessful
                } catch (e: Exception) {
                } finally {
                    tryCount++
                }
            }

            // otherwise just pass the original response on
            response ?: chain.proceed(request)
        })
        return httpClient.build()
    }
    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)
    @Provides
    @Singleton
    fun provideApiHelper(apiHelper: ApiHelperImpl): ApiHelper = apiHelper
}