package com.example.state


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.state.ui.theme.ComposeMasterTheme
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeMasterTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MainScreen()
                }
            }
        }
    }
}

enum class StateTopic(val label: String) {
    LOCAL_STATE("Local State"),
    PERSISTENT_STATE("Persistent State"),
    SHARED_STATE("Shared State"),
    DERIVED_STATE("Derived State"),
    LIST_STATE("List State"),
    MAP_STATE("Map State"),
    SET_STATE("Set State"),
    SNAPSHOT_FLOW("Snapshot Flow"),
    LAB("Number Guessing Game"),
    GO("Computer Go"),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    var selectedTopic by remember { mutableStateOf(StateTopic.LOCAL_STATE) }
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("State Examples") },
                actions = {
                    IconButton(onClick = { expanded = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "More")
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        StateTopic.entries.forEach { topic ->
                            DropdownMenuItem(
                                text = { Text(topic.label) },
                                onClick = {
                                    selectedTopic = topic
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (selectedTopic) {
                StateTopic.LOCAL_STATE -> LocalStateExample()
                StateTopic.PERSISTENT_STATE -> PersistentStateExample()
                StateTopic.SHARED_STATE -> SharedStateExample()
                StateTopic.DERIVED_STATE -> DerivedStateExample()
                StateTopic.LIST_STATE -> ListStateExample()
                StateTopic.MAP_STATE -> MapStateExample()
                StateTopic.SET_STATE -> SetStateExample()
                StateTopic.SNAPSHOT_FLOW -> SnapshotFlowExample()
                StateTopic.LAB -> Lab()
                StateTopic.GO -> ComputerGoGame()
            }
        }
    }
}

// 1. Local State
@Composable
fun LocalStateExample() {
    var count by remember { mutableIntStateOf(0) }
    StateExampleTemplate("Local State", "Count: $count", { count++ })
}

// 2. Persistent State
@Composable
fun PersistentStateExample() {
    var count by rememberSaveable { mutableStateOf(0) }
    StateExampleTemplate("Persistent State", "Count: $count", { count++ })
}

// 3. Shared State (ViewModel)
class CounterViewModel : ViewModel() {
    var count by mutableStateOf(0)
        private set

    fun increment() {
        count++
    }
}

@Composable
fun SharedStateExample(viewModel: CounterViewModel = viewModel()) {
    StateExampleTemplate("Shared State", "Count: ${viewModel.count}", { viewModel.increment() })
}

// 4. Derived State
@Composable
fun DerivedStateExample() {
    var count by remember { mutableIntStateOf(0) }
    val isEven by remember { derivedStateOf { count % 2 == 0 } }
    StateExampleTemplate("Derived State", "Count: $count\nIs Even: $isEven", { count++ })
}

// 5. List State
@Composable
fun ListStateExample() {
    val items = remember { mutableStateListOf("Item 1", "Item 2", "Item 3") }
    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        items.forEach { item -> Text(text = item, Modifier.padding(4.dp)) }
        Button(onClick = { items.add("New Item") }) { Text("Add Item") }
    }
}

// 6. Map State
@Composable
fun MapStateExample() {
    val settings = remember { mutableStateMapOf<String, Boolean>("darkMode" to false, "anotherSetting" to true) }
    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        settings.forEach { (key, value) -> Text("$key: $value", Modifier.padding(4.dp)) }
        Button(onClick = {
            val currentDarkMode = settings["darkMode"] ?: false
            settings["darkMode"] = !currentDarkMode
        }) {
            Text("Toggle Dark Mode")
        }

    }
}

// 7. Set State
@Composable
fun SetStateExample() {
    val items = remember { mutableStateOf(setOf("Item1", "Item2")) }

    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        items.value.forEach { item ->
            Text(text = item, Modifier.padding(4.dp))
        }
        Button(onClick = {
            items.value = items.value + "Item${items.value.size + 1}"
        }) {
            Text("Add Item")
        }
    }
}



// 8. SnapshotFlow
@Composable
fun SnapshotFlowExample() {
    var count by remember { mutableStateOf(0) }
    LaunchedEffect(Unit) {
        snapshotFlow { count }.distinctUntilChanged().collect {
            println("SnapshotFlow - Count: $it")
        }
    }
    StateExampleTemplate("SnapshotFlow", "Count: $count", { count++ })
}

@Composable
fun StateExampleTemplate(title: String, description: String, onIncrement: () -> Unit) {
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = title, style = MaterialTheme.typography.titleLarge)
        Text(text = description, Modifier.padding(16.dp))
        Button(onClick = onIncrement) { Text("Increment") }
    }
}

/* ======================================================
    LAB: Number Guessing Game
 */
class GameViewModel : ViewModel() {
    var totalWins by mutableStateOf(0)
        private set

    fun incrementWins() { totalWins++ }
}
@Composable
fun Lab(viewModel: GameViewModel = viewModel()) {
    var guess by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("Guess a number between 1 and 20") }
    var targetNumber by rememberSaveable { mutableStateOf(Random.nextInt(1, 21)) }
    var attempts by rememberSaveable { mutableStateOf(0) }

    Column(
        Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = message, style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = guess,
            onValueChange = { guess = it.filter { char -> char.isDigit() } },
            label = { Text("Your Guess") }
        )
        Spacer(Modifier.height(16.dp))
        Button(onClick = {
            attempts++
            val guessedNumber = guess.toIntOrNull()
            message = when {
                guessedNumber == null -> "Enter a valid number"
                guessedNumber > targetNumber -> "Too high!"
                guessedNumber < targetNumber -> "Too low!"
                else -> {
                    viewModel.incrementWins()
                    targetNumber = Random.nextInt(1, 21)
                    val resultMessage = "Correct! Total Wins: ${viewModel.totalWins}. Attempts: $attempts. New number set!"
                    attempts = 0
                    resultMessage
                }
            }
            guess = ""
        }) {
            Text("Guess")
        }
    }
}

/* ======================================================
Computer GO
 */
private const val BOARD_SIZE = 9

enum class Stone { BLACK, WHITE }

val stoneSaver = listSaver<MutableList<Stone?>, String?>(
    save = { list -> list.map { it?.name } },
    restore = { saved -> saved.map { it?.let { Stone.valueOf(it) } }.toMutableStateList() }
)

@Composable
fun ComputerGoGame() {
    val board = rememberSaveable(saver = stoneSaver) {
        mutableStateListOf<Stone?>().apply { repeat(BOARD_SIZE * BOARD_SIZE) { add(null) } }
    }
    var currentPlayer by rememberSaveable { mutableStateOf(Stone.BLACK) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Computer Go - 9x9", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(16.dp))

        BoxWithConstraints(modifier = Modifier.size(320.dp)) {
            val boxSizePx = with(LocalDensity.current) { maxWidth.toPx() }
            val cellSizePx = boxSizePx / BOARD_SIZE

            Canvas(modifier = Modifier.fillMaxSize()) {
                val cellSize = size.width / BOARD_SIZE

                // Draw board grid
                for (i in 0..BOARD_SIZE) {
                    drawLine(
                        Color.Black,
                        Offset(i * cellSize, 0f),
                        Offset(i * cellSize, size.height)
                    )
                    drawLine(
                        Color.Black,
                        Offset(0f, i * cellSize),
                        Offset(size.width, i * cellSize)
                    )
                }

                // Draw stones
                board.forEachIndexed { index, stone ->
                    stone?.let {
                        val x = (index % BOARD_SIZE) * cellSize + cellSize / 2
                        val y = (index / BOARD_SIZE) * cellSize + cellSize / 2
                        drawCircle(
                            color = if (it == Stone.BLACK) Color.Black else Color.Red,
                            radius = cellSize * 0.4f,
                            center = Offset(x, y)
                        )
                    }
                }
            }

            Box(
                Modifier
                    .matchParentSize()
                    .pointerInput(Unit) {
                        detectTapGestures { offset ->
                            val x = (offset.x / cellSizePx).toInt()
                            val y = (offset.y / cellSizePx).toInt()

                            if (x in 0 until BOARD_SIZE && y in 0 until BOARD_SIZE) {
                                val index = y * BOARD_SIZE + x
                                if (board[index] == null) {
                                    board[index] = currentPlayer
                                    currentPlayer = if (currentPlayer == Stone.BLACK) Stone.WHITE else Stone.BLACK
                                }
                            }
                        }
                    }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Current Player: ${currentPlayer.name}", style = MaterialTheme.typography.bodyLarge)
    }
}

/* ======================================================
Preview
 */
@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MaterialTheme {
        MainScreen()
    }
}