package com.him.landlordtenant.app.ui.screens.scaffold

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme

@Composable
fun AppScaffold(
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    content: @Composable (Modifier) -> Unit
) {
    Scaffold(
        topBar = topBar,
        bottomBar = bottomBar
    ) { innerPadding ->
        content(Modifier.padding(innerPadding))
    }
}

@Preview(showBackground = true)
@Composable
fun AppScaffoldPreview() {
    PropertyOSTheme {
        AppScaffold(
            topBar = { Text("Top Bar") },
            bottomBar = { Text("Bottom Bar") }
        ) { modifier ->
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Main Content")
            }
        }
    }
}
