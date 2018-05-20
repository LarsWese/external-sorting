package de.wesemann.es

import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

class FirstStepTest {


    @Test
    fun testReadTwoLines() {
        val first = FirstStep()
        val file = readFile("page1")
        val words = first.readWords(file, 0)
        assertEquals(mutableListOf(2, 3, 4, 6), words)

    }

    @Test
    fun testReadTwoLines2() {
        val first = FirstStep()
        val file = readFile("page1")
        val words = first.readWords(file, 2)
        assertEquals(mutableListOf(4, 7, 8, 9), words)
    }

    @Test
    fun testReadTwoLines3() {
        val first = FirstStep()
        val file = readFile("page1Test")
        val words = first.readWords(file, 8)
        assertEquals(mutableListOf(3, 9), words)
    }

    @Test
    fun testWriteToFile() {
        val first = FirstStep()
        appendWordChunks(mutableListOf(1, 2, 3, 4, 5), "chunktest")


        val lines = File("${System.getProperty("java.io.tmpdir")}/chunktest").readLines()
        assertEquals(mutableListOf("1,2", "3,4", "5"), lines)
    }

    @Test
    fun readLines() {
        val first = FirstStep()
        val lines = getLines(File("${System.getProperty("java.io.tmpdir")}/chunktest"))

        assertEquals(3, lines)
    }

    @Test
    fun runTest() {
        val first = FirstStep()
        first.run("page1")
    }
}