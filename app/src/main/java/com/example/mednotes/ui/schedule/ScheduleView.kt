package com.example.mednotes.ui.schedule

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mednotes.data.*
import com.example.mednotes.ui.theme.MomentSelectedBgColor
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import org.koin.androidx.compose.getViewModel

@Preview
@Composable
fun PreviewScheduleViewComponent() {
    Box(modifier = Modifier.background(Color.White)) {
        ScheduleViewComponent()
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ScheduleViewComponent(
    viewModel: ScheduleViewModel = getViewModel()
) {
    val pagerState = rememberPagerState(
        pageCount = 2,
        initialOffscreenLimit = 2,
    )
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {

        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp),
            activeColor = Color.Black,
            inactiveColor = Color.LightGray
        )

        HorizontalPager(state = pagerState) { page ->
            when (page) {
                0 -> ScheduleMomentsView(viewModel)
                1 -> ScheduleMedicinesView(viewModel)
            }
        }
    }
}

//region Weekly overview
@Composable
fun ScheduleMedicinesView(
    viewModel: ScheduleViewModel
) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 24.dp, horizontal = 16.dp)
    ) {
        items(viewModel.scheduleWeeklyData) { weeklyData ->
            ScheduleMomentsWeeklyView(scheduleWeeklyData = weeklyData)
        }
    }
}

@Composable
fun ScheduleMomentsWeeklyView(
    scheduleWeeklyData: ScheduleWeeklyData
) {
    Text(
        text = scheduleWeeklyData.timeLabel,
        style = MaterialTheme.typography.h6,
        color = Color.Black
    )

    Spacer(modifier = Modifier.height(24.dp))

    scheduleWeeklyData.data.forEach { data ->
        ScheduleMedicineItemView(data = data)
        Spacer(modifier = Modifier.height(8.dp))
    }

    Spacer(modifier = Modifier.height(24.dp))
}

@Composable
fun ScheduleMedicineItemView(
    data: ScheduleWeeklyMedicineData,
) {
    var isCollapsible by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp)
            .background(Color.LightGray, MaterialTheme.shapes.small)
            .padding(horizontal = 16.dp)
            .clickable {
                isCollapsible = !isCollapsible
            },
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = data.medicineName,
            style = MaterialTheme.typography.subtitle1,
            color = Color.Black,
            modifier = Modifier.padding(start = 16.dp)
        )
    }

    if (isCollapsible) {
        showMomentsCollapsibleView(data = data.weeklyMomentData)
    }
}

@Composable
fun showMomentsCollapsibleView(
    data: List<ScheduleWeeklyMomentData>,
) {
    data.forEach { momentData ->

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .background(Color.White, MaterialTheme.shapes.small)
                .padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly

        ) {

            Text(
                text = momentData.day,
                style = MaterialTheme.typography.caption,
                color = Color.Black
            )

            Text(
                text = momentData.time,
                style = MaterialTheme.typography.caption,
                color = Color.Black
            )

            Text(
                text = momentData.momentTitle,
                style = MaterialTheme.typography.caption,
                color = Color.Black
            )

            val icon = if (momentData.isTaken) Icons.Default.Check else Icons.Default.Close
            val colorFilter = ColorFilter.tint(
                if (momentData.isTaken) Color.Green else Color.Red
            )
            Image(
                imageVector = icon,
                colorFilter = colorFilter,
                contentDescription = null
            )
        }

        Divider()
    }

    Spacer(modifier = Modifier.height(8.dp))
}

// endregion


// region Monthly overview
@Composable
fun ScheduleMomentsView(
    viewModel: ScheduleViewModel
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 24.dp, horizontal = 16.dp)
    ) {
        items(viewModel.scheduleData) { scheduleData ->
            ScheduleMomentsGroupView(data = scheduleData, viewModel)
        }
    }
}

@Composable
fun ScheduleMomentsGroupView(
    data: ScheduleData,
    viewModel: ScheduleViewModel
) {
    Text(
        text = data.label,
        style = MaterialTheme.typography.h6,
        color = Color.Black
    )

    Spacer(modifier = Modifier.height(24.dp))

    data.moments.forEach { moment ->
        ScheduleMomentItemView(moment, viewModel)
        Spacer(modifier = Modifier.height(8.dp))
    }

    Spacer(modifier = Modifier.height(24.dp))
}

@Composable
fun ScheduleMomentItemView(
    data: ScheduleMomentData,
    viewModel: ScheduleViewModel
) {
    var isSelected by remember { mutableStateOf(data.withMedicines.all { it.isTaken }) }
    val medicines by remember { mutableStateOf(data.withMedicines) }
    val backgroundColor = if (isSelected) MomentSelectedBgColor else Color.White
    val contentColor = if (isSelected) Color.White else Color.Black
    var isCollapsible by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp)
            .background(backgroundColor, MaterialTheme.shapes.small)
            .padding(horizontal = 16.dp)
            .clickable {
                isCollapsible = !isCollapsible
            }
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                painter = painterResource(id = data.iconResource),
                contentDescription = "",
                modifier = Modifier.size(24.dp),
                colorFilter = ColorFilter.tint(contentColor)
            )

            Column {
                Text(
                    text = data.title,
                    style = MaterialTheme.typography.subtitle1,
                    color = contentColor,
                    modifier = Modifier.padding(start = 16.dp)
                )
                Text(
                    text = data.timeLabel,
                    style = MaterialTheme.typography.caption,
                    color = contentColor,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = isSelected,
                onClick = {
                    isSelected = !isSelected
                    medicines.map { it.isTaken = isSelected }
                    viewModel.updateAllMedicines(data, isTaken = isSelected)
                }
            )
        }
    }

    if (isCollapsible) {
        showMedicinesCollapsibleView(medicines, isSelected)
    }
}

@Composable
fun showMedicinesCollapsibleView(
    medicines: List<Medicine>,
    isSelected: Boolean,
) {
    medicines.forEach { medicine ->

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .background(Color.White, MaterialTheme.shapes.small)
                .padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween

        ) {

            Text(
                text = medicine.name,
                style = MaterialTheme.typography.subtitle1,
                color = Color.Black
            )

            RadioButton(
                selected = isSelected,
                enabled = false,
                onClick = {}
            )
        }
    }

    Spacer(modifier = Modifier.height(8.dp))
}

//endregion
