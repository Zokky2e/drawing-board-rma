import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.google.firebase.auth.FirebaseAuth

// Initialize Firebase Authentication

class AuthViewModel(private val firebaseAuthManager: FirebaseAuthManager) : ViewModel() {
    // Use a StateFlow to represent the authentication state
    private val _isUserSignedIn = MutableStateFlow(false)
    var auth = firebaseAuthManager.auth
    val isUserSignedIn = _isUserSignedIn.asStateFlow()

    init {
        checkUserSignIn()
    }
    fun checkUserSignIn() {
        val currentUser = auth.currentUser
        _isUserSignedIn.value = currentUser != null
    }

}