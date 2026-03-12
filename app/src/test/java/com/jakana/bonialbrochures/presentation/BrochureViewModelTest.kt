package com.jakana.bonialbrochures.presentation

import com.jakana.bonialbrochures.domain.model.Brochure
import com.jakana.bonialbrochures.domain.usecase.GetFilteredBrochuresUseCase
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BrochureViewModelTest {
    private val dispatcher = UnconfinedTestDispatcher()
    private lateinit var useCase: GetFilteredBrochuresUseCase
    private lateinit var viewModel: BrochureViewModel

    private val fakeList = listOf(
        Brochure(1L, "Lidl", "https://img/1.jpg", "brochure", 0.93),
        Brochure(2L, "MediaMarkt", "https://img/2.jpg", "brochurePremium", 1.86)
    )

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        useCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `state is Success with data when use case succeeds`() = runTest {
        coEvery { useCase() } returns fakeList
        viewModel = BrochureViewModel(useCase)
        val state = viewModel.uiState.value
        assertTrue(state is BrochureUiState.Success)
        assertEquals(2, (state as BrochureUiState.Success).brochures.size)
    }

    @Test
    fun `state is Error when use case throws`() = runTest {
        coEvery { useCase() } throws RuntimeException("Network error")
        viewModel = BrochureViewModel(useCase)
        val state = viewModel.uiState.value
        assertTrue(state is BrochureUiState.Error)
        assertEquals("Network error", (state as BrochureUiState.Error).message)
    }

    @Test
    fun `success state contains correct retailer names`() = runTest {
        coEvery { useCase() } returns fakeList
        viewModel = BrochureViewModel(useCase)
        val brochures = (viewModel.uiState.value as BrochureUiState.Success).brochures
        assertEquals("Lidl", brochures[0].retailerName)
        assertEquals("MediaMarkt", brochures[1].retailerName)
    }

    @Test
    fun `reload triggers new data load`() = runTest {
        coEvery { useCase() } returns fakeList
        viewModel = BrochureViewModel(useCase)
        viewModel.loadBrochures()
        assertTrue(viewModel.uiState.value is BrochureUiState.Success)
    }
}