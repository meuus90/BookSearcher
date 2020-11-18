package com.meuus90.booksearcher.di.module.fragment

import com.meuus90.booksearcher.view.fragment.BookDetailFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BookDetailFragmentModule {
    @ContributesAndroidInjector
    internal abstract fun contributeBookDetailFragment(): BookDetailFragment
}