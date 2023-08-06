package com.romeat.smashup.presentation.home.common.composables

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.DragScope
import androidx.compose.foundation.gestures.DraggableState
import androidx.compose.foundation.gestures.GestureCancellationException
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.lerp
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.setProgress
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.constraintlayout.compose.ConstraintLayout
import com.romeat.smashup.data.BitrateOption
import com.romeat.smashup.ui.theme.SmashupTheme
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

val trackHeight = 48.dp
val trackStrokeWidth = 10.dp
val markWidth = 6.dp
val markHeight = 22.dp

val thumbHeight = 30.dp
val thumbWidth = 12.dp
val thumbOffset = 4.dp // offset is needed when thumbWidth < 20.dp, and calculated as (20 - thumbWidth)/2

@Composable
@Preview
fun MySliderPreview() {
    SmashupTheme() {
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = (MaterialTheme.colors.background)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                CustomBitrateSlider(
                    BitrateOption.KB64,
                    listOf(
                        BitrateOption.KB64,
                        BitrateOption.KB96,
                        BitrateOption.KB128,
                        BitrateOption.KB160,
                        BitrateOption.KB320,
                    ),
                    {}
                )
                Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)) {
                    CustomBitrateSlider(
                        BitrateOption.KB96,
                        listOf(
                            BitrateOption.KB64,
                            BitrateOption.KB96,
                            BitrateOption.KB128,
                            BitrateOption.KB160,
                            BitrateOption.KB320,
                        ),
                        {}
                    )
                }

                CustomBitrateSlider(
                    BitrateOption.KB128,
                    listOf(
                        BitrateOption.KB64,
                        BitrateOption.KB96,
                        BitrateOption.KB128,
                        BitrateOption.KB160,
                        BitrateOption.KB320,
                    ),
                    {}
                )
                CustomBitrateSlider(
                    BitrateOption.KB320,
                    listOf(
                        BitrateOption.KB64,
                        BitrateOption.KB96,
                        BitrateOption.KB128,
                        BitrateOption.KB160,
                        BitrateOption.KB320,
                    ),
                    {}
                )
            }
        }
    }
}



@Composable
fun CustomBitrateSlider(
    selected: BitrateOption,
    bitrates: List<BitrateOption>, // size must always be 5
    onBitrateChange: (BitrateOption) -> Unit
) {
    val sliderValue = bitrates.indexOf(selected).toFloat()

    ConstraintLayout {
        val (slider, label1, label2, label3, label4, label5) = createRefs()
        val topBarrier = createTopBarrier(slider)
        val startBarrier = createStartBarrier(slider, margin = 25.dp)
        val endBarrier = createEndBarrier(slider, margin = (-25).dp)

        val textStyle = MaterialTheme.typography.overline.copy(fontSize = 12.sp)
        ModifiedSlider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .constrainAs(slider) {
                    top.linkTo(topBarrier)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            value = sliderValue,
            onValueChange = {
                onBitrateChange(bitrates[it.roundToInt()])
            },
            steps = 3,
            valueRange = 0f..4f
        )

        Text(
            text = stringResource(id = bitrates[0].displayResId),
            style = textStyle,
            modifier = Modifier
                .constrainAs(label1) {
                    top.linkTo(parent.top)
                    bottom.linkTo(topBarrier)
                    start.linkTo(startBarrier)
                    end.linkTo(startBarrier)
                }
        )
        Text(
            text = stringResource(id = bitrates[1].displayResId),
            style = textStyle,
            modifier = Modifier
                .constrainAs(label2) {
                    top.linkTo(parent.top)
                    bottom.linkTo(topBarrier)
                    start.linkTo(label1.end)
                    end.linkTo(label3.start)
                }
        )
        Text(
            text = stringResource(id = bitrates[2].displayResId),
            style = textStyle,
            modifier = Modifier
                .constrainAs(label3) {
                    top.linkTo(parent.top)
                    bottom.linkTo(topBarrier)
                    start.linkTo(label1.end)
                    end.linkTo(label5.start)
                }
        )
        Text(
            text = stringResource(id = bitrates[3].displayResId),
            style = textStyle,
            modifier = Modifier
                .constrainAs(label4) {
                    top.linkTo(parent.top)
                    bottom.linkTo(topBarrier)
                    start.linkTo(label3.end)
                    end.linkTo(label5.start)
                }
        )
        Text(
            text = stringResource(id = bitrates[4].displayResId),
            style = textStyle,
            modifier = Modifier
                .constrainAs(label5) {
                    top.linkTo(parent.top)
                    bottom.linkTo(topBarrier)
                    start.linkTo(endBarrier)
                    end.linkTo(endBarrier)
                }
        )
    }
}

@Composable
fun ModifiedSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int,
    onValueChangeFinished: (() -> Unit)? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    require(steps >= 0) { "steps should be >= 0" }
    val onValueChangeState = rememberUpdatedState(onValueChange)
    val tickFractions = remember(steps) {
        stepsToTickFractions(steps)
    }
    BoxWithConstraints(
        modifier
            //.minimumTouchTargetSize()
            .requiredSizeIn(minWidth = trackHeight, minHeight = trackHeight)
            .sliderSemantics(
                value,
                enabled,
                onValueChange,
                onValueChangeFinished,
                valueRange,
                steps
            )
            .focusable(enabled, interactionSource)
    ) {
        val isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl
        val widthPx = constraints.maxWidth.toFloat()
        val maxPx: Float
        val minPx: Float

        with(LocalDensity.current) {
            maxPx = max(widthPx - thumbHeight.toPx(), 0f)
            minPx = min(thumbHeight.toPx(), maxPx)
        }

        fun scaleToUserValue(offset: Float) =
            scale(minPx, maxPx, offset, valueRange.start, valueRange.endInclusive)

        fun scaleToOffset(userValue: Float) =
            scale(valueRange.start, valueRange.endInclusive, userValue, minPx, maxPx)

        val scope = rememberCoroutineScope()
        val rawOffset = remember { mutableStateOf(scaleToOffset(value)) }
        val pressOffset = remember { mutableStateOf(0f) }

        val draggableState = remember(minPx, maxPx, valueRange) {
            SliderDraggableState {
                rawOffset.value = (rawOffset.value + it + pressOffset.value)
                pressOffset.value = 0f
                val offsetInTrack = rawOffset.value.coerceIn(minPx, maxPx)
                onValueChangeState.value.invoke(scaleToUserValue(offsetInTrack))
            }
        }

        CorrectValueSideEffect(::scaleToOffset, valueRange, minPx..maxPx, rawOffset, value)

        val gestureEndAction = rememberUpdatedState<(Float) -> Unit> { velocity: Float ->
            val current = rawOffset.value
            val target = snapValueToTick(current, tickFractions, minPx, maxPx)
            if (current != target) {
                scope.launch {
                    animateToTarget(draggableState, current, target, velocity)
                    onValueChangeFinished?.invoke()
                }
            } else if (!draggableState.isDragging) {
                // check ifDragging in case the change is still in progress (touch -> drag case)
                onValueChangeFinished?.invoke()
            }
        }
        val press = Modifier.sliderTapModifier(
            draggableState,
            interactionSource,
            widthPx,
            isRtl,
            rawOffset,
            gestureEndAction,
            pressOffset,
            enabled
        )

        /*
        // drag works wacky, so I disabled it
        val drag = Modifier.draggable(
            orientation = Orientation.Horizontal,
            reverseDirection = isRtl,
            enabled = enabled,
            interactionSource = interactionSource,
            onDragStopped = { velocity -> gestureEndAction.value.invoke(velocity) },
            startDragImmediately = draggableState.isDragging,
            state = draggableState
        )
         */

        val coerced = value.coerceIn(valueRange.start, valueRange.endInclusive)
        val fraction = calcFraction(valueRange.start, valueRange.endInclusive, coerced)
        CustomSliderImpl(
            fraction,
            tickFractions,
            maxPx - minPx,
            interactionSource,
            modifier = press//.then(drag)
        )
    }
}

@Composable
private fun CustomSliderImpl(
    positionFraction: Float,
    tickFractions: List<Float>,
    width: Float,
    interactionSource: MutableInteractionSource,
    modifier: Modifier
) {
    Box(modifier) {

        val widthDp: Dp
        with(LocalDensity.current) {
            widthDp = width.toDp()
        }

        val offset = (widthDp + thumbWidth*3) * positionFraction + thumbWidth/2
        CustomTrack(
            0f,
            positionFraction,
            tickFractions,
        )
        CustomThumb(interactionSource, offset)
    }
}

@Composable
fun CustomThumb(
    interactionSource: MutableInteractionSource,
    offset: Dp,
    modifier: Modifier = Modifier,
) {
    val interactions = remember { mutableStateListOf<Interaction>() }
    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            when (interaction) {
                is PressInteraction.Press -> interactions.add(interaction)
                is PressInteraction.Release -> interactions.remove(interaction.press)
                is PressInteraction.Cancel -> interactions.remove(interaction.press)
                is DragInteraction.Start -> interactions.add(interaction)
                is DragInteraction.Stop -> interactions.remove(interaction.start)
                is DragInteraction.Cancel -> interactions.remove(interaction.start)
            }
        }
    }
    Box(
        modifier
            .height(thumbHeight)
            .width(thumbWidth)
            .offset(x = offset, y = (trackHeight - thumbHeight)/2)
            .indication(
                interactionSource = interactionSource,
                indication = rememberRipple(
                    bounded = false,
                    radius = 30.dp
                )
            )
            .hoverable(interactionSource = interactionSource)
            .background(MaterialTheme.colors.onSurface, RoundedCornerShape(50))
    )
}

@Composable
fun CustomTrack(
    positionFractionStart: Float,
    positionFractionEnd: Float,
    tickFractions: List<Float>,
    modifier: Modifier = Modifier,
) {
    val inactiveTrackColor = MaterialTheme.colors.surface
    val activeTrackColor = MaterialTheme.colors.primaryVariant
    val inactiveTickColor = MaterialTheme.colors.surface
    val activeTickColor = MaterialTheme.colors.primaryVariant

    Canvas(
        modifier
            .fillMaxWidth()
            .height(trackHeight)
            .padding(horizontal = thumbWidth)
    ) {
        val isRtl = layoutDirection == LayoutDirection.Rtl
        val sliderLeft = Offset(0f, center.y)
        val sliderRight = Offset(size.width, center.y)
        val sliderStart = if (isRtl) sliderRight else sliderLeft
        val sliderEnd = if (isRtl) sliderLeft else sliderRight
        drawLine(
            inactiveTrackColor,
            sliderStart,
            sliderEnd,
            trackStrokeWidth.toPx(),
            StrokeCap.Round
        )
        val sliderValueEnd = Offset(
            sliderStart.x + (sliderEnd.x - sliderStart.x) * positionFractionEnd,
            center.y
        )

        val sliderValueStart = Offset(
            sliderStart.x + (sliderEnd.x - sliderStart.x) * positionFractionStart,
            center.y
        )

        drawLine(
            activeTrackColor,
            sliderValueStart,
            sliderValueEnd,
            trackStrokeWidth.toPx(),
            StrokeCap.Round
        )
        // "points"
        tickFractions.groupBy {
            it > positionFractionEnd || it < positionFractionStart
        }.forEach { (outsideFraction, list) ->
            list.forEach { offset ->
                drawRoundRect(
                    color = (if (outsideFraction) inactiveTickColor else activeTickColor),
                    topLeft = Offset(lerp(sliderStart, sliderEnd, offset).x - (markWidth/2).toPx(), center.y - (markHeight/2).toPx()),
                    size = Size(markWidth.toPx(), markHeight.toPx()),
                    cornerRadius = CornerRadius((markWidth/2).toPx()) // rounded by 50%
                )
            }
        }
    }
}

// Scale x1 from a1..b1 range to a2..b2 range

private fun scale(a1: Float, b1: Float, x1: Float, a2: Float, b2: Float) =
    lerp(a2, b2, calcFraction(a1, b1, x1))

// Scale x.start, x.endInclusive from a1..b1 range to a2..b2 range
private fun scale(a1: Float, b1: Float, x: ClosedFloatingPointRange<Float>, a2: Float, b2: Float) =
    scale(a1, b1, x.start, a2, b2)..scale(a1, b1, x.endInclusive, a2, b2)

// Calculate the 0..1 fraction that `pos` value represents between `a` and `b`
private fun calcFraction(a: Float, b: Float, pos: Float) =
    (if (b - a == 0f) 0f else (pos - a) / (b - a)).coerceIn(0f, 1f)


private fun Modifier.sliderTapModifier(
    draggableState: DraggableState,
    interactionSource: MutableInteractionSource,
    maxPx: Float,
    isRtl: Boolean,
    rawOffset: State<Float>,
    gestureEndAction: State<(Float) -> Unit>,
    pressOffset: MutableState<Float>,
    enabled: Boolean
) = composed(
    factory = {
        if (enabled) {
            val scope = rememberCoroutineScope()
            pointerInput(draggableState, interactionSource, maxPx, isRtl) {
                detectTapGestures(
                    onPress = { pos ->
                        val to = if (isRtl) maxPx - pos.x else pos.x
                        pressOffset.value = to - rawOffset.value
                        try {
                            awaitRelease()
                        } catch (_: GestureCancellationException) {
                            pressOffset.value = 0f
                        }
                    },
                    onTap = {
                        scope.launch {
                            draggableState.drag(MutatePriority.UserInput) {
                                // just trigger animation, press offset will be applied
                                dragBy(0f)
                            }
                            gestureEndAction.value.invoke(0f)
                        }
                    }
                )
            }
        } else {
            this
        }
    },
    inspectorInfo = debugInspectorInfo {
        name = "sliderTapModifier"
        properties["draggableState"] = draggableState
        properties["interactionSource"] = interactionSource
        properties["maxPx"] = maxPx
        properties["isRtl"] = isRtl
        properties["rawOffset"] = rawOffset
        properties["gestureEndAction"] = gestureEndAction
        properties["pressOffset"] = pressOffset
        properties["enabled"] = enabled
    })

private fun stepsToTickFractions(steps: Int): List<Float> {
    return if (steps == 0) emptyList() else List(steps + 2) { it.toFloat() / (steps + 1) }
}

private suspend fun animateToTarget(
    draggableState: DraggableState,
    current: Float,
    target: Float,
    velocity: Float
) {
    draggableState.drag {
        var latestValue = current
        Animatable(initialValue = current).animateTo(target, SliderToTickAnimation, velocity) {
            dragBy(this.value - latestValue)
            latestValue = this.value
        }
    }
}

private val SliderToTickAnimation = TweenSpec<Float>(durationMillis = 100)

private class SliderDraggableState(
    val onDelta: (Float) -> Unit
) : DraggableState {

    var isDragging by mutableStateOf(false)
        private set

    private val dragScope: DragScope = object : DragScope {
        override fun dragBy(pixels: Float): Unit = onDelta(pixels)
    }

    private val scrollMutex = MutatorMutex()

    override suspend fun drag(
        dragPriority: MutatePriority,
        block: suspend DragScope.() -> Unit
    ): Unit = coroutineScope {
        isDragging = true
        scrollMutex.mutateWith(dragScope, dragPriority, block)
        isDragging = false
    }

    override fun dispatchRawDelta(delta: Float) {
        return onDelta(delta)
    }
}

private fun snapValueToTick(
    current: Float,
    tickFractions: List<Float>,
    minPx: Float,
    maxPx: Float
): Float {
    // target is a closest anchor to the `current`, if exists
    return tickFractions
        .minByOrNull { abs(lerp(minPx, maxPx, it) - current) }
        ?.run { lerp(minPx, maxPx, this) }
        ?: current
}

@Composable
private fun CorrectValueSideEffect(
    scaleToOffset: (Float) -> Float,
    valueRange: ClosedFloatingPointRange<Float>,
    trackRange: ClosedFloatingPointRange<Float>,
    valueState: MutableState<Float>,
    value: Float
) {
    SideEffect {
        val error = (valueRange.endInclusive - valueRange.start) / 1000
        val newOffset = scaleToOffset(value)
        if (abs(newOffset - valueState.value) > error) {
            if (valueState.value in trackRange) {
                valueState.value = newOffset
            }
        }
    }
}

private fun Modifier.sliderSemantics(
    value: Float,
    enabled: Boolean,
    onValueChange: (Float) -> Unit,
    onValueChangeFinished: (() -> Unit)? = null,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0
): Modifier {
    val coerced = value.coerceIn(valueRange.start, valueRange.endInclusive)
    return semantics {
        if (!enabled) disabled()
        setProgress(
            action = { targetValue ->
                var newValue = targetValue.coerceIn(valueRange.start, valueRange.endInclusive)
                val originalVal = newValue
                val resolvedValue = if (steps > 0) {
                    var distance: Float = newValue
                    for (i in 0..steps + 1) {
                        val stepValue = lerp(
                            valueRange.start,
                            valueRange.endInclusive,
                            i.toFloat() / (steps + 1))
                        if (abs(stepValue - originalVal) <= distance) {
                            distance = abs(stepValue - originalVal)
                            newValue = stepValue
                        }
                    }
                    newValue
                } else {
                    newValue
                }
                // This is to keep it consistent with AbsSeekbar.java: return false if no
                // change from current.
                if (resolvedValue == coerced) {
                    false
                } else {
                    onValueChange(resolvedValue)
                    onValueChangeFinished?.invoke()
                    true
                }
            }
        )
    }.progressSemantics(value, valueRange, steps)
}
