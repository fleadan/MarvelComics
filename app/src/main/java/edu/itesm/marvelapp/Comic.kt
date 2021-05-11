package edu.itesm.marvelapp

data class Results(
    var data: Data
)

data class Data(
    var results : MutableList<Comic>
)

data class Comic(
    val title: String,
    var description: String,
    val thumbnail: Thumbnail
)

data class Thumbnail(
    var path: String,
    var extension: String
)
