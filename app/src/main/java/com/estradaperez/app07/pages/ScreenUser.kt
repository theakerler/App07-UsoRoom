package com.estradaperez.app07.pages

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.Room
import com.estradaperez.app07.UserDatabase
import com.estradaperez.app07.model.dao.UserDao
import com.estradaperez.app07.model.entity.User
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenUser() {
    val context = LocalContext.current
    val db = crearDatabase(context)
    val dao = db.userDao()

    var id by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var dataUser = remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gestión de Usuarios") },
                actions = {
                    IconButton(onClick = {
                        val user = User(0, firstName, lastName)
                        coroutineScope.launch {
                            AgregarUsuario(user = user, dao = dao)
                        }
                        firstName = ""
                        lastName = ""
                    }) {
                        Icon(Icons.Default.Add, contentDescription = "Agregar Usuario")
                    }

                    IconButton(onClick = {
                        coroutineScope.launch {
                            val data = getUsers(dao)
                            dataUser.value = data
                        }
                    }) {
                        Icon(Icons.Default.List, contentDescription = "Listar Usuarios")
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Spacer(Modifier.height(16.dp))

                TextField(
                    value = id,
                    onValueChange = { id = it },
                    label = { Text("ID (solo lectura)") },
                    readOnly = true,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                TextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    label = { Text("First Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                TextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    label = { Text("Last Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = {
                        coroutineScope.launch {
                            eliminarUltimoUsuario(dao)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Eliminar Último Usuario", fontSize = 16.sp)
                }

                Spacer(Modifier.height(16.dp))

                Text(
                    text = dataUser.value,
                    fontSize = 20.sp
                )
            }
        }
    )
}

@Composable
fun crearDatabase(context: Context): UserDatabase {
    return Room.databaseBuilder(
        context,
        UserDatabase::class.java,
        "user_db"
    ).build()
}

suspend fun getUsers(dao: UserDao): String {
    var rpta = ""
    val users = dao.getAll()
    users.forEach { user ->
        rpta += "${user.firstName} - ${user.lastName}\n"
    }
    return rpta
}

suspend fun AgregarUsuario(user: User, dao: UserDao) {
    try {
        dao.insert(user)
    } catch (e: Exception) {
        Log.e("User", "Error: insert: ${e.message}")
    }
}

suspend fun eliminarUltimoUsuario(dao: UserDao) {
    try {
        val users = dao.getAll()
        if (users.isNotEmpty()) {
            val ultimo = users.last()
            dao.delete(ultimo)
        }
    } catch (e: Exception) {
        Log.e("User", "Error: delete: ${e.message}")
    }
}
