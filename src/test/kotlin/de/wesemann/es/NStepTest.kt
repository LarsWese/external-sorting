package de.wesemann.es

import de.wesemann.es.dto.FileInfo
import de.wesemann.es.dto.RunMeta
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

class NStepTest {
    @Test
    fun sotreLeastTwoWords() {
        val step = NStep(2)
        val words = mutableListOf(2, 3, 4, 5)
        step.removeFristNWords(words, 2)
        assertEquals(mutableListOf(4, 5), words)
    }

    @Test
    fun storeFirstThreeWords() {
        val step = NStep(3)
        val words = mutableListOf(2, 3, 4, 5)
        step.removeFristNWords(words, 3)
        assertEquals(mutableListOf(5), words)
    }

    @Test
    fun sortAndWriteTest() {
        val step = NStep()
        step.newFileName = "run0chunk0"
        val wordsLeft = mutableListOf(4, 5, 2, 3)
        step.sortAndWriteFirstNWords(wordsLeft)
        assertEquals(mutableListOf(4, 5), wordsLeft)
        val words = readLineAndReturnWords(readFile(fileName = step.newFileName), 0)
        assertEquals(mutableListOf(2, 3), words)
    }

    @Test
    fun getCurrentWordsTest() {
        val step = NStep()
        val fileInfo = mutableListOf<FileInfo>().apply {
            val file1 = readFile("page1")
            add(FileInfo(file1, getTotalLines(file1)))
            val file2 = readFile("page1Test")
            add(FileInfo(file2, getTotalLines(file2)))
        }
        val meta = RunMeta(2, fileInfo = fileInfo)
        var words = step.getCurrentWords(meta, 0)
        assertEquals(mutableListOf(3, 4, 3, 4), words)

         words = step.getCurrentWords(meta, 4)
        assertEquals(mutableListOf(5,6,5,6), words)
    }
}

fun readFile(fileName: String)= File(NStepTest().javaClass.classLoader.getResource(fileName).toURI())