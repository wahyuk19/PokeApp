package com.compose.pokeapp.screens.login

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compose.pokeapp.model.MUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class LoginScreenViewModel : ViewModel() {
    // val loadingState = MutableStateFlow(LoadingState.IDLE)
    private val auth: FirebaseAuth = Firebase.auth

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading


    fun signInWithEmailAndPassword(
        context: Context,
        email: String,
        password: String,
        home: () -> Unit
    ) = viewModelScope.launch {
        try {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("FB", "signInWithEmailAndPassword: ${task.result.toString()}")
                        home()
                    } else {
                        try {
                            Log.d("FB", "signInWithEmailAndPassword: ${task.result.toString()}")
                            Toast.makeText(context, task.result.toString(), Toast.LENGTH_SHORT)
                                .show()
                        } catch (e: Exception) {
                            Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                }

        } catch (ex: Exception) {
            Log.d("FB", "signInWithEmailAndPassword: ${ex.message}")
            Toast.makeText(context, ex.message.toString(), Toast.LENGTH_SHORT).show()
        }


    }


    fun createUserWithEmailAndPassword(
        context: Context,
        email: String,
        password: String,
        home: () -> Unit
    ) {
        if (_loading.value == false) {
            _loading.value = true
            try {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            //me
                            val displayName = task.result?.user?.email?.split('@')?.get(0)
                            createUser(displayName)
                            home()
                        } else {
                            Log.d("FB", "createUserWithEmailAndPassword: ${task.result.toString()}")
                            Toast.makeText(context, task.result.toString(), Toast.LENGTH_SHORT)
                                .show()
                        }
                        _loading.value = false


                    }
            } catch (e: Exception) {
                Log.d("FB", "signInWithEmailAndPassword: ${e.message}")
                Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT).show()
            }

        }


    }

    private fun createUser(displayName: String?) {
        val userId = auth.currentUser?.uid

        val user = MUser(
            userId = userId.toString(),
            displayName = displayName.toString(),
            avatarUrl = "",
            quote = "Life is great",
            profession = "Android Developer",
            id = null
        ).toMap()

        FirebaseFirestore.getInstance().collection("users")
            .add(user)
    }


}