package com.example.upcar

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

object Rotas {
    const val MOTORISTAS = "motoristas"
    const val CHAT = "chat"
    const val CONFIRMADA = "confirmada"

    const val ARG_CARONA_ID = "caronaId"

    const val CHAT_ROTA = "$CHAT/{$ARG_CARONA_ID}"
    const val CONFIRMADA_ROTA = "$CONFIRMADA/{$ARG_CARONA_ID}"

    fun chat(caronaId: Int) = "$CHAT/$caronaId"
    fun confirmada(caronaId: Int) = "$CONFIRMADA/$caronaId"
}

@Composable
fun AppNavigation(state: CaronaViewModel = viewModel()) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Rotas.MOTORISTAS
    ) {
        composable(Rotas.MOTORISTAS) {
            MotoristasScreen(
                state = state,
                onSolicitar = { motorista ->
                    val carona = state.solicitarCarona(motorista)
                    navController.navigate(Rotas.chat(carona.id)) { launchSingleTop = true }
                }
            )
        }

        composable(
            route = Rotas.CHAT_ROTA,
            arguments = listOf(navArgument(Rotas.ARG_CARONA_ID) { type = NavType.IntType })
        ) { backStackEntry ->
            val caronaId = backStackEntry.arguments?.getInt(Rotas.ARG_CARONA_ID) ?: -1
            Chat(
                state = state,
                caronaId = caronaId,
                onVoltar = { navController.popBackStack() },
                onConfirmar = {
                    navController.navigate(Rotas.confirmada(caronaId)) { launchSingleTop = true }
                }
            )
        }

        composable(
            route = Rotas.CONFIRMADA_ROTA,
            arguments = listOf(navArgument(Rotas.ARG_CARONA_ID) { type = NavType.IntType })
        ) { backStackEntry ->
            val caronaId = backStackEntry.arguments?.getInt(Rotas.ARG_CARONA_ID) ?: -1
            CaronaConfirmada(
                state = state,
                caronaId = caronaId,
                onVoltarInicio = { navController.popBackStack(Rotas.MOTORISTAS, false) }
            )
        }
    }
}
