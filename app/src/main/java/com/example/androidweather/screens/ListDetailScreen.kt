package com.example.androidweather.screens

import kotlinx.coroutines.launch
import androidx.compose.material3.Icon
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import com.example.androidweather.viewModels.ListDetailViewModel
import com.example.androidweather.views.ItemDetailPane
import com.example.androidweather.views.ItemListPane

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ListDetailScreen(
    listDetailViewModel: ListDetailViewModel = viewModel()
) {
    val uiState by listDetailViewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()

    val navigator = rememberListDetailPaneScaffoldNavigator<String?>()

    LaunchedEffect(uiState.selectedItemId, uiState.showDetailPaneFullScreen) {
        if (uiState.selectedItemId != null) {
            navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, uiState.selectedItemId)
        } else {
            navigator.navigateTo(ListDetailPaneScaffoldRole.List, contentKey = null)
        }

        // Handle the case where the detail pane is being shown full screen (compact)
        // and the user wants to go back (e.g. uiState.showDetailPaneFullScreen becomes false)
        if (!uiState.showDetailPaneFullScreen && navigator.currentDestination?.pane == ListDetailPaneScaffoldRole.Detail) {
            navigator.navigateTo(ListDetailPaneScaffoldRole.List)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Weather") },
                navigationIcon = {
                    IconButton(onClick = {
                        scope.launch {
                            val listVisible = navigator.scaffoldValue[ListDetailPaneScaffoldRole.List] != PaneAdaptedValue.Hidden

                            if (listVisible) {
                                navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, contentKey = uiState.selectedItemId)
                            } else {
                                navigator.navigateTo(ListDetailPaneScaffoldRole.List, contentKey = null)
                            }
                        }
                    }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                }
            )
        }
    ) { innerPadding ->
        ListDetailPaneScaffold(
            modifier = Modifier.padding(innerPadding),
            directive = navigator.scaffoldDirective,
            value = navigator.scaffoldValue,
            listPane = {
                AnimatedPane(modifier = Modifier) {
                    ItemListPane(
                        items = uiState.items,
                        onItemSelected = { itemId ->
                            listDetailViewModel.selectItem(itemId)
                        }
                    )
                }
            },
            detailPane = {
                AnimatedPane(modifier = Modifier) {
                    val currentSelectedItemId = navigator.currentDestination?.takeIf { it.pane == ListDetailPaneScaffoldRole.Detail }?.contentKey

                    val detailsToDisplay = if (currentSelectedItemId == uiState.selectedItemId) {
                        uiState.selectedItemDetails
                    } else {
                        if (currentSelectedItemId != null) {
                            null
                        } else {
                            uiState.selectedItemDetails
                        }
                    }

                    ItemDetailPane(
                        itemDetails = detailsToDisplay,
                        onNavigateBack = if (navigator.scaffoldValue[ListDetailPaneScaffoldRole.List] == PaneAdaptedValue.Hidden &&
                            navigator.scaffoldValue[ListDetailPaneScaffoldRole.Detail] == PaneAdaptedValue.Expanded) {
                            { scope.launch { navigator.navigateBack() } }
                        } else {
                            null
                        }
                    )
                }
            }
        )
    }
}
