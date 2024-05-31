package com.example.myapplication.timesheet.presentation.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import com.example.myapplication.timesheet.presentation.DateFilterType
import com.example.myapplication.timesheet.presentation.components.datefilter.DateFilter

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SearchBar(
    isSearching: Boolean,
    searchText: String,
    subjects: List<String>,
    selectedFilter: DateFilterType,
    filterDateOptions: List<DateFilterType>,
    onSearchTextChanged: (String) -> Unit,
    onSearchToggled: () -> Unit,
    onSubjectSelected: (String) -> Unit,
    onSelectedFilterChanged: (DateFilterType) -> Unit
) {
    val focusRequester = remember { FocusRequester() }

    SearchBar(
        query = searchText,
        onQueryChange = onSearchTextChanged,
        onSearch = onSearchTextChanged,
        active = isSearching,
        onActiveChange = { onSearchToggled() },
        placeholder = { Text("Search") },
        leadingIcon = {
            SearchBarLeadingIconButton(
                isSearching = isSearching,
                selectedFilter = selectedFilter,
                searchText = searchText,
                onSearchToggled = onSearchToggled,
                onSearchTextChanged = onSearchTextChanged,
                onSelectedFilterChanged = onSelectedFilterChanged,
                onFocusRequested = { focusRequester.requestFocus() }
            )
        },
        trailingIcon = {
            SearchBarClearButton(
                searchText = searchText,
                onSearchTextChanged = onSearchTextChanged,
                onFocusRequested = { focusRequester.requestFocus() }
            )
        },
        modifier = Modifier
            .then(
                if (!isSearching) Modifier.padding(start = 12.dp, end = 12.dp) else Modifier
            )
            .fillMaxWidth()
            .animateContentSize(spring(stiffness = Spring.StiffnessHigh))
            .focusRequester(focusRequester)
    ) {
        DateFilter(
            selectedFilter = selectedFilter,
            filterDateOptions = filterDateOptions,
            onSelectedFilterChanged = onSelectedFilterChanged,
            onSearchToggled = onSearchToggled
        )
        if (subjects.isNotEmpty()) {
            LazyColumn {
                items(subjects) {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 8.dp,
                                top = 4.dp,
                                end = 8.dp,
                                bottom = 4.dp
                            )
                            .clickable {
                                onSubjectSelected(it)
                            }
                    )
                }
            }
        }
    }
}

@Composable
private fun SearchBarClearButton(
    searchText: String,
    onSearchTextChanged: (String) -> Unit,
    onFocusRequested: () -> Unit
) {
    if (searchText.isNotEmpty()) {
        IconButton(
            onClick = {
                onSearchTextChanged("")
                onFocusRequested()
            },
        ) {
            Icon(
                imageVector = Icons.Rounded.Clear,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Composable
private fun SearchBarLeadingIconButton(
    isSearching: Boolean,
    selectedFilter: DateFilterType,
    searchText: String,
    onSearchToggled: () -> Unit,
    onSearchTextChanged: (String) -> Unit,
    onSelectedFilterChanged: (DateFilterType) -> Unit,
    onFocusRequested: () -> Unit
) {
    if (isSearching || selectedFilter != DateFilterType.ALL || searchText.isNotEmpty()) {
        IconButton(
            onClick = {
                if (isSearching) {
                    onSearchToggled()
                } else {
                    onSearchTextChanged("")
                    onSelectedFilterChanged(DateFilterType.ALL)
                }
            },
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
            )
        }
    } else {
        IconButton(
            onClick = onFocusRequested,
        ) {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
