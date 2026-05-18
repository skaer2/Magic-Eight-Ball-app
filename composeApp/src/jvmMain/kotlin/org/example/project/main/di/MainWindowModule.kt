package org.example.project.main.di

import org.example.project.main.presentation.MainWindowViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val mainWindowModule = module {
    viewModelOf(::MainWindowViewModel)
}