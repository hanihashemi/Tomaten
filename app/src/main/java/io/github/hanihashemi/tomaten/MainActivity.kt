package io.github.hanihashemi.tomaten

import android.Manifest
import android.app.ActivityManager
import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.core.content.ContextCompat
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import io.github.hanihashemi.tomaten.navigation.TomatenNavigation
import io.github.hanihashemi.tomaten.services.TimerService
import io.github.hanihashemi.tomaten.services.TimerService.Companion.REMAINING_TIME
import io.github.hanihashemi.tomaten.services.TimerService.Companion.TIME_PARAM
import io.github.hanihashemi.tomaten.services.TimerService.Companion.TIME_UPDATE_ACTION
import io.github.hanihashemi.tomaten.theme.TomatenTheme
import io.github.hanihashemi.tomaten.ui.events.UiEvents
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModel()
    private lateinit var auth: FirebaseAuth
    private lateinit var timerUpdateReceiver: BroadcastReceiver
    private var timeLimit: Long = 0

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission(),
        ) { isGranted: Boolean ->
            if (isGranted) {
                startTimerService(timeLimit)
            } else {
                showPermissionRationaleDialog()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        listenToTimerService()
        setContent {
            TomatenTheme {
                LaunchedEffect(Unit) {
                    listenToUiEvents()
                }

                TomatenNavigation(viewModel = viewModel)
            }
        }
        auth = Firebase.auth
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(timerUpdateReceiver)
    }

    private suspend fun listenToUiEvents() {
        viewModel.uiEvents.collect { event ->
            when (event) {
                is UiEvents.Login -> login()
                is UiEvents.StopTimer -> {
                    val intent = Intent(this, TimerService::class.java)
                    stopService(intent)
                }

                is UiEvents.StartTimer -> {
                    timeLimit = event.timeLimit
                    askNotificationPermissionAndStartTimerService()
                }
            }
        }
    }

    private fun listenToTimerService() {
        timerUpdateReceiver =
            object : BroadcastReceiver() {
                override fun onReceive(
                    context: Context?,
                    intent: Intent?,
                ) {
                    val remainingTime = intent?.getLongExtra(REMAINING_TIME, 0) ?: 0
                    viewModel.actions.timer.updateTimer(remainingTime)
                }
            }

        LocalBroadcastManager.getInstance(this).registerReceiver(
            timerUpdateReceiver,
            IntentFilter(TIME_UPDATE_ACTION),
        )
    }

    private fun login() {
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
            } catch (e: SecurityException) {
                if (e.message?.contains("Unknown calling package name") == true) {
                    viewModel.actions.login.setErrorMessage(
                        "Authentication service error. Please restart the app and try again.",
                    )
                } else {
                    viewModel.actions.login.setErrorMessage("Security error: ${e.message}")
                }
            } catch (e: Exception) {
                viewModel.actions.login.setErrorMessage("Login failed: ${e.message}")
            }
        }
    }

    private fun askNotificationPermissionAndStartTimerService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permission =
                ContextCompat.checkSelfPermission(
                    this@MainActivity,
                    Manifest.permission.POST_NOTIFICATIONS,
                )
            when {
                permission == PackageManager.PERMISSION_GRANTED -> startTimerService(timeLimit)
                else -> requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            startTimerService(timeLimit)
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
                        val errorMessage = task.exception?.message ?: "Sign in failed."
                        viewModel.actions.login.setErrorMessage("Firebase authentication failed: $errorMessage")
                    }
                }
                .addOnFailureListener { exception ->
                    viewModel.actions.login.setErrorMessage("Authentication error: ${exception.message}")
                }
        } else {
            viewModel.actions.login.setErrorMessage(errorMessage = "Credential is not of type Google ID!")
        }
    }

    private fun startTimerService(timeLimit: Long) {
        val intent =
            Intent(this, TimerService::class.java).apply {
                putExtra(TIME_PARAM, timeLimit)
            }
        startForegroundService(intent)
    }

    private fun showPermissionRationaleDialog() {
        val title = "Notification Permission Needed"
        val message =
            "To keep the timer running in the background, please grant notification permission."
        val positiveButton = "OK"

        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveButton) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun isTimerServiceRunning(): Boolean {
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in activityManager.getRunningServices(Int.MAX_VALUE)) {
            if (TimerService::class.java.name == service.service.className) {
                return true
            }
        }
        return false
    }
}
