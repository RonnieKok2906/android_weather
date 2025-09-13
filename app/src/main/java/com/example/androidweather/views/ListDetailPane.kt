package com.example.androidweather.views

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.example.androidweather.viewModels.ListDetailViewModel
import kotlinx.coroutines.launch
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun ListDetailPane(
    listDetailViewModel: ListDetailViewModel,
    menuVisible: Boolean
) {
    val navigator = rememberListDetailPaneScaffoldNavigator<String?>()
    val uiState by listDetailViewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()

    ListDetailPaneScaffold(
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,

        // LIST
        listPane = {
            if (menuVisible) {
                AnimatedPane {
                    ListPane(
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

                DetailPane(
                    itemDetails = detailsToDisplay,
                    modifier = Modifier.fillMaxWidth(),
                    onNavigateBack = null
                )
            }
        }
    )
}