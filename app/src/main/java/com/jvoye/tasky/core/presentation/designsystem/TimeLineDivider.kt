package com.jvoye.tasky.core.presentation.designsystem

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun TimelineDivider(
    modifier: Modifier = Modifier,
    circleColor: Color = Color.Black,
    lineColor: Color = Color.Black,
    circleRadius: Dp = 5.dp,
    lineStrokeWidth: Dp = 2.dp,
    paddingStart: Dp = 0.dp,
    paddingEnd: Dp = 0.dp
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(circleRadius * 2) // Ensure enough height for the circle
            .padding(start = paddingStart, end = paddingEnd),
        contentAlignment = Alignment.CenterStart // Align the content to the start
    ) {
        // Draw the horizontal line
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(lineStrokeWidth)
                .align(Alignment.Center) // Center the line vertically
        ) {
            drawLine(
                color = lineColor,
                start = Offset(x = circleRadius.toPx(), y = size.height / 2),
                end = Offset(x = size.width, y = size.height / 2),
                strokeWidth = lineStrokeWidth.toPx()
            )
        }

        // Draw the large circle at the beginning
        Canvas(
            modifier = Modifier
                .size(circleRadius * 2) // Set size based on radius
                .align(Alignment.CenterStart) // Align the circle to the start
        ) {
            drawCircle(
                color = circleColor,
                radius = circleRadius.toPx(),
                center = Offset(x = circleRadius.toPx(), y = size.height / 2)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTimelineDivider() {
    TimelineDivider(
        modifier = Modifier.padding(vertical = 20.dp),
        circleRadius = 5.dp,
        lineStrokeWidth = 2.dp,
        paddingStart = 24.dp,
        paddingEnd = 24.dp
    )
}