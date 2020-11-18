package com.meuus90.booksearcher.model.schema.book

import com.meuus90.booksearcher.R

data class BookRequest(
    var query: String = "",
    var sort: SortType = SortType.accuracy,
    var target: TargetType = TargetType.all,
) {
    enum class SortType(val idRes: Int, val textRes: Int) {
        accuracy(R.id.v_doc_sort_accuracy, R.string.doc_sort_accuracy),
        recency(R.id.v_doc_sort_recency, R.string.doc_sort_recency);
    }

    enum class TargetType(val idRes: Int, val textRes: Int) {
        all(R.id.v_search_target_all, R.string.search_target_all),
        title(R.id.v_search_target_title, R.string.search_target_title),
        isbn(R.id.v_search_target_isbn, R.string.search_target_isbn),
        publisher(R.id.v_search_target_publisher, R.string.search_target_publisher),
        person(R.id.v_search_target_person, R.string.search_target_person);
    }
}