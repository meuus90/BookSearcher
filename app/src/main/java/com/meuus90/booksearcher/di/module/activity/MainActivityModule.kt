package com.meuus90.booksearcher.di.module.activity

import com.meuus90.booksearcher.view.activity.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainActivityModule {
    @ContributesAndroidInjector(
        modules = [
        ]
    )
    internal abstract fun contributeMainActivity(): MainActivity
}