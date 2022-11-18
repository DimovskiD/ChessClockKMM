package com.deluxe1.chessclock

import com.squareup.sqldelight.db.SqlDriver

internal expect fun testDbConnection(): SqlDriver
