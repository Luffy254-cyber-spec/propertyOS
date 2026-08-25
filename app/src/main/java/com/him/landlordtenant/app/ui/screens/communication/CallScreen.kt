package com.him.landlordtenant.app.ui.screens.communication

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.him.landlordtenant.app.data.model.communication.CallStatus
import com.him.landlordtenant.app.ui.viewmodel.communication.ChatViewModel

import androidx.compose.ui.tooling.preview.Preview
import com.him.landlordtenant.app.data.model.communication.CallSession
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme

@Composable
fun CallScreen(
    onDismiss: () -> Unit,
    viewModel: ChatViewModel = hiltViewModel()
) {
    val activeCall by viewModel.activeCall.collectAsState()

    if (activeCall == null) {
        onDismiss()
        return
    }

    CallContent(
        activeCall = activeCall!!,
        currentUserId = viewModel.currentUserId,
        onDecline = { viewModel.endCall() },
        onAccept = { /* viewModel.acceptCall() */ },
        onEnd = { viewModel.endCall() }
    )
}

@Composable
fun CallContent(
    activeCall: CallSession,
    currentUserId: String,
    onDecline: () -> Unit,
    onAccept: () -> Unit,
    onEnd: () -> Unit
) {
    val isIncoming = activeCall.receiverId == currentUserId
    val isVideo = activeCall.isVideo

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer) 
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(64.dp))
            
            // Avatar
            Box(
                modifier = Modifier.size(120.dp).clip(CircleShape).background(MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Person, null, modifier = Modifier.size(80.dp), tint = MaterialTheme.colorScheme.onPrimaryContainer)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = if (isIncoming) activeCall.callerName else activeCall.receiverName,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = when(activeCall.status) {
                    CallStatus.DIALING -> "Dialing..."
                    CallStatus.RINGING -> "Ringing..."
                    CallStatus.CONNECTED -> "Connected"
                    else -> "Call ended"
                },
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.weight(1f))

            if (isIncoming && activeCall.status == CallStatus.RINGING) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    CallActionButton(Icons.Default.CallEnd, "Decline", Color.Red) {
                        onDecline()
                    }
                    CallActionButton(if (isVideo) Icons.Default.VideoCall else Icons.Default.Call, "Accept", Color(0xFF4CAF50)) {
                        onAccept()
                    }
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    CallActionButton(Icons.Default.MicOff, "Mute", MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.2f)) {}
                    CallActionButton(Icons.Default.VolumeUp, "Speaker", MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.2f)) {}
                    if (isVideo) {
                        CallActionButton(Icons.Default.VideocamOff, "Video", MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.2f)) {}
                    }
                }
                
                Spacer(modifier = Modifier.height(48.dp))
                
                CallActionButton(Icons.Default.CallEnd, "End", Color.Red, modifier = Modifier.size(72.dp)) {
                    onEnd()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CallScreenPreview() {
    PropertyOSTheme {
        CallContent(
            activeCall = CallSession(
                id = "1",
                callerId = "u1",
                callerName = "John Doe",
                receiverId = "u2",
                receiverName = "Jane Doe",
                status = CallStatus.RINGING,
                isVideo = false
            ),
            currentUserId = "u2",
            onDecline = {},
            onAccept = {},
            onEnd = {}
        )
    }
}

@Composable
fun CallActionButton(
    icon: ImageVector,
    label: String,
    color: Color,
    modifier: Modifier = Modifier.size(64.dp),
    onClick: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        FloatingActionButton(
            onClick = onClick,
            containerColor = color,
            contentColor = Color.White,
            shape = CircleShape,
            modifier = modifier
        ) {
            Icon(icon, label)
        }
        if (label != "End") {
            Spacer(modifier = Modifier.height(8.dp))
            Text(label, color = MaterialTheme.colorScheme.onPrimaryContainer, fontSize = 12.sp)
        }
    }
}
