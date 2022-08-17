package com.fly.me.realtimechatappcompose.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import com.fly.me.realtimechatappcompose.R
import com.fly.me.realtimechatappcompose.routing.ChatRouting
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay


@Composable
fun SplashScreen(navHostController: NavHostController) {
    val firebaseAuth = FirebaseAuth.getInstance()
    Scaffold {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(painter = painterResource(id = R.drawable.splash_background),
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                contentDescription = "")
        }
    }

    if(firebaseAuth.currentUser != null){
        LaunchedEffect(Unit){
            delay(5000)
            navHostController.navigate(ChatRouting.Home.route)
        }
    } else {
        LaunchedEffect(Unit){
            delay(5000)
            navHostController.navigate(ChatRouting.Login.route)
        }
    }

}