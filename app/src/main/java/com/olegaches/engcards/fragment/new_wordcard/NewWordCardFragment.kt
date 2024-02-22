package com.olegaches.engcards.fragment.new_wordcard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.olegaches.engcards.R
import com.olegaches.engcards.ui.theme.EngCardsTheme

class NewWordCardFragment : Fragment() {
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
                    NewWordCardScreen(navController)
                }
            }
        }
        return view
    }

    @Composable
    fun NewWordCardScreen(
        navController: NavController,
        viewModel: NewWordCardViewModel = hiltViewModel()
    ) {
        val word = viewModel.wordState.collectAsStateWithLifecycle().value
        val nativeTranslation = viewModel.nativeTranslationState.collectAsStateWithLifecycle().value
        Scaffold(
            bottomBar = {
                Column(
                    modifier = Modifier.padding(horizontal = 20.dp)
                ) {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        onClick = {
                            viewModel.createWordCard()
                            navController.navigate(R.id.action_NewWordCardFragment_to_SavedCardsFragment)
                        }
                    ) {
                        Text(
                            text = stringResource(R.string.add)
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        onClick = navController::navigateUp
                    ) {
                        Text(
                            text = stringResource(R.string.cancel)
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 20.dp),
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    singleLine = true,
                    label = { Text(stringResource(R.string.word)) },
                    shape = RoundedCornerShape(30.dp),
                    value = word,
                    onValueChange = viewModel::onWordChanged
                )
                Spacer(modifier = Modifier.height(30.dp))
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    label = { Text(stringResource(R.string.translation)) },
                    shape = RoundedCornerShape(30.dp),
                    value = nativeTranslation,
                    onValueChange = viewModel::onNativeTranslationChanged
                )
            }
        }
    }
}