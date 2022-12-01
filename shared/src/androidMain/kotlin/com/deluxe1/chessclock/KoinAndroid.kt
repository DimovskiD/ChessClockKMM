package com.deluxe1.chessclock

import com.deluxe1.chessclock.db.KaMPKitDb
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    single<SqlDriver> {
        AndroidSqliteDriver(
            KaMPKitDb.Schema,
            get(),
            "ChessClockDb"
        )
    }

    single<Settings> {
        SharedPreferencesSettings(get())
    }

    single {
        OkHttp.create()
    }
}
