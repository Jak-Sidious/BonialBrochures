package com.jakana.bonialbrochures.data.model

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.ToJson

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
                                    "brochureImage" -> brochureImage =
                                        if (reader.peek() == JsonReader.Token.NULL) {
                                            reader.skipValue(); null
                                        } else reader.nextString()

                                    "distance" -> distance =
                                        if (reader.peek() == JsonReader.Token.NULL) {
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