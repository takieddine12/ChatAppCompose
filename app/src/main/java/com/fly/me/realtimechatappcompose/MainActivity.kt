package com.fly.me.realtimechatappcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fly.me.realtimechatappcompose.routing.ChatRouting
import com.fly.me.realtimechatappcompose.screens.*
import com.fly.me.realtimechatappcompose.ui.theme.RealTimeChatAppComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            RealTimeChatAppComposeTheme {
                NavHost(navHostController = navController)
            }
        }
    }
}


@Composable
fun NavHost(navHostController: NavHostController){

    androidx.navigation.compose.NavHost(navController = navHostController, startDestination = ChatRouting.Splash.route) {
        composable(route = ChatRouting.Splash.route) {
            SplashScreen(navHostController = navHostController)
        }
        composable(route = ChatRouting.Login.route) {
            LoginScreen(navHostController = navHostController)
        }
        composable(route = ChatRouting.Registration.route) {
            RegistrationScreen(navHostController = navHostController)
        }
        composable(route = ChatRouting.Home.route){
            MainScreen(navHostController = navHostController)
        }

    }

}

