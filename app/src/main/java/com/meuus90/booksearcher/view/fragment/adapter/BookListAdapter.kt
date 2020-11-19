package com.meuus90.booksearcher.view.fragment.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.meuus90.booksearcher.R
import com.meuus90.booksearcher.base.common.util.NumberTools
import com.meuus90.booksearcher.base.common.util.TimeTools
import com.meuus90.booksearcher.base.common.util.TimeTools.Companion.ISO8601
import com.meuus90.booksearcher.base.common.util.TimeTools.Companion.YMD
import com.meuus90.booksearcher.base.view.util.BaseViewHolder
import com.meuus90.booksearcher.base.view.util.loadGlideImage
import com.meuus90.booksearcher.model.schema.book.BookItem
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_book.view.*

class BookListAdapter(val doOnClick: (position: Int, item: BookItem, sharedView: View, thumbsUpView: View) -> Unit) :
    PagingDataAdapter<BookItem, BaseViewHolder<BookItem>>(DIFF_CALLBACK) {
    companion object {
        private val PAYLOAD_TITLE = Any()

        private val DIFF_CALLBACK =
            object : DiffUtil.ItemCallback<BookItem>() {
                override fun areItemsTheSame(oldItem: BookItem, newItem: BookItem): Boolean =
                    oldItem.isbn == newItem.isbn

                override fun areContentsTheSame(oldItem: BookItem, newItem: BookItem): Boolean =
                    oldItem == newItem

                override fun getChangePayload(oldItem: BookItem, newItem: BookItem): Any? {
                    return if (sameExceptTitle(oldItem, newItem)) PAYLOAD_TITLE
                    else null
                }
            }

        private fun sameExceptTitle(
            oldItem: BookItem,
            newItem: BookItem
        ): Boolean {
            return oldItem.copy(isbn = newItem.isbn) == newItem
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<BookItem> {
        val inflater = LayoutInflater.from(parent.context.applicationContext)
        val view = inflater.inflate(R.layout.item_book, parent, false)
        return BookItemHolder(view, this)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<BookItem>, position: Int) {
        getItem(position)?.let { item ->
            holder.bindItemHolder(holder, item, position)
        }
    }

    class BookItemHolder(
        override val containerView: View,
        private val adapter: BookListAdapter
    ) : BaseViewHolder<BookItem>(containerView), LayoutContainer {

        override fun bindItemHolder(
            holder: BaseViewHolder<BookItem>,
            item: BookItem,
            position: Int
        ) {
            containerView.apply {
                iv_thumbnail.loadGlideImage(item.thumbnail, item.databaseId)

                iv_thumbnail.transitionName = item.databaseId.toString() + item.isbn

                tv_title.text = item.title

                tv_date.text = TimeTools.convertDateFormat(item.datetime, ISO8601, YMD)

                iv_thumbs_up.isSelected = item.thumbsUp

                tv_desc.text = item.contents

                tv_price.text = context.getString(
                    R.string.currency_korean,
                    NumberTools.convertToString(item.price)
                )

                v_root.setOnClickListener {
                    adapter.doOnClick(position, item, iv_thumbnail, iv_thumbs_up)
                }
            }
        }

        override fun onItemSelected() {
            containerView.setBackgroundColor(Color.LTGRAY)
        }

        override fun onItemClear() {
            containerView.setBackgroundColor(0)
        }
    }
}