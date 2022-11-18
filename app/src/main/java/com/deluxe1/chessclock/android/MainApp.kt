package com.deluxe1.chessclock.android

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.deluxe1.chessclock.AppInfo
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
                viewModel { (gameId: Long) -> ChessGameViewModel(gameId, get(), get { parametersOf("ChessGameViewModel") }) }
                single<SharedPreferences> {
                    get<Context>().getSharedPreferences("CHESS_CLOCK_SETTINGS", Context.MODE_PRIVATE)
                }
                single<AppInfo> { com.deluxe1.chessclock.android.AndroidAppInfo }
                single {
                    { Log.i("Startup", "Hello from Android/Kotlin!") }
                }
            }
        )
    }
}

object AndroidAppInfo : AppInfo {
    override val appId: String = com.deluxe1.chessclock.android.BuildConfig.APPLICATION_ID
}
