package com.example.androidweather.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androidweather.viewModels.ListDetailViewModel
import com.example.androidweather.views.AnimatedDetailListPane
import com.example.androidweather.views.ItemDetailPane
import com.example.androidweather.views.ItemListPane
import com.example.androidweather.views.ListDetailPane
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ListDetailScreen(
    listDetailViewModel: ListDetailViewModel = viewModel()
) {
    val uiState by listDetailViewModel.uiState.collectAsState()
    val navigator = rememberListDetailPaneScaffoldNavigator<String?>()
    val scope = rememberCoroutineScope()

    var menuVisible by remember { mutableStateOf(true) }

    val animateSideMenu = true

    LaunchedEffect(uiState.selectedItemId, uiState.showDetailPaneFullScreen) {
        navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, uiState.selectedItemId)
    }

    // TOP BAR
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Weather") },
                navigationIcon = {
                    IconButton(onClick = {
                        scope.launch {
                            menuVisible = !menuVisible
                        }
                    }) {
                        Icon(Icons.Filled.Menu, contentDescription = "Menu")
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(Modifier.padding(innerPadding)) {
            if(animateSideMenu) {
                AnimatedDetailListPane(
                    showList = menuVisible,
                    list = {
                        ItemListPane(
                            items = uiState.items,
                            onItemSelected = { id -> listDetailViewModel.selectItem(id) },
                            modifier = Modifier.fillMaxSize()
                        )
                    },
                    detail = {
                        val currentSelectedId: String? =
                            navigator.currentDestination
                                ?.takeIf { it.pane == ListDetailPaneScaffoldRole.Detail }
                                ?.contentKey

                        val detailsToDisplay =
                            if (currentSelectedId == uiState.selectedItemId) {
                                uiState.selectedItemDetails
                            } else {
                                null
                            }

                        ItemDetailPane(
                            itemDetails = detailsToDisplay,
                            modifier = Modifier.fillMaxWidth(),
                            onNavigateBack = null
                        )
                    }
                )
            } else {
                ListDetailPane(
                    uiState = uiState,
                    listDetailViewModel = listDetailViewModel,
                    menuVisible = menuVisible
                )
            }
        }
    }
}