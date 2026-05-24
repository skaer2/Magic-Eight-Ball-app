package org.example.project.settings.di

import androidx.datastore.core.DataStoreFactory
import org.example.project.common.data.ListStringSerializer
import org.example.project.settings.data.AnswersDataSource
import org.example.project.settings.presentation.SettingsWindowMapper
import org.example.project.settings.presentation.SettingsWindowViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import java.io.File
import java.util.prefs.Preferences

val settingsWindowModule = module {
    single<Preferences> { Preferences.userRoot() }
    single { DataStoreFactory.create(
        serializer = ListStringSerializer,
        produceFile = { File(System.getProperty("java.io.tmpdir"), "magicEightBall.preferences_pb") }
    )}

    singleOf(::AnswersDataSource)

    factoryOf(::SettingsWindowMapper)

    viewModelOf(::SettingsWindowViewModel)
}