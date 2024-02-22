package com.olegaches.engcards.fragment.saved_cards

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.olegaches.engcards.R
import com.olegaches.engcards.domain.model.WordCard
import com.olegaches.engcards.fragment.components.PremiumAdDialog
import com.olegaches.engcards.ui.theme.EngCardsTheme

class SavedCardsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val composeView = view.findViewById<ComposeView>(R.id.compose_view)
        val navController = findNavController()
        composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                EngCardsTheme {
                    SavedCardsScreen(navController)
                }
            }
        }
        return view
    }

    @Composable
    private fun SavedCardsScreen(
        navController: NavController,
        viewModel: SavedCardsViewModel = hiltViewModel()
    ) {
        val state = viewModel.state.collectAsStateWithLifecycle().value
        if (state.showPremiumAd) {
            PremiumAdDialog(
                title = stringResource(R.string.addition_attempts_limit),
                onDismiss = viewModel::dismissPremiumAd,
                onConfirm = viewModel::onActivatePremium,
            )
        }
        Scaffold(
            topBar = {
                TopBar(
                    query = state.query,
                    onQueryCleared = viewModel::onQueryCleared,
                    onQueryChanged = viewModel::onQueryChanged,
                    onBackIconClicked = navController::navigateUp
                )
            },
            floatingActionButtonPosition = FabPosition.End,
            floatingActionButton = {
                Column {
                    FloatingActionButton(onClick = {
                        if (viewModel.onRequireTranslation())
                            navController.navigate(R.id.NewWordCardFragment)
                    }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = stringResource(R.string.add_new_card)
                        )
                    }
                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                if (state.loading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                } else {
                    CardList(
                        modifier = Modifier.fillMaxSize(),
                        cards = state.cards
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun TopBar(
        query: String,
        onQueryChanged: (String) -> Unit,
        onQueryCleared: () -> Unit,
        onBackIconClicked: () -> Unit,
    ) {
        val primaryColor = MaterialTheme.colorScheme.primary
        val onPrimaryColor = MaterialTheme.colorScheme.onPrimary
        CenterAlignedTopAppBar(
            colors = TopAppBarColors(
                containerColor = primaryColor,
                actionIconContentColor = onPrimaryColor,
                navigationIconContentColor = onPrimaryColor,
                scrolledContainerColor = primaryColor,
                titleContentColor = onPrimaryColor
            ),
            navigationIcon = {
                IconButton(onClick = onBackIconClicked) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(id = R.string.back)
                    )
                }
            },
            title = {
                CustomSearch(
                    modifier = Modifier
                        .height(45.dp)
                        .fillMaxWidth(),
                    searchValue = query,
                    onValueChange = onQueryChanged,
                    onSearchClear = onQueryCleared
                )
            }
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun CustomSearch(
        modifier: Modifier = Modifier,
        searchValue: String,
        onValueChange: (String) -> Unit,
        onSearchClear: () -> Unit,
    ) {
        BasicTextField(
            modifier = modifier,
            value = searchValue,
            onValueChange = onValueChange,
            textStyle = TextStyle(
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            ),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurfaceVariant),
            decorationBox = { innerTextField ->
                OutlinedTextFieldDefaults.DecorationBox(
                    value = searchValue,
                    innerTextField = innerTextField,
                    enabled = true,
                    singleLine = true,
                    visualTransformation = VisualTransformation.None,
                    interactionSource = remember { MutableInteractionSource() },
                    placeholder = {
                        Text(
                            text = stringResource(R.string.search)
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    },
                    trailingIcon = {
                        if (searchValue.isNotEmpty()) {
                            IconButton(
                                onClick = onSearchClear,
                            ) {
                                Icon(
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    imageVector = Icons.Default.Clear,
                                )
                            }
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(),
                    contentPadding = PaddingValues(0.dp),
                    container = {
                        OutlinedCard {
                            Box(
                                modifier =
                                Modifier
                                    .fillMaxWidth(),
                            )
                        }
                    },
                )
            },
            singleLine = true,
        )
    }

    @Composable
    private fun CardList(
        modifier: Modifier,
        cards: List<WordCard>
    ) {
        LazyColumn(
            modifier = modifier
        ) {
            items(cards) { card ->
                ListItem(
                    modifier = Modifier.fillMaxWidth(),
                    headlineContent = {
                        Text(
                            text = card.word,
                        )
                    },
                    supportingContent = {
                        Text(
                            text = card.nativeTranslation
                        )
                    }
                )
                HorizontalDivider(thickness = 0.dp)
            }
        }
    }
}