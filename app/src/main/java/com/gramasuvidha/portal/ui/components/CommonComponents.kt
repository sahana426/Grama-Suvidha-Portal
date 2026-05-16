package com.gramasuvidha.portal.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gramasuvidha.portal.R
import com.gramasuvidha.portal.data.model.Project
import com.gramasuvidha.portal.data.model.ProjectStatus
import com.gramasuvidha.portal.utils.localized
import java.text.NumberFormat
import java.util.Locale

fun formatRupees(amount: Long): String = NumberFormat.getCurrencyInstance(Locale("en", "IN")).format(amount)

@Composable
fun StatusChip(status: ProjectStatus, languageTag: String) {
    val color = when (status) {
        ProjectStatus.Completed -> Color(0xFF2E7D32)
        ProjectStatus.InProgress -> Color(0xFF1976D2)
        ProjectStatus.Delayed -> Color(0xFFC62828)
        ProjectStatus.NotStarted -> Color(0xFF6D4C41)
    }
    AssistChip(
        onClick = {},
        label = { Text(localized(status.labelRes, languageTag), color = color, fontWeight = FontWeight.Bold) }
    )
}

@Composable
fun AnimatedProjectProgress(progress: Int, modifier: Modifier = Modifier) {
    val animated by animateFloatAsState(progress / 100f, label = "projectProgress")
    Column(modifier) {
        LinearProgressIndicator(progress = { animated }, modifier = Modifier.fillMaxWidth().height(10.dp))
        Text("$progress%", style = MaterialTheme.typography.labelLarge, modifier = Modifier.align(Alignment.End))
    }
}

@Composable
fun SummaryTile(label: String, value: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(18.dp)).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(label, style = MaterialTheme.typography.labelLarge)
        Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun ProgressAnalyticsChart(projects: List<Project>, languageTag: String, modifier: Modifier = Modifier) {
    val primary = MaterialTheme.colorScheme.primary
    val secondary = MaterialTheme.colorScheme.secondary
    Column(modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(localized(R.string.analytics_chart, languageTag), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Canvas(modifier = Modifier.fillMaxWidth().height(160.dp)) {
            val barWidth = size.width / (projects.size * 2f + 1f)
            projects.forEachIndexed { index, project ->
                val left = barWidth + index * barWidth * 2f
                val height = size.height * project.progress / 100f
                drawRoundRect(
                    color = if (project.status == ProjectStatus.Completed) primary else secondary,
                    topLeft = Offset(left, size.height - height),
                    size = Size(barWidth, height),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(10f, 10f)
                )
            }
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            projects.forEach { Text(it.id.toString(), style = MaterialTheme.typography.labelSmall) }
        }
    }
}
