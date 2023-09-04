
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthViewModel(private val firebaseAuthManager: FirebaseAuthManager) : ViewModel() {
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