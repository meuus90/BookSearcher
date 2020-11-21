package com.meuus90.booksearcher.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.transition.Fade
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.doOnPreDraw
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.Behavior.DragCallback
import com.meuus90.booksearcher.R
import com.meuus90.booksearcher.base.constant.AppConfig
import com.meuus90.booksearcher.base.view.BaseActivity.Companion.BACK_STACK_STATE_ADD
import com.meuus90.booksearcher.base.view.BaseFragment
import com.meuus90.booksearcher.base.view.ext.gone
import com.meuus90.booksearcher.base.view.ext.show
import com.meuus90.booksearcher.base.view.util.DetailsTransition
import com.meuus90.booksearcher.base.view.util.autoCleared
import com.meuus90.booksearcher.model.paging.BooksPageKeyedMediator
import com.meuus90.booksearcher.model.schema.book.BookRequest
import com.meuus90.booksearcher.view.dialog.SearchOptionDialog
import com.meuus90.booksearcher.view.fragment.BookDetailFragment.Companion.KEY_BOOK
import com.meuus90.booksearcher.view.fragment.adapter.BookListAdapter
import com.meuus90.booksearcher.viewmodel.book.BookListViewModel
import kotlinx.android.synthetic.main.fragment_book_list.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import javax.inject.Inject


class BookListFragment : BaseFragment() {
    private var acvView by autoCleared<View>()

    @Inject
    internal lateinit var bookListViewModel: BookListViewModel

    private var searchSchema = BookRequest()

    private var isInitialized = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        acvView = inflater.inflate(R.layout.fragment_book_list, container, false)
        return acvView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        recyclerView?.doOnPreDraw { startPostponedEnterTransition() }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (!isInitialized) {
            isInitialized = true
            bookListViewModel.initialize()

            initAppBar()
            initViewsListener()
            initAdapter()

            showErrorView(
                0,
                getString(R.string.network_message_welcome_title),
                getString(R.string.network_message_welcome_message)
            )

        } else {
            adapter.notifyDataSetChanged()
        }
    }

    private val adapter = BookListAdapter { item, sharedView ->
        val bundle = Bundle()
        bundle.putParcelable(KEY_BOOK, item)

        val newFragment = addFragment(
            BookDetailFragment::class.java,
            BACK_STACK_STATE_ADD,
            bundle,
            sharedView
        )

        newFragment.sharedElementEnterTransition = DetailsTransition()
        newFragment.enterTransition = Fade()
        exitTransition = Fade()
        newFragment.sharedElementReturnTransition = DetailsTransition()
    }

    private fun initAppBar() {
        val layoutParams = appbar_search.layoutParams as CoordinatorLayout.LayoutParams
        val behavior =
            layoutParams.behavior as AppBarLayout.Behavior? ?: AppBarLayout.Behavior()
        behavior.setDragCallback(object : DragCallback() {
            override fun canDrag(appBarLayout: AppBarLayout): Boolean {
                return false
            }
        })
        layoutParams.behavior = behavior
    }

    private fun initViewsListener() {
        iv_filter.setOnClickListener {
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

        et_search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    if (searchSchema.query != it.toString())
                        updateBooksByDebounce()
                }
            }
        })
    }

    private fun updateBooks() {
        hideKeyboard()

        et_search.text?.trim().toString().let {
            searchSchema.query = it
            bookListViewModel.postBookSchema(searchSchema)
        }
    }

    private fun updateBooksByDebounce() {
        et_search.text?.trim().toString().let {
            searchSchema.query = it
            bookListViewModel.postBookSchemaWithDebounce(searchSchema)
        }
    }

    private fun initAdapter() {
        recyclerView.adapter = adapter

        recyclerView.setItemViewCacheSize(AppConfig.recyclerViewCacheSize)
        recyclerView.setHasFixedSize(false)
        recyclerView.isVerticalScrollBarEnabled = false

        val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        layoutManager.isItemPrefetchEnabled = true
        recyclerView.layoutManager = layoutManager

        lifecycleScope.launchWhenCreated {
            bookListViewModel.books
                .collectLatest {
                    adapter.submitData(it)
                }
        }

        lifecycleScope.launchWhenCreated {
            adapter.loadStateFlow
                .distinctUntilChangedBy { it.mediator }
                .collectLatest {
                    if (it.refresh is LoadState.Loading)
                        v_loading.show()
                    else
                        v_loading.gone()

                    when {
                        it.refresh is LoadState.Error -> {
                            updateErrorUI(it.refresh as LoadState.Error)
                        }
                        it.append is LoadState.Error -> {
                            updateErrorUI(it.append as LoadState.Error)
                        }
                        else -> {
                            recyclerView.show()
                            v_error.gone()
                        }
                    }
                }
        }
    }

    private fun updateErrorUI(state: LoadState.Error) {
        val error = state.error
        if (error is BooksPageKeyedMediator.EmptyResultException) {
            showToast(
                getString(
                    R.string.network_message_no_item_title,
                    searchSchema.query
                )
            )
            showErrorView(
                R.drawable.ic_warning,
                getString(
                    R.string.network_message_no_item_title,
                    searchSchema.query
                ),
                getString(R.string.network_message_no_item_message)
            )
        } else {
            showToast(getString(R.string.network_message_error_title))
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