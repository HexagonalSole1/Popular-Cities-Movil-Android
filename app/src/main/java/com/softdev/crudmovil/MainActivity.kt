package com.softdev.crudmovil

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.softdev.crudmovil.core.navigation.NavGraph
import com.softdev.crudmovil.ui.theme.CrudMovilTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CrudMovilTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
                    NavGraph()
                }
            }
        }
    }
}

