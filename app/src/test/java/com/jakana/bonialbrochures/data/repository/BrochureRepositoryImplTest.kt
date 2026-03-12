package com.jakana.bonialbrochures.data.repository

import com.jakana.bonialbrochures.data.model.BrochureContent
import com.jakana.bonialbrochures.data.model.ContentItem
import com.jakana.bonialbrochures.data.model.Embedded
import com.jakana.bonialbrochures.data.model.Publisher
import com.jakana.bonialbrochures.data.model.ShelfResponse
import com.jakana.bonialbrochures.data.remote.ShelfApiService
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class BrochureRepositoryImplTest {
    private lateinit var api: ShelfApiService
    private lateinit var repository: BrochureRepositoryImpl

    @Before
    fun setup() {
        api = mockk()
        repository = BrochureRepositoryImpl(api)
    }

    @Test
    fun `returns mapped brochures for valid content types`() = runTest {
        coEvery { api.getShelf() } returns shelfWith(
            contentItem(1L, "Lidl", "brochure", 0.93),
            contentItem(2L, "MediaMarkt", "brochurePremium", 1.86)
        )
        val result = repository.getBrochures()
        assertEquals(2, result.size)
    }

    @Test
    fun `filters out superBannerCarousel items`() = runTest {
        coEvery { api.getShelf() } returns shelfWith(
            contentItem(1L, "Ad", "superBannerCarousel", 1.0)
        )
        val result = repository.getBrochures()
        assertTrue(result.isEmpty())
    }

    @Test
    fun `maps retailer name correctly`() = runTest {
        coEvery { api.getShelf() } returns shelfWith(
            contentItem(1L, "Lidl", "brochure", 0.93)
        )
        val result = repository.getBrochures()
        assertEquals("Lidl", result.first().retailerName)
    }

    @Test
    fun `maps imageUrl correctly`() = runTest {
        coEvery { api.getShelf() } returns shelfWith(
            contentItem(1L, "Lidl", "brochure", 0.93, imageUrl = "https://img/1.jpg")
        )
        val result = repository.getBrochures()
        assertEquals("https://img/1.jpg", result.first().imageUrl)
    }

    @Test
    fun `maps null imageUrl correctly`() = runTest {
        coEvery { api.getShelf() } returns shelfWith(
            contentItem(1L, "Lidl", "brochure", 0.93, imageUrl = null)
        )
        val result = repository.getBrochures()
        assertNull(result.first().imageUrl)
    }

    @Test
    fun `items with null content are excluded`() = runTest {
        coEvery { api.getShelf() } returns ShelfResponse(
            Embedded(listOf(ContentItem("brochure", null)))
        )
        val result = repository.getBrochures()
        assertTrue(result.isEmpty())
    }

    @Test
    fun `returns empty list when shelf is empty`() = runTest {
        coEvery { api.getShelf() } returns ShelfResponse(Embedded(emptyList()))
        val result = repository.getBrochures()
        assertTrue(result.isEmpty())
    }

    // Helpers
    private fun shelfWith(vararg items: ContentItem) =
        ShelfResponse(Embedded(items.toList()))

    private fun contentItem(
        id: Long,
        name: String,
        type: String,
        distance: Double?,
        imageUrl: String? = "https://img/$id.jpg"
    ) = ContentItem(
        contentType = type,
        content = BrochureContent(
            id = id,
            brochureImage = imageUrl,
            distance = distance,
            publisher = Publisher("pub-$id", name)
        )
    )
}