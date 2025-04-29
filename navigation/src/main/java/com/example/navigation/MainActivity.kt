package com.example.navigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.navigation.ui.theme.ComposeMasterTheme
import androidx.navigation.NavController
import androidx.navigation.NavDestination

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeMasterTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    navController.addOnDestinationChangedListener { controller, destination, arguments ->
                        println("Destination changed: ${destination.route}")
                        // You can perform actions here based on the current route
                        // For instance:
                        when(destination.route){
                            "home" -> println("You are in home screen")
                            "screen1" -> println("You are in screen 1")
                            "screen2" -> println("You are in screen 2")
                            "screen3" -> println("You are in screen 3")
                            "screen4" -> println("You are in screen 4")
                            "screen5" -> println("You are in screen 5")
                            "lab" -> println("You are in lab screen")
                        }
                    }
                    MainNavigation(navController)
                }
            }
        }
    }
}

@Composable
fun MainNavigation(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    LaunchedEffect(key1 = currentRoute) {
        // This block will execute whenever currentRoute changes
        println("Current route changed to: $currentRoute")
        // You can perform actions here based on the current route
        // For instance:
        when(currentRoute){
            "home" -> println("You are in home screen")
            "screen1" -> println("You are in screen 1")
            "screen2" -> println("You are in screen 2")
            "screen3" -> println("You are in screen 3")
            "screen4" -> println("You are in screen 4")
            "screen5" -> println("You are in screen 5")
            "lab" -> println("You are in lab screen")
        }
    }
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController) }
        composable("screen1") { Screen1(navController) }
        composable(
            route = "screen2/{data}",
            arguments = listOf(navArgument("data") { type = NavType.StringType })
        ) { backStackEntry ->
            Screen2(navController, data = backStackEntry.arguments?.getString("data"))
        }
        composable("screen3?optionalData={optionalData}",
            arguments = listOf(navArgument("optionalData"){
                type = NavType.StringType
                defaultValue = "Default Optional Data"
            })
        ) { backStackEntry ->
            Screen3(navController, backStackEntry.arguments?.getString("optionalData"))
        }
        composable("screen4") {
            Screen4(navController)
        }
        composable(
            route = "screen5?name={name}&age={age}",
            arguments = listOf(
                navArgument("name") {
                    type = NavType.StringType
                    defaultValue = "Guest"
                    nullable = true
                },
                navArgument("age") {
                    type = NavType.IntType
                    defaultValue = 0
                }
            )
        ) { backStackEntry ->
            Screen5(
                navController = navController,
                name = backStackEntry.arguments?.getString("name"),
                age = backStackEntry.arguments?.getInt("age") ?: 0
            )
        }
        composable("lab"){
            LabScreen(navController)
        }
    }
}

@Composable
fun HomeScreen(navController: NavHostController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Home Screen")
        Button(onClick = { navController.navigate("screen1") }) {
            Text("Go to Screen 1")
        }
        Button(onClick = { navController.navigate("screen2/HelloFromHome") }) {
            Text("Go to Screen 2 with Data")
        }
        Button(onClick = { navController.navigate("screen3?optionalData=PassedOptionalData")}) {
            Text("Go to Screen 3 with Optional Data")
        }
        Button(onClick = { navController.navigate("screen3")}) {
            Text("Go to Screen 3 with default Data")
        }
        Button(onClick = { navController.navigate("screen4")}) {
            Text("Go to Screen 4")
        }
        Button(onClick = { navController.navigate("screen5?name=JohnDoe&age=30") }) {
            Text("Go to Screen 5 with name and age")
        }
        Button(onClick = { navController.navigate("lab") }) {
            Text("Go to Lab Screen")
        }
    }
}

@Composable
fun Screen1(navController: NavHostController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Screen 1")
        Button(onClick = { navController.popBackStack() }) {
            Text("Go Back")
        }
    }
}

@Composable
fun Screen2(navController: NavHostController, data: String?) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Screen 2")
        Text(text = "Data received: $data")
        Button(onClick = { navController.popBackStack() }) {
            Text("Go Back")
        }
    }
}
@Composable
fun Screen3(navController: NavHostController, data: String?) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Screen 3")
        Text(text = "Optional Data received: $data")
        Button(onClick = { navController.popBackStack() }) {
            Text("Go Back")
        }
    }
}

@Composable
fun Screen4(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Screen 4")
        Button(onClick = { navController.navigate("home") {
            popUpTo("home") {
                inclusive = true
            }
        }}) {
            Text("Go Back to Home (Clearing Backstack)")
        }
    }
}

@Composable
fun Screen5(navController: NavHostController, name: String?, age: Int) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Screen 5")
        Text(text = "Name received: $name")
        Text(text = "Age received: $age")
        Button(onClick = { navController.popBackStack() }) {
            Text("Go Back")
        }
    }
}

@Composable
fun LabScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.Green),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Lab Screen")
        Text(text = "Navigate through the other screens")
        // Test: Add a Button



        Button(onClick = { navController.navigate("home") }) {
            Text("Move Back to Home")
        }
    }
}