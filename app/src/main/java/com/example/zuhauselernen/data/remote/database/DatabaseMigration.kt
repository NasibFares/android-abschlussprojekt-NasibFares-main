package com.example.zuhauselernen.data.remote.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_3_4: Migration = object : Migration(3, 4) {
    override fun migrate(database: SupportSQLiteDatabase) {

        database.execSQL("ALTER TABLE game_table ADD COLUMN isFavourite INTEGER NOT NULL DEFAULT 0")
    }
}