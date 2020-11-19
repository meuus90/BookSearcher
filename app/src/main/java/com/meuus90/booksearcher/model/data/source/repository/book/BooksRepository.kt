package com.meuus90.booksearcher.model.data.source.repository.book

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.meuus90.booksearcher.base.constant.AppConfig
import com.meuus90.booksearcher.model.data.source.api.DaumAPI
import com.meuus90.booksearcher.model.data.source.local.Cache
import com.meuus90.booksearcher.model.paging.BooksPageKeyedMediator
import com.meuus90.booksearcher.model.paging.PagingDataInterface
import com.meuus90.booksearcher.model.schema.book.BookItem
import com.meuus90.booksearcher.model.schema.book.BookRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class BooksRepository
@Inject
constructor(private val db: Cache, private val daumAPI: DaumAPI) :
    PagingDataInterface<BookRequest, Flow<PagingData<BookItem>>> {

    @ExperimentalPagingApi
    override fun execute(bookSchema: BookRequest) = Pager(
        config = PagingConfig(
            initialLoadSize = AppConfig.localInitialLoadSize,
            pageSize = AppConfig.localPagingSize,
            prefetchDistance = AppConfig.localPrefetchDistance,
            enablePlaceholders = false
        ),
        remoteMediator = BooksPageKeyedMediator(
            db,
            daumAPI,
            bookSchema
        )
    ) {
        db.bookDao().getBooksPagingSource()
    }.flow

    open fun clearCache() = runBlocking { db.bookDao().clear() }
}
