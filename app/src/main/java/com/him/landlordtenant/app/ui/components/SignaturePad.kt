package com.him.landlordtenant.app.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp

@Composable
fun SignaturePad(
    onSignatureCaptured: (Path) -> Unit,
    modifier: Modifier = Modifier
) {
    var currentPath by remember { mutableStateOf<Path?>(null) }
    val paths = remember { mutableStateListOf<Path>() }

    Column(modifier = modifier) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            currentPath = Path().apply { moveTo(offset.x, offset.y) }
                        },
                        onDrag = { change, dragAmount ->
                            currentPath?.lineTo(change.position.x, change.position.y)
                            // Note: Real-time update is tricky with mutableStateOf(Path)
                            // A better way is using a custom State or triggering recomposition
                        },
                        onDragEnd = {
                            currentPath?.let { paths.add(it) }
                            currentPath = null
                            // Simplified: in a real app, you'd convert paths to a Bitmap
                        }
                    )
                },
            color = Color.White,
            shape = MaterialTheme.shapes.medium,
            border = androidx.compose.foundation.BorderStroke(1.dp, Color.Gray)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                paths.forEach { path ->
                    drawPath(
                        path = path,
                        color = Color.Black,
                        style = Stroke(width = 4f, cap = StrokeCap.Round)
                    )
                }
                currentPath?.let { path ->
                    drawPath(
                        path = path,
                        color = Color.Black,
                        style = Stroke(width = 4f, cap = StrokeCap.Round)
                    )
                }
            }
        }
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = { paths.clear() }) {
                Text("Clear")
            }
            Button(onClick = { /* Process paths */ }) {
                Text("Confirm Signature")
            }
        }
    }
}
