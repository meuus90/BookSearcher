package com.meuus90.booksearcher.model.mock

import com.meuus90.booksearcher.model.schema.book.BookRequest

object FakeSchema {
    val mockBookSchema = BookRequest(
        query = "test",
        sort = BookRequest.SortType.accuracy,
        target = BookRequest.TargetType.all
    )

    val mockBookSchema0 = BookRequest(
        "test0",
        sort = BookRequest.SortType.accuracy,
        target = BookRequest.TargetType.all
    )
    val mockBookSchema1 = BookRequest(
        "test1",
        sort = BookRequest.SortType.accuracy,
        target = BookRequest.TargetType.all
    )
    val mockBookSchema2 = BookRequest(
        "test2",
        sort = BookRequest.SortType.accuracy,
        target = BookRequest.TargetType.all
    )
}