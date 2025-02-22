package com.example.advancedlayouts


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.advancedlayouts.ui.theme.ComposeMasterTheme
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeMasterTheme {
                DemoApp( )

            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DemoApp(modifier: Modifier = Modifier) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Form", "List", "Grid", "Constraint", "Lab")

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Advanced Layouts and Navigation") })
        },
        bottomBar = {
            NavigationBar {
                tabs.forEachIndexed { index, label ->
                    NavigationBarItem(
                        selected = (selectedTab == index),
                        onClick = { selectedTab = index },
                        label = { Text(label) },
                        icon = {}
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (selectedTab) {
                0 -> FormElementsDemo()
                1 -> ListDemo()
                2 -> GridDemo()
                3 -> ConstraintLayoutExample()
                4 -> Lab()
            }
        }
    }
}

// --------------------------------------------------
// 1. Common UI Elements
// --------------------------------------------------
@Composable
fun FormElementsDemo() {
    var text by remember { mutableStateOf("") }
    var checked by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("Option A") }
    var isSwitched by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(16.dp)) {
        // TextField
        TextField(
            value = text,
            onValueChange = { newValue: String -> text = newValue },
            label = { Text("Enter text") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        // Checkbox
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = checked,
                onCheckedChange = { isChecked -> checked = isChecked }
            )
            Text("Check me!", modifier = Modifier.padding(start = 8.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Radio Buttons
        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(
                selected = (selectedOption == "Option A"),
                onClick = { selectedOption = "Option A" }
            )
            Text("Option A", modifier = Modifier.padding(start = 4.dp))

            Spacer(modifier = Modifier.width(16.dp))

            RadioButton(
                selected = (selectedOption == "Option B"),
                onClick = { selectedOption = "Option B" }
            )
            Text("Option B", modifier = Modifier.padding(start = 4.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Switch
        Row(verticalAlignment = Alignment.CenterVertically) {
            Switch(
                checked = isSwitched,
                onCheckedChange = { toggled -> isSwitched = toggled }
            )
            Text("Switch me!", modifier = Modifier.padding(start = 8.dp))
        }
    }
}

// --------------------------------------------------
// 2. Lists – LazyColumn & LazyRow
// --------------------------------------------------
@Composable
fun ListDemo() {
    // We'll show both a LazyColumn and a LazyRow for completeness.
    val items = listOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5")

    Column {
        Text(
            text = "LazyColumn Example",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(16.dp)
        )
        LazyColumn(modifier = Modifier.height(200.dp)) {
            items(items.size) { index ->
                Text(
                    text = items[index],
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)


                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "LazyRow Example",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(16.dp)
        )
        LazyRow (modifier=Modifier.height(200.dp)) {
            items(items.size) { index ->
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
                        .size(width = 120.dp, height = 80.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(items[index])
                }
            }
        }
    }
}

// --------------------------------------------------
// 3. Grids – LazyVerticalGrid
// --------------------------------------------------
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GridDemo() {
    val gridItems = (1..20).map { "Grid Item $it" }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .padding(8.dp)
    ) {
        items(gridItems.size) { index ->
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
                    .fillMaxSize()
                    .aspectRatio(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(gridItems[index])
            }
        }
    }
}

// --------------------------------------------------
// 4. Advanced Layouts with ConstraintLayout
// --------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConstraintLayoutExample() {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (title, button) = createRefs()

        Text(
            text = "ConstraintLayout Title",
            modifier = Modifier
                .constrainAs(title) {
                    top.linkTo(parent.top, margin = 16.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )

        Button(
            onClick = { /* TODO: Add your action */ },
            modifier = Modifier.constrainAs(button) {
                bottom.linkTo(parent.bottom, margin = 16.dp)
                end.linkTo(parent.end, margin = 16.dp)
            }
        ) {
            Text("Click Me")
        }
    }
}

// --------------------------------------------------
// 4. Advanced Layouts with ConstraintLayout
// --------------------------------------------------

//@Composable
//fun Lab() {
//    Column(modifier = Modifier.fillMaxSize()) {
//        Text(
//            text = "Lab: Image Grid",
//            style = MaterialTheme.typography.titleMedium,
//            modifier = Modifier.padding(16.dp)
//        )
//
//        // Sample image data (replace with your actual image resources)
//        val imageResources = listOf(
//            R.drawable.ic_launcher_foreground,
//            R.drawable.ic_launcher_background,
//            R.drawable.ic_launcher_foreground,
//            R.drawable.ic_launcher_background,
//            R.drawable.ic_launcher_foreground,
//            R.drawable.ic_launcher_background,
//            R.drawable.ic_launcher_foreground,
//            R.drawable.ic_launcher_background,
//            R.drawable.ic_launcher_foreground,
//            R.drawable.ic_launcher_background,
//            R.drawable.ic_launcher_foreground,
//            R.drawable.ic_launcher_background,
//            R.drawable.ic_launcher_foreground,
//            R.drawable.ic_launcher_background,
//            R.drawable.ic_launcher_foreground,
//            R.drawable.ic_launcher_background,
//            R.drawable.ic_launcher_foreground,
//            R.drawable.ic_launcher_background,
//            R.drawable.ic_launcher_foreground,
//            R.drawable.ic_launcher_background,
//            R.drawable.ic_launcher_foreground,
//            R.drawable.ic_launcher_background,
//            R.drawable.ic_launcher_foreground,
//            R.drawable.ic_launcher_background,
//            R.drawable.ic_launcher_foreground,
//            R.drawable.ic_launcher_background,
//            R.drawable.ic_launcher_foreground,
//            R.drawable.ic_launcher_background,
//            R.drawable.ic_launcher_foreground,
//            R.drawable.ic_launcher_background,
//            R.drawable.ic_launcher_foreground,
//            R.drawable.ic_launcher_background
//        )
//        // State to track which image is selected
//        var selectedImageIndex by remember { mutableStateOf(-1) }
//
//        LazyVerticalGrid(
//            columns = GridCells.Fixed(3),
//            modifier = Modifier.fillMaxSize(),
//            contentPadding = PaddingValues(8.dp)
//        ) {
//            items(imageResources.size) { index ->
//                val isSelected = selectedImageIndex == index
//                Box(
//                    modifier = Modifier
//                        .padding(8.dp)
//                        .aspectRatio(1f)
//                        .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
//                        .border(
//                            width = if (isSelected) 4.dp else 0.dp,
//                            color = if (isSelected) Color.Blue else Color.Transparent,
//                            shape = RoundedCornerShape(8.dp)
//                        )
//                        .clickable {
//                            selectedImageIndex = if (isSelected) -1 else index
//                        },
//                    contentAlignment = Alignment.Center
//                ) {
//                    Image(
//                        painter = painterResource(id = imageResources[index]),
//                        contentDescription = "Grid Image",
//                        modifier = Modifier.fillMaxSize()
//                    )
//                }
//            }
//        }
//    }
//}

enum class CellState {
    EMPTY, X, O
}

@Composable
fun Lab() {
    var board by remember { mutableStateOf(Array(9) { CellState.EMPTY }) }
    var currentPlayer by remember { mutableStateOf(CellState.X) }
    var winner by remember { mutableStateOf<CellState?>(null) }
    var isDraw by remember { mutableStateOf(false) }

    fun checkWin(): CellState? {
        val winPatterns = listOf(
            listOf(0, 1, 2), listOf(3, 4, 5), listOf(6, 7, 8), // Rows
            listOf(0, 3, 6), listOf(1, 4, 7), listOf(2, 5, 8), // Columns
            listOf(0, 4, 8), listOf(2, 4, 6) // Diagonals
        )

        for (pattern in winPatterns) {
            val (a, b, c) = pattern
            if (board[a] != CellState.EMPTY && board[a] == board[b] && board[a] == board[c]) {
                return board[a]
            }
        }
        return null
    }

    fun checkDraw(): Boolean {
        return board.all { it != CellState.EMPTY }
    }

    fun computerMove() {
        val emptyCells = board.indices.filter { board[it] == CellState.EMPTY }
        if (emptyCells.isNotEmpty()) {
            val randomIndex = Random.nextInt(emptyCells.size)
            val computerMoveIndex = emptyCells[randomIndex]
            val newBoard = board.copyOf()
            newBoard[computerMoveIndex] = CellState.O
            board = newBoard
            winner = checkWin()
            isDraw = checkDraw()
            currentPlayer = CellState.X
        }
    }

    fun handleCellClick(index: Int) {
        if (board[index] == CellState.EMPTY && winner == null && !isDraw) {
            val newBoard = board.copyOf()
            newBoard[index] = currentPlayer
            board = newBoard
            winner = checkWin()
            isDraw = checkDraw()
            currentPlayer = if (currentPlayer == CellState.X) CellState.O else CellState.X
            if (currentPlayer == CellState.O && winner == null && !isDraw) {
                computerMove()
            }
        }
    }


    fun resetGame() {
        board = Array(9) { CellState.EMPTY }
        currentPlayer = CellState.X
        winner = null
        isDraw = false
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Tic-Tac-Toe",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(16.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(9) { index ->
                val cellState = board[index]
                val cellCharacter = when (cellState) {
                    CellState.X -> "X"
                    CellState.O -> "O"
                    else -> " "
                }

                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .aspectRatio(1f)
                        .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
                        .border(2.dp, Color.Black, RoundedCornerShape(8.dp))
                        .clickable { handleCellClick(index) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = cellCharacter,
                        style = TextStyle(fontSize = 60.sp, textAlign = TextAlign.Center),
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }

        if (winner != null) {
            Text(
                text = "${winner!!.name} wins!",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(16.dp)
            )
        } else if (isDraw) {
            Text(
                text = "It's a draw!",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(16.dp)
            )
        }

        Button(
            onClick = { resetGame() },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Reset Game")
        }
    }
}

// --------------------------------------------------
// 5. Main Activity or Preview
// --------------------------------------------------
@Composable
@Preview(showSystemUi = true, showBackground = true)
fun DemoAppPreview() {
    MaterialTheme {
        DemoApp()
    }
}
