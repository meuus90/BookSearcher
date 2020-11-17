package com.meuus90.booksearcher.viewmodel.book

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import com.meuus90.booksearcher.base.arch.util.livedata.LiveEvent
import com.meuus90.booksearcher.base.common.util.customDebounce
import com.meuus90.booksearcher.model.data.source.repository.book.BooksRepository
import com.meuus90.booksearcher.model.schema.book.BookRequest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BooksViewModel
@Inject
constructor(private val repository: BooksRepository) : ViewModel() {
    val org = MutableLiveData<BookRequest>()
    lateinit var schemaLiveData: LiveEvent<BookRequest>

    init {
        viewModelScope.launch {
            schemaLiveData = LiveEvent<BookRequest>()
            org.asFlow()
                .customDebounce(500L)
                .collect {
                    postBookSchema(it)
                }
        }
    }

    @ExperimentalCoroutinesApi
    @ExperimentalPagingApi
    val books = schemaLiveData.asFlow()
        .distinctUntilChangedBy {
            Timber.e("BooksViewModel bookSchema distinctUntilChangedBy hash ${it.hashCode()}")
            it.hashCode()
        }
        .flatMapLatest {
            repository.execute(it)
        }

    fun postBookSchema(bookSchema: BookRequest) {
        schemaLiveData.value = bookSchema
    }

    fun postBookSchemaWithDebounce(bookSchema: BookRequest) {
        org.value = bookSchema
    }
}