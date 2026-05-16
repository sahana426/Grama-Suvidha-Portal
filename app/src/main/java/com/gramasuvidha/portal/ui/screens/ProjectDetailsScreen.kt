package com.gramasuvidha.portal.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.gramasuvidha.portal.R
import com.gramasuvidha.portal.data.model.Project
import com.gramasuvidha.portal.ui.components.AnimatedProjectProgress
import com.gramasuvidha.portal.ui.components.StatusChip
import com.gramasuvidha.portal.ui.components.formatRupees
import com.gramasuvidha.portal.utils.localized
import com.gramasuvidha.portal.viewmodel.ProjectDetailsViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectDetailsScreen(viewModel: ProjectDetailsViewModel, onBack: () -> Unit) {
    val project by viewModel.project.collectAsState()
    var language by remember { mutableStateOf("en") }
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(localized(R.string.project_details, language), fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = onBack) { Text("‹", style = MaterialTheme.typography.headlineMedium) } },
                actions = {
                    TextButton(onClick = { language = if (language == "en") "kn" else "en" }) {
                        Text(if (language == "en") localized(R.string.language_kannada, language) else localized(R.string.language_english, language))
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        project?.let {
            ProjectDetailsContent(it, language, Modifier.padding(padding), snackbarHostState)
        }
    }
}

@Composable
private fun ProjectDetailsContent(project: Project, languageTag: String, modifier: Modifier, snackbarHostState: SnackbarHostState) {
    LazyColumn(modifier = modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            AsyncImage(model = project.imageUrl, contentDescription = project.title, modifier = Modifier.fillMaxWidth().height(240.dp), contentScale = ContentScale.Crop)
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(project.title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                    StatusChip(project.status, languageTag)
                }
                Text(project.description, style = MaterialTheme.typography.bodyLarge)
                DetailLine(localized(R.string.department, languageTag), project.department)
                DetailLine(localized(R.string.contractor, languageTag), project.contractor)
                DetailLine(localized(R.string.budget, languageTag), formatRupees(project.budget))
                DetailLine(localized(R.string.budget_utilization, languageTag), formatRupees(project.budgetUtilization))
                AnimatedProjectProgress(project.progress)
            }
        }
        item { TimelineTracker(project, languageTag) }
        item { BeforeAfterGallery(project, languageTag) }
        item { RecentUpdates(project, languageTag) }
        item { CitizenFeedback(languageTag, snackbarHostState) }
        item { AssistChip(onClick = {}, label = { Text("🔔 ${localized(R.string.notifications, languageTag)}") }) }
    }
}

@Composable
private fun DetailLine(label: String, value: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(0.42f))
        Text(value, modifier = Modifier.weight(0.58f))
    }
}

@Composable
private fun TimelineTracker(project: Project, languageTag: String) {
    Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(22.dp)) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(localized(R.string.timeline_tracker, languageTag), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text("${localized(R.string.start_date, languageTag)}: ${project.startDate}")
            Text("${localized(R.string.expected_completion, languageTag)}: ${project.expectedCompletion}")
            Text("${localized(R.string.progress, languageTag)}: ${project.progress}%")
        }
    }
}

@Composable
private fun BeforeAfterGallery(project: Project, languageTag: String) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(localized(R.string.before_after_gallery, languageTag), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(project.beforeImages + project.afterImages) { image ->
                AsyncImage(model = image, contentDescription = localized(R.string.before_after_gallery, languageTag), modifier = Modifier.height(140.dp).fillParentMaxWidth(0.72f), contentScale = ContentScale.Crop)
            }
        }
    }
}

@Composable
private fun RecentUpdates(project: Project, languageTag: String) {
    Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(22.dp)) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(localized(R.string.recent_updates, languageTag), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            project.updates.forEachIndexed { index, update -> Text("${index + 1}. $update") }
        }
    }
}

@Composable
private fun CitizenFeedback(languageTag: String, snackbarHostState: SnackbarHostState) {
    var rating by remember { mutableIntStateOf(0) }
    var showDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val success = localized(R.string.issue_success, languageTag)

    Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(22.dp)) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(localized(R.string.citizen_feedback, languageTag), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(localized(R.string.rate_project, languageTag))
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                (1..5).forEach { star ->
                    Text(if (star <= rating) "★" else "☆", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.clickable { rating = star })
                }
            }
            Button(onClick = { showDialog = true }) { Text(localized(R.string.report_issue, languageTag)) }
        }
    }

    if (showDialog) {
        IssueDialog(
            languageTag = languageTag,
            onDismiss = { showDialog = false },
            onSubmit = {
                showDialog = false
                scope.launch { snackbarHostState.showSnackbar(success) }
            }
        )
    }
}

@Composable
private fun IssueDialog(languageTag: String, onDismiss: () -> Unit, onSubmit: () -> Unit) {
    var selected by remember { mutableStateOf(R.string.poor_quality) }
    var description by remember { mutableStateOf("") }
    val categories = listOf(R.string.poor_quality, R.string.delay, R.string.corruption_concern, R.string.water_leakage, R.string.other)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(localized(R.string.report_issue, languageTag)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(localized(R.string.issue_category, languageTag), fontWeight = FontWeight.Bold)
                categories.forEach { category ->
                    AssistChip(onClick = { selected = category }, label = { Text(if (selected == category) "✓ ${localized(category, languageTag)}" else localized(category, languageTag)) })
                }
                OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text(localized(R.string.description, languageTag)) }, minLines = 3)
                TextButton(onClick = {}) { Text("📷 ${localized(R.string.choose_image, languageTag)}") }
            }
        },
        confirmButton = { Button(onClick = onSubmit) { Text(localized(R.string.submit, languageTag)) } },
        dismissButton = { TextButton(onClick = onDismiss) { Text(localized(R.string.back, languageTag)) } }
    )
}
