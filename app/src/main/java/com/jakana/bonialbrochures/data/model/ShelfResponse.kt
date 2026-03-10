package com.jakana.bonialbrochures.data.model

import com.squareup.moshi.FromJson
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.ToJson

@JsonClass(generateAdapter = true)
data class ShelfResponse(
    @Json(name = "_embedded")
    val embedded: Embedded
)

@JsonClass(generateAdapter = true)
data class Embedded(val contents: List<ContentItem>
)

@JsonClass(generateAdapter = true)
data class ContentItem(
    val contentType: String,
    val content: BrochureContent? = null // nullable — array types return null safely
)

@JsonClass(generateAdapter = true)
data class BrochureContent(
    val id: Long,
    val brochureImage: String?, // nullable — show placeholder when null
    val distance: Double?, // nullable — include item when null
    val publisher: Publisher
)

@JsonClass(generateAdapter = true)
data class Publisher(val id: String, val name: String)

class ContentItemAdapter {
    @FromJson
    fun fromJson(reader: JsonReader): ContentItem {
        var contentType = ""
        var content: BrochureContent? = null

        reader.beginObject()
        while (reader.hasNext()) {
            when (reader.nextName()) {
                "contentType" -> contentType = reader.nextString()
                "content" -> {
                    when (reader.peek()) {
                        JsonReader.Token.BEGIN_ARRAY -> reader.skipValue()
                        JsonReader.Token.BEGIN_OBJECT -> {
                            var contentId = 0L
                            var brochureImage: String? = null
                            var distance: Double? = null
                            var publisher = Publisher("", "")

                            reader.beginObject()
                            while (reader.hasNext()) {
                                when (reader.nextName()) {
                                    "id" -> contentId = reader.nextLong()
                                    "brochureImage" -> brochureImage = if (reader.peek() == JsonReader.Token.NULL) {
                                        reader.skipValue(); null
                                    } else reader.nextString()
                                    "distance" -> distance = if (reader.peek() == JsonReader.Token.NULL) {
                                        reader.skipValue(); null
                                    } else reader.nextDouble()
                                    "publisher" -> {
                                        var pid = ""
                                        var pname = ""
                                        reader.beginObject()
                                        while (reader.hasNext()) {
                                            when (reader.nextName()) {
                                                "id" -> pid = reader.nextString()
                                                "name" -> pname = reader.nextString()
                                                else -> reader.skipValue()
                                            }
                                        }
                                        reader.endObject()
                                        publisher = Publisher(pid, pname)
                                    }
                                    else -> reader.skipValue()
                                }
                            }
                            reader.endObject()
                            content = BrochureContent(contentId, brochureImage, distance, publisher)
                        }
                        else -> reader.skipValue()
                    }
                }
                else -> reader.skipValue()
            }
        }
        reader.endObject()
        return ContentItem(contentType, content)
    }

    @ToJson
    fun toJson(writer: JsonWriter, value: ContentItem) {
        throw UnsupportedOperationException()
    }
}