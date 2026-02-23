package com.pedro.test_1.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.pedro.test_1.data.repo.FakeRepository
import com.pedro.test_1.data.source.DataStoreSource
import com.pedro.test_1.domain.usecase.FetchListUseCase
import com.pedro.test_1.domain.usecase.AuthUseCase
import com.pedro.test_1.domain.usecase.CheckSessionUseCase
import com.pedro.test_1.domain.usecase.SaveSessionUseCase
import com.pedro.test_1.domain.usecase.LogoutUseCase
import com.pedro.test_1.ui.screen.auth.AuthScreen
import com.pedro.test_1.ui.screen.auth.AuthViewModel
import com.pedro.test_1.ui.screen.detail.DetailScreen
import com.pedro.test_1.ui.screen.detail.DetailViewModel
import com.pedro.test_1.ui.screen.main.MainScreen
import com.pedro.test_1.ui.screen.main.MainViewModel
import com.pedro.test_1.ui.screen.splash.SplashScreen
import com.pedro.test_1.ui.screen.splash.SplashViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier
) {
    val context = LocalContext.current
    val localDataSource = DataStoreSource(context)
    val repository = FakeRepository(localDataSource)
    val fetchListUseCase = FetchListUseCase(repository)
    val authUseCase = AuthUseCase(repository)
    val checkSessionUseCase = CheckSessionUseCase(repository)
    val saveSessionUseCase = SaveSessionUseCase(repository)
    val logoutUseCase = LogoutUseCase(repository)

    NavHost(navController = navController, startDestination = Splash, modifier = modifier) {
        composable<Splash> {
            val viewModel = viewModel { SplashViewModel(checkSessionUseCase) }
            val state = viewModel.state.collectAsState().value

            LaunchedEffect(Unit) {
                viewModel.actions.collectLatest { action ->
                    when (action) {
                        is com.pedro.test_1.ui.screen.splash.SplashUiAction.ToAuth ->
                            navController.navigate(Auth) { popUpTo(Splash) { inclusive = true } }

                        is com.pedro.test_1.ui.screen.splash.SplashUiAction.ToMain ->
                            navController.navigate(Main) { popUpTo(Splash) { inclusive = true } }
                    }
                }
            }

            LaunchedEffect(Unit) {
                viewModel.onIntent(com.pedro.test_1.ui.screen.splash.SplashIntent.CheckSession)
            }

            SplashScreen(state = state, onIntent = viewModel::onIntent)
        }

        composable<Auth> {
            val viewModel = viewModel { AuthViewModel(authUseCase, saveSessionUseCase) }
            val state = viewModel.state.collectAsState().value

            LaunchedEffect(Unit) {
                viewModel.actions.collectLatest { action ->
                    when (action) {
                        is com.pedro.test_1.ui.screen.auth.AuthUiAction.ToMain ->
                            navController.navigate(Main) { popUpTo(Auth) { inclusive = true } }
                    }
                }
            }

            AuthScreen(state = state, onIntent = viewModel::onIntent)
        }

        composable<Main> {
            val viewModel = viewModel { MainViewModel(fetchListUseCase, logoutUseCase, repository) }
            val state = viewModel.state.collectAsState().value

            LaunchedEffect(Unit) {
                viewModel.actions.collectLatest { action ->
                    when (action) {
                        is com.pedro.test_1.ui.screen.main.MainUiAction.ToDetail ->
                            navController.navigate(Detail(itemId = action.itemId))
                        is com.pedro.test_1.ui.screen.main.MainUiAction.ToAuth ->
                            navController.navigate(Auth) { popUpTo(Main) { inclusive = true } }
                    }
                }
            }

            MainScreen(state = state, onIntent = viewModel::onIntent)
        }

        composable<Detail> { backStackEntry ->
            val detail: Detail? = backStackEntry.toRoute()
            val itemId = detail?.itemId ?: ""

            val viewModel = viewModel { DetailViewModel(repository, fetchListUseCase) }
            val state = viewModel.state.collectAsState().value

            LaunchedEffect(itemId) {
                if (itemId.isNotEmpty()) {
                    viewModel.onIntent(com.pedro.test_1.ui.screen.detail.DetailIntent.Load(itemId))
                }
            }

            DetailScreen(state = state, onIntent = viewModel::onIntent)
        }
    }
}
