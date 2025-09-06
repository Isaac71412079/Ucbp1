package com.example.ucbp1.di

import com.example.ucbp1.features.github.data.repository.GithubRepository
import com.example.ucbp1.features.github.domain.repository.IGithubRepository
import com.example.ucbp1.features.github.domain.usecase.FindByNicknameUseCase
import com.example.ucbp1.features.github.presentation.GithubViewModel
import com.example.ucbp1.features.profile.application.ProfileViewModel
import com.example.ucbp1.features.profile.data.repository.ProfileRepository
import com.example.ucbp1.features.profile.domain.repository.IProfileRepository
import com.example.ucbp1.features.profile.domain.usecase.GetProfileUseCase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<IGithubRepository>{ GithubRepository() }
    factory { FindByNicknameUseCase(get()) }
    viewModel { GithubViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
    factory { GetProfileUseCase(get()) }
    single<IProfileRepository> { ProfileRepository() }
}