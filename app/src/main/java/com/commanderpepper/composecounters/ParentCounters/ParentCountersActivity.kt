package com.commanderpepper.composecounters.ParentCounters

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.commanderpepper.composecounters.App
import com.commanderpepper.composecounters.Utils.CounterResult
import com.commanderpepper.composecounters.ui.theme.ComposeCountersTheme
import counter.Counter
import kotlinx.coroutines.flow.collect
import javax.inject.Inject
import kotlin.random.Random

class ParentCountersActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModel: ParentCountersViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (applicationContext as App).applicationComponent.inject(this)

        setContent {
            ComposeCountersTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Column(
                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = Alignment.End) {
                        FloatingActionButton(onClick = {
                            Toast.makeText(this@ParentCountersActivity, "This is a test", Toast.LENGTH_SHORT).show()
                        }) {
//
                        }
                    }
                    ParentCountersTres(viewModel)
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Composable
fun ParentCounters(viewModel: ParentCountersViewModel) {
    val parentList = viewModel.parentCounters
    when(parentList.collect())
    val stringList = List<String>(10) { Random.nextDouble().toString() }
    Column {
        stringList.forEach {
            Row {
                Column {
                    Text(it)
                }
            }
        }
    }
}

@Composable
fun ParentCountersDos(textToDisplay: String) {
    val stringList = List<String>(10) { Random.nextDouble().toString() }
    Column {
        stringList.forEach {
            Row {
                Column {
                    Text(textToDisplay)
                }
            }
        }
    }
}

@Composable
fun ParentCountersTres(vm: ParentCountersViewModel) {
    val counterResponse = vm.counterResponse.collectAsState()

    when (counterResponse.value) {
        is CounterResult.Loading -> {
            ShowLoading(loading = counterResponse.value as CounterResult.Loading)
        }
        is CounterResult.Error -> {
            ShowError(error = counterResponse.value as CounterResult.Error)
        }
        else -> {
            ShowSuccess(success = counterResponse.value as CounterResult.Success<List<Counter>>)
        }
    }
}

@Composable
fun ShowLoading(loading: CounterResult.Loading) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = loading.message)
        CircularProgressIndicator(modifier = Modifier.wrapContentWidth(CenterHorizontally))
    }
}

@Composable
fun <T: List<Counter>>ShowSuccess(success: CounterResult.Success<T>) {
    if (success.result.isNotEmpty()) {
        Column(Modifier.fillMaxSize()) {
            success.result.forEach {
                ShowParentCounter(counter = it)
            }
        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Add a timer")
        }
    }
}

@Composable
fun ShowParentCounter(counter: Counter) {
    Row {
        Text(text = counter.name)
    }
}

@Composable
fun ShowError(error: CounterResult.Error) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = error.message)
    }
}

@Composable
fun ShowAddTimerFab() {

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeCountersTheme {
//        Greeting("Android")
//        ParentCounters()
//        ParentCountersDos("meow")
//        ShowLoading(loading = CounterResponse.Loading("Loading"))
        ShowSuccess(
            success = CounterResult.Success(
                listOf(
                    Counter(1L, 0, null, 10, 1, 1, "CounterA"),
                    Counter(2L, 0, null, 10, 1, 1, "CounterB")
                )
            )
        )
    }
}