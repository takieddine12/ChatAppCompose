package com.fly.me.realtimechatappcompose.screens

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.fly.me.realtimechatappcompose.R
import com.fly.me.realtimechatappcompose.models.ChatModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*

@Composable
fun ChatScreen() {
    val firebaseDatabase = FirebaseDatabase.getInstance()
    val firebaseAuth = FirebaseAuth.getInstance()
    val userName = remember { mutableStateOf("") }
    val userImage = remember { mutableStateOf("") }
    val shouldSendMessage = remember { mutableStateOf(false) }
    val messagesList = remember { mutableStateListOf<ChatModel>() }
    val scrollState = rememberLazyListState()
    val context = LocalContext.current
    val messageField = remember { mutableStateOf("") }

    // GET USER Info
    firebaseDatabase
        .reference
        .child("Chatters")
        .child(firebaseAuth.currentUser!!.uid)
        .addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    userName.value = snapshot.child("fullName").value.toString()
                    userImage.value = snapshot.child("image").value.toString()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    // GET MESSAGES
    firebaseDatabase
        .reference
        .child("Messages")
        .addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                messagesList.clear()
                if(snapshot.exists()){
                    for(shot in snapshot.children){
                        val userN = shot.child("userName").value.toString()
                        val userMessage = shot.child("message").value.toString()
                        val  messageTime = shot.child("messageTime").value.toString().toLong()
                        val messageId = shot.child("messageId").value.toString().toInt()
                        val userI = shot.child("userImage").value.toString()

                        val chatModel = ChatModel(userN,userMessage,messageTime,messageId, userI)
                        messagesList.add(chatModel)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    LaunchedEffect(Unit){
        if(messagesList.size > 0){
            scrollState.animateScrollToItem(messagesList.size - 1)
        }
    }

    Column {
        LazyColumn(state  = scrollState , modifier = Modifier
            .weight(1f)
            .padding(top = 20.dp)){
            items(messagesList.size){ pos ->
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)) {
                    AsyncImage(
                        model = ImageRequest.Builder(context).data(messagesList[pos].userImage).build(),
                        modifier = Modifier
                            .size(70.dp)
                            .clip(CircleShape)
                            .border(width = 2.dp, color = Color.White, shape = CircleShape),
                        contentScale = ContentScale.Crop,
                        contentDescription = "")
                    Column(modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()) {
                        Text(text = messagesList[pos].userName!!, color = Color.White, modifier = Modifier.padding(start = 10.dp))
                        Text(text = messagesList[pos].message!!, color = Color.White, modifier = Modifier.padding(start = 10.dp,top = 5.dp))
                    }
                }
            }
        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 40.dp)) {
            IconButton(onClick = { }) {
                Icon(painterResource(id = R.drawable.ic_baseline_image_24), contentDescription = "", tint = Color.White)
            }
            TextField(
                modifier = Modifier.weight(1f),
                value = messageField.value,
                textStyle = TextStyle(color = Color.White),
                placeholder = { Text("message", color = Color.White, fontStyle = FontStyle.Italic) },
                onValueChange = {
                    messageField.value = it
                })
            IconButton(onClick = {
                // send message
                if(messageField.value.isNotEmpty()){
                    shouldSendMessage.value = true
                    val view = (context as Activity).currentFocus
                    if (view != null) {
                        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                        imm!!.hideSoftInputFromWindow(view.windowToken, 0)
                    }
                } else {
                    Toast.makeText(context,"Message cannot be empty", Toast.LENGTH_SHORT).show()
                }

            }) {
                Icon(painterResource(id = R.drawable.ic_baseline_send_24), contentDescription = "", tint = Color.White)
            }
        }


    }
    if(shouldSendMessage.value){
        val messageMap = hashMapOf<String,Any>()
        messageMap["userName"] = userName.value
        messageMap["message"] = messageField.value
        messageMap["messageTime"] = Calendar.getInstance().timeInMillis
        messageMap["messageId"] = Calendar.getInstance().timeInMillis.toInt()
        messageMap["userImage"] = userImage.value
        firebaseDatabase
            .reference
            .child("Messages")
            .child(Calendar.getInstance().timeInMillis.toString())
            .setValue(messageMap)
            .addOnSuccessListener {
                shouldSendMessage.value = false
                messageField.value = ""
            }
            .addOnFailureListener {

            }
    }
}