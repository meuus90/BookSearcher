package com.meuus90.booksearcher.view.activity

import android.os.Bundle
import com.meuus90.booksearcher.R
import com.meuus90.booksearcher.base.view.BaseActivity
import com.meuus90.booksearcher.view.fragment.BookListFragment

class MainActivity : BaseActivity() {
    override val frameLayoutId = R.id.contentFrame

    override fun setContentView() {
        setContentView(R.layout.activity_main)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        replaceFragment(BookListFragment::class.java)
    }
}