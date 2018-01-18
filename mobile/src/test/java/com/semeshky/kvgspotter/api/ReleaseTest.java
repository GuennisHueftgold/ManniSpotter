package com.semeshky.kvgspotter.api;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReleaseTest {

    private Release.TypeAdapter adapter;

    static Release.Builder createBuilder(int index) {
        return new Release.Builder()
                .setName("Release 1.0." + index)
                .setTagName("1.0." + index)
                .setPreRelease(index % 2 == 0)
                .setDraft(index % 2 == 1)
                .setId(index)
                .setHtmlUrl("https://test.url/" + index)
                .setUrl("https://test.url/" + index + "/url");
    }

    @Before
    public void setup() {
        this.adapter = new Release.TypeAdapter();
    }

    @Test
    public void should_convert_complete_object() throws IOException {
        final Release original = createBuilder(29).build();
        final Release output = adapter.fromJson(adapter.toJson(original));
        assertEquals(original, output);
    }

    @Test
    public void TypeAdapter_should_handle_read_null() throws IOException {
        assertNull(adapter.fromJson("null"));
    }

    @Test
    public void TypeAdapter_should_handle_write_null() throws IOException {
        JsonWriter jsonWriter = mock(JsonWriter.class);
        adapter.write(jsonWriter, null);
        verify(jsonWriter, times(1)).nullValue();
    }

    @Test
    public void TypeAdapter_should_handle_unknown_name() throws IOException {
        JsonReader jsonReader = mock(JsonReader.class);
        when(jsonReader.peek()).thenReturn(JsonToken.BEGIN_OBJECT, JsonToken.STRING);
        when(jsonReader.hasNext()).thenReturn(true, false);
        when(jsonReader.nextName()).thenReturn("random_name");
        assertNotNull(adapter.read(jsonReader));
        verify(jsonReader, times(1)).skipValue();
    }
}
