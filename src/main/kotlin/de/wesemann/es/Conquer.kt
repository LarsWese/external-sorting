package de.wesemann.es

import java.io.File
import java.io.InputStreamReader

data class WordDto(val word: String, val isLast: Boolean)

class Conquer(private val maxWords: Int = 2) {
    var fileNames = mutableListOf<String>()
    internal var newFileName = "tmp"
    // Step till I have only one file
    val words = mutableListOf<String>()

    private fun readChar(reader: InputStreamReader) = reader.read()

    private fun readWord(reader: InputStreamReader): String {

        val sb = StringBuilder()

        var currentCharInt = readChar(reader)
        var currentChar = currentCharInt.toChar()

        while (currentChar != ',' && currentChar != '\n' && currentCharInt != -1) {
            sb.append(currentChar)
            currentCharInt = readChar(reader)
            currentChar = currentCharInt.toChar()
        }
        return sb.toString()
    }

    private fun readAndSort(reader1: InputStreamReader, reader2: InputStreamReader?, round: Int): WordDto {

        var empty = 0
        var pos = round
        while (words.size < maxWords && empty < 2) {
            val currentWord = if (reader2 != null) {
                if (pos++ % 2 == 0) readWord(reader1) else readWord(reader2)
            } else {
                readWord(reader1)
            }
            if (currentWord.isNotEmpty()) {
                words.add(currentWord)
            } else {
                empty++
            }

        }
        words.sort()
        return WordDto(if (words.isNotEmpty()) words.removeAt(0) else "", words.isEmpty())

    }

    private fun filesRun(reader1: InputStreamReader, reader2: InputStreamReader?, newFile: File) {
        var round = 0
        var word = readAndSort(reader1, reader2, round++)
        while (word.word.isNotEmpty()) {
            println("write $word in $newFileName")
            appendWord(word.word, newFile, round % maxWords == 0, word.isLast)
            word = readAndSort(reader1, reader2, round++)
        }
        fileNames.add(newFileName)
    }

    fun run(currentFileNames: List<String>, runNumber: Long) {

        if (currentFileNames.size == 1) {
            println("finished. Final file $currentFileNames")
        } else {
            fileNames.clear()
            println("Current number of files $currentFileNames.size")
            var step = 1
            for (i in 0..currentFileNames.size step 2) {
                // Two files left
                newFileName = "run${runNumber}Chunk$step"
                val newfile = getTmpFile(newFileName)
                if (i + 1 < currentFileNames.size) {
                    val reader1 = getTmpFileReader(currentFileNames[i])
                    val reader2 = getTmpFileReader(currentFileNames[i + 1])
                    filesRun(reader1, reader2, newfile)
                } else if (i < currentFileNames.size) {
                    val reader1 = getTmpFileReader(currentFileNames[i])
                    filesRun(reader1, null, newfile)
                }
                step++
            }
        }

    }

    fun cleanup() {
        // TODO Clean all old files
        val tmpFiles = File(System.getProperty("java.io.tmpdir")).list({ _, n -> n.startsWith("run") })
        for (f in tmpFiles) {
            File("${System.getProperty("java.io.tmpdir")}/$f").delete()
        }
    }

}


