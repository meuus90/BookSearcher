package com.meuus90.booksearcher.model.schema.book

import com.meuus90.booksearcher.model.schema.BaseSchema

data class BookResponse(
    val meta: BookMeta,
    val documents: MutableList<BookItem>
) : BaseSchema() {

    data class BookMeta(
        val is_end: Boolean,
        val pageable_count: Int,
        val total_count: Int
    ) : BaseSchema()

}