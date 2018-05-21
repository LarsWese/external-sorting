package de.wesemann.es

import java.io.File

class FirstStep(val maxLines: Int) {

    var fileNames = arrayListOf<String>()

    var totalLines = -1
    private fun readFile(fileName: String): File {
        return File(FirstStep(2).javaClass.classLoader.getResource(fileName).toURI())
    }

    fun readWords(file: File, lineStart: Int): List<Int> {
        val words = mutableListOf<Int>()

        val reader = file.reader()

        var currentLine = 0
        var linesRead = 0
        reader.forEachLine {
            if (currentLine >= lineStart && linesRead < maxLines) {
                words.addAll(it.split(",").map { it.toInt() })
                linesRead++
            }
            currentLine++
        }
        words.sort()
        return words

    }

    fun unEvenFiles(): String {
        val lastFileName = fileNames[fileNames.size - 1]
        val fileName = "run0Chunk${fileNames.size + 1}"
        val totalLines = getTotalLines(readTmpFile(lastFileName))
        for (i in 0..(totalLines-1)) {
            appendWordChunks(mutableListOf(0, 0), fileName)
        }
        return fileName
    }

    fun run(fileName: String) {
        val file = readFile(fileName)
        totalLines = getTotalLines(file)
        var chunkEnd = 1
        for (i in 0..totalLines step maxLines) {
            val words = readWords(file, i)
            if (words.isNotEmpty()) {
                val fileName = "run0Chunk$chunkEnd"
                appendWordChunks(words, fileName)
                fileNames.add(fileName)
                chunkEnd++
            }
        }
        if (fileNames.size % 2 != 0) {
            // Uneven files will create tmp file
            val fileName = unEvenFiles()
            fileNames.add(fileName)
        }
    }
}