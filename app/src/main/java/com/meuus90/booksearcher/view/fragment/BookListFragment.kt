package com.meuus90.booksearcher.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.meuus90.booksearcher.R
import com.meuus90.booksearcher.base.constant.AppConfig
import com.meuus90.booksearcher.base.view.BaseFragment
import com.meuus90.booksearcher.base.view.ext.gone
import com.meuus90.booksearcher.base.view.ext.show
import com.meuus90.booksearcher.model.paging.BooksPageKeyedMediator
import com.meuus90.booksearcher.model.schema.book.BookRequest
import com.meuus90.booksearcher.view.dialog.SearchOptionDialog
import com.meuus90.booksearcher.view.fragment.adapter.BookListAdapter
import com.meuus90.booksearcher.viewmodel.book.BooksViewModel
import kotlinx.android.synthetic.main.fragment_book_list.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class BookListFragment : BaseFragment() {
    @Inject
    internal lateinit var bookViewModel: BooksViewModel

    private var searchSchema = BookRequest()

    private var createdView: View? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (createdView == null)
            createdView = inflater.inflate(R.layout.fragment_book_list, container, false)
        return createdView
    }

    @ExperimentalCoroutinesApi
    @ExperimentalPagingApi
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViewsListener()
        initAdapter()

        showErrorView(
            0,
            getString(R.string.network_message_welcome_title),
            getString(R.string.network_message_welcome_message)
        )
    }

    private val adapter = BookListAdapter { item, sharedView ->
        val bundle = Bundle()
        bundle.putParcelable("KEY_BOOK", item)

//        val newFragment = addFragment(
//            BookDetailFragment::class.java,
//            BACK_STACK_STATE_ADD,
//            bundle,
//            sharedView,
//            false
//        )

//        newFragment.sharedElementEnterTransition = DetailsTransition()
//        newFragment.enterTransition = null
//        exitTransition = null
//        newFragment.sharedElementReturnTransition = DetailsTransition()
    }

    private fun initViewsListener() {
        iv_filter.setOnClickListener {
//            recyclerView.smoothScrollToPosition(0)
            val dialog =
                SearchOptionDialog(searchSchema.copy()) { schema ->
                    if (searchSchema != schema) {
                        this.searchSchema = schema
                        updateBooks()
                    }
                }
            dialog.show(childFragmentManager, "SearchOptionDialog")
        }

        iv_search.setOnClickListener {
            recyclerView.smoothScrollToPosition(0)
            updateBooks()
        }

        et_search.setOnClickListener {
            appbar_search.setExpanded(true)
        }

        et_search.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || event.keyCode == KeyEvent.KEYCODE_ENTER) {
                updateBooks()
                true
            } else {
                false
            }
        }
    }

    private fun updateBooks() {
        hideKeyboard()

        et_search.text?.trim().toString().let {
            searchSchema.query = it
            bookViewModel.postBookSchema(searchSchema)
        }
    }

    @ExperimentalCoroutinesApi
    @ExperimentalPagingApi
    private fun initAdapter() {
        recyclerView.adapter = adapter

        recyclerView.setItemViewCacheSize(AppConfig.recyclerViewCacheSize)
        recyclerView.setHasFixedSize(false)
        recyclerView.isVerticalScrollBarEnabled = false

        val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        layoutManager.isItemPrefetchEnabled = true
        recyclerView.layoutManager = layoutManager

        lifecycleScope.launchWhenCreated {
            bookViewModel.books
                .collectLatest {
                    adapter.submitData(it)
                }
        }

        lifecycleScope.launchWhenCreated {
            adapter.loadStateFlow
//                .distinctUntilChangedBy { it.append }
                .collectLatest {
                    val state = it.refresh

                    if (state is LoadState.Loading)
                        v_loading.show()
                    else
                        v_loading.gone()

                    if (state is LoadState.Error)
                        updateErrorUI(state)
                    else {
                        recyclerView.show()
                        v_error.gone()
                    }
                }
        }
    }

    @ExperimentalPagingApi
    private fun updateErrorUI(state: LoadState.Error) {
        val error = state.error
        if (error is BooksPageKeyedMediator.EmptyResultException) {
            showErrorView(
                R.drawable.ic_warning,
                getString(
                    R.string.network_message_no_item_title,
                    searchSchema.query
                ),
                getString(R.string.network_message_no_item_message)
            )
        } else {
            showErrorView(
                R.drawable.ic_error,
                getString(R.string.network_message_error_title),
                getString(R.string.network_message_error_message)
            )

        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun showErrorView(drawableResId: Int, title: String, message: String) {
        recyclerView.gone()
        if (drawableResId != 0)
            iv_error.setImageDrawable(context.getDrawable(drawableResId))
        tv_error_title.text = title
        tv_error_message.text = message
        v_error.show()
    }
}