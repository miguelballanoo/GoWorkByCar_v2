package com.mballano.goworkbycar.ui

import java.io.Serializable

data class UsuarioModel(var direccion: String = "",
                        var edad: String = "",
                        var nombre: String = "",
                        var provider: String = "",
                        var email: String = "",
                        var telefono: String = "",
                        var viajesReservados: ArrayList<ViajeModel> = arrayListOf()): Serializable