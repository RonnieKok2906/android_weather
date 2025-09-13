package com.example.androidweather.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androidweather.viewModels.ListDetailUiState
import com.example.androidweather.viewModels.ListDetailViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun ListDetailPane(
    uiState: ListDetailUiState,
    listDetailViewModel: ListDetailViewModel = viewModel(),
    menuVisible: Boolean
) {
    val navigator = rememberListDetailPaneScaffoldNavigator<String?>()
    val scope = rememberCoroutineScope()

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
                    modifier = Modifier.fillMaxWidth(),
                    onNavigateBack = null
                )
            }
        }
    )
}