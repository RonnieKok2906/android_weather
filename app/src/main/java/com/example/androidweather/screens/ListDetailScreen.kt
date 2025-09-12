package com.example.androidweather.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContextWrapper
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import kotlinx.coroutines.launch
import androidx.compose.material3.Icon
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.window.core.layout.WindowWidthSizeClass


@Composable
fun currentActivity(): Activity? {
    val context = LocalContext.current
    return generateSequence(context) { (it as? ContextWrapper)?.baseContext }
        .filterIsInstance<Activity>()
        .firstOrNull()
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class,
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3WindowSizeClassApi::class
)
@Composable
fun ListDetailScreen(
    listDetailViewModel: ListDetailViewModel = viewModel()
) {
    val uiState by listDetailViewModel.uiState.collectAsState()
    val navigator = rememberListDetailPaneScaffoldNavigator<String?>()
    val scope = rememberCoroutineScope()

//    val adaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo()
//    var isCompact = !adaptiveInfo.isWidthAtLeastBreakpoint(Breakpoint.Medium)
//    val isMediumOrLarger = adaptiveInfo.windowSizeClass.isWidthAtLeastBreakpoint(Breakpoint.Medium)

    var menuVisible by remember { mutableStateOf(true) }

    // Keep navigator in sync with selection/fullscreen flags
    LaunchedEffect(uiState.selectedItemId, uiState.showDetailPaneFullScreen) {
        if (uiState.selectedItemId != null) {
            navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, uiState.selectedItemId)
        } else {
            navigator.navigateTo(ListDetailPaneScaffoldRole.List, contentKey = null)
        }

        if (!uiState.showDetailPaneFullScreen && navigator.currentDestination?.pane == ListDetailPaneScaffoldRole.Detail) {
            navigator.navigateTo(ListDetailPaneScaffoldRole.List, contentKey = null)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Weather") },
                navigationIcon = {
                    IconButton(onClick = {
                        scope.launch {
                            if (uiState.showDetailPaneFullScreen) {
                                menuVisible = !menuVisible
                            } else {
                                val listVisible = navigator.scaffoldValue[ListDetailPaneScaffoldRole.List] != PaneAdaptedValue.Hidden

                                if (listVisible) {
                                    // Compact: switch focus to Detail; Expanded: keeps both panes, focuses Detail
                                    navigator.navigateTo(
                                        ListDetailPaneScaffoldRole.Detail,
                                        contentKey = uiState.selectedItemId
                                    )
                                } else {
                                    navigator.navigateTo(
                                        ListDetailPaneScaffoldRole.List,
                                        contentKey = null
                                    )
                                }
                            }
                        }
                    }) {
                        Icon(Icons.Filled.Menu, contentDescription = "Menu")
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(Modifier.padding(innerPadding)) {
            ListDetailPaneScaffold(
                directive = navigator.scaffoldDirective,
                value = navigator.scaffoldValue,

                // LIST
                listPane = {
                    if (menuVisible) {
                        AnimatedPane {
                            ItemListPane(
                                items = uiState.items,
                                onItemSelected = { id ->
                                    listDetailViewModel.selectItem(id)
                                    scope.launch {
                                        navigator.navigateTo(
                                            ListDetailPaneScaffoldRole.Detail,
                                            contentKey = id
                                        )
                                    }
                                },
                                modifier = Modifier
                            )
                        }
                    }
                },
                // DETAIL
                detailPane = {
                    AnimatedPane {
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
                            modifier = Modifier.background(Color.Magenta),
                            onNavigateBack =
                                // Only provide back in compact mode where List is hidden
                                if (navigator.scaffoldValue[ListDetailPaneScaffoldRole.List] == PaneAdaptedValue.Hidden &&
                                    navigator.scaffoldValue[ListDetailPaneScaffoldRole.Detail] != PaneAdaptedValue.Hidden) { {
                                        scope.launch { navigator.navigateBack() }
                                    }
                                } else null
                        )
                    }
                }
            )
        }
    }
}