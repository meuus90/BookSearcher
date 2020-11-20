package com.meuus90.booksearcher.model.schema.book

import com.meuus90.booksearcher.R

data class BookRequest(
    var query: String = "",
    var sort: SortType = SortType.ACCURACY,
    var target: TargetType = TargetType.ALL,
) {
    enum class SortType(val radioBtnResId: Int, val queryStr: String) {
        ACCURACY(R.id.v_doc_sort_accuracy, "accuracy"),
        RECENCY(R.id.v_doc_sort_recency, "recency");
    }

    enum class TargetType(val radioBtnResId: Int, val queryStr: String) {
        ALL(R.id.v_search_target_all, "all"),
        TITLE(R.id.v_search_target_title, "title"),
        ISBN(R.id.v_search_target_isbn, "isbn"),
        PUBLISHER(R.id.v_search_target_publisher, "publisher"),
        PERSON(R.id.v_search_target_person, "person");
    }
}