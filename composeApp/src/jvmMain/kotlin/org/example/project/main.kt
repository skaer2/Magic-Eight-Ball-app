package org.example.project

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.MenuBar
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
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

fun main() {
    initKoin()
    application {

        // Список открытых окон приложения
        val windows = remember { mutableStateListOf<MyWindow>(MyWindow.Main) }
        windows.forEach { window ->
            when (window) {
                // Отрисовка главного окна
                MyWindow.Main -> RealMainWindow(windowProvider = {
                    windows.add(it)
                })

                // Отрисовка окна справки
                MyWindow.Help -> HelpWindow(onCloseRequest = {
                    windows.removeIf { it is MyWindow.Help }
                })

                // Отрисовка окна информации о разработчике
                MyWindow.DeveloperInfo -> DeveloperInfoWindow(onCloseRequest = {
                    windows.removeIf { it is MyWindow.DeveloperInfo }
                })
            }
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
        title = "Справка",
        resizable = false,
        state = rememberWindowState(size = DpSize(500.dp, 500.dp)),
    ) {
        Text("Инструкция по использованию приложения «Магический шар»\n" +
                "1. Правила ввода вопросов:\n" +
                "Перейдите на главный экран приложения.\n" +
                "Введите интересующий вас вопрос в специальное текстовое поле. Постарайтесь формулировать вопрос так, чтобы он предполагал закрытый ответ (например, требующий ответа «Да», «Нет» или «Возможно»).\n" +
                "Нажмите кнопку получения предсказания («Готово»).\n" +
                "Дождитесь завершения анимации встряхивания шара, после чего внутри графического контура отобразится случайный ответ.\n" +
                "2. Добавление и удаление вариантов ответов:\n" +
                "Для изменения базы предсказаний перейдите в окно настроек приложения.\n" +
                "Добавление нового ответа: введите текст ответа в поле ввода в нижней части экрана и нажмите кнопку добавления. Ответ не может быть больше 40 символов.\n" +
                "Удаление ответа: найдите нужный вариант в списке доступных ответов и нажмите на кнопку удаления (иконку крестика) рядом с ним. Изменения сразу же запишутся в постоянное хранилище приложения.")
    }
}

@Composable
private fun DeveloperInfoWindow(onCloseRequest: () -> Unit) {
    Window(
        onCloseRequest = onCloseRequest,
        title = "О разработчике",
        resizable = false,
        state = rememberWindowState(size = DpSize(500.dp, 500.dp)),
    ) {
        Text("Информация об авторе проекта\n" +
                "О проекте: Данное приложение представляет собой программную реализацию логики классического генератора предсказаний «Магический шар» (Magic 8 Ball) с возможностью динамического управления базой данных ответов и реактивным обновлением интерфейса. Разработано в рамках выполнения курсового проекта по дисциплине «Информатика и программирование».\n" +
                "Год разработки: 2026 г.\n")
    }
}

fun initKoin() {
    startKoin {
        printLogger()
        modules(mainWindowModule, settingsWindowModule)
    }
}