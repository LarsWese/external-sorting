package de.wesemann.es

import java.io.File

/**
 * returns the number of total lines
 */
fun getTotalLines(file: File): Int {
    val reader = file.reader()

    var currentLine = 0

    reader.forEachLine {
        currentLine++
    }
    return currentLine
}

/**
 * append the words on a file
 */
fun appendWordChunks(words: List<Int>, fileName: String) {
    val allWords = createFileText(words)
    File("${System.getProperty("java.io.tmpdir")}/$fileName").appendText(allWords)
}

/**
 * write the words into the file
 */
fun writeWordChunks(words: List<Int>, fileName: String) {
    val allWords = createFileText(words)
    File("${System.getProperty("java.io.tmpdir")}/$fileName").writeText(allWords)
}

private fun createFileText(words: List<Int>): String {
    var allWords = String()

    for (i in 0..(words.size - 1) step 2) {
        val word1 = words[i]

        allWords = if (words.size <= (i + 1)) {
            allWords.plus("$word1")
        } else {
            val word2 = words[i + 1]
            allWords.plus("$word1,$word2\n")
        }
    }
    return allWords
}

/**
 * reads the @line of the given file and return the words as a list of integer where the coma is the delimiter
 */
fun readLineAndReturnWords(file: File, line: Int): List<Int> {
    val lineRead = file.reader().readLines()[line].split(",").map { it.toInt() }
    println("READ ${file.name} line $line: $lineRead")
    return lineRead
}


fun readTmpFile(fileName: String) = File("${System.getProperty("java.io.tmpdir")}/$fileName")