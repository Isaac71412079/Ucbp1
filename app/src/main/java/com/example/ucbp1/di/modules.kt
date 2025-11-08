package com.example.ucbp1.di

import com.example.ucbp1.R
//import com.example.ucbp1.BuildConfig // <--- AÑADIR ESTE IMPORT
import com.example.ucbp1.features.dollar.data.database.AppRoomDatabase
import com.example.ucbp1.features.dollar.data.datasource.DollarLocalDataSource
import com.example.ucbp1.features.dollar.data.repository.DollarRepository
import com.example.ucbp1.features.dollar.data.datasource.RealTimeRemoteDataSource
import com.example.ucbp1.features.dollar.domain.repository.IDollarRepository

import com.example.ucbp1.features.github.data.api.GithubService
import com.example.ucbp1.features.github.data.datasource.GithubRemoteDataSource
import com.example.ucbp1.features.github.data.repository.GithubRepository
import com.example.ucbp1.features.github.domain.repository.IGithubRepository
import com.example.ucbp1.features.github.domain.usecase.FindByNicknameUseCase
import com.example.ucbp1.features.github.presentation.GithubViewModel

import com.example.ucbp1.features.movie.data.api.MovieService
import com.example.ucbp1.features.movie.data.datasource.MovieLocalDataSource
import com.example.ucbp1.features.movie.data.repository.MovieRepository
import com.example.ucbp1.features.movie.domain.usecase.FetchPopularMoviesUseCase
import com.example.ucbp1.features.movie.presentation.PopularMoviesViewModel
import com.example.ucbp1.features.movie.data.database.AppRoomDataBase as MovieAppRoomDatabase

import com.example.ucbp1.features.profile.application.ProfileViewModel
import com.example.ucbp1.features.profile.data.repository.ProfileRepository
import com.example.ucbp1.features.profile.domain.repository.IProfileRepository
import com.example.ucbp1.features.profile.domain.usecase.GetProfileUseCase

import com.example.ucbp1.features.login.domain.repository.ILoginRepository
import com.example.ucbp1.features.login.domain.usecase.LoginUseCase
import com.example.ucbp1.features.login.presentation.LoginViewModel
import com.example.ucbp1.features.login.data.LoginDataStore

import com.example.ucbp1.navigation.NavigationViewModel
import okhttp3.OkHttpClient
import org.koin.android.BuildConfig
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import com.example.ucbp1.features.dollar.domain.usecase.GetDollarUseCase
import com.example.ucbp1.features.dollar.presentation.DollarViewModel
import com.example.ucbp1.features.login.data.repository.LoginRepository
import com.example.ucbp1.features.movie.data.database.dao.IMovieDao
import com.example.ucbp1.features.movie.domain.repository.IMovieRepository
import com.example.ucbp1.features.movie.domain.usecase.RateMovieUseCase
import com.example.ucbp1.features.movie.domain.usecase.ToggleMovieLikeUseCase
object NetworkConstants {
    const val RETROFIT_GITHUB = "RetrofitGithub"
    const val GITHUB_BASE_URL = "https://api.github.com/"
    const val RETROFIT_MOVIE = "RetrofitMovie"
    const val MOVIE_BASE_URL = "https://api.themoviedb.org/3/"
}

val appModule = module {

    // OkHttpClient
    single {
        OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    // Retrofit github
    single(named(NetworkConstants.RETROFIT_GITHUB)) {
        Retrofit.Builder()
            .baseUrl(NetworkConstants.GITHUB_BASE_URL)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Retrofit movies
    single(named(NetworkConstants.RETROFIT_MOVIE)) {
        Retrofit.Builder()
            .baseUrl(NetworkConstants.MOVIE_BASE_URL)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // GithubService
    single<GithubService> {
        get<Retrofit>( named(NetworkConstants.RETROFIT_GITHUB)).create(GithubService::class.java)
    }

    // login
    single { LoginDataStore(androidContext()) }
    single<ILoginRepository> { LoginRepository(get()) }
    factory { LoginUseCase(get()) }
    viewModel { LoginViewModel(get(), get()) }

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
    viewModel { DollarViewModel(get(), get()) }
    // 1. Database específica para Movie
    single<MovieAppRoomDatabase> { MovieAppRoomDatabase.getDatabase(androidContext()) }
    single<IMovieDao> { get<MovieAppRoomDatabase>().movieDao() }

    // 2. MovieService
    single<MovieService> {
        get<Retrofit>(named(NetworkConstants.RETROFIT_MOVIE)).create(MovieService::class.java)
    }

    // 3. MovieLocalDataSource
    single { MovieLocalDataSource(movieDao = get()) }
    single<IMovieRepository> {
        MovieRepository(
            movieApiService = get(),
            localDataSource = get(),
            apiKey = "fa3e844ce31744388e07fa47c7c5d8c3"
        )
    }

    // 5. UseCases para Movie
    factory { FetchPopularMoviesUseCase(movieRepository = get()) }
    factory { ToggleMovieLikeUseCase(movieRepository = get()) }
    factory { RateMovieUseCase(movieRepository = get()) }
    // 6. ViewModel para Movie
    viewModel {
        PopularMoviesViewModel(
            fetchPopularMoviesUseCase = get(),
            toggleMovieLikeUseCase = get(),
            rateMovieUseCase = get()
        )
    }

    // AÑADIDO: ViewModel para la navegación global
    viewModel { NavigationViewModel() }
}