package com.meuus90.booksearcher.di.module.fragment

import androidx.paging.ExperimentalPagingApi
import com.meuus90.booksearcher.view.fragment.BookListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BookListFragmentModule {
    @ContributesAndroidInjector
    internal abstract fun contributeBookListFragment(): BookListFragment
}