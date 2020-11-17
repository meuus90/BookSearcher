package com.meuus90.booksearcher.model.paging


interface PagingDataInterface<Request, Response> {
    fun execute(bookSchema: Request): Response
}