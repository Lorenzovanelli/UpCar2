package com.example.upcar

const val DESTINO_PADRAO = "Campus Centro • Bloco B"


const val CAMPUS_LAT = -25.4284
const val CAMPUS_LNG = -49.2733

data class Motorista(
    val id: Int,
    val nome: String,
    val nota: Double,
    val horario: String,
    val preco: Double,
    val corridas: Int,
    val carro: String,
    val placa: String,
    val origem: String,
    val destino: String,
    val lat: Double,
    val lng: Double
)

data class Carona(
    val id: Int,
    val motoristaId: Int,
    val origem: String,
    val destino: String,
    val horario: String,
    val valor: Double,
    val confirmada: Boolean = false,
    val aguardandoResposta: Boolean = false
)

data class Mensagem(
    val id: Int,
    val caronaId: Int,
    val texto: String,
    val horario: String,
    val minha: Boolean
)
