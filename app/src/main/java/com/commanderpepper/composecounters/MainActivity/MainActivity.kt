package com.commanderpepper.composecounters.MainActivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.Composable
import androidx.compose.collectAsState
import androidx.compose.state
import androidx.ui.core.Modifier
import androidx.ui.core.setContent
import androidx.ui.foundation.Text
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.graphics.Color
import androidx.ui.layout.Column
import androidx.ui.layout.Row
import androidx.ui.layout.fillMaxWidth
import androidx.ui.layout.padding
import androidx.ui.material.Card
import androidx.ui.tooling.preview.Preview
import androidx.ui.unit.dp
import com.commanderpepper.composecounters.Database
import com.commanderpepper.composecounters.ui.ComposeCountersTheme
import com.squareup.sqldelight.Query
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import counter.Counter
import kotlinx.coroutines.flow.Flow
import kotlin.reflect.KFunction1

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val databaseCounter = "counter.db"
        val driver: SqlDriver = AndroidSqliteDriver(Database.Schema, this, databaseCounter)
        val database = Database(driver)

        val flow = database.counterQueries.getParentCounters().asFlow().mapToList()

        setContent {
            ComposeCountersTheme {
                MainActivityParentCounter(
                    flow,
                    database.counterQueries::getChildCountersCount
                )
            }
        }


    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeCountersTheme {
        Greeting("Android")
    }
}

@Composable
fun MainActivityParentCounter(
    flow: Flow<List<Counter>>,
    childCounters: KFunction1<Long?, Query<Long>>
) {
    val parentCounters = flow.collectAsState()

    if (parentCounters.value != null) {
        Column {
            parentCounters.value?.forEach { counter ->
                ParentCounter(counter = counter, childCounters = childCounters)
            }
        }
    }

}

@Composable
fun ParentCounter(counter: Counter, childCounters: KFunction1<Long?, Query<Long>>) {
    val counterState = state { counter }
    Row(modifier = Modifier.padding(16.dp) + Modifier.fillMaxWidth()) {
        Column() {
            Text(text = counterState.value.name)
            Text(text = "Child Counters: ${childCounters.invoke(counterState.value.id).executeAsOne()}")
        }
        Card(
            shape = RoundedCornerShape(4.dp),
            color = Color.Gray
        ) {
            Text(text = "${counterState.value.value}")
        }
    }
}

