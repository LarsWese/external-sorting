package de.wesemann.es

import de.wesemann.es.dto.FileInfo
import de.wesemann.es.dto.RunMeta
import java.io.File

class NStep(private val maxWords: Int = 2, private val maxFileRead: Int = 2) {
    val fileNames = mutableListOf<String>()
    internal var newFileName = "tmp"
    // Step till I have only one file
    private fun readSortAndWrite(meta: RunMeta) {

        val words = mutableListOf<Int>()

        // step zero, no tmp files needed
        words.addAll(getCurrentWords(meta, 0))
        sortAndWriteFirstNWords(words)

        var currentLine = 1
        while (true) {
            // write the last two words in a tmp file and clear the words
            // so I can get the new line from both files
            if (words.size == maxWords) {
                writeWordChunks(words, "oldtmp")
                words.clear()
            }


            // add the one line of the current files to the words
            words.addAll(getCurrentWords(meta, currentLine))
            println("\tcurrent words $words")


            // one file does not contain data anymore -> this are the last dataparts
            if (words.size <= maxWords) {
                val oldwords = readLineAndReturnWords(readTmpFile("oldtmp"), 0)
                words.addAll(oldwords)
                println("\tlast write $words")
                sortAndWriteAllWords(words)
                break
            }

            // both files have data an can be merged
            words.sort()
            // write the last x words in a tmp file
            writeWordChunks(words.subList(maxWords, words.size), "newtmp")
            // remove the first x words
            removeLastNWords(words, maxWords)

            // read the old words, add them and sort and write
            addTmpAndWrite(words, "oldtmp")
            // get the last x words from the newly created tmp file
            addTmpAndWrite(words, "newtmp")
            currentLine++

        }
    }

    internal fun getCurrentWords(meta: RunMeta, currentLine: Int): List<Int> {
        val words = mutableListOf<Int>()
        for (fileInfo in meta.fileInfo) {
            if (fileInfo.maxLines > currentLine) {
                words.addAll(readLineAndReturnWords(fileInfo.file, currentLine))
            }
        }
        return words
    }

    private fun addTmpAndWrite(words: MutableList<Int>, fileName: String) {
        val oldWords = readLineAndReturnWords(readTmpFile(fileName), 0)
        words.addAll(oldWords)
        sortAndWriteFirstNWords(words)
    }


    internal fun sortAndWriteFirstNWords(words: MutableList<Int>) {
        words.sort()
        println("\tCurrent words $words for file $newFileName")
        val wordsToWrite = words.subList(0, maxWords)
        appendWordChunks(wordsToWrite, newFileName)
//        println("words sublist $wordsToWrite")
        removeFristNWords(words, maxWords)
        println("\twords left $words")

    }

    internal fun removeFristNWords(words: MutableList<Int>, maxWords: Int) {
        for (i in 0..(maxWords - 1)) {
            words.removeAt(0)
        }
    }

    internal fun removeLastNWords(words: MutableList<Int>, maxWords: Int) {
        for (i in maxWords..(words.size - 1)) {
            words.removeAt(maxWords)
        }
    }

    private fun sortAndWriteAllWords(words: MutableList<Int>) {
        words.sort()
        println("\tCurrent words $words for file $newFileName")
        appendWordChunks(words, newFileName)
    }


    fun run(currentFileNames: List<String>, runNumber: Int) {

        if (currentFileNames.size == 1) {
            println("finished. Final file $currentFileNames")
        } else {
            fileNames.clear()
            var step = 1
            for (i in 0..currentFileNames.size step 2) {
                newFileName = "run${runNumber}Chunk$step"
                // Two files left
                if (i + 1 < currentFileNames.size) {
                    val file1 = readTmpFile(currentFileNames[i])
                    val file2 = readTmpFile(currentFileNames[i + 1])
                    val fileInfos = mutableListOf<FileInfo>().apply {
                        add(FileInfo(file1, getTotalLines(file1)))
                        add(FileInfo(file2, getTotalLines(file2)))
                    }
                    val meta = RunMeta(2, fileInfos)
                    readSortAndWrite(meta)

                }
                fileNames.add(newFileName)
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

fun main(args: Array<String>) {

    val readMaxLinesAtOnce = 2
    val start = FirstStep(readMaxLinesAtOnce)
    start.run("page2")

    val fileNames = start.fileNames
    val step = NStep(2)
    val maxRuns = (fileNames.size / 2)
    for (i in 1..maxRuns) {

        step.run(fileNames, i)
        fileNames.clear()
        fileNames.addAll(step.fileNames)
    }
    println("$fileNames")
}
