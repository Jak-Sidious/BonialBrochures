package com.jakana.bonialbrochures.domain.usecase

import com.jakana.bonialbrochures.domain.model.Brochure
import com.jakana.bonialbrochures.domain.repository.BrochureRepository
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetFilteredBrochuresUseCaseTest {
    private lateinit var repository: BrochureRepository
    private lateinit var useCase: GetFilteredBrochuresUseCase

    private val lidl = brochure(1, "Lidl", "brochure", 0.93)
    private val mediaMarkt = brochure(2, "MediaMarkt", "brochurePremium", 1.86)
    private val globus = brochure(3, "Globus", "brochure", 8.75)
    private val banner = brochure(4, "Ad", "superBannerCarousel", 1.0)
    private val noGps = brochure(5, "Store", "brochure", null)
    private val exactly5 = brochure(6, "Edge", "brochure", 5.0)
    private val just4999 = brochure(7, "Near", "brochure", 4.999)

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetFilteredBrochuresUseCase(repository)
    }

    @Test
    fun `returns only brochure and brochurePremium types`() = runTest {
        coEvery { repository.getBrochures() } returns listOf(lidl, mediaMarkt, banner)
        val result = useCase()
        assertEquals(2, result.size)
        assertTrue(result.none { it.contentType == "superBannerCarousel" })
    }

    @Test
    fun `excludes brochures further than 5km`() = runTest {
        coEvery { repository.getBrochures() } returns listOf(lidl, globus)
        val result = useCase()
        assertEquals(1, result.size)
        assertEquals("Lidl", result.first().retailerName)
    }

    @Test
    fun `includes items with null distance`() = runTest {
        coEvery { repository.getBrochures() } returns listOf(noGps)
        assertEquals(1, useCase().size)
    }

    @Test
    fun `excludes item with distance exactly 5km`() = runTest {
        coEvery { repository.getBrochures() } returns listOf(exactly5)
        assertTrue(useCase().isEmpty())
    }

    @Test
    fun `includes item with distance just under 5km`() = runTest {
        coEvery { repository.getBrochures() } returns listOf(just4999)
        assertEquals(1, useCase().size)
    }

    @Test
    fun `returns empty list when all filtered out`() = runTest {
        coEvery { repository.getBrochures() } returns listOf(globus, banner)
        assertTrue(useCase().isEmpty())
    }

    @Test
    fun `returns empty on empty repository`() = runTest {
        coEvery { repository.getBrochures() } returns emptyList()
        assertTrue(useCase().isEmpty())
    }

    @Test
    fun `brochurePremium is marked as premium`() = runTest {
        coEvery { repository.getBrochures() } returns listOf(mediaMarkt)
        assertTrue(useCase().first().isPremium)
    }

    private fun brochure(id: Long, name: String, type: String, dist: Double?) =
        Brochure(id, name, "https://img/$id.jpg", type, dist)
}