package com.meuus90.booksearcher.viewmodel.book

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.meuus90.booksearcher.base.arch.util.livedata.LiveEvent
import com.meuus90.booksearcher.base.common.util.customDebounce
import com.meuus90.booksearcher.model.data.source.repository.book.BooksRepository
import com.meuus90.booksearcher.model.schema.book.BookItem
import com.meuus90.booksearcher.model.schema.book.BookRequest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookListViewModel
@Inject
constructor(private val repository: BooksRepository) : ViewModel() {
    lateinit var org: LiveEvent<BookRequest>
    lateinit var schemaLiveData: LiveEvent<BookRequest>
    lateinit var books: Flow<PagingData<BookItem>>

    @OptIn(ExperimentalPagingApi::class, ExperimentalCoroutinesApi::class)
    fun initialize() {
        org = LiveEvent()
        schemaLiveData = LiveEvent()
        books = schemaLiveData.asFlow()
            .distinctUntilChangedBy { it.hashCode() }
            .flatMapLatest {
                repository.execute(it)
            }

        viewModelScope.launch {
            org.asFlow()
                .customDebounce(1000L)
                .collect {
                    postBookSchema(it)
                }
        }
    }

    fun postBookSchema(bookSchema: BookRequest) {
        schemaLiveData.value = bookSchema
    }

    fun postBookSchemaWithDebounce(bookSchema: BookRequest) {
        org.value = bookSchema
    }
}