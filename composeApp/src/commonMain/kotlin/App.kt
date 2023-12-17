import ListViewState.Data
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import data.Api
import data.QuoteJson
import feed.FeedScreen
import feed.FullScreenQuoteContent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi


@Composable
fun App() {
    Navigator(
        screen = HomeScreen()
    )
}

class HomeScreen : Screen {

    @Composable
    override fun Content() {

        MaterialTheme {

            val screenModel = rememberScreenModel {
                MainViewModel(
                    api = Api("https://quotes-408216.lm.r.appspot.com")
                )
            }

            val viewState = screenModel.state.collectAsState()

            when (val state = viewState.value) {
                is Data -> {
                    FeedScreen(
                        initialPage = 0,
                        content = state.elements,
                        click = { share(it) }) { page, index ->
                        screenModel.pageVisible(page, index)
                    }
                }

                is ListViewState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(state.errorMessage)
                    }
                }

                ListViewState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }

    private fun share(quote: QuoteJson) {
        // todo
    }
}

class MainViewModel(val api: Api) : StateScreenModel<ListViewState>(ListViewState.INITIAL) {

    init {
        screenModelScope.launch(Dispatchers.IO) {
            try {

                mutableState.emit(ListViewState.Loading)

                try {

                    val newState = api.getAll().let(::Data).also {
                        println(it)
                    }
                    mutableState.emit(newState)
                } catch (exception: Exception) {
                    mutableState.emit(ListViewState.Error(exception.message ?: "Something failed"))
                }


            } catch (e: Exception) {
                println(e)
            }
        }
    }

    fun pageVisible(page: QuoteJson, index: Int) {

    }
}

sealed interface ListViewState {

    data class Data(val elements: List<QuoteJson>) : ListViewState
    data class Error(val errorMessage: String) : ListViewState
    object Loading : ListViewState

    companion object {
        val INITIAL = ListViewState.Loading
    }
}

