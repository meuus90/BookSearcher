package com.meuus90.booksearcher.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.paging.PagingData
import com.meuus90.booksearcher.model.data.source.repository.book.BooksRepository
import com.meuus90.booksearcher.model.schema.book.BookItem
import com.meuus90.booksearcher.utils.CoroutineTestRule
import com.meuus90.booksearcher.viewmodel.book.BooksViewModel
import io.mockk.MockKAnnotations
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.rules.TestWatcher
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito
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

    private val coroutineContext = coroutineTestRule.coroutineContext

    private lateinit var viewModel: BooksViewModel

    @Mock
    private lateinit var mockObserver: Observer<PagingData<BookItem>>

    @Captor
    private lateinit var captor: ArgumentCaptor<PagingData<BookItem>>

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)

        val repository = Mockito.mock(BooksRepository::class.java) as BooksRepository

        runBlockingTest {
//            val flow = flowOf(PagingData.empty<BookItem>())
            val flow = flow<PagingData<BookItem>> {
                emit(PagingData.empty())
            }

//            Mockito.`when`(repository.execute(FakeSchema.mockBookSchema))
//                .thenReturn(flow)
        }

        viewModel = BooksViewModel(repository)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun postBookSchemaWithDebounceTest() {
        runBlockingTest {
//            viewModel.postBookSchemaWithDebounce(mockBookSchema0)
//            viewModel.postBookSchemaWithDebounce(mockBookSchema1)
//            viewModel.postBookSchemaWithDebounce(mockBookSchema2)
//
//            Assert.assertEquals(
//                viewModel.org.getOrAwaitValue(),
//                mockBookSchema2
//            )
//
//            println("postBookSchemaWithDebounceTest() pass")
        }
    }
}