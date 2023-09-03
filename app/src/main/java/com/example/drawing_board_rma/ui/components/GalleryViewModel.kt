import androidx.lifecycle.ViewModel
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GalleryViewModel(private val storage: FirebaseStorage, authViewModel: AuthViewModel) : ViewModel() {
    private val _imageUrls = MutableStateFlow<List<String>>(emptyList())
    val imageUrls: StateFlow<List<String>> = _imageUrls.asStateFlow()

    init {
        if(authViewModel.auth.currentUser != null) {
            val uid = authViewModel.auth.currentUser!!.uid
            fetchImagesFromStorage(uid)
        }
    }

    fun fetchImagesFromStorage(uid: String) {
        val listRef = storage.reference.child(uid)
        listRef.listAll()
            .addOnCompleteListener { items ->
                var urls = mutableListOf<String>()
                items.result.items.forEach { item ->
                    item.downloadUrl.addOnCompleteListener{
                        var url = it.result
                        var urlString = url.toString()
                        urls.add(urlString)
                        _imageUrls.value = urls
                    }
                }
            }
            .addOnFailureListener { exception ->
                var error = exception
            }
    }
}
