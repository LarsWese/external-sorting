package de.wesemann.es

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.io.File
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class FirstStepTest {


    init {
        val tmpFiles = File(System.getProperty("java.io.tmpdir")).list({ _, n -> n.startsWith("run") || n == "chunktest" })
        for (f in tmpFiles) {
            File("${System.getProperty("java.io.tmpdir")}/$f").delete()
        }
    }

    @Test
    fun testReadTwoLines() {
        val first = FirstStep(2)
        val file = readFile("page1")
        val words = first.readWords(file, 0)
        assertEquals(mutableListOf(2, 3, 4, 6), words)

    }

    @Test
    fun testReadTwoLines2() {
        val first = FirstStep(2)
        val file = readFile("page1")
        val words = first.readWords(file, 2)
        assertEquals(mutableListOf(4, 7, 8, 9), words)
    }

    @Test
    fun testReadTwoLines3() {
        val first = FirstStep(2)
        val file = readFile("page1Test")
        val words = first.readWords(file, 8)
        assertEquals(mutableListOf(3, 9), words)
    }

    @Test
    fun testWriteToFile() {
        appendWordChunks(mutableListOf(1, 2, 3, 4, 5), "chunktest")
        val lines = File("${System.getProperty("java.io.tmpdir")}/chunktest").readLines()
        assertEquals(mutableListOf("1,2", "3,4", "5"), lines)
    }

    @Test
    fun runTest() {
        val first = FirstStep(3)
        first.run("page1")
        assertEquals(3, first.fileNames.size)
        var lines = getTotalLines(File("${System.getProperty("java.io.tmpdir")}/run0Chunk1"))
        assertEquals(3, lines)
        lines = getTotalLines(File("${System.getProperty("java.io.tmpdir")}/run0Chunk2"))
        assertEquals(3, lines)
        lines = getTotalLines(File("${System.getProperty("java.io.tmpdir")}/run0Chunk3"))
        assertEquals(2, lines)
    }

    @Test
    fun runTest2() {
        val first = FirstStep(2)
        first.run("page1")
        assertEquals(4, first.fileNames.size)
        for(fileName in first.fileNames){
            val lines = getTotalLines(File("${System.getProperty("java.io.tmpdir")}/$fileName"))
            assertEquals(2, lines)
        }

    }

    @Test
    fun runTest3() {
        val first = FirstStep(2)
        first.run("page2")
        assertEquals(5, first.fileNames.size)
        for(fileName in first.fileNames){
            val lines = getTotalLines(File("${System.getProperty("java.io.tmpdir")}/$fileName"))
            assertEquals(2, lines)
        }
    }
}