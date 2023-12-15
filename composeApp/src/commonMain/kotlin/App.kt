import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.MarqueeSpacing
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import data.Api
import data.QuoteJson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi

class HomeScreen : Screen {

    @OptIn(ExperimentalResourceApi::class, ExperimentalMaterialApi::class,
        ExperimentalFoundationApi::class
    )
    @Composable
    override fun Content() {

        MaterialTheme {

            val screenModel = rememberScreenModel {
                MainViewModel(
//                    api = Api("http://192.168.0.192:8080")
                    api = Api("https://quotes-408216.lm.r.appspot.com")
                )
            }

            val viewState = screenModel.state.collectAsState()

            Box {
                LazyColumn() {
                    item {
                        Text("Quotes. Size: ${viewState.value.elements.size}")
                    }
                    items(viewState.value.elements) {
                        Card(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
                            Column(Modifier.padding(4.dp)) {
                                Row {
                                    Text(it.author)
                                }
                                Row {
                                    Text(it.categories.joinToString { it.value })
                                }
                                Row {
                                    Text(it.quote)
                                }
                            }
                        }
                    }
                }
            }
        }


    }
}

@Composable
fun App() {
    Navigator(
        screen = HomeScreen()
    )
}

class MainViewModel(val api: Api) : StateScreenModel<ListViewState>(ListViewState.INITIAL) {

    init {
        screenModelScope.launch(Dispatchers.IO) {
            try {

                val newState = api.getAll().let(::ListViewState).also {
                    println(it)
                }

                mutableState.emit(newState)

            } catch (e: Exception) {
                println(e)
            }
        }
    }
}

data class ListViewState(val elements: List<QuoteJson>) {
    companion object {
        val INITIAL = ListViewState(emptyList())
    }
}