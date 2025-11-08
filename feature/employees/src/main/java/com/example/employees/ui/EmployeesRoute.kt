package com.example.employees.ui

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.coerceAtMost
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.data.models.Department
import com.example.data.models.SortType
import com.example.employees.EmployeesViewModel
import com.example.employees.R
import com.example.employees.models.EmployeeUi
import com.example.ui.AppGradient
import com.example.ui.InterFontFamily
import com.example.ui.KodeStaffTheme


@Composable
fun EmployeesScreen(
    modifier: Modifier = Modifier,
    viewModel: EmployeesViewModel = hiltViewModel(),
    onEmployeeClick: () -> Unit = {},
) {
    val state by viewModel.state.collectAsState()

    when (val currentState = state) {
        is EmployeesScreenState.Initial -> {
            InitialScreen(
                modifier = modifier
            )
        }

        is EmployeesScreenState.Employees -> {
            EmployeesList(
                modifier = modifier,
                state = currentState,
                onPullToRefresh = { viewModel.handleIntent(EmployeesIntent.Refresh) },
                onDepartmentTabClick = { department ->
                    viewModel.handleIntent(
                        EmployeesIntent.DepartmentSelected(
                            department
                        )
                    )
                },
                onQueryChanged = { query ->
                    viewModel.handleIntent(EmployeesIntent.SearchQueryChanged(query))
                },
                onSortButtonClick = {
                    viewModel.handleIntent(EmployeesIntent.ShowSortBottomSheet)
                }
            )

            SortBottomSheet(
                visible = currentState.isSortBottomSheetVisible,
                currentSort = currentState.selectedSort,
                onSortSelected = { sortType ->
                    viewModel.handleIntent(EmployeesIntent.SortTypeSelected(sortType))
                    viewModel.handleIntent(EmployeesIntent.HideSortBottomSheet)
                },
                onDismiss = { viewModel.handleIntent(EmployeesIntent.HideSortBottomSheet) }
            )
        }

        is EmployeesScreenState.Error ->
            ErrorScreen(
                modifier = modifier,
                onRetryClick = { viewModel.handleIntent(EmployeesIntent.Refresh) }
            )

    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    KodeStaffTheme {
//        EmployeeItem(
//            employee = mockEmployees[0]
//        )
    }
}

@Composable
private fun EmployeesList(
    modifier: Modifier = Modifier,
    state: EmployeesScreenState.Employees,
    onPullToRefresh: () -> Unit,
    onQueryChanged: (String) -> Unit,
    onDepartmentTabClick: (department: Department?) -> Unit,
    onSortButtonClick: () -> Unit
) {
    val pullRefreshState = rememberPullToRefreshState()

    val cardOffset by animateDpAsState(
        targetValue = when {
            state.isLoading -> 70.dp
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
        SearchBar(
            onQueryChanged = onQueryChanged,
            query = state.searchQuery,
            onSortButtonClick = onSortButtonClick
        )
        NavigationBarDepartmentTabs(
            onTabClick = onDepartmentTabClick,
            selectedDepartment = state.selectedDepartment
        )
        PullToRefreshBox(
            state = pullRefreshState,
            isRefreshing = state.isLoading,
            onRefresh = onPullToRefresh,
            modifier = modifier.windowInsetsPadding(WindowInsets.systemBars)
        ) {
            if (state.employees.isEmpty()) {
                NoSearchResultPlaceHolder()
            } else {
                LazyColumn(
                    Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {
                    items(
                        items = state.employees,
                        key = { it.id }
                    ) {
                        EmployeeItem(
                            modifier = Modifier.offset(y = cardOffset),
                            employee = it,
                            isDateVisible = state.selectedSort == SortType.BIRTHDAY
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun InitialScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .windowInsetsPadding(WindowInsets.systemBars)
            .padding()

    ) {
        SearchBar(onQueryChanged = {}, query = "", onSortButtonClick = {})
        NavigationBarDepartmentTabs(
            onTabClick = {},
            selectedDepartment = null
        )
        DesignersPlaceholder()
        LazyColumn(Modifier.fillMaxSize()) {
            items(20) {
                EmployeeItemPlaceHolder()
            }
        }
    }
}


@Composable
private fun DesignersPlaceholder(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
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
}

@Composable
private fun SearchBar(
    modifier: Modifier = Modifier,
    query: String,
    onQueryChanged: (String) -> Unit,
    onSortButtonClick: () -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 6.dp, start = 16.dp, end = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            modifier = modifier
                .weight(1f)
                .height(50.dp)
                .shadow(0.dp)
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                },
            placeholder = {
                if (!isFocused) {
                    Text(
                        text = "Введи имя, тег, почту...",
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSecondary,
                        fontFamily = InterFontFamily,
                        fontWeight = FontWeight.Normal
                    )
                }
            },
            value = query,
            onValueChange = { onQueryChanged(it) },
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.ic_search),
                    contentDescription = "Search icon",
                    Modifier.clickable(enabled = true, onClick = {})
                )
            },
            trailingIcon = {
                if (isFocused && query.isNotEmpty()) {
                    Icon(
                        painter = painterResource(com.example.ui.R.drawable.ic_x),
                        contentDescription = "Clear search",
                        Modifier.clickable { onQueryChanged("") },
                        tint = MaterialTheme.colorScheme.onSecondary
                    )
                } else if (isFocused) {
                    Box(Modifier.size(24.dp))
                } else {
                    Icon(
                        painter = painterResource(R.drawable.ic_sort),
                        contentDescription = "Sort icon",
                        Modifier.clickable(
                            enabled = true,
                            onClick = onSortButtonClick
                        )
                    )
                }
            },
            shape = RoundedCornerShape(16.dp),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.secondary,
                focusedContainerColor = MaterialTheme.colorScheme.secondary,
                unfocusedTextColor = MaterialTheme.colorScheme.onSecondary,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedLeadingIconColor = MaterialTheme.colorScheme.onSecondary,
                focusedLeadingIconColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTrailingIconColor = MaterialTheme.colorScheme.onSecondary,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
            )
        )
        if (isFocused) {
            val focusManager = LocalFocusManager.current
            Text(
                text = "Отмена",
                modifier = Modifier
                    .padding(start = 8.dp)
                    .clickable {
                        focusManager.clearFocus()
                        onQueryChanged("")
                    },
                color = MaterialTheme.colorScheme.primaryFixed,
                fontSize = 14.sp,
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun NavigationBarDepartmentTabs(
    modifier: Modifier = Modifier,
    selectedDepartment: Department?,
    onTabClick: (department: Department?) -> Unit
) {
    fun getSelectedTabIndex(selectedDepartment: Department?): Int {
        return if (selectedDepartment == null) {
            0
        } else {
            Department.entries.indexOf(selectedDepartment) + 1
        }
    }

    val selectedTabIndex = getSelectedTabIndex(selectedDepartment)

    Column {
        PrimaryScrollableTabRow(
            modifier = modifier,
            selectedTabIndex = selectedTabIndex,
            edgePadding = 16.dp,
            minTabWidth = 0.dp,
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.primary,
            indicator = {
                TabRowDefaults.PrimaryIndicator(
                    Modifier.tabIndicatorOffset(selectedTabIndex, matchContentSize = true),
                    color = MaterialTheme.colorScheme.primaryFixed,
                    width = Dp.Unspecified
                )
            }
        ) {
            DepartmentTab(
                selected = selectedDepartment == null,
                selectedDepartment = null,
                onTabClick = onTabClick
            )
            Department.entries.forEachIndexed { index, department ->
                DepartmentTab(
                    selected = department == selectedDepartment,
                    selectedDepartment = department,
                    onTabClick = onTabClick
                )
            }
        }
    }
}

@Composable
fun DepartmentTab(
    modifier: Modifier = Modifier,
    selectedDepartment: Department?,
    selected: Boolean,
    onTabClick: (department: Department?) -> Unit
) {
    Tab(
        modifier = modifier.padding(),
        selected = selected,
        onClick = { onTabClick(selectedDepartment) },
        text = {
            Text(
                text = selectedDepartment?.stringName ?: "Все",
                fontSize = 15.sp,
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.Normal
            )
        },
        selectedContentColor = MaterialTheme.colorScheme.onSurface,
        unselectedContentColor = MaterialTheme.colorScheme.tertiary
    )
}

@Composable
fun EmployeeItem(
    modifier: Modifier = Modifier,
    employee: EmployeeUi,
    isDateVisible: Boolean = false
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(top = 6.dp, bottom = 6.dp, end = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(shape = CircleShape)
                .background(MaterialTheme.colorScheme.primaryFixed)
                .align(Alignment.CenterStart)

        ) {
            Image(
                painterResource(com.example.ui.R.drawable.avatar_placeholder),
                contentDescription = "Avatar",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .fillMaxHeight()
                .padding(start = 88.dp), // 72dp + 16dp
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier
                    .height(26.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "${employee.firstName} ${employee.lastName}",
                    fontSize = 16.sp,
                    fontFamily = InterFontFamily,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = employee.userTag,
                    modifier = Modifier.padding(start = 4.dp),
                    fontSize = 14.sp,
                    fontFamily = InterFontFamily,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
            Text(
                text = employee.department.stringName,
                fontSize = 13.sp,
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        if (isDateVisible) {
            Text(
                text = employee.birthday,
                fontSize = 15.sp,
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
            )
        }
    }
}

@Composable
fun EmployeeItemPlaceHolder(
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

@Composable
fun NoSearchResultPlaceHolder(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 80.dp, start = 16.dp, end = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painterResource(R.drawable.ic_no_result_search),
                contentDescription = "No results",
                modifier = Modifier.size(56.dp),
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Мы никого не нашли",
                fontSize = 17.sp,
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Попробуйте скорректировать запрос",
                fontSize = 16.sp,
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.tertiary
            )
        }
    }
}

@Composable
fun ErrorScreen(
    modifier: Modifier = Modifier,
    onRetryClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painterResource(com.example.ui.R.drawable.ic_flying_saucer),
            contentDescription = "Error",
            modifier = Modifier.size(56.dp),
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Какой-то сверхразум всё сломал",
            fontSize = 17.sp,
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text = "Постараемся быстро починить",
            fontSize = 16.sp,
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.tertiary
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text = "Попробовать снова",
            fontSize = 16.sp,
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primaryFixed,
            modifier = Modifier.clickable {
                onRetryClick()
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortBottomSheet(
    modifier: Modifier = Modifier,
    visible: Boolean,
    currentSort: SortType,
    onSortSelected: (SortType) -> Unit,
    onDismiss: () -> Unit
) {
    if (!visible) return
    ModalBottomSheet(
        modifier = modifier
            .padding(start = 8.dp, end = 8.dp),
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        containerColor = Color.White
    ) {
        Column {
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = "Сортировка",
                fontSize = 20.sp,
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )

            val sortOptions = listOf(
                SortType.ALPHABETIC to "По алфавиту",
                SortType.BIRTHDAY to "По дню рождения"
            )

            sortOptions.forEach { (sortType, label) ->
                val selected = currentSort == sortType
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .clickable { onSortSelected(sortType) },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selected,
                        onClick = { onSortSelected(sortType) }
                    )
                    Text(
                        text = label,
                        fontSize = 16.sp,
                        fontFamily = InterFontFamily,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}
