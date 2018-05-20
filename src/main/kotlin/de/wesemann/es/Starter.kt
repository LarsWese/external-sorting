package de.wesemann.es

import java.io.File

class Starter {
    var finished = false
    private fun readAllFiles(run: String): Array<String> {
        return if (run == "0") {
            File(Starter().javaClass.classLoader.getResource("pages").toURI()).list().sortedArray()
        } else {
            File(Starter().javaClass.classLoader.getResource("pages").toURI()).list { _, n -> n.startsWith("run$run") }.sortedArray()
        }
    }

    private fun readWordsFromFile(file: File, start: Int): List<String> {
        // TODO - Datei lesen
        val lines = file.reader()

        val words = mutableListOf<String>()
        if (start > 0) {
            for (i in 0..start) {
                lines.read()
            }
        }
        while (words.size < 2) {
            val number = lines.read()
            if (number == -1 || number == 32) {
                return words
            }
            val ch = number.toChar()
            if (ch in '0'..'9')
                words.add(String(charArrayOf(ch)))
        }
        return words
    }

    private fun sortData(data: List<String>): List<String> {
        // sort
        val sort = data.sorted()
        return sort
    }

    private fun writeData(data: String, run: String, pagenames: String) {
        // schreiben
        val fileWritePath = Starter().javaClass.classLoader.getResource("pages").toURI()
        val file = File("${fileWritePath.rawPath}/run$run$pagenames")
        println(file.absolutePath)
        if (file.exists()) {
            file.appendText(",$data")

        } else {
            file.writeText(data)
        }
    }

    fun step(step: String) {
        val allFiles = readAllFiles(step)
        if (allFiles.size == 1) {
            finished = true
            return
        }
        for (i in 0..(allFiles.size - 1) step 2) {

            val file1 = allFiles[i]

            val file2 = allFiles[i + 1]

            println("read $file1, $file2")
            for (j in 0..file1.length step 3) {
                // TODO - nach jedem step wieder in die neue run file schauen und gucken ob ich zahlen tauschen muss
                val words1 = readWordsFromFile(getFile(file1), j)
                val words2 = readWordsFromFile(getFile(file2), j)
                val wordsTogether = mutableListOf<String>().apply {
                    addAll(words1)
                    addAll(words2)
                }
                if (wordsTogether.isNotEmpty()) {
                    val sortedWords = sortData(wordsTogether)
                    println("sorted: $sortedWords")
                    writeData(sortedWords.joinToString(","), (step.toInt() + 1).toString(), file1 + file2)
                }
            }
        }
    }

    private fun getFile(file1: String) = File(Starter().javaClass.classLoader.getResource("pages/$file1").toURI())
}

fun main(args: Array<String>) {
    val start = Starter()
    start.step("0")

    var step = 1
    while (!start.finished) {

        start.step("$step")

        step += 1
        println("step $step")

    }


}

