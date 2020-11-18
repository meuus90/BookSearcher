package com.meuus90.booksearcher.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.meuus90.booksearcher.R
import com.meuus90.booksearcher.base.common.util.NumberTools
import com.meuus90.booksearcher.base.common.util.TimeTools
import com.meuus90.booksearcher.base.view.BaseFragment
import com.meuus90.booksearcher.base.view.util.autoCleared
import com.meuus90.booksearcher.model.schema.book.BookItem
import com.meuus90.booksearcher.view.Caller
import com.meuus90.booksearcher.view.activity.MainActivity
import com.meuus90.booksearcher.viewmodel.book.BookDetailViewModel
import kotlinx.android.synthetic.main.fragment_book_detail.*
import javax.inject.Inject


class BookDetailFragment : BaseFragment() {
    companion object {
        const val KEY_BOOK = "KEY_BOOK"
    }

    private val mainActivity: MainActivity by lazy {
        activity as MainActivity
    }

    var acvView by autoCleared<View>()

    @Inject
    internal lateinit var bookDetailViewModel: BookDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        acvView = inflater.inflate(R.layout.fragment_book_detail, container, false)

        return acvView
    }

    lateinit var bookItem: BookItem
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val bundle = arguments
        bookItem = bundle?.getParcelable(KEY_BOOK)!!

        Glide.with(context).asDrawable().clone()
            .load(bookItem.thumbnail)
            .dontAnimate()
            .error(R.drawable.no_image)
            .into(iv_thumbnail)

        iv_thumbnail.transitionName =
            bookItem.title + bookItem.authors + bookItem.publisher + bookItem.datetime

        tv_title.text = bookItem.title

        tv_date.text =
            TimeTools.convertDateFormat(bookItem.datetime, TimeTools.ISO8601, TimeTools.YMD)

        tv_price.text = NumberTools.convertToString(bookItem.sale_price)

        tv_publisher.text = bookItem.publisher

        tv_contents.text = getString(R.string.contents_format, bookItem.contents)

        iv_thumbs_up.isSelected = bookItem.thumbsUp

        iv_thumbs_up.setOnClickListener {
            bookItem.thumbsUp = !bookItem.thumbsUp
            iv_thumbs_up.isSelected = bookItem.thumbsUp

            bookDetailViewModel.updateBookItem(bookItem)
        }

        tv_next.setOnClickListener {
            val url = bookItem.url
            Caller.openUrlLink(context, url)
        }
        iv_back.setOnClickListener {
            mainActivity.onBackPressed()
        }
    }
}