package com.example.mednotes.ui.schedule

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mednotes.data.ScheduleData
import com.example.mednotes.data.ScheduleMomentData
import com.example.mednotes.ui.theme.MomentSelectedBgColor
import org.koin.androidx.compose.getViewModel

@Preview
@Composable
fun PreviewScheduleViewComponent() {
    Box(modifier = Modifier.background(Color.White)) {
        ScheduleViewComponent()
    }
}

@Composable
fun ScheduleViewComponent(
    viewModel: ScheduleViewModel = getViewModel()
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
    val medicines by remember { mutableStateOf(data.withMedicines)}
    val backgroundColor = if (isSelected) MomentSelectedBgColor else Color.White
    val contentColor = if (isSelected) Color.White else Color.Black

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp)
            .background(backgroundColor, MaterialTheme.shapes.small)
            .padding(horizontal = 16.dp)
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
                    medicines.map{it.isTaken = isSelected}
                    viewModel.updateAllMedicines(data, isTaken = isSelected)
                }
            )
        }
    }
}
