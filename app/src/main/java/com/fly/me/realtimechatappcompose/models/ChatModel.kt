package com.fly.me.realtimechatappcompose.models

data class ChatModel(
    val userName : String? = null,
    val message : String? = null,
    val messageTime : Long? = null,
    val messageId : Int? = null,
    val userImage : String? = null
)