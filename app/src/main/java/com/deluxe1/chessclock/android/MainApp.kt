package com.deluxe1.chessclock.android

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.deluxe1.chessclock.AppInfo
import com.deluxe1.chessclock.db.ChessGame
import com.deluxe1.chessclock.initKoin
import com.deluxe1.chessclock.models.ChessGamePickerViewModel
import com.deluxe1.chessclock.models.ChessGameViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

class MainApp : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin(
            module {
                single<Context> { this@MainApp }
                viewModel { ChessGamePickerViewModel(get(), get { parametersOf("ChessGameViewModel") }) }
                viewModel { (game: ChessGame) -> ChessGameViewModel(game, get { parametersOf("ChessGameViewModel") }) }
                single<SharedPreferences> {
                    get<Context>().getSharedPreferences("CHESS_CLOCK_SETTINGS", Context.MODE_PRIVATE)
                }
                single<AppInfo> { AndroidAppInfo }
                single {
                    { Log.i("Startup", "Hello from Android/Kotlin!") }
                }
            }
        )
    }
}

object AndroidAppInfo : AppInfo {
    override val appId: String = BuildConfig.APPLICATION_ID
}
