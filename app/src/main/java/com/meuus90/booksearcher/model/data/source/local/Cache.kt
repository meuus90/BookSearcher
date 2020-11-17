package com.meuus90.booksearcher.model.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.meuus90.booksearcher.base.arch.util.converter.StringListTypeConverter
import com.meuus90.booksearcher.base.arch.util.converter.BigDecimalTypeConverter
import com.meuus90.booksearcher.base.constant.AppConfig
import com.meuus90.booksearcher.model.data.source.local.book.BookDao
import com.meuus90.booksearcher.model.schema.book.BookItem

/**
 * Main cache description.
 */
@Database(
    entities = [BookItem::class],
    exportSchema = false,
    version = AppConfig.roomVersionCode
)

@TypeConverters(BigDecimalTypeConverter::class, StringListTypeConverter::class)
abstract class Cache : RoomDatabase() {
    abstract fun bookDao(): BookDao
}