package com.linguaflow.app.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.linguaflow.app.navigation.Destinations

private data class NavItem(val route: String, val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector)

private val navItems = listOf(
    NavItem(Destinations.Home.route, "Home", Icons.Filled.Home),
    NavItem(Destinations.Lessons.route, "Lessons", Icons.Filled.MenuBook),
    NavItem(Destinations.Practice.route, "Practice", Icons.Filled.GpsFixed),
    NavItem(Destinations.Progress.route, "Progress", Icons.Filled.BarChart),
    NavItem(Destinations.Profile.route, "Profile", Icons.Filled.Person)
)

@Composable
fun LinguaFlowBottomNavBar(navController: NavHostController, currentRoute: String?) {
    NavigationBar {
        navItems.forEach { item ->
            val selected = currentRoute == item.route
            val scale by animateFloatAsState(if (selected) 1.15f else 1f, animationSpec = tween(200), label = "navIconScale")

            NavigationBarItem(
                selected = selected,
                onClick = {
                    if (!selected) {
                        navController.navigate(item.route) {
                            popUpTo(Destinations.Home.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        modifier = Modifier.size(24.dp).scale(scale)
                    )
                },
                label = { Text(item.label) }
            )
        }
    }
}
