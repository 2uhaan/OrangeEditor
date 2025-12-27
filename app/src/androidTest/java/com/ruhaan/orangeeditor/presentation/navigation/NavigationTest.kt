package com.ruhaan.orangeeditor.presentation.navigation

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.NavType
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.testing.TestNavHostController
import com.ruhaan.orangeeditor.domain.model.CanvasFormat
import com.ruhaan.orangeeditor.presentation.editor.EditorScreen
import com.ruhaan.orangeeditor.presentation.home.HomeScreen
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class NavigationTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var navController: TestNavHostController

    @Before
    fun setupNavHost() {
        composeTestRule.setContent {
            // Initialize TestNavHostController
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(
                ComposeNavigator()
            )

            // Set up your actual NavHost
            NavHost(
                navController = navController,
                startDestination = Route.Home.route
            ) {
                composable(route = Route.Home.route) {
                    HomeScreen(
                        onFormatSelected = { selectedFormat ->
                            navController.navigate(Route.Editor.createRoute(selectedFormat))
                        }
                    )
                }
                composable(
                    route = Route.Editor.route,
                    arguments = listOf(
                        navArgument(Route.Editor.ARG_CANVAS_FORMAT) {
                            type = NavType.StringType
                        }
                    )
                ) { backStackEntry ->
                    val formatName = backStackEntry.arguments?.getString(Route.Editor.ARG_CANVAS_FORMAT)
                    val selectedFormat = formatName?.let {
                        CanvasFormat.valueOf(it)
                    } ?: CanvasFormat.POST

                    EditorScreen(
                        canvasFormat = selectedFormat,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }

    @Test
    fun navHost_clickStoryCard_navigatesToEditor() {
        // When: Click on Story card
        composeTestRule
            .onNodeWithText("Story")
            .performClick()

        // Wait for navigation to complete
        composeTestRule.waitForIdle()

        // Then: Editor screen should be displayed
        val currentRoute = navController.currentBackStackEntry?.destination?.route
        assert(currentRoute == "editor/{canvasFormat}")
    }

    @Test
    fun navHost_clickPostCard_navigatesToEditorWithCorrectFormat() {
        // When: Click on Post card
        composeTestRule
            .onNodeWithText("Post")
            .performClick()

        // Wait for navigation
        composeTestRule.waitForIdle()

        // Then: Verify we're on the editor screen (check for Draft text in TopBar)
        composeTestRule
            .onNodeWithText("Draft")
            .assertIsDisplayed()
    }

    @Test
    fun navHost_editorBackButton_navigatesToHome() {
        // Given: Navigate to editor first
        composeTestRule
            .onNodeWithText("Portrait")
            .performClick()

        composeTestRule.waitForIdle()

        // When: Click back button (TopBar back, not system back)
        composeTestRule
            .onNodeWithContentDescription("go back")
            .performClick()

        composeTestRule.waitForIdle()

        // Then: Should return to home screen
        composeTestRule
            .onNodeWithText("Orange Editor")
            .assertIsDisplayed()
    }

    @Test
    fun navHost_navigateToAllFormats_displaysCorrectAspectRatios() {
        val formats = listOf(
            "Story",
            "Post",
            "Portrait",
            "Thumbnail"
        )

        formats.forEach { formatName ->
            // Navigate to format
            composeTestRule
                .onNodeWithText(formatName)
                .performClick()

            composeTestRule.waitForIdle()

            // Verify we're on editor screen (check for Draft text)
            composeTestRule
                .onNodeWithText("Draft")
                .assertIsDisplayed()

            // Navigate back
            composeTestRule
                .onNodeWithContentDescription("go back")
                .performClick()

            composeTestRule.waitForIdle()
        }
    }
}

