package com.meuus90.booksearcher.base.util

import com.meuus90.booksearcher.base.common.util.TimeTools
import org.junit.Assert
import org.junit.Test

class TimeToolsTest {

    @Test
    fun convertDateFormatTest() {
        val converted = TimeTools.convertDateFormat(
            "2000-02-05T00:00:00.000+09:00",
            TimeTools.ISO8601,
            TimeTools.YMD
        )

        Assert.assertEquals(converted, "2000.02.05.")

        println("convertDateFormatTest() pass")
    }
}