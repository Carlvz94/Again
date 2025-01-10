package com.example.servidor

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CargarDatosAsync() /*Esta funcion permite una carga rapidad de datos*/

        }
    }
}


fun convertirPresion(valor: Double, unidadOrigen: String, unidadDestino: String): Double {
    val conversiones = mapOf(
        "psi" to 6894.76,
        "MPa" to 1e6,
        "Pa" to 1.0,
        "kg/cm²" to 98066.5,
        "bar" to 100000.0
    )

    val valorEnPa = valor * (conversiones[unidadOrigen] ?: 1.0)
    return valorEnPa / (conversiones[unidadDestino] ?: 1.0)
}
/*Esta funcion establece los factores de conversion y la logica de operacion*/

/*Aqui empiezan los elementos componibles, visibles, entradas, espacios*/
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ConversionApp() {
    var inputValue by remember { mutableStateOf("") }
    var unidadOrigen by remember { mutableStateOf("psi") }
    var unidadDestino by remember { mutableStateOf("Pa") }
    var resultado by remember { mutableStateOf(0.0) }
    val focusManager = LocalFocusManager.current
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = scaffoldState,
        content = {
            Column(modifier = Modifier.padding(16.dp)) {
                TextField(
                    value = inputValue,
                    onValueChange = { inputValue = it },
                    label = { Text("Valor") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row {
                    Button(onClick = {
                        val temp = unidadOrigen
                        unidadOrigen = unidadDestino
                        unidadDestino = temp
                    }) {
                        Text("Invertir Unidades")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                UnidadSelector("Unidad de Origen", unidadOrigen) { unidadOrigen = it }
                UnidadSelector("Unidad de Destino", unidadDestino) { unidadDestino = it }

                Spacer(modifier = Modifier.height(8.dp))

                Button(onClick = {
                    val valor = inputValue.toDoubleOrNull()
                    if (valor == null) {
                        coroutineScope.launch {
                            scaffoldState.snackbarHostState.showSnackbar("Se requiere agregar un valor por abhimael")
                        }
                    } else {
                        resultado = convertirPresion(valor, unidadOrigen, unidadDestino)

                    }
                }) {
                    Text("Convertir")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text("Resultado: ${String.format("%.2f", resultado)} $unidadDestino",
                    fontSize = 24.sp)



            }
        }
    )
}




/*Aqui se establecen elementos componibles selecionables y operacionales*/
@Composable
fun UnidadSelector(titulo: String, unidadSeleccionada: String, onSeleccionarUnidad: (String) -> Unit) {
    val unidades = listOf("psi", "MPa", "Pa", "kg/cm²","bar")
    Column {
        Text(titulo)
        unidades.forEach { unidad ->
            Row {Modifier.padding(start = 4.dp)
                RadioButton(

                    selected = unidad == unidadSeleccionada,
                    onClick = { onSeleccionarUnidad(unidad) }
                )
                Text(unidad, modifier = Modifier.padding(start = 6.dp))
            }
        }
    }
}


/*Aqui nuevamente la funcion de optimizacion para un uso eficiente de recursos*/
@Composable
fun CargarDatosAsync() {
    var datosCargados by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
// Carga de datos o recursos pesados
            delay(2000) // Simulación de carga
            datosCargados = true
        }
    }
    if (datosCargados) {
// Mostrar contenido de la aplicación
        ConversionApp()
    } else {
// Mostrar pantalla de carga
        Text("Cargando...")
        CircularProgressIndicator(modifier = Modifier.fillMaxSize())
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewApp() {
    ConversionApp()
}

