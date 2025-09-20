package com.example.ucbp1.di

import com.example.ucbp1.features.dollar.data.database.AppRoomDatabase
import com.example.ucbp1.features.dollar.data.datasource.DollarLocalDataSource
import com.example.ucbp1.features.dollar.data.repository.DollarRepository
import com.example.ucbp1.features.dollar.datasource.RealTimeRemoteDataSource
import com.example.ucbp1.features.dollar.data.datasource.RealTimeRemoteDataSource
import com.example.ucbp1.features.dollar.domain.repository.IDollarRepository
import com.example.ucbp1.features.github.data.api.GithubService
import com.example.ucbp1.features.github.data.datasource.GithubRemoteDataSource
import com.example.ucbp1.features.github.data.repository.GithubRepository
import com.example.ucbp1.features.github.domain.repository.IGithubRepository
import com.example.ucbp1.features.github.domain.usecase.FindByNicknameUseCase
import com.example.ucbp1.features.github.presentation.GithubViewModel
import com.example.ucbp1.features.profile.application.ProfileViewModel
import com.example.ucbp1.features.profile.data.repository.ProfileRepository
import com.example.ucbp1.features.profile.domain.repository.IProfileRepository
import com.example.ucbp1.features.profile.domain.usecase.GetProfileUseCase
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import com.example.ucbp1.features.dollar.domain.usecase.GetDollarUseCase
import com.example.ucbp1.features.dollar.presentation.DollarViewModel
val appModule = module {

    // OkHttpClient
    single {
        OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    // Retrofit
    single {
        Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // GithubService
    single<GithubService> {
        get<Retrofit>().create(GithubService::class.java)
    }

    single{ GithubRemoteDataSource(get()) }
    single<IGithubRepository>{ GithubRepository(get()) }

    factory { FindByNicknameUseCase(get()) }
    viewModel { GithubViewModel(get(), get()) }

    single<IProfileRepository> { ProfileRepository() }
    factory { GetProfileUseCase(get()) }
    viewModel { ProfileViewModel(get()) }

    single { AppRoomDatabase.getDatabase(get()) }
    single { get<AppRoomDatabase>().dollarDao() }
    single { RealTimeRemoteDataSource() }
    single { DollarLocalDataSource(get()) }
    single<IDollarRepository> { DollarRepository(get(), get()) }
    factory { GetDollarUseCase(get()) }
    viewModel { DollarViewModel(get()) }
}