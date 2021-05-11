package edu.itesm.marvelapp

data class miComic(val id: String, val titulo: String, val descripcion: String, val imagen: String){
    constructor():this("", "", "","")
}
