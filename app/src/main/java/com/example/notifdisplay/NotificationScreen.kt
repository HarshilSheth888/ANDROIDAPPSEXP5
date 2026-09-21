package com.example.notifdisplay

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun NotificationScreen(viewModel: NotificationViewModel) {
    val notifications by viewModel.notifications.collectAsStateWithLifecycle()

    val backgroundColors = listOf(
        Color(0xFFE3F2FD), // Very light blue
        Color(0xFFF3E5F5), // Very light purple
        Color(0xFFFFF9C4)  // Very light yellow
    )

    val infiniteTransition = rememberInfiniteTransition(label = "background")
    val colorOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "colorOffset"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = backgroundColors,
                    start = Offset(colorOffset, 0f),
                    end = Offset(0f, colorOffset)
                )
            )
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "Inner Peace",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Light,
                                color = Color(0xFF455A64),
                                letterSpacing = 2.sp
                            )
                        )
                    },
                    actions = {
                        if (notifications.isNotEmpty()) {
                            IconButton(onClick = { viewModel.clearAll() }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Clear All",
                                    tint = Color(0xFF455A64).copy(alpha = 0.6f)
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            }
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                AnimatedContent(
                    targetState = notifications.isEmpty(),
                    transitionSpec = {
                        fadeIn(animationSpec = tween(500)) togetherWith
                                fadeOut(animationSpec = tween(500))
                    },
                    label = "content"
                ) { isEmpty ->
                    if (isEmpty) {
                        EmptyState()
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            contentPadding = PaddingValues(16.dp)
                        ) {
                            items(
                                items = notifications,
                                key = { it.id }
                            ) { item ->
                                NotificationCard(
                                    item = item,
                                    modifier = Modifier.animateItem()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NotificationCard(item: NotificationItem, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.4f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White.copy(alpha = 0.1f))
                .padding(20.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.appName.uppercase(),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color(0xFF607D8B),
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                item.title?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF263238)
                        ),
                        maxLines = 1
                    )
                }

                item.text?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color(0xFF455A64),
                            lineHeight = 20.sp
                        ),
                        maxLines = 3
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val infiniteTransition = rememberInfiniteTransition(label = "emptyState")
        val alpha by infiniteTransition.animateFloat(
            initialValue = 0.3f,
            targetValue = 0.7f,
            animationSpec = infiniteRepeatable(
                animation = tween(3000, easing = LinearOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "alpha"
        )

        Text(
            text = "Your digital space is clear.",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineSmall.copy(
                color = Color(0xFF607D8B).copy(alpha = alpha),
                fontWeight = FontWeight.ExtraLight
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Enjoy the quiet moment.",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge.copy(
                color = Color(0xFF607D8B).copy(alpha = alpha * 0.8f),
                fontWeight = FontWeight.ExtraLight
            )
        )
    }
}
