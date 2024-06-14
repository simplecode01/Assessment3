package com.simplecode01.assessment3.ui.screen

import android.content.ContentResolver
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.simplecode01.assessment3.R
import com.simplecode01.assessment3.model.User
import com.simplecode01.assessment3.navigation.Screen
import com.simplecode01.assessment3.network.UserDataStore
import com.simplecode01.assessment3.ui.theme.Assessment3Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItemScreen(navController: NavHostController){
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
                    Text(text = stringResource(id = R.string.tambah_item))
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                )
            )
        }
    ){ padding ->
        ScreenContentAddItem(Modifier.padding(padding), navController)
    }
}
@Composable
fun ScreenContentAddItem(modifier: Modifier, navController: NavHostController) {
    val context = LocalContext.current
    val dataStore = UserDataStore(context)
    val user by dataStore.userFlow.collectAsState(User())
    val viewModel: AddItemViewModel = viewModel()
    val errorMessage by viewModel.errorMessage
    var bitmap: Bitmap? by remember { mutableStateOf(null) }
    val launcher = rememberLauncherForActivityResult(CropImageContract()) {
        bitmap = getCroppedImage(context.contentResolver, it)
    }
    val options = CropImageContractOptions(
        null, CropImageOptions(
            imageSourceIncludeGallery = false,
            imageSourceIncludeCamera = true,
            fixAspectRatio = true
        )
    )

    var nama by remember { mutableStateOf("") }
    var detail by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 70.dp, start = 5.dp, end = 5.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Input Photo
        if (bitmap == null) {
            Image(painter = painterResource(id = R.drawable.image_add),
                contentDescription = stringResource(id = R.string.imagedesc),
                modifier = Modifier
                    .size(200.dp)
                    .clickable { launcher.launch(options) })
        } else {
            Image(
                bitmap = bitmap!!.asImageBitmap(),
                contentDescription = stringResource(id = R.string.imagedesc),
                modifier = Modifier
                    .size(200.dp)
                    .clickable { launcher.launch(options) })
        }

        // Input Name
        OutlinedTextField(
            value = nama,
            onValueChange = { nama = it },
            label = { Text(text = stringResource(id = R.string.nama)) },
            maxLines = 1,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )

        // Input Details
        OutlinedTextField(
            value = detail,
            onValueChange = { detail = it },
            label = { Text(text = stringResource(id = R.string.detail)) },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Default
            ),
            modifier = Modifier
                .fillMaxWidth()
                .size(250.dp)
        )

        // Input Quantity
        OutlinedTextField(
            value = quantity,
            onValueChange = { quantity = it },
            label = { Text("Quantity") },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.fillMaxWidth()
        )

        //Button save and cancel
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SmoothLoginButton(onLoginClick = {navController.navigate(Screen.List.route) }, title = stringResource(R.string.batal) )
            Spacer(modifier = Modifier.height(16.dp))
            SmoothLoginButton(onLoginClick = {
                if (nama.isNotEmpty() && detail.isNotEmpty() && quantity.isNotEmpty() && bitmap != null) {
                    viewModel.saveData(user.email, nama, detail, quantity, bitmap!!)
                    navController.navigate(Screen.Home.route)
                } else {
                    Toast.makeText(context, "Pastikan semua data sudah terisi!", Toast.LENGTH_LONG).show()
                }
            }, title = stringResource(R.string.simpan))
            if (errorMessage != null) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                viewModel.clearMessage()
            }
        }
    }
}


private fun getCroppedImage(
    resolver: ContentResolver,
    result: CropImageView.CropResult
): Bitmap? {
    if (!result.isSuccessful) {
        Log.e("IMAGE", "Error: ${result.error}")
        return null
    }

    val uri = result.uriContent ?: return null

    return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
        MediaStore.Images.Media.getBitmap(resolver, uri)
    } else {
        val source = ImageDecoder.createSource(resolver, uri)
        ImageDecoder.decodeBitmap(source)
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun AddItemScreenPreview() {
    Assessment3Theme {
        AddItemScreen(rememberNavController())
    }
}