package de.wesemann.es


fun main(args: Array<String>) {

    start(4, "page1", "sortedFile")
}

fun start(maxWords: Int = 4, fileToSort: String, finalFileName: String) {
    val divide = Divide(maxWords)
    divide.run(fileToSort)

    val fileNames = divide.fileNames

    if (fileNames.size == 1) {
        val file = getTmpFile(fileNames[0])
        val sortedFile = getTmpFile(finalFileName)
        file.renameTo(sortedFile)
    } else {
        val conquer = Conquer(maxWords)
        val maxRuns = Math.round((fileNames.size / 2.0))
        for (i in 1..maxRuns) {

            conquer.run(fileNames, i)
            fileNames.clear()
            fileNames.addAll(conquer.fileNames)
        }
        val file = getTmpFile(fileNames[0])
        val sortedFile = getTmpFile(finalFileName)
        file.renameTo(sortedFile)
        println("And the final file is here: ${sortedFile.absolutePath}")
        conquer.cleanup()
    }
}
