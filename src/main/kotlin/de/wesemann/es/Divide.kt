package de.wesemann.es

import java.io.File
import java.io.InputStreamReader

class Divide(private val maxWords: Int = 2) {

    var fileNames = arrayListOf<String>()

    private fun readFile(fileName: String): InputStreamReader = File(Divide().javaClass.classLoader.getResource(fileName).toURI()).reader()

    internal fun readWords(reader: InputStreamReader): List<String> {
        val words = mutableListOf<String>()

        var character = reader.read()

        var sb = StringBuilder()
        while (character != -1) {
            val currentChar = character.toChar()
            if (currentChar == ',' || currentChar == '\n') {
                words.add(sb.toString())
                sb = StringBuilder()
                if (words.size >= maxWords) {
                    break
                }
            } else {
                sb.append(currentChar)
            }
            character = reader.read()
        }
        if (sb.isNotEmpty()) {
            words.add(sb.toString())
        }
        words.sort()
        return words
    }

    fun run(fileName: String) {
        val reader = readFile(fileName)
        var chunkEnd = 1
        while (true) {
            val words = readWords(reader)
            if (words.isNotEmpty()) {
                println("Divided $words")
                val devidedFileName = "run0Chunk$chunkEnd"
                writeWordChunks(words, devidedFileName)
                fileNames.add(devidedFileName)
                chunkEnd++

            } else {
                break
            }
        }
    }

    /**
     * write the words into the file
     */
    private fun writeWordChunks(words: List<String>, fileName: String) = getTmpFile(fileName).writeText(words.joinToString(","))
}