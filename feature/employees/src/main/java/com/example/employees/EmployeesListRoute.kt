package com.example.employees

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.coerceAtMost
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppGradient
import com.example.ui.InterFontFamily
import com.example.ui.KodeStaffTheme


// для тестирования
private const val QUERY_TEST = "er"
private const val DEPARTMENT_TEST = "android"

@Composable
fun EmployeesScreen(
    modifier: Modifier = Modifier,
//    viewModel: EmployeesViewModel = hiltViewModel(),
    onEmployeeClick: () -> Unit = {},
) {
    var isRefreshing by remember { mutableStateOf(false) }
    val pullRefreshState = rememberPullToRefreshState()

    val cardOffset by animateDpAsState(
        targetValue = when {
            isRefreshing -> 70.dp
            pullRefreshState.distanceFraction > 0f -> (70.dp * pullRefreshState.distanceFraction).coerceAtMost(
                80.dp
            )

            else -> 0.dp
        },
        label = "cardOffset"
    )

    Column(
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.systemBars)
            .padding()

    ) {
        SearchBar(onQueryChanged = {})
        RoleTabs(onTabClick = {})
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .background(brush = AppGradient.PrimaryGradient)
                .padding(top = 22.dp, start = 24.dp)
        ) {
            Text(
                text = "Designers",
                fontSize = 24.sp,
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.Bold
            )
        }

        PullToRefreshBox(
            state = pullRefreshState,
            isRefreshing = isRefreshing,
            onRefresh = {
                isRefreshing = true
            },
            modifier = modifier.windowInsetsPadding(WindowInsets.systemBars)
        ) {
            LazyColumn(Modifier.fillMaxSize()) {
                items(20) {
                    PlugListItem(modifier = Modifier.offset(y = cardOffset))
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    KodeStaffTheme {
        EmployeesScreen()
    }
}


@Composable
private fun SearchBar(
    modifier: Modifier = Modifier,
    query: String = "Введи имя, тег, почту...",
    onQueryChanged: (String) -> Unit
) {
    TextField(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 6.dp, start = 16.dp, end = 16.dp)
            .height(50.dp)
            .shadow(0.dp),
        placeholder = {
            Text(
                text = "Введи имя, тег, почту...",
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSecondary,
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.Normal
            )
        },
        value = query,
        onValueChange = onQueryChanged,
        leadingIcon = {
            Icon(
                painter = painterResource(R.drawable.ic_search),
                contentDescription = "Search icon",
                Modifier.clickable(enabled = true, onClick = {}),
                tint = MaterialTheme.colorScheme.onSecondary

            )
        },
        trailingIcon = {
            Icon(
                painter = painterResource(R.drawable.ic_sort),
                contentDescription = "Sort icon",
                Modifier.clickable(enabled = true, onClick = {}),
                tint = MaterialTheme.colorScheme.onSecondary
            )
        },
        shape = RoundedCornerShape(16.dp),
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = MaterialTheme.colorScheme.secondary,
            unfocusedTextColor = MaterialTheme.colorScheme.onSecondary,
            focusedContainerColor = MaterialTheme.colorScheme.secondary,
            focusedTextColor = MaterialTheme.colorScheme.onSecondary,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
        )
    )
}

@Composable
private fun RoleTabs(
    modifier: Modifier = Modifier,
    onTabClick: () -> Unit
) {

    var state by remember { mutableIntStateOf(0) }
    val titles =
        listOf(
            "Tab 1",
            "Tab 2",
            "Tab 3 with lots of text",
            "Tab 4",
            "Tab 5",
            "Tab 6 with lots of text",
            "Tab 7",
            "Tab 8",
            "Tab 9 with lots of text",
            "Tab 10",
        )
    Column {
        PrimaryScrollableTabRow(
            selectedTabIndex = state,
            edgePadding = 16.dp,
            minTabWidth = 0.dp,
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.primary,
            indicator = {
                TabRowDefaults.PrimaryIndicator(
                    Modifier.tabIndicatorOffset(state, matchContentSize = true),
                    color = MaterialTheme.colorScheme.primaryFixed,
                    width = Dp.Unspecified
                )
            }
        ) {
            titles.forEachIndexed { index, title ->
                Tab(
                    modifier = Modifier.padding(),
                    selected = state == index,
                    onClick = { state = index },
                    text = {
                        Text(
                            title,
                            fontSize = 15.sp,
                            fontFamily = InterFontFamily,
                            fontWeight = FontWeight.Normal
                        )
                    },
                    selectedContentColor = MaterialTheme.colorScheme.onSurface,
                    unselectedContentColor = MaterialTheme.colorScheme.tertiary
                )
            }
        }
    }
}

@Composable
fun PlugListItem(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(84.dp)
            .padding(top = 6.dp, start = 16.dp, bottom = 6.dp, end = 16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .background(
                    brush = AppGradient.PrimaryGradient,
                    shape = CircleShape
                )
                .align(Alignment.CenterStart)
        )

        Column(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .fillMaxHeight()
                .padding(start = 88.dp), // 72dp + 16dp
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .height(16.dp)
                    .width(144.dp)
                    .background(
                        brush = AppGradient.PrimaryGradient,
                        shape = RoundedCornerShape(50.dp)
                    )
            )
            Spacer(modifier = Modifier.height(6.dp))
            Box(
                modifier = Modifier
                    .height(12.dp)
                    .width(80.dp)
                    .background(
                        brush = AppGradient.PrimaryGradient,
                        shape = RoundedCornerShape(50.dp)
                    )
            )
        }
    }
}

