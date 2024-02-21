package com.olegaches.engcards.fragment.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.haroncode.lazycardstack.LazyCardStack
import com.haroncode.lazycardstack.LazyCardStackState
import com.haroncode.lazycardstack.items
import com.haroncode.lazycardstack.rememberLazyCardStackState
import com.haroncode.lazycardstack.swiper.SwipeDirection
import com.olegaches.engcards.R
import com.olegaches.engcards.domain.model.WordCard
import com.olegaches.engcards.ui.theme.EngCardsTheme
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

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
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        HomeScreen(navController)
                    }
                }
            }
        }
        return view
    }

    @Composable
    private fun HomeScreen(
        navController: NavController,
        viewModel: HomeViewModel = hiltViewModel()
    ) {
        val state = viewModel.state.collectAsStateWithLifecycle().value
        val cardStackState = rememberLazyCardStackState()
        val coroutineScope = rememberCoroutineScope()
        LaunchedEffect(state.cards.hashCode()) {
            cardStackState.snapTo(0)
        }
        Scaffold(
            topBar = {
                HomeTopBar(
                    onPremiumClicked = viewModel::showPremiumAd,
                    onSettingsClicked = { navController.navigate(R.id.SettingsFragment) },
                    onWordCardListClicked = { TODO() }
                )
            },
            floatingActionButton = {
                Column {
                    ExtendedFloatingActionButton(
                        content = {
                            Text(
                                text = stringResource(R.string.next_card)
                            )
                        },
                        onClick = {
                            coroutineScope.launch {
                                cardStackState.animateToNext(
                                    direction = SwipeDirection.Right,
                                    animation = tween(500)
                                )
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(40.dp))
                }
            },
            floatingActionButtonPosition = FabPosition.End
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (state.showPremiumAd) {
                    PremiumAdDialog(
                        title = stringResource(R.string.translation_attempts_limit),
                        onDismiss = viewModel::dismissPremiumAd,
                        onConfirm = viewModel::onActivatePremium,
                    )
                }
                CardStack(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .padding(24.dp),
                    state = cardStackState,
                    loading = state.loading,
                    cards = state.cards,
                    onRequireTranslation = viewModel::onRequireTranslation,
                    onPlayAgainClicked = viewModel::onPlayAgainClicked,
                )
            }
        }
    }

    @Composable
    private fun PremiumAdDialog(
        title: String,
        onDismiss: () -> Unit,
        onConfirm: () -> Unit,
    ) {
        AlertDialog(
            title = {
                Text(
                    text = title
                )
            },
            text = {
                Text(
                    stringResource(R.string.premium_ad_text)
                )
            },
            onDismissRequest = onDismiss,
            confirmButton = {
                Button(
                    onClick = { onConfirm() }
                ) {
                    Text(
                        text = stringResource(R.string.buy)
                    )
                }
            },
            dismissButton = {
                Button(
                    onClick = { onDismiss() }
                ) {
                    Text(
                        text = stringResource(R.string.decline)
                    )
                }
            }
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun HomeTopBar(
        onPremiumClicked: () -> Unit,
        onSettingsClicked: () -> Unit,
        onWordCardListClicked: () -> Unit
    ) {
        CenterAlignedTopAppBar(
            colors = TopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
                navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                scrolledContainerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary
            ),
            title = {
                Text(
                    text = stringResource(id = R.string.app_name)
                )
            },
            navigationIcon = {
                IconButton(onClick = { onWordCardListClicked() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.book_icon_24),
                        contentDescription = stringResource(R.string.your_card_list)
                    )
                }
            },
            actions = {
                Box {
                    var expanded by remember { mutableStateOf(false) }
                    IconButton(
                        onClick = {
                            expanded = !expanded
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = null
                        )
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.settings)) },
                            onClick = { onSettingsClicked() }
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.premium)) },
                            onClick = { onPremiumClicked() }
                        )
                    }
                }
            }
        )
    }

    @Composable
    private fun CardStack(
        modifier: Modifier,
        state: LazyCardStackState,
        cards: List<WordCard>,
        loading: Boolean,
        onRequireTranslation: () -> Boolean,
        onPlayAgainClicked: () -> Unit
    ) {
        LazyCardStack(
            modifier = modifier,
            state = state
        ) {
            items(
                items = cards,
                key = { card -> card.word }
            ) { card ->
                var rotated by rememberSaveable { mutableStateOf(false) }
                val rotation by animateFloatAsState(
                    targetValue = if (rotated) 180f else 0f,
                    animationSpec = tween(500), label = ""
                )
                OutlinedCard(
                    modifier = Modifier
                        .graphicsLayer {
                            rotationY = rotation
                            cameraDistance = 9 * density
                        },
                    shape = RoundedCornerShape(30.dp),
                    elevation = CardDefaults.outlinedCardElevation(
                        defaultElevation = 6.dp
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable {
                                if (onRequireTranslation()) {
                                    rotated = !rotated
                                }
                            }
                    ) {
                        if (rotation >= 90f) {
                            Text(
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .graphicsLayer {
                                        rotationY = 180f
                                    },
                                textAlign = TextAlign.Center,
                                text = card.nativeTranslation,
                                style = MaterialTheme.typography.titleLarge
                            )
                        } else {
                            Text(
                                modifier = Modifier.align(Alignment.Center),
                                textAlign = TextAlign.Center,
                                text = card.word,
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                    }
                }
            }

            item(
                key = { "placeholder" }
            ) {
                ElevatedCard(
                    Modifier
                        .dragEnabled(false)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        if (loading) {
                            CircularProgressIndicator()
                        } else {
                            Text(
                                text = stringResource(R.string.the_end),
                                style = MaterialTheme.typography.titleLarge
                            )
                            Spacer(modifier = Modifier.height(25.dp))
                            OutlinedButton(onClick = { onPlayAgainClicked() }) {
                                Text(stringResource(R.string.play_again))
                            }
                        }
                    }
                }
            }
        }
    }
}