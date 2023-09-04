import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.drawing_board_rma.R
import com.example.drawing_board_rma.ui.theme.ButtonText
import com.google.firebase.storage.FirebaseStorage
import java.time.LocalDateTime


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GalleryScreen(
    authViewModel: AuthViewModel,
    storage: FirebaseStorage,
    galleryViewModel: GalleryViewModel
) {
    val imageUrls = galleryViewModel.imageUrls
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (authViewModel.auth.currentUser != null) {
            val uid = authViewModel.auth.currentUser!!.uid
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(imageUrls.value) { imageUrl ->

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                    ) {
                        Box {
                            AsyncImage(
                                model = imageUrl,
                                contentDescription = "description",
                                contentScale = ContentScale.Crop
                            )
                            IconButton(
                                onClick = {
                                    downloadImage(
                                        imageUrl,
                                        ImageLoader(context),
                                        onSuccess = { bitmap ->
                                            val savedFile =
                                                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                                                    ?.let {
                                                        val date =
                                                            LocalDateTime.now().toString() + ".png"
                                                        val savedUri = saveBitmapToMediaStore(
                                                            context.contentResolver,
                                                            bitmap,
                                                            date
                                                        )
                                                        if (savedUri != null) {
                                                        } else {
                                                            val toast = Toast(context)
                                                            toast.setText("Drawing saving failed!")
                                                            toast.show()
                                                        }
                                                    }
                                            if (savedFile != null) {
                                                val toast = Toast(context)
                                                toast.setText("Drawing saved to device!")
                                                toast.show()
                                            } else {
                                                val toast = Toast(context)
                                                toast.setText("Drawing saving failed!")
                                                toast.show()
                                            }
                                        },
                                        onError = { error ->
                                            val toast = Toast(context)
                                            toast.setText("Drawing saving failed!")
                                            toast.show()
                                        },
                                        context
                                    )
                                },
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(8.dp)
                                    .size(48.dp)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.save),
                                    contentDescription = "Save",
                                    tint = ButtonText
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

fun saveBitmapToMediaStore(
    contentResolver: ContentResolver,
    bitmap: Bitmap,
    displayName: String,
    mimeType: String = "image/jpeg"
): Uri? {
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, displayName)
        put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        }
    }

    val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
    } else {
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    }

    val uri = contentResolver.insert(collection, contentValues)

    try {
        uri?.let { outputStream ->
            contentResolver.openOutputStream(outputStream)?.use { stream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            }
        }
    } catch (e: Exception) {
        return null
    }

    return uri
}

fun downloadImage(
    url: String,
    imageLoader: ImageLoader,
    onSuccess: (Bitmap) -> Unit,
    onError: (Exception) -> Unit,
    context: Context
) {
    val request = ImageRequest.Builder(context)
        .data(url)
        .target { drawable: Drawable ->
            if (drawable is BitmapDrawable) {
                val bitmap = drawable.bitmap
                onSuccess(bitmap)
            } else {
                onError(Exception("Downloaded data is not a Bitmap"))
            }
        }
        .build()

    imageLoader.enqueue(request)
}