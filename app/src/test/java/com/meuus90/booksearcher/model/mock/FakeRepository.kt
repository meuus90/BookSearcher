package com.meuus90.booksearcher.model.mock

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.meuus90.booksearcher.model.data.source.repository.book.BooksRepository
import com.meuus90.booksearcher.model.mock.FakeModel.mockPagingData
import com.meuus90.booksearcher.model.schema.book.BookItem
import com.meuus90.booksearcher.model.schema.book.BookRequest
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking

class FakeRepository : BooksRepository(FakeCache(), FakeDaumAPI()) {
    @OptIn(ExperimentalPagingApi::class)
    override fun execute(bookSchema: BookRequest): Flow<PagingData<BookItem>> {

        return flow {
            emit(mockPagingData)
            awaitFrame()
        }
    }

    override fun clearCache() = runBlocking { FakeCache().bookDao().clear() }
}