package com.deluxe1.chessclock

import com.deluxe1.chessclock.db.KaMPKitDb
import co.touchlab.kermit.Logger
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
    single<SqlDriver> { NativeSqliteDriver(KaMPKitDb.Schema, "ChessGamesDb") }

    single { Darwin.create() }

    single { ChessGamePickerCallbackViewModel(get(), getWith("ChessGamePickerCallbackViewModel")) }

    factory { (gameId: Long) ->  ChessGameCallbackViewModel(gameId, get(), getWith("ChessGameCallbackViewModel")) }

}

// Access from Swift to create a logger
@Suppress("unused")
fun Koin.loggerWithTag(tag: String) =
    get<Logger>(qualifier = null) { parametersOf(tag) }

@Suppress("unused") // Called from Swift
object KotlinDependencies : KoinComponent {

    fun getChessGamePickerViewModel() = getKoin().get<ChessGamePickerCallbackViewModel>()

    fun getChessGameViewModel(gameId: Long) = getKoin().get<ChessGameCallbackViewModel> { parametersOf(gameId) }

}
