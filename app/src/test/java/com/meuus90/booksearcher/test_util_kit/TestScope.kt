package com.meuus90.booksearcher.test_util_kit

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineScope
import kotlin.coroutines.CoroutineContext

@ExperimentalCoroutinesApi
class TestScope(override val coroutineContext: CoroutineContext) : ManagedCoroutineScope {
    private val scope = TestCoroutineScope(coroutineContext)
    override fun launch(block: suspend CoroutineScope.() -> Unit): Job {
        return scope.launch {
            block.invoke(this)
        }
    }
}