package com.meuus90.booksearcher.viewmodel.book

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import com.meuus90.booksearcher.base.arch.util.livedata.LiveEvent
import com.meuus90.booksearcher.model.data.source.repository.book.BooksRepository
import com.meuus90.booksearcher.model.schema.book.BookRequest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BooksViewModel
@Inject
constructor(private val repository: BooksRepository) : ViewModel() {
    private lateinit var schemaLiveData: LiveEvent<BookRequest>

    init {
        viewModelScope.launch {
            schemaLiveData = LiveEvent()
        }
    }

    @ExperimentalCoroutinesApi
    @ExperimentalPagingApi
    val books = schemaLiveData.asFlow()
        .flatMapLatest {
            repository.execute(it)
        }

    fun postBookSchema(bookSchema: BookRequest) {
        schemaLiveData.value = bookSchema
    }
}