package com.simplecode01.assessment3.ui.screen

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.simplecode01.assessment3.R
import com.simplecode01.assessment3.model.User
import com.simplecode01.assessment3.navigation.Screen
import com.simplecode01.assessment3.network.UserDataStore
import com.simplecode01.assessment3.ui.theme.Assessment3Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController){
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                )
            )
        }
    ){ padding ->
        ScreenContent(Modifier.padding(padding), navController)
    }
}

@Composable
fun DashboardItemCard(title: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(100.dp)
            .clickable(onClick = onClick)
            .background(color = Color.LightGray, shape = RoundedCornerShape(16.dp))
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}
@Composable
fun ScreenContent(modifier: Modifier, navController: NavHostController) {
    val context = LocalContext.current
    val dataStore = UserDataStore(context)
    val user by dataStore.userFlow.collectAsState(User())
    val userEmail = user.email
    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.size(300.dp),
            painter = painterResource(id = R.drawable.logoapp),
            contentDescription = stringResource(id = R.string.imagedesc)
        )
        Spacer(modifier = Modifier.height(16.dp)) // Adjusted spacing
        Row(
            modifier = Modifier
                .wrapContentSize()
                .padding(horizontal = 5.dp, vertical = 5.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            DashboardItemCard(stringResource(id = R.string.listtitle)){
                if(user.email.isEmpty()){
                    Toast.makeText(context,"Pastikan sudah melakukan login !", Toast.LENGTH_LONG).show()
                    navController.navigate(Screen.Login.route)
                }else{
                    navController.navigate(Screen.List.withID(userEmail))
                }}
        }
        Spacer(modifier = Modifier.height(16.dp)) // Adjusted spacing
        Row(
            modifier = Modifier
                .wrapContentSize()
                .padding(horizontal = 5.dp, vertical = 5.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            DashboardItemCard(stringResource(id = R.string.logintitle)){navController.navigate(Screen.Login.route)}
            Spacer(modifier = Modifier.width(16.dp))
            DashboardItemCard(stringResource(id = R.string.abouttitle)){navController.navigate(Screen.AboutApp.route)}
        }
    }
}




@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun ScreenPreview() {
    Assessment3Theme {
        MainScreen(rememberNavController())
    }
}