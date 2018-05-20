package de.wesemann.es

import java.io.File

class FirstStep {

    var fileNames = arrayListOf<String>()

    var totalLines = -1
    private fun readFile(fileName: String): File {
        return File(FirstStep().javaClass.classLoader.getResource(fileName).toURI())
    }

    fun readWords(file: File, lineStart: Int): List<Int> {
        val words = mutableListOf<Int>()

        val reader = file.reader()

        var currentLine = 0

        reader.forEachLine {
            if (currentLine >= lineStart && words.size <= 2) {
                words.addAll(it.split(",").map { it.toInt() })
            }
            currentLine++
        }
        words.sort()
        return words

    }

    fun run(fileName: String) {
        val file = readFile(fileName)
        totalLines = getLines(file)
        var chunkEnd = 1
        for (i in 0..totalLines step 2) {
            val words = readWords(file, i)
            if (words.isNotEmpty()) {
                val fileName = "run0Chunk$chunkEnd"
                appendWordChunks(words, fileName)
                fileNames.add(fileName)
                chunkEnd++
            }
        }
    }
}