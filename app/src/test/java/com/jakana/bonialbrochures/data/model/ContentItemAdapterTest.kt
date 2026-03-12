package com.jakana.bonialbrochures.data.model

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertNotNull
import org.junit.Test

class ContentItemAdapterTest {

    private val moshi = Moshi.Builder()
        .add(ContentItemAdapter())
        .add(KotlinJsonAdapterFactory())
        .build()

    private val adapter = moshi.adapter(ContentItem::class.java)

    @Test
    fun `parses brochure content object correctly`() {
        val json = """
            {
                "contentType": "brochure",
                "content": {
                    "id": 123,
                    "brochureImage": "https://img/1.jpg",
                    "distance": 1.5,
                    "publisher": { "id": "pub1", "name": "Lidl" }
                }
            }
        """.trimIndent()
        val result = adapter.fromJson(json)
        assertNotNull(result)
        assertEquals("brochure", result!!.contentType)
        assertEquals(123L, result.content!!.id)
        assertEquals("Lidl", result.content!!.publisher.name)
        assertEquals(1.5, result.content!!.distance)
    }

    @Test
    fun `returns null content when content is array`() {
        val json = """
            {
                "contentType": "superBannerCarousel",
                "content": []
            }
        """.trimIndent()
        val result = adapter.fromJson(json)
        assertNotNull(result)
        assertNull(result!!.content)
    }

    @Test
    fun `handles null brochureImage`() {
        val json = """
            {
                "contentType": "brochure",
                "content": {
                    "id": 1,
                    "brochureImage": null,
                    "distance": 2.0,
                    "publisher": { "id": "p1", "name": "Auchan" }
                }
            }
        """.trimIndent()
        val result = adapter.fromJson(json)
        assertNull(result!!.content!!.brochureImage)
    }

    @Test
    fun `handles null distance`() {
        val json = """
            {
                "contentType": "brochure",
                "content": {
                    "id": 2,
                    "brochureImage": "https://img/2.jpg",
                    "distance": null,
                    "publisher": { "id": "p2", "name": "Carrefour" }
                }
            }
        """.trimIndent()
        val result = adapter.fromJson(json)
        assertNull(result!!.content!!.distance)
    }

    @Test
    fun `parses brochurePremium type correctly`() {
        val json = """
            {
                "contentType": "brochurePremium",
                "content": {
                    "id": 3,
                    "brochureImage": "https://img/3.jpg",
                    "distance": 0.5,
                    "publisher": { "id": "p3", "name": "MediaMarkt" }
                }
            }
        """.trimIndent()
        val result = adapter.fromJson(json)
        assertEquals("brochurePremium", result!!.contentType)
    }
}