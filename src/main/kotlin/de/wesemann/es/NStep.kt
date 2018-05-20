package de.wesemann.es

import java.io.File

class NStep {
    val fileNames = mutableListOf<String>()

    // Step till I have only one file

    private fun readSortAndWrite(file1: File, file2: File, newFileName: String) {

        val maxLinesFile1 = getLines(file1)
        val maxLinesFile2 = getLines(file2)
        val words = mutableListOf<Int>()

        // step zero, no tmp files needed
        words.addAll(readLineAndReturnWords(file1, 0))
        words.addAll(readLineAndReturnWords(file2, 0))
        sortAndWrite(words, newFileName)

        var currentLine = 1
        while (true) {

            // write the last two words in a tmp file and clear the words
            if (words.isNotEmpty() && words.size == 2) {
                writeWordChunks(words, "oldtmp")
                words.clear()
            }

            // add the one line of the current files to the words
            if (maxLinesFile1 > currentLine)
                words.addAll(readLineAndReturnWords(file1, currentLine))
            if (maxLinesFile2 > currentLine)
                words.addAll(readLineAndReturnWords(file2, currentLine))
            println("\tcurrent words $words")


            // one file does not contain data anymore -> this are the last dataparts
            if (words.size == 2 || words.isEmpty()) {
                val oldwords = readLineAndReturnWords(readFile("oldtmp"), 0)
                words.addAll(oldwords)
                println("\tlast write $words")
                sortAndWriteAllWords(words, newFileName)
                break
            }

            words.sort()
            writeWordChunks(words.subList(2, 4), "newtmp")
            words.removeAt(2)
            words.removeAt(2)
            val oldwords = readLineAndReturnWords(readFile("oldtmp"), 0)
            words.addAll(oldwords)
            words.sort()

            sortAndWrite(words, newFileName)
            val newWords = readLineAndReturnWords(readFile("newtmp"), 0)
            words.addAll(newWords)

            words.sort()
            sortAndWrite(words, newFileName)

            currentLine++

        }
    }

    fun run(currentFileNames: List<String>, runNumber: Int) {
        if (currentFileNames.size == 1) {
            println("finished. Final file $currentFileNames")
        } else {
            var step = 1
            for (i in 0..currentFileNames.size step 2) {
                val newFileName = "run${runNumber}Chunk$step"
                if (i + 1 < currentFileNames.size) {
                    val file1 = readFile(currentFileNames[i])
                    val file2 = readFile(currentFileNames[i + 1])

                    readSortAndWrite(file1, file2, newFileName)

                } else if (i < currentFileNames.size) {
                    val file1 = readFile(currentFileNames[i])
                    var currentLine = 0
                    file1.reader().forEachLine {
                        sortAndWrite(it.split(",").map { it.toInt() }.toMutableList(), newFileName)
                        currentLine++
                    }
                }
                fileNames.add(newFileName)
                step++
            }
        }
    }

    fun cleanup() {
        // TODO Clean all old files
    }


    private fun sortAndWrite(words: MutableList<Int>, newFileName: String) {
        words.sort()
        println("\tCurrent words $words for file $newFileName")
        val wordsToWrite = words.subList(0, 2)
        appendWordChunks(wordsToWrite, newFileName)
//        println("words sublist $wordsToWrite")
        words.removeAt(0)
        words.removeAt(0)
        println("\twords left $words")

    }

    private fun sortAndWriteAllWords(words: MutableList<Int>, newFileName: String) {
        words.sort()
        println("\tCurrent words $words for file $newFileName")
        appendWordChunks(words, newFileName)
    }
}

fun main(args: Array<String>) {

    val start = FirstStep()
    start.run("page2")

    val fileNames = start.fileNames
    val step = NStep()
    val maxRuns = (fileNames.size / 2)
    for (i in 1..maxRuns) {

        step.run(fileNames, i)
        fileNames.clear()
        fileNames.addAll(step.fileNames)
    }

}

fun readFile(fileName: String) = File("${System.getProperty("java.io.tmpdir")}/$fileName")