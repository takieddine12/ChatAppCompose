package com.fly.me.realtimechatappcompose.screens

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.fly.me.realtimechatappcompose.R
import com.fly.me.realtimechatappcompose.routing.ChatRouting
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

@Composable
fun RegistrationScreen(navHostController: NavHostController) {
    val firebaseAuth = FirebaseAuth.getInstance()
    val firebaseDatabase = FirebaseDatabase.getInstance()
    val storageReference = FirebaseStorage.getInstance()
    val shouldLoginIn = remember { mutableStateOf(false) }
    val shouldShowProgress = remember { mutableStateOf(false) }
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val fullName = remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val bitMap = remember { mutableStateOf<Bitmap?>(null) }
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()){
        imageUri = it
    }

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

            imageUri?.let {
                if(Build.VERSION.SDK_INT < 28){
                    bitMap.value = MediaStore.Images.Media.getBitmap(context.contentResolver,it)
                } else {
                    val imageSource = ImageDecoder.createSource(context.contentResolver,it)
                    bitMap.value = ImageDecoder.decodeBitmap(imageSource)
                }
            }
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
                if(bitMap.value == null){
                    Image(painter = painterResource(id = R.drawable.user_icon),
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .border(width = 1.dp, color = Color.White, shape = CircleShape)
                            .clickable {
                                launcher.launch("image/*")
                            },
                        contentDescription ="" )
                }
                else {
                    Image(
                        bitmap = bitMap.value!!.asImageBitmap(),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .border(width = 1.dp, color = Color.White, shape = CircleShape)
                            .clickable {
                                launcher.launch("image/*")
                            },
                        contentDescription = "")
                }
            }

            Text("Sign Up", fontSize = 35.sp, color = Color.White, fontFamily = FontFamily(Font(R.font.roboto_bold)) ,
                modifier = Modifier.padding(start = 20.dp,top = 20.dp))

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
                    password.value = it
                })

            Text(text = "FullName", fontSize = 14.sp, color = Color.White,modifier = Modifier.padding(start = 20.dp,
                top = 10.dp))


            OutlinedTextField(
                modifier = Modifier
                    .padding(start = 20.dp, top = 5.dp, end = 20.dp)
                    .fillMaxWidth(),
                value = fullName.value,
                textStyle  = TextStyle(color = Color.White, fontFamily = FontFamily(Font(R.font.roboto_medium))),
                shape = RoundedCornerShape(6.dp),
                trailingIcon = {
                    Icon(Icons.Filled.AccountCircle, tint = Color.White, contentDescription = "")
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    backgroundColor = Color.DarkGray,
                    focusedBorderColor = Color.White
                ),
                placeholder = { Text("FullName" , color = Color.White ,fontFamily = FontFamily(Font(R.font.roboto_medium)))},
                onValueChange = {
                    fullName.value = it
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
                    Text("Sign Up")
                }
            }
            Box(contentAlignment = Alignment.Center, modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp)
                .clickable {
                    navHostController.navigate(ChatRouting.Login.route)
                }) {
                Text(text = "Have an account ? Login", color = Color.White,
                    textAlign = TextAlign.Center, fontFamily = FontFamily(Font(R.font.roboto_bold)))
            }


            if(shouldLoginIn.value){
                if(bitMap.value != null){
                    if(email.value.isNotEmpty() and password.value.isNotEmpty() and fullName.value.isNotEmpty()){
                        SignUp(bitMap.value!!,email.value,password.value,fullName.value,firebaseAuth,
                        firebaseDatabase,storageReference,shouldLoginIn,shouldShowProgress,context,imageUri,navHostController)
                    } else {
                        Toast.makeText(context,"Email , Password and fullName cannot be empty",Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context,"Please add an image",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

@Composable
fun SignUp(
    value: Bitmap,
    email : String,
    password : String,
    fullName : String,
    firebaseAuth: FirebaseAuth,
    firebaseDatabase: FirebaseDatabase,
    storageReference: FirebaseStorage,
    shouldLoginIn: MutableState<Boolean>,
    shouldShowProgress: MutableState<Boolean>,
    context: Context,
    imageUri: Uri?,
    navHostController: NavHostController
) {

    storageReference
        .reference
        .child("Images")
        .putFile(imageUri!!)
        .addOnSuccessListener {
            it.storage.downloadUrl.addOnSuccessListener { imageUri ->
                if(imageUri != null){
                    firebaseAuth
                        .createUserWithEmailAndPassword(email,password)
                        .addOnSuccessListener {
                            if(it.user != null){
                                val map = hashMapOf<String,Any>()
                                map["email"]  = email
                                map["fullName"] = fullName
                                map["image"] = imageUri.toString()
                                map["uid"] = firebaseAuth.currentUser!!.uid
                                map["registrationDate"] = Calendar.getInstance().timeInMillis.toString()
                                firebaseDatabase
                                    .reference
                                    .child("Chatters")
                                    .child(firebaseAuth.currentUser?.uid!!)
                                    .setValue(map)
                                    .addOnSuccessListener {
                                        shouldLoginIn.value = false
                                        shouldShowProgress.value = false
                                        Toast.makeText(context,"Successfully Registered...",Toast.LENGTH_SHORT).show()
                                        navHostController.navigate(ChatRouting.Home.route)
                                    }
                                    .addOnFailureListener {

                                    }

                            }
                        }
                        .addOnFailureListener {
                            //Toast.makeText(context,"Error...",Toast.LENGTH_SHORT).show()
                        }
                }
            }.addOnFailureListener {
               // Toast.makeText(context,"Error...",Toast.LENGTH_SHORT).show()
            }
        }
        .addOnFailureListener {
            //Toast.makeText(context,"Error..",Toast.LENGTH_SHORT).show()
        }
}

