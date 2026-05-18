package org.example.project.settings.di

import androidx.datastore.core.DataStoreFactory
import org.example.project.settings.data.AnswersDataSource
import org.example.project.settings.presentation.SettingsWindowMapper
import org.example.project.settings.presentation.SettingsWindowViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import java.util.prefs.Preferences

val settingsWindowModule = module {
    single<Preferences> { Preferences.userRoot() }
    single { DataStoreFactory }

    singleOf(::AnswersDataSource)

    factoryOf(::SettingsWindowMapper)

    viewModelOf(::SettingsWindowViewModel)
}