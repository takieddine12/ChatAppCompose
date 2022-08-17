package com.fly.me.realtimechatappcompose.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.fly.me.realtimechatappcompose.R
import com.fly.me.realtimechatappcompose.routing.ChatRouting
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

@Composable
fun LoginScreen(navHostController: NavHostController) {
    val firebaseAuth = FirebaseAuth.getInstance()
    val shouldLoginIn = remember { mutableStateOf(false) }
    val shouldShowProgress = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    Scaffold {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(painter = painterResource(id = R.drawable.login_background),
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                contentDescription = "")
        }
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            AnimatedVisibility(visible = shouldShowProgress.value) {
                CircularProgressIndicator(color = Color.White)
            }
        }
        Column(modifier = Modifier.fillMaxSize(),
              verticalArrangement = Arrangement.Center,
              horizontalAlignment = Alignment.Start) {

              Text("Login", fontSize = 35.sp, color = Color.White, fontFamily = FontFamily(Font(R.font.roboto_bold)) ,
                  modifier = Modifier.padding(start = 20.dp))

              Text(text = "Email", fontSize = 14.sp, color = Color.White, fontFamily = FontFamily(Font(R.font.roboto_medium)),
                  modifier = Modifier.padding(start = 20.dp, top = 30.dp))

              OutlinedTextField(
                  modifier = Modifier
                      .padding(start = 20.dp, top = 5.dp, end = 20.dp)
                      .fillMaxWidth(),
                  value = email.value,
                  textStyle  = TextStyle(color = Color.White, fontFamily = FontFamily(Font(R.font.roboto_medium))),
                  shape = RoundedCornerShape(6.dp),
                  trailingIcon = {
                      Icon(Icons.Filled.Email, tint = Color.White, contentDescription = "")
                  },
                  colors = TextFieldDefaults.outlinedTextFieldColors(
                      backgroundColor = Color.DarkGray,
                      focusedBorderColor = Color.White
                  ),
                  placeholder = { Text("Email" , color = Color.White ,fontFamily = FontFamily(Font(R.font.roboto_medium)))},
                  onValueChange = {
                  email.value = it
              })

              Text(text = "Password", fontSize = 14.sp, color = Color.White,modifier = Modifier.padding(start = 20.dp,
                  top = 10.dp))

              OutlinedTextField(
                  modifier = Modifier
                      .padding(start = 20.dp, top = 5.dp, end = 20.dp)
                      .fillMaxWidth(),
                  value = password.value,
                  textStyle  = TextStyle(color = Color.White, fontFamily = FontFamily(Font(R.font.roboto_medium))),
                  shape = RoundedCornerShape(6.dp),
                  trailingIcon = {
                      Icon(Icons.Filled.AccountCircle, tint = Color.White, contentDescription = "")
                  },
                  colors = TextFieldDefaults.outlinedTextFieldColors(
                      backgroundColor = Color.DarkGray,
                      focusedBorderColor = Color.White
                  ),
                  placeholder = { Text("Password" , color = Color.White ,fontFamily = FontFamily(Font(R.font.roboto_medium)))},
                  onValueChange = {
                      password.value  = it
                  })

              Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center){
                  Button(
                      modifier = Modifier
                          .width(250.dp)
                          .padding(top = 30.dp),
                      colors = ButtonDefaults.buttonColors(
                          backgroundColor = Color.White
                      ),
                      onClick = {
                          shouldLoginIn.value = true
                          shouldShowProgress.value = true
                      }) {
                      Text("Login")
                  }
              }
              Box(contentAlignment = Alignment.Center, modifier = Modifier
                  .fillMaxWidth()
                  .padding(top = 40.dp)
                  .clickable {
                      navHostController.navigate(ChatRouting.Registration.route)
                  }) {
                  Text(text = "Don't have an account ? Sign up", color = Color.White,
                      textAlign = TextAlign.Center, fontFamily = FontFamily(Font(R.font.roboto_bold)))
              }

              if(shouldLoginIn.value){
                  if(email.value.isNotEmpty() and password.value.isNotEmpty()){
                      beginLogin(context,email.value,password.value,firebaseAuth,shouldLoginIn,shouldShowProgress,
                      navHostController)
                  } else {
                      shouldShowProgress.value = false
                      shouldLoginIn.value = false
                      Toast.makeText(context,"Email and Password cannot be empty",Toast.LENGTH_SHORT).show()
                  }
              }
        }
    }
}

fun beginLogin(
    context : Context,
    value: String,
    value1: String,
    firebaseAuth: FirebaseAuth,
    shouldLoginIn: MutableState<Boolean>,
    shouldShowProgress: MutableState<Boolean>,
    navHostController: NavHostController
) {
    firebaseAuth
        .signInWithEmailAndPassword(value,value1)
        .addOnSuccessListener {
            if(it.user != null){
                shouldShowProgress.value = false
                shouldLoginIn.value = false
                Toast.makeText(context,"Successfully Logged In",Toast.LENGTH_SHORT).show()
                navHostController.navigate(ChatRouting.Home.route)
            }
        }
        .addOnFailureListener {
            shouldShowProgress.value = false
            shouldLoginIn.value = false
        }
}


