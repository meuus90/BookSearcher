package com.meuus90.booksearcher.viewmodel.book

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.withTransaction
import com.meuus90.booksearcher.model.data.source.local.Cache
import com.meuus90.booksearcher.model.schema.book.BookItem
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookDetailViewModel
@Inject
constructor(private val db: Cache) : ViewModel() {
    fun updateBookItem(bookItem: BookItem) {
        viewModelScope.launch {
            db.withTransaction { db.bookDao().update(bookItem) }
        }
    }
}