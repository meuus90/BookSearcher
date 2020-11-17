package com.meuus90.booksearcher.model.mock

import com.meuus90.booksearcher.model.data.source.api.DaumAPI
import com.meuus90.booksearcher.model.schema.book.BookResponse

class FakeDaumAPI : DaumAPI {
    override suspend fun getBookListSus(
        query: String,
        sort: String?,
        target: String?,
        size: Int?,
        page: Int?
    ): BookResponse {
        return FakeModel.mockResponseModel
    }
}