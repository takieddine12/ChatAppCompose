package com.fly.me.realtimechatappcompose.screens

import android.app.Activity
import android.content.Context
import android.text.style.TabStopSpan
import android.view.inputmethod.InputMethodManager
import android.widget.TableLayout
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.getSystemService
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.fly.me.realtimechatappcompose.R
import com.fly.me.realtimechatappcompose.models.ChatModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch
import java.util.*


@Composable
fun MainScreen() {

    val firebaseDatabase = FirebaseDatabase.getInstance()
    val firebaseAuth = FirebaseAuth.getInstance()
    val userName = remember { mutableStateOf("") }
    val userImage = remember { mutableStateOf("") }
    val isHandlerCalled = remember { mutableStateOf(false) }
    val context = LocalContext.current
    // GET USER Info
    firebaseDatabase
        .reference
        .child("Chatters")
        .child(firebaseAuth.currentUser!!.uid)
        .addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                     userName.value = snapshot.child("fullName").value.toString()
                     userImage.value = snapshot.child("image").value.toString()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    Scaffold {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.login_background),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                contentDescription = ""
            )
        }
        Column {
            Text(
                text = "Welcome",
                color = Color.White,
                fontSize = 25.sp,
                modifier = Modifier.padding(
                    top = 40.dp,
                    start = 20.dp
                ),
                fontFamily = FontFamily(Font(R.font.roboto_bold))
            )
            Card(
                Modifier
                    .padding(top = 20.dp, start = 20.dp)
                    .clip(shape = RoundedCornerShape(8.dp)),
                backgroundColor = Color.Green
            ) {
                Text(
                    userName.value,
                    color = Color.Black,
                    fontSize = 17.sp,
                    modifier = Modifier.padding(5.dp),
                    fontFamily = FontFamily(Font(R.font.roboto_medium))
                )
            }

            TabLayout()
        }

    }
    BackHandler {
        // show exit dialog
        isHandlerCalled.value = true
    }
    if(isHandlerCalled.value){
        AlertDialog(
            onDismissRequest = {
                isHandlerCalled.value = false
            },
            confirmButton = {  TextButton(onClick = {
                isHandlerCalled.value = false
                (context as Activity).finish()
            }) {
                Text("Exit")
            }},
            dismissButton = { TextButton(onClick = {
                isHandlerCalled.value = false
            }) {
                Text("Cancel")
            }},
            title = { Text("Exit App!")},
            text = { Text("Do you want to exit app !")}
        )
    }

}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TabLayout(){
    val pagerState = rememberPagerState(pageCount = 2)
    TabS(pagerState)
    TabsContent(pagerState = pagerState)
}


@OptIn(ExperimentalPagerApi::class)
@Composable
fun TabS(pagerState: PagerState){

    val coroutineScope = rememberCoroutineScope()
    val list = listOf(
        "Chat",
        "Profile")

    TabRow(
        modifier  = Modifier.padding(top = 10.dp),
        selectedTabIndex = pagerState.currentPage,
        backgroundColor = Color.Transparent,
        contentColor = Color.White) {
        list.forEachIndexed { index, s ->
            Tab(
                text = { Text(text = list[index])},
                selected = pagerState.currentPage == index,
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                })
        }
    }

}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TabsContent(pagerState: PagerState) {
    HorizontalPager(state = pagerState) { page ->
        when (page) {
            0 -> ChatScreen()
            1 -> ProfileScreen()
        }
    }
}