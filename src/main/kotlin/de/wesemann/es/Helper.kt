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
fun appendWord(word: String, file: File, newLine: Boolean, lastWord: Boolean) {
    file.appendText(word)
    if (!lastWord) {
        file.appendText(if (newLine) "\n" else ",")
    }
}

fun getTmpFile(fileName: String) = File("${System.getProperty("java.io.tmpdir")}/$fileName")
fun getTmpFileReader(fileName: String) = getTmpFile(fileName).reader()

