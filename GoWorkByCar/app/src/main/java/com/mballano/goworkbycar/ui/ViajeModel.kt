package com.mballano.goworkbycar.ui


import java.io.Serializable

data class ViajeModel(var id: String = "",
                      var origen: String = "",
                        var destino: String = "",
                        var plazas: String = "",
                        var marca: String = "",
                        var modelo: String = "",
                        var matricula: String = "",
                        var anfitrion: String = "",
                      var usuarioLogeado: UsuarioModel? = null,
                        var participantes: ArrayList<String> = arrayListOf()): Serializable
