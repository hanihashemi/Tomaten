package io.github.hanihashemi.tomaten

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.lifecycleScope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import io.github.hanihashemi.tomaten.theme.TomatenTheme
import io.github.hanihashemi.tomaten.ui.events.UiEvents
import io.github.hanihashemi.tomaten.ui.screens.main.MainScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModel()
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TomatenTheme {
                LaunchedEffect(Unit) {
                    listenToUiEvents()
                }

                MainScreen()
            }
        }
        auth = Firebase.auth
    }

    private suspend fun listenToUiEvents() {
        viewModel.uiEvents.collect { event ->
            when (event) {
                is UiEvents.Login -> {
                    val googleIdOption =
                        GetGoogleIdOption.Builder()
                            .setServerClientId(getString(R.string.default_web_client_id))
                            .setFilterByAuthorizedAccounts(false)
                            .build()

                    val request =
                        GetCredentialRequest.Builder()
                            .addCredentialOption(googleIdOption)
                            .build()

                    val credentialManager = CredentialManager.create(this@MainActivity)
                    lifecycleScope.launch(Dispatchers.IO) {
                        try {
                            val result =
                                credentialManager.getCredential(
                                    request = request,
                                    context = this@MainActivity,
                                )
                            handleSignIn(result.credential)
                        } catch (e: GetCredentialException) {
                            if (e.message?.contains("16") == true || e.message?.contains("28433") == true) {
                                viewModel.actions.login.setErrorMessage(
                                    "No Google account found. Please sign in to your device and try again.",
                                )
                            } else {
                                viewModel.actions.login.setErrorMessage("Sign-in failed: ${e.message}")
                            }
                        }
                    }
                }
            }
        }
    }

    private fun handleSignIn(credential: Credential) {
        if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
            val firebaseAuthCredential =
                GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)

            // Sign in to Firebase with using the token
            auth.signInWithCredential(firebaseAuthCredential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser

                        if (user != null) {
                            viewModel.actions.login.setUser(
                                User(
                                    name = user.displayName,
                                    email = user.email,
                                    photoUrl = user.photoUrl.toString(),
                                    uid = user.uid,
                                ),
                            )
                        } else {
                            viewModel.actions.login.setErrorMessage("Sign in failed.")
                        }
                    } else {
                        viewModel.actions.login.setErrorMessage("Sign in failed.")
                    }
                }
        } else {
            viewModel.actions.login.setErrorMessage(errorMessage = "Credential is not of type Google ID!")
        }
    }
}
