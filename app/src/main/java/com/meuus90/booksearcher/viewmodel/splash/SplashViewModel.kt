package com.meuus90.booksearcher.viewmodel.splash

import androidx.lifecycle.ViewModel
import com.meuus90.booksearcher.model.data.source.repository.book.BooksRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SplashViewModel
@Inject
constructor(private val repository: BooksRepository) : ViewModel() {
    fun clearCache() = repository.clearCache()
}