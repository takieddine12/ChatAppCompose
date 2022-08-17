package com.fly.me.realtimechatappcompose.routing

sealed class ChatRouting(var route : String) {
    object Splash : ChatRouting("chat")
    object Login : ChatRouting("login")
    object Registration : ChatRouting("registration")
    object Home : ChatRouting("home")
}