package com.jvoye.tasky.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.scene.SceneStrategyScope
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_EXPANDED_LOWER_BOUND
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_MEDIUM_LOWER_BOUND
import com.jvoye.tasky.core.presentation.designsystem.util.DeviceConfiguration

// --- TwoPaneScene ---
/**
 * A custom [Scene] that displays two [NavEntry]s side-by-side in a 50/50 split.
 */
class TaskyTwoPaneScene<T : Any>(
    override val key: Any,
    override val previousEntries: List<NavEntry<T>>,
    val firstEntry: NavEntry<T>,
    val secondEntry: NavEntry<T>
) : Scene<T> {
    override val entries: List<NavEntry<T>> = listOf(firstEntry, secondEntry)
    override val content: @Composable (() -> Unit) = {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier
                    .weight(0.35f)
                    .padding(end = 24.dp)
            ) {
                firstEntry.Content()
            }
            Column(
                modifier = Modifier
                    .weight(0.65f)

            ) {
                /*Box(
                    modifier = Modifier.fillMaxSize()
                ) { Text(
                    modifier = Modifier
                        .align(Alignment.Center),
                    text = "Placeholder"
                ) }*/

                secondEntry.Content()
            }
        }
    }

    companion object {
        internal const val TWO_PANE_KEY = "TwoPane"
        /**
         * Helper function to add metadata to a [NavEntry] indicating it can be displayed
         * in a two-pane layout.
         */
        fun twoPane() = mapOf(TWO_PANE_KEY to true)
    }
}

@Composable
fun <T: Any> rememberTwoPaneSceneStrategy() : TwoPaneSceneStrategy<T> {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass

    return remember(windowSizeClass){
        TwoPaneSceneStrategy(windowSizeClass)
    }
}


// --- TwoPaneSceneStrategy ---
/**
 * A [SceneStrategy] that activates a [TwoPaneScene] if the window is wide enough
 * and the top two back stack entries declare support for two-pane display.
 */
class TwoPaneSceneStrategy<T : Any>(val windowSizeClass: WindowSizeClass) : SceneStrategy<T> {

    override fun SceneStrategyScope<T>.calculateScene(entries: List<NavEntry<T>>): Scene<T>? {


        val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)

        // Condition 1: Only return a Scene if the window is sufficiently wide to render two panes.
        // We use isWidthAtLeastBreakpoint with WIDTH_DP_MEDIUM_LOWER_BOUND (600dp).
        if (!windowSizeClass.isWidthAtLeastBreakpoint(WIDTH_DP_EXPANDED_LOWER_BOUND)) {
            return null
        }


        val lastTwoEntries = entries.takeLast(2)

        // Condition 2: Only return a Scene if there are two entries, and both have declared
        // they can be displayed in a two pane scene.
        return if (lastTwoEntries.size == 2
            && lastTwoEntries.all { it.metadata.containsKey(TaskyTwoPaneScene.TWO_PANE_KEY) }
        ) {
            val firstEntry = lastTwoEntries.first()
            val secondEntry = lastTwoEntries.last()

            // The scene key must uniquely represent the state of the scene.
            // A Pair of the first and second entry keys ensures uniqueness.
            val sceneKey = Pair(firstEntry.contentKey, secondEntry.contentKey)

            TaskyTwoPaneScene(
                key = sceneKey,
                // Where we go back to is a UX decision. In this case, we only remove the top
                // entry from the back stack, despite displaying two entries in this scene.
                // This is because in this app we only ever add one entry to the
                // back stack at a time. It would therefore be confusing to the user to add one
                // when navigating forward, but remove two when navigating back.
                previousEntries = entries.dropLast(1),
                firstEntry = firstEntry,
                secondEntry = secondEntry
            )

        } else {
            null
        }
    }


}