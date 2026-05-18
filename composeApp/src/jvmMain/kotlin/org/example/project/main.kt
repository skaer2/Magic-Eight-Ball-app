package org.example.project

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.MenuBar
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import magiceight_ball.composeapp.generated.resources.Res
import magiceight_ball.composeapp.generated.resources.info_menu_icon
import org.example.project.main.di.mainWindowModule
import org.example.project.main.ui.MainWindow
import org.example.project.settings.di.settingsWindowModule
import org.example.project.settings.ui.SettingsWindow
import org.jetbrains.compose.resources.painterResource
import org.koin.core.context.startKoin

private sealed interface MyWindow {
    data object Main : MyWindow
    data object Help : MyWindow
    data object DeveloperInfo : MyWindow
}

private sealed interface ScreenRoute

data object SettingsRoute : ScreenRoute
data object MainRoute : ScreenRoute

fun main() = application {
    initKoin()

    val windows = remember { mutableStateListOf<MyWindow>(MyWindow.Main) }
    windows.forEach { window ->
        when (window) {
            MyWindow.Main -> RealMainWindow(windowProvider = {
                windows.add(it)
            })

            MyWindow.Help -> HelpWindow(onCloseRequest = {
                windows.removeIf { it is MyWindow.Help }
            })

            MyWindow.DeveloperInfo -> DeveloperInfoWindow(onCloseRequest = {
                windows.removeIf { it is MyWindow.DeveloperInfo }
            })
        }
    }
}

@Composable
private fun ApplicationScope.RealMainWindow(windowProvider: (MyWindow) -> Unit) {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Magic Eight-ball",
    ) {
        val backstack = remember { mutableStateListOf<ScreenRoute>(MainRoute) }

        NavDisplay(
            backStack = backstack,
            onBack = { backstack.removeLastOrNull() },
            entryProvider = { key ->
                when (key) {
                    MainRoute -> NavEntry(key) {
                        MainWindow({
                            backstack.add(SettingsRoute)
                        })
                    }

                    SettingsRoute -> NavEntry(key) {
                        SettingsWindow({
                            backstack.removeLastOrNull()
                        })
                    }
                }
            }
        )

        MenuBar {
            Menu("О программе") {
                Item(
                    "Справка",
                    icon = painterResource(Res.drawable.info_menu_icon),
                    onClick = {
                        windowProvider(MyWindow.Help)
                    }
                )
                Item(
                    "О разработчике",
                    icon = painterResource(Res.drawable.info_menu_icon),
                    onClick = {
                        windowProvider(MyWindow.DeveloperInfo)
                    }
                )
            }
        }
    }
}

@Composable
private fun HelpWindow(onCloseRequest: () -> Unit) {
    Window(
        onCloseRequest = onCloseRequest,
        title = "Справка"
    ) {

    }
}

@Composable
private fun DeveloperInfoWindow(onCloseRequest: () -> Unit) {
    Window(
        onCloseRequest = onCloseRequest,
        title = "О разработчике"
    ) {

    }
}

fun initKoin() {
    startKoin {
        printLogger()
        modules(mainWindowModule, settingsWindowModule)
    }
}