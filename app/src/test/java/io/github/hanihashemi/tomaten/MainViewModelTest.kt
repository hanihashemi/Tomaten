package io.github.hanihashemi.tomaten

import app.cash.turbine.test
import io.github.hanihashemi.tomaten.ui.events.UiEvents
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {
    private lateinit var viewModel: MainViewModel
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = MainViewModel(shouldFetchCurrentUser = false)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Nested
    @DisplayName("Initial State Tests")
    inner class InitialStateTests {
        @Test
        @DisplayName("Should initialize with default UI state")
        fun shouldInitializeWithDefaultUIState() {
            val initialState = viewModel.uiState.value

            assertNotNull(initialState)
            assertNotNull(initialState.login)
            assertNotNull(initialState.timer)

            // Login state defaults
            assertNull(initialState.login.user)
            assertFalse(initialState.login.isLoading)
            assertNull(initialState.login.errorMessage)
            assertFalse(initialState.login.isLoggedIn)

            // Timer state defaults
            assertFalse(initialState.timer.isRunning)
            assertEquals(15 * 60L, initialState.timer.timeRemaining)
            assertEquals(15 * 60L, initialState.timer.timeLimit)
            assertFalse(initialState.timer.isDialogVisible)
        }

        @Test
        @DisplayName("Should initialize actions")
        fun shouldInitializeActions() {
            assertNotNull(viewModel.actions)
            assertNotNull(viewModel.actions.login)
            assertNotNull(viewModel.actions.timer)
        }
    }

    @Nested
    @DisplayName("State Management Tests")
    inner class StateManagementTests {
        @Test
        @DisplayName("Should update state correctly")
        fun shouldUpdateStateCorrectly() {
            val testUser =
                User(
                    name = "Test User",
                    email = "test@example.com",
                    photoUrl = "http://example.com/photo.jpg",
                    uid = "test-uid",
                )

            viewModel.updateState { currentState ->
                currentState.copy(
                    login = currentState.login.copy(user = testUser),
                )
            }

            val updatedState = viewModel.uiState.value
            assertEquals(testUser, updatedState.login.user)
            assertTrue(updatedState.login.isLoggedIn)
        }

        @Test
        @DisplayName("Should update timer state correctly")
        fun shouldUpdateTimerStateCorrectly() {
            val newTimeLimit = 25 * 60L

            viewModel.updateState { currentState ->
                currentState.copy(
                    timer =
                        currentState.timer.copy(
                            timeLimit = newTimeLimit,
                            timeRemaining = newTimeLimit,
                            isRunning = true,
                        ),
                )
            }

            val updatedState = viewModel.uiState.value
            assertEquals(newTimeLimit, updatedState.timer.timeLimit)
            assertEquals(newTimeLimit, updatedState.timer.timeRemaining)
            assertTrue(updatedState.timer.isRunning)
        }
    }

    @Nested
    @DisplayName("Event Management Tests")
    inner class EventManagementTests {
        @Test
        @DisplayName("Should emit login event")
        fun shouldEmitLoginEvent() =
            testScope.runTest {
                viewModel.uiEvents.test {
                    viewModel.sendEvent(UiEvents.Login)

                    val event = awaitItem()
                    assertEquals(UiEvents.Login, event)
                }
            }

        @Test
        @DisplayName("Should emit start timer event")
        fun shouldEmitStartTimerEvent() =
            testScope.runTest {
                val timeLimit = 20 * 60L

                viewModel.uiEvents.test {
                    viewModel.sendEvent(UiEvents.StartTimer(timeLimit))

                    val event = awaitItem()
                    assertTrue(event is UiEvents.StartTimer)
                    assertEquals(timeLimit, (event as UiEvents.StartTimer).timeLimit)
                }
            }

        @Test
        @DisplayName("Should emit stop timer event")
        fun shouldEmitStopTimerEvent() =
            testScope.runTest {
                viewModel.uiEvents.test {
                    viewModel.sendEvent(UiEvents.StopTimer)

                    val event = awaitItem()
                    assertEquals(UiEvents.StopTimer, event)
                }
            }

        @Test
        @DisplayName("Should handle multiple events in sequence")
        fun shouldHandleMultipleEventsInSequence() =
            testScope.runTest {
                viewModel.uiEvents.test {
                    viewModel.sendEvent(UiEvents.Login)
                    viewModel.sendEvent(UiEvents.StartTimer(1500L))
                    viewModel.sendEvent(UiEvents.StopTimer)

                    assertEquals(UiEvents.Login, awaitItem())

                    val startEvent = awaitItem()
                    assertTrue(startEvent is UiEvents.StartTimer)
                    assertEquals(1500L, (startEvent as UiEvents.StartTimer).timeLimit)

                    assertEquals(UiEvents.StopTimer, awaitItem())
                }
            }
    }

    @Nested
    @DisplayName("Timer Action Tests")
    inner class TimerActionTests {
        @Test
        @DisplayName("Should start timer when not running")
        fun shouldStartTimerWhenNotRunning() =
            testScope.runTest {
                viewModel.uiEvents.test {
                    viewModel.actions.timer.startOrStop()

                    val event = awaitItem()
                    assertTrue(event is UiEvents.StartTimer)
                    assertTrue(viewModel.uiState.value.timer.isRunning)
                }
            }

        @Test
        @DisplayName("Should stop timer when running")
        fun shouldStopTimerWhenRunning() =
            testScope.runTest {
                // First start the timer
                viewModel.updateState { it.copy(timer = it.timer.copy(isRunning = true)) }

                viewModel.uiEvents.test {
                    viewModel.actions.timer.startOrStop()

                    val event = awaitItem()
                    assertEquals(UiEvents.StopTimer, event)
                    assertFalse(viewModel.uiState.value.timer.isRunning)
                }
            }

        @Test
        @DisplayName("Should set time limit when timer is not running")
        fun shouldSetTimeLimitWhenTimerNotRunning() {
            val newTimeLimit = 30 * 60L

            viewModel.actions.timer.setTimeLimit(newTimeLimit)

            val state = viewModel.uiState.value.timer
            assertEquals(newTimeLimit, state.timeLimit)
            assertEquals(newTimeLimit, state.timeRemaining)
        }

        @Test
        @DisplayName("Should not set time limit when timer is running")
        fun shouldNotSetTimeLimitWhenTimerRunning() {
            val originalTimeLimit = viewModel.uiState.value.timer.timeLimit
            viewModel.updateState { it.copy(timer = it.timer.copy(isRunning = true)) }

            viewModel.actions.timer.setTimeLimit(30 * 60L)

            val state = viewModel.uiState.value.timer
            assertEquals(originalTimeLimit, state.timeLimit)
        }

        @Test
        @DisplayName("Should show dialog when timer is not running")
        fun shouldShowDialogWhenTimerNotRunning() {
            viewModel.actions.timer.displayDialog(true)

            assertTrue(viewModel.uiState.value.timer.isDialogVisible)
        }

        @Test
        @DisplayName("Should not show dialog when timer is running")
        fun shouldNotShowDialogWhenTimerRunning() {
            viewModel.updateState { it.copy(timer = it.timer.copy(isRunning = true)) }

            viewModel.actions.timer.displayDialog(true)

            assertFalse(viewModel.uiState.value.timer.isDialogVisible)
        }

        @Test
        @DisplayName("Should update timer remaining time")
        fun shouldUpdateTimerRemainingTime() {
            val remainingTime = 300L // 5 minutes

            viewModel.actions.timer.updateTimer(remainingTime)

            assertEquals(remainingTime, viewModel.uiState.value.timer.timeRemaining)
        }
    }

    @Nested
    @DisplayName("User Model Tests")
    inner class UserModelTests {
        @Test
        @DisplayName("Should create user with all fields")
        fun shouldCreateUserWithAllFields() {
            val user =
                User(
                    name = "John Doe",
                    email = "john@example.com",
                    photoUrl = "http://example.com/photo.jpg",
                    uid = "user-123",
                )

            assertEquals("John Doe", user.name)
            assertEquals("john@example.com", user.email)
            assertEquals("http://example.com/photo.jpg", user.photoUrl)
            assertEquals("user-123", user.uid)
        }

        @Test
        @DisplayName("Should create user with null optional fields")
        fun shouldCreateUserWithNullOptionalFields() {
            val user = User(uid = "user-123")

            assertNull(user.name)
            assertNull(user.email)
            assertNull(user.photoUrl)
            assertEquals("user-123", user.uid)
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    inner class IntegrationTests {
        @Test
        @DisplayName("Should handle complete timer workflow")
        fun shouldHandleCompleteTimerWorkflow() =
            testScope.runTest {
                val customTimeLimit = 10 * 60L // 10 minutes

                viewModel.uiEvents.test {
                    // Set custom time limit
                    viewModel.actions.timer.setTimeLimit(customTimeLimit)
                    assertEquals(customTimeLimit, viewModel.uiState.value.timer.timeLimit)

                    // Start timer
                    viewModel.actions.timer.startOrStop()
                    val startEvent = awaitItem()
                    assertTrue(startEvent is UiEvents.StartTimer)
                    assertTrue(viewModel.uiState.value.timer.isRunning)

                    // Update timer (simulate service update)
                    viewModel.actions.timer.updateTimer(customTimeLimit - 60) // 1 minute elapsed
                    assertEquals(customTimeLimit - 60, viewModel.uiState.value.timer.timeRemaining)

                    // Stop timer
                    viewModel.actions.timer.startOrStop()
                    val stopEvent = awaitItem()
                    assertEquals(UiEvents.StopTimer, stopEvent)
                    assertFalse(viewModel.uiState.value.timer.isRunning)
                }
            }

        @Test
        @DisplayName("Should handle login state changes")
        fun shouldHandleLoginStateChanges() =
            testScope.runTest {
                val testUser =
                    User(
                        name = "Test User",
                        email = "test@example.com",
                        uid = "test-uid",
                    )

                // Initially not logged in
                assertFalse(viewModel.uiState.value.login.isLoggedIn)

                // Login user
                viewModel.updateState { currentState ->
                    currentState.copy(
                        login =
                            currentState.login.copy(
                                user = testUser,
                                isLoading = false,
                            ),
                    )
                }

                val state = viewModel.uiState.value
                assertTrue(state.login.isLoggedIn)
                assertEquals(testUser, state.login.user)
                assertFalse(state.login.isLoading)
            }
    }
}
