package com.example.ucbp1.di

import com.example.ucbp1.data.repository.GithubRepository
import com.example.ucbp1.domain.repository.IGithubRepository
import com.example.ucbp1.domain.usecase.FindByNicknameUseCase
import com.example.ucbp1.presentation.GithubViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<IGithubRepository>{ GithubRepository() }
    factory { FindByNicknameUseCase(get()) }
    viewModel { GithubViewModel(get()) }
}