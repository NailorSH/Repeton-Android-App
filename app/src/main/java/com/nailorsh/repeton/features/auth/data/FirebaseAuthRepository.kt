package com.nailorsh.repeton.features.auth.data

import android.util.Log
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.nailorsh.repeton.MainActivity
import com.nailorsh.repeton.R
import com.nailorsh.repeton.features.auth.data.model.UserData
import com.nailorsh.repeton.features.auth.presentation.viewmodel.AuthUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class FirebaseAuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore,
    private val context: MainActivity
) : AuthRepository {
    private val TAG = AuthRepository::class.java.simpleName

    var verificationOtp: String = ""
    var resentToken: PhoneAuthProvider.ForceResendingToken? = null
    override var authState: MutableStateFlow<AuthUiState> =
        MutableStateFlow(AuthUiState.NotInitialized)
        private set

    private val authCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            Log.i(
                TAG,
                "onVerificationCompleted: Verification completed. ${context.getString(R.string.verification_complete)}"
            )
            authState.value =
                AuthUiState.Loading(message = context.getString(R.string.verification_complete))

            // Use obtained credential to sign in user
            signInWithAuthCredential(credential)
        }

        override fun onVerificationFailed(exception: FirebaseException) {
            when (exception) {
                is FirebaseAuthInvalidCredentialsException -> {
                    authState.value = AuthUiState.Error(
                        exception = Exception(context.getString(R.string.verification_failed_try_again))
                    )
                }

                is FirebaseTooManyRequestsException -> {
                    authState.value = AuthUiState.Error(
                        exception = Exception(context.getString(R.string.quota_exceeded))
                    )
                }

                else -> {
                    authState.value = AuthUiState.Error(exception)
                }
            }
        }

        override fun onCodeSent(
            code: String, token:
            PhoneAuthProvider.ForceResendingToken
        ) {
            super.onCodeSent(code, token)
            verificationOtp = code
            resentToken = token
            authState.value = AuthUiState.Loading(
                message = context.getString(R.string.code_sent)
            )
        }

    }

    private val authBuilder: PhoneAuthOptions.Builder = PhoneAuthOptions.newBuilder(auth)
        .setCallbacks(authCallbacks)
        .setActivity(context)
        .setTimeout(120L, TimeUnit.SECONDS)

    private fun signInWithAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.i(TAG, "signInWithAuthCredential:The sign in succeeded ")
                    authState.value = AuthUiState.Success(
                        message = context.getString(R.string.phone_auth_success)
                    )
                } else {
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        Log.e(TAG, context.getString(R.string.invalid_code))
                        authState.value = AuthUiState.Error(
                            exception = Exception(context.getString(R.string.invalid_code))
                        )

                        return@addOnCompleteListener
                    } else {
                        authState.value = AuthUiState.Error(task.exception)
                        Log.e(TAG, "signInWithAuthCredential: Error ${task.exception?.message}")

                    }
                }
            }
    }

    override fun getUserId(): String {
        return auth.currentUser?.uid.orEmpty()
    }

    override fun authenticate(phone: String) {
        authState.value =
            AuthUiState.Loading("${context.getString(R.string.code_will_be_sent)} $phone")

        val options = authBuilder
            .setPhoneNumber(phone)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
        authCallbacks.onCodeSent(verificationId, token)
    }

    override fun onVerifyOtp(code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationOtp, code)
        signInWithAuthCredential(credential)
    }

    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
        authCallbacks.onVerificationCompleted(credential)
    }

    override fun onVerificationFailed(exception: Exception) {
        authCallbacks.onVerificationFailed(exception as FirebaseException)
    }

    override fun getUserPhone(): String {
        return auth.currentUser?.phoneNumber.orEmpty()
    }

    override suspend fun createAnonymousAccount() {
        authState.value = AuthUiState.Loading(context.getString(R.string.creating_guest_mode))
        try {
            auth.signInAnonymously().await()
            authState.value = AuthUiState.Success(context.getString(R.string.guest_account_created))
        } catch (e: Exception) {
            authState.value = AuthUiState.Error(e)
        }
    }

    override fun checkUserExists(onComplete: (Boolean) -> Unit) {
        // Пытаемся получить документ с данным UID
        val uid = auth.currentUser?.uid
        if (uid != null) {
            val docRef = db.collection("users").document(uid)
            docRef.get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        // Пользователь с данным UID существует
                        onComplete(true)
                    } else {
                        // Пользователь с данным UID не существует
                        onComplete(false)
                    }
                }
                .addOnFailureListener { e ->
                    // Произошла ошибка при попытке получить документ
                    Log.d("FIRESTORE", "Error checking user existence: $e")
                    onComplete(false)
                }
        } else {
            onComplete(false)
        }
    }

    override suspend fun register(user: UserData) = withContext(Dispatchers.IO) {
        val currentUid = auth.currentUser?.uid
        if (currentUid != null) {
            val newUserRef = db.collection("users").document(currentUid)
            newUserRef.set(user)
        }
        // TODO добавить обработку ошибок
    }
}