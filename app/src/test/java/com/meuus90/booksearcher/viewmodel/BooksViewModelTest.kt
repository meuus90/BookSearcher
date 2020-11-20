package com.meuus90.booksearcher.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.meuus90.booksearcher.model.mock.FakeModel
import com.meuus90.booksearcher.model.mock.FakeRepository
import com.meuus90.booksearcher.model.mock.FakeSchema.mockBookSchema0
import com.meuus90.booksearcher.test_util_kit.CoroutineTestRule
import com.meuus90.booksearcher.test_util_kit.flow.test
import com.meuus90.booksearcher.viewmodel.book.BooksViewModel
import io.mockk.MockKAnnotations
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.rules.TestWatcher
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner


@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
@ExtendWith(MockKExtension::class)
@RunWith(MockitoJUnitRunner::class)
class BooksViewModelTest : TestWatcher() {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private lateinit var viewModel: BooksViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)

        viewModel = BooksViewModel(FakeRepository())
    }

    @After
    fun tearDown() {
    }

    @Test
    fun postBookSchemaTest() {
        runBlockingTest {
            viewModel.postBookSchema(mockBookSchema0)

            val flow = viewModel.books
            flow.test(this) {
                assertValue(FakeModel.mockPagingData)
                println("postBookSchemaTest() pass")
            }
        }
    }
}