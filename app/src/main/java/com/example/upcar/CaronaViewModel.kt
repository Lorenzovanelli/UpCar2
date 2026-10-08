package com.example.upcar

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private fun horaAgora(): String =
    SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())


class CaronaViewModel : ViewModel() {

    val motoristas = mutableStateListOf<Motorista>().apply { addAll(motoristasIniciais()) }
    val caronas = mutableStateListOf<Carona>()
    val mensagens = mutableStateListOf<Mensagem>()

    private var proximoMotoristaId = motoristas.maxOf { it.id } + 1
    private var proximaCaronaId = 1
    private var proximaMensagemId = 1

    fun motoristaPorId(id: Int): Motorista? = motoristas.firstOrNull { it.id == id }

    fun caronaPorId(id: Int): Carona? = caronas.firstOrNull { it.id == id }

    fun mensagensDaCarona(caronaId: Int): List<Mensagem> =
        mensagens.filter { it.caronaId == caronaId }


    fun adicionarMotorista(
        nome: String,
        carro: String,
        placa: String,
        horario: String,
        preco: Double,
        origem: String,
        lat: Double? = null,
        lng: Double? = null
    ) {
        val id = proximoMotoristaId++
        val latFinal = lat ?: (CAMPUS_LAT + ((id * 37) % 21 - 10) * 0.0007)
        val lngFinal = lng ?: (CAMPUS_LNG + ((id * 53) % 21 - 10) * 0.0007)
        motoristas.add(
            Motorista(id, nome, 5.0, horario, preco, 0, carro, placa, origem, DESTINO_PADRAO, latFinal, lngFinal)
        )
    }

    fun removerMotorista(motorista: Motorista) {
        val idsCaronas = caronas.filter { it.motoristaId == motorista.id }.map { it.id }
        mensagens.removeAll { it.caronaId in idsCaronas }
        caronas.removeAll { it.motoristaId == motorista.id }
        motoristas.remove(motorista)
    }

    fun solicitarCarona(motorista: Motorista): Carona {
        val pendente = caronas.firstOrNull { it.motoristaId == motorista.id && !it.confirmada }
        if (pendente != null) return pendente

        val carona = Carona(
            id = proximaCaronaId++,
            motoristaId = motorista.id,
            origem = motorista.origem,
            destino = motorista.destino,
            horario = motorista.horario,
            valor = motorista.preco
        )
        caronas.add(carona)
        enviarMensagem(
            carona.id,
            "Olá! Tudo bem? Vi que você pediu a carona para o ${motorista.destino}.",
            minha = false
        )
        return carona
    }

    fun enviarMensagem(caronaId: Int, texto: String, minha: Boolean = true) {
        mensagens.add(Mensagem(proximaMensagemId++, caronaId, texto, horaAgora(), minha))
    }

    fun removerMensagem(mensagem: Mensagem) {
        mensagens.remove(mensagem)
    }


    fun enviarContraproposta(caronaId: Int, valor: Double) {
        val i = caronas.indexOfFirst { it.id == caronaId }
        if (i < 0) return
        val carona = caronas[i]
        if (carona.confirmada || carona.aguardandoResposta) return

        caronas[i] = carona.copy(valor = valor, aguardandoResposta = true)
        enviarMensagem(caronaId, "Que tal ${formatarReais(valor)}?")

        viewModelScope.launch {
            delay(1500)
            val j = caronas.indexOfFirst { it.id == caronaId }
            if (j >= 0) {
                caronas[j] = caronas[j].copy(aguardandoResposta = false)
                enviarMensagem(caronaId, "Combinado! Pode ser ${formatarReais(valor)}.", minha = false)
            }
        }
    }

    fun confirmarCarona(caronaId: Int) {
        val i = caronas.indexOfFirst { it.id == caronaId }
        if (i < 0) return
        val carona = caronas[i]
        if (carona.confirmada || carona.aguardandoResposta) return

        caronas[i] = carona.copy(confirmada = true)
        enviarMensagem(caronaId, "Fechado! Já aceito o valor de ${formatarReais(carona.valor)}.")
    }
}
