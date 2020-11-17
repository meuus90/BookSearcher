package com.meuus90.booksearcher.viewmodel

import androidx.lifecycle.ViewModel

abstract class BaseViewModel<in P, T> : ViewModel() {
    abstract fun pullTrigger(params: P)
}