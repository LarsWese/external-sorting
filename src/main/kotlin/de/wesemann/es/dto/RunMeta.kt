package de.wesemann.es.dto

import java.io.File

data class RunMeta(val totalFiles: Int, val fileInfo:List<FileInfo> )

data class FileInfo(val file: File, val maxLines: Int)