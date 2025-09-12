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

    // `rememberListDetailPaneScaffoldNavigator` helps manage navigation between list/detail
    // and adapts to screen size.
    val navigator = rememberListDetailPaneScaffoldNavigator<String?>()
    // For String? type argument: pass the selected item's ID or null if nothing is selected.

    LaunchedEffect(uiState.selectedItemId, uiState.showDetailPaneFullScreen) {
        if (uiState.selectedItemId != null) {
            // If an item is selected, try to navigate to the detail pane.
            // `canNavigateTo` checks if the detail pane can be shown (e.g., if it's not already visible
            // in a side-by-side layout).
            navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, uiState.selectedItemId)
        } else {
            // If no item is selected (or selection cleared), navigate back to the list pane.
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
                                // Hide list by navigating to detail only
                                navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, contentKey = uiState.selectedItemId)
                            } else {
                                // Show list again
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
                    // Check if there is a selected item based on the navigator's scaffoldValue
                    val currentSelectedItemId: String? = navigator.currentDestination?.takeIf { it.pane == ListDetailPaneScaffoldRole.Detail }?.contentKey

                    val detailsToDisplay = if (currentSelectedItemId == uiState.selectedItemId) {
                        uiState.selectedItemDetails
                    } else {
                        // If navigator's selected item differs from ViewModel (e.g., during back navigation),
                        // you might want to load details for navigator.scaffoldValue.secondaryPaneState?.content
                        // or simply show a placeholder/loading until ViewModel catches up.
                        // For simplicity, we'll rely on the ViewModel being the source of truth for details.
                        // This could also mean we should fetch details based on `currentSelectedItemId` if it's different.
                        if (currentSelectedItemId != null && currentSelectedItemId != uiState.selectedItemId) {
                            // Potentially trigger a load for currentSelectedItemId if not already loaded
                            // For now, let's assume the ViewModel handles keeping selectedItemDetails consistent
                            // with selectedItemId
                            null // Or a loading state
                        } else {
                            uiState.selectedItemDetails
                        }
                    }

                    ItemDetailPane(
                        itemDetails = detailsToDisplay,
                        // Pass onNavigateBack only if the detail pane is the primary one (compact mode)
                        onNavigateBack = if (navigator.scaffoldValue[ListDetailPaneScaffoldRole.List] == PaneAdaptedValue.Hidden &&
                            navigator.scaffoldValue[ListDetailPaneScaffoldRole.Detail] == PaneAdaptedValue.Expanded) {
                            // In dual-pane layout, back might be handled differently or not needed here
                            { scope.launch { navigator.navigateBack() } }
                        } else {
                            null // Or navigator::navigateBack
                        }
                    )
                }
            },
            // You can also provide an optional `extraPane` for three-pane layouts
        )
    }
}
