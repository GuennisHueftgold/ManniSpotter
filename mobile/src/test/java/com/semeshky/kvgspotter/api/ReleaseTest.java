package com.semeshky.kvgspotter.api;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
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

    @Test
    public void equals_should_return_true() {
        Release release1 = createBuilder(29).build();
        Release release2 = createBuilder(29).build();
        assertTrue(release1.equals(release1));
        assertTrue(release1.equals(release2));
    }

    @Test
    public void equals_should_return_false() {
        Release release1 = createBuilder(29).build();
        assertFalse(release1.equals(null));
        assertFalse(release1.equals(new Object()));
        Release release2 = createBuilder(29)
                .setUrl("other_url")
                .build();
        assertFalse(release1.equals(release2));
        release2 = createBuilder(29)
                .setId(-2999)
                .build();
        assertFalse(release1.equals(release2));
        release2 = createBuilder(29)
                .setTagName("other_url")
                .build();
        assertFalse(release1.equals(release2));
        release2 = createBuilder(29)
                .setName("other_url")
                .build();
        assertFalse(release1.equals(release2));
        release2 = createBuilder(29)
                .setDraft(!release1.isDraft())
                .build();
        assertFalse(release1.equals(release2));
        release2 = createBuilder(29)
                .setPreRelease(!release1.isPreRelease())
                .build();
        assertFalse(release1.equals(release2));
        release2 = createBuilder(29)
                .setHtmlUrl("other_url")
                .build();
        assertFalse(release1.equals(release2));
    }

    @Test
    public void hashCode_should_return_equal_values() {
        Release release1 = createBuilder(29).build();
        Release release2 = createBuilder(29).build();
        assertEquals(release1.hashCode(), release2.hashCode());
    }

    @Test
    public void hashCode_should_return_no_equal_values() {
        Release release1 = createBuilder(29).build();
        Release release2 = createBuilder(29)
                .setUrl("other_url")
                .build();
        assertNotEquals(release1.hashCode(), release2.hashCode());
        release2 = createBuilder(29)
                .setId(-219)
                .build();
        assertNotEquals(release1.hashCode(), release2.hashCode());
        release2 = createBuilder(29)
                .setTagName("other_url")
                .build();
        assertNotEquals(release1.hashCode(), release2.hashCode());
        release2 = createBuilder(29)
                .setName("other_url")
                .build();
        assertNotEquals(release1.hashCode(), release2.hashCode());
        release2 = createBuilder(29)
                .setDraft(!release1.isDraft())
                .build();
        assertNotEquals(release1.hashCode(), release2.hashCode());
        release2 = createBuilder(29)
                .setPreRelease(!release1.isPreRelease())
                .build();
        assertNotEquals(release1.hashCode(), release2.hashCode());
        release2 = createBuilder(29)
                .setHtmlUrl("other_url")
                .build();
        assertNotEquals(release1.hashCode(), release2.hashCode());
    }
}
