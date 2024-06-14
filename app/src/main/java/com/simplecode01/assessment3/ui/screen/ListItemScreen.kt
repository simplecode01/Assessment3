package com.simplecode01.assessment3.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.simplecode01.assessment3.R
import com.simplecode01.assessment3.model.Items
import com.simplecode01.assessment3.model.User
import com.simplecode01.assessment3.navigation.Screen
import com.simplecode01.assessment3.network.ApiStatus
import com.simplecode01.assessment3.network.UserDataStore
import com.simplecode01.assessment3.network.WarehouseApi
import com.simplecode01.assessment3.ui.theme.Assessment3Theme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val KEY_ID_USER = "idUser"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListItemScreen(navController: NavHostController, id: String?){
    val viewModel: ListItemViewModel = viewModel()
    val dataStore = UserDataStore(LocalContext.current)
    val showList by dataStore.layoutFlow.collectAsState(true)
    val itemss : Items
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = {navController.popBackStack()}) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.kembali),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                title = {
                    Text(text = stringResource(id = R.string.ListItem))
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                actions = {
                    IconButton(onClick = {
                        CoroutineScope(Dispatchers.IO).launch {
                            dataStore.saveLayout(!showList)
                        }
                    }) {
                        Icon(
                            painter = painterResource(
                                if (showList) R.drawable.baseline_grid_view_24
                                else R.drawable.baseline_list_24
                            ),
                            contentDescription = stringResource(
                                if (showList) R.string.grid
                                else R.string.list
                            ),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.Add.route)
                },
                content = {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.baseline_add_24),
                        contentDescription = "Add"
                    )
                }
            )
        }
    ){ padding ->
        ScreenContentListItem(showList, viewModel ,Modifier.padding(padding), id!!)
    }
}

@Composable
fun ListItem(item: Items, onClick: () -> Unit){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onClick() }
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(WarehouseApi.getItemsUrl(item.imageId))
                .crossfade(true)
                .build(),
            contentDescription = stringResource(R.string.imagedesc),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.loading_img),
            error = painterResource(id = R.drawable.baseline_broken_image_24),
            modifier = Modifier
                .size(80.dp)
                .padding(end = 16.dp)
        )
        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            Text(
                text = item.nama,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                maxLines = 1
            )
            Text(
                text = item.detail,
                fontSize = 14.sp,
                color = Color.Black,
                maxLines = 2
            )
        }
    }
}

@Composable
fun GridItem(item: Items, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(4.dp)
            .border(1.dp, Color.Black)
            .clickable { onClick() },
        contentAlignment = Alignment.BottomCenter
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(WarehouseApi.getItemsUrl(item.imageId))
                .crossfade(true)
                .build(),
            contentDescription = stringResource(R.string.imagedesc),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.loading_img),
            error = painterResource(id = R.drawable.baseline_broken_image_24),
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .background(Color(red = 0f, green = 0f, blue = 0f, alpha = 0.5f))
                .padding(4.dp)
        ) {
            Text(
                text = item.nama,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = item.waktu,
                fontStyle = FontStyle.Italic,
                fontSize = 14.sp,
                color = Color.White
            )
        }
    }
}

@Composable
fun DetailDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirmation: () -> Unit,
    image: String,
    title: String,
    details: String,
    quantity: String,
    date: String,
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(text = title)
            },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(WarehouseApi.getItemsUrl(image))
                            .crossfade(true)
                            .build(),
                        contentDescription = stringResource(R.string.imagedesc),
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(id = R.drawable.loading_img),
                        error = painterResource(id = R.drawable.baseline_broken_image_24),
                        modifier = Modifier
                            .size(100.dp)
                            .padding(8.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = details)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = quantity)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = date)
                }
            },
            confirmButton = {
                TextButton(onClick = onConfirmation) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun ScreenContentListItem(showList: Boolean, viewModel: ListItemViewModel, modifier: Modifier, userId : String) {
    val context = LocalContext.current
    val data by viewModel.data
    val status by viewModel.status.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    val selectedItem = remember { mutableStateOf<Items?>(null) }

    LaunchedEffect(userId) {
        viewModel.retrieveData(userId)
    }

    when (status) {
        ApiStatus.LOADING -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        ApiStatus.SUCCESS -> {

            if(data.isEmpty()){
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = stringResource(id = R.string.no_data))
                }
            }else{
                if (showList) {
                    LazyColumn(
                        modifier = modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 84.dp)
                    ) {
                        items(data) {items ->
                            ListItem(item = items) {
                                showDialog = true
                                selectedItem.value = items
                            }
                            Divider()
                        }
                    }
                }else{
                    LazyVerticalGrid(
                        modifier = modifier
                            .fillMaxSize()
                            .padding(4.dp),
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(bottom = 80.dp)
                    ) {
                        items(data) { items ->
                            GridItem(item = items){
                                showDialog = true
                                selectedItem.value = items
                            }
                        }
                    }
                }
                // Display dialog
                selectedItem.value?.let { item ->
                    DetailDialog(
                        showDialog = showDialog,
                        onDismiss = { showDialog = false },
                        onConfirmation = {
                            //delete here
                            viewModel.deleteData(userId, item.id)
                            showDialog = false
                        },
                        image = item.imageId,
                        title = item.nama,
                        details = item.detail,
                        quantity = "Quantity : ${item.quantity}",
                        date = "Waktu : ${item.waktu}"
                    )
                }
            }
        }
        ApiStatus.FAILED -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = stringResource(id = R.string.error))
                Button(
                    onClick = { viewModel.retrieveData(userId) },
                    modifier = Modifier.padding(top = 16.dp),
                    contentPadding = PaddingValues(horizontal=32.dp, vertical=16.dp)
                ) {
                    Text(text = stringResource(id = R.string.try_again))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun ListItemScreenPreview() {
    Assessment3Theme {
        ListItemScreen(rememberNavController(), "")
    }
}