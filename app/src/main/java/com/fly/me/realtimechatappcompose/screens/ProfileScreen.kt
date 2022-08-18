package com.fly.me.realtimechatappcompose.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.fly.me.realtimechatappcompose.R
import com.fly.me.realtimechatappcompose.routing.ChatRouting
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

@Composable
fun ProfileScreen() {
    val firebase = FirebaseAuth.getInstance()
    val firebaseDatabase = FirebaseDatabase.getInstance()
    val userImage = remember { mutableStateOf("") }
    val userName = remember { mutableStateOf("") }
    val userEmail = remember { mutableStateOf("") }
    val isNameEnabled = remember { mutableStateOf(false) }
    val buttonName = remember { mutableStateOf("Edit") }
    val shouldShowProgress = remember { mutableStateOf(false) }
    val context = LocalContext.current

    firebaseDatabase
        .reference
        .child("Chatters")
        .child(firebase.currentUser!!.uid)
        .addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    userEmail.value  = snapshot.child("email").value.toString()
                    userImage.value = snapshot.child("image").value.toString()
                    userName.value = snapshot.child("fullName").value.toString()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 50.dp), contentAlignment = Alignment.Center){
            AsyncImage(
                model = ImageRequest
                    .Builder(context)
                    .data(userImage.value)
                    .placeholder(R.drawable.user_icon)
                    .build(),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(140.dp)
                    .clip(CircleShape),
                contentDescription = "")
        }

        OutlinedTextField(
            enabled = isNameEnabled.value,
            value = userName.value,
            textStyle = TextStyle(color = Color.White, fontFamily = FontFamily(Font(R.font.roboto_medium))),
            modifier = Modifier
                .padding(top = 40.dp, start = 15.dp, end = 15.dp)
                .fillMaxWidth(),
            trailingIcon = {
                           Icon(painterResource(id = R.drawable.ic_baseline_person_24),
                               tint = Color.White,
                               contentDescription = "")
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                backgroundColor = Color.DarkGray,
                focusedBorderColor = Color.White
            ),
            onValueChange = {
                userName.value = it
            })

        OutlinedTextField(
            enabled = false,
            value = userEmail.value,
            textStyle = TextStyle(color = Color.White, fontFamily = FontFamily(Font(R.font.roboto_medium))),
            modifier = Modifier
                .padding(top = 20.dp, start = 15.dp, end = 15.dp)
                .fillMaxWidth(),
            trailingIcon = {
                     Icon(painterResource(id = R.drawable.ic_baseline_alternate_email_24),
                         tint = Color.White,
                         contentDescription = "")
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                backgroundColor = Color.DarkGray,
                focusedBorderColor = Color.White
            ),
            onValueChange = {
                userEmail.value = it
            })

        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
            Button(
                modifier = Modifier
                    .width(200.dp)
                    .padding(top = 40.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFFD36B00)
                ),
                onClick = {
                    if(buttonName.value == "Edit"){
                        buttonName.value = "Update"
                        isNameEnabled.value = true
                    }
                    else if (buttonName.value == "Update"){
                        shouldShowProgress.value = true
                        if(userEmail.value.isNotEmpty() and userName.value.isNotEmpty()){
                            val profileMap = hashMapOf<String,Any>()
                            profileMap["fullName"] = "John Cena"
                            firebaseDatabase
                                .reference
                                .child("Chatters")
                                .child(firebase.currentUser!!.uid)
                                .updateChildren(profileMap)
                                .addOnSuccessListener {
                                    firebase.currentUser!!.updateEmail("dallasson580@gmail.com")
                                    buttonName.value = "Edit"
                                    shouldShowProgress.value = false
                                    isNameEnabled.value = false
                                    Toast.makeText(context,"Profile Updated Successfully",Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener {
                                    shouldShowProgress.value = false
                                }

                        }
                        else {
                            Toast.makeText(context,"Email And Full Name cannot be empty",Toast.LENGTH_SHORT).show()
                        }
                    }
                }) {
                   Text(buttonName.value, color = Color.White, fontFamily = FontFamily(Font(R.font.roboto_bold)))
            }
        }

        Box(contentAlignment = Alignment.Center, modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)) {
            androidx.compose.animation.AnimatedVisibility(visible  = shouldShowProgress.value) {
                LinearProgressIndicator(color = Color.White)
            }
        }
    }

}