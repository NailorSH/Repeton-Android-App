package com.nailorsh.repeton.features.navigation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.nailorsh.repeton.core.navigation.sharedViewModel
import com.nailorsh.repeton.features.navigation.routes.Graph
import com.nailorsh.repeton.features.navigation.routes.TutorViewScreen
import com.nailorsh.repeton.features.tutorprofile.presentation.ui.TutorProfileScreen
import com.nailorsh.repeton.features.tutorprofile.presentation.viewmodel.TutorProfileViewModel

fun NavGraphBuilder.tutorViewNavGraph(
    navController: NavHostController
) {
    navigation(
        route = Graph.TUTOR_VIEW,
        startDestination = TutorViewScreen.TutorView.route
    ) {
        composable(route = TutorViewScreen.TutorView.route) { backStackEntry ->
            backStackEntry.arguments?.getString("id")?.let { id ->
                val viewModel = backStackEntry.sharedViewModel<TutorProfileViewModel>(navController)

                TutorProfileScreen(
                    tutorId = id.toInt(),
                    onBackClicked = { navController.popBackStack() },
                    viewModel = viewModel
                )
            }
        }
    }
}