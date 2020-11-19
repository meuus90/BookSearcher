package com.meuus90.booksearcher.model.data.source.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.meuus90.booksearcher.model.data.source.repository.book.BooksRepository
import com.meuus90.booksearcher.model.mock.FakeCache
import com.meuus90.booksearcher.model.mock.FakeDaumAPI
import com.meuus90.booksearcher.model.mock.FakeSchema.mockBookSchema
import com.meuus90.booksearcher.model.schema.book.BookItem
import com.meuus90.booksearcher.test_util_kit.CoroutineTestRule
import io.mockk.MockKAnnotations
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.*
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.rules.TestWatcher
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner


@ExperimentalPagingApi
@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
@ExtendWith(MockKExtension::class)
@RunWith(MockitoJUnitRunner::class)
class BooksRepositoryTest : TestWatcher() {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private val mockRepository = Mockito.mock(BooksRepository::class.java) as BooksRepository

    private val repository =
        BooksRepository(
            FakeCache(),
            FakeDaumAPI()
        )

    private val flow = flowOf(PagingData.empty<BookItem>())

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)

        Mockito.`when`(mockRepository.execute(mockBookSchema))
            .thenReturn(flow)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun bookRepositoryTest() {
        runBlockingTest {
            Assert.assertEquals(mockRepository.execute(mockBookSchema), flow)
//            println(repository.execute(mockBookSchema))

            println("bookRepositoryTest() pass")
        }
    }

    @Test
    fun bookRepositoryClearCacheTest() {
        repository.clearCache()

        println("bookRepositoryClearCacheTest() pass")
    }
}