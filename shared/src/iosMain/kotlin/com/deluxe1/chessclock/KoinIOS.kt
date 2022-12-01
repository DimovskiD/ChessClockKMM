package com.deluxe1.chessclock

import co.touchlab.kermit.Logger
import com.deluxe1.chessclock.db.ChessClockDb
import com.deluxe1.chessclock.db.ChessGame
import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.Settings
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import io.ktor.client.engine.darwin.Darwin
import org.koin.core.Koin
import org.koin.core.KoinApplication
import org.koin.core.component.KoinComponent
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import platform.Foundation.NSUserDefaults

fun initKoinIos(
    userDefaults: NSUserDefaults,
    appInfo: AppInfo,
    doOnStartup: () -> Unit
): KoinApplication = initKoin(
    module {
        single<Settings> { NSUserDefaultsSettings(userDefaults) }
        single { appInfo }
        single { doOnStartup }
    }
)

actual val platformModule = module {
    single<SqlDriver> { NativeSqliteDriver(ChessClockDb.Schema, "ChessGamesDb") }

    single { Darwin.create() }

    single { ChessGamePickerCallbackViewModel(get(), getWith("ChessGamePickerCallbackViewModel")) }

    factory { (game: ChessGame) ->  ChessGameCallbackViewModel(game, getWith("ChessGameCallbackViewModel")) }

}

// Access from Swift to create a logger
@Suppress("unused")
fun Koin.loggerWithTag(tag: String) =
    get<Logger>(qualifier = null) { parametersOf(tag) }

@Suppress("unused") // Called from Swift
object KotlinDependencies : KoinComponent {

    fun getChessGamePickerViewModel() = getKoin().get<ChessGamePickerCallbackViewModel>()

    fun getChessGameViewModel(game: ChessGame) = getKoin().get<ChessGameCallbackViewModel> { parametersOf(game) }

}
