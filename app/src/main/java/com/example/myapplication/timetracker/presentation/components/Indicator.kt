package com.example.myapplication.timetracker.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.progressSemantics
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Indicator(
    timeAmountInMilliseconds: Long,
    color: Color = MaterialTheme.colorScheme.primary, // color of indicator arc line
    strokeWidth: Dp = 5.dp //width of cicle and ar lines
) {
    val sweepAngle = timeAmountInMilliseconds % 60000 *360/60000f

    // define stroke with given width and arc ends type considering device DPI
    val stroke = with(LocalDensity.current) {
        Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Square)
    }

    // draw on canvas
    Canvas(
        Modifier
            .progressSemantics() // (optional) for Accessibility services
            .fillMaxWidth(0.5f)
            .aspectRatio(1f) // canvas size
            .padding(strokeWidth / 2) //padding. otherwise, not the whole circle will fit in the canvas
    ) {

        // draw arc with the same stroke
        drawArc(
            color = color,
            // arc start angle
            // 270 shifts the start position towards the y-axis
            startAngle = 270f,
            sweepAngle = sweepAngle,
            useCenter = false,
            style = stroke
        )
    }
}