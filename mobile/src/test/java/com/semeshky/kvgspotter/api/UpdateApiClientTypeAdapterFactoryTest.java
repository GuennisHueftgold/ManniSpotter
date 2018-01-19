package com.semeshky.kvgspotter.api;

import com.google.gson.reflect.TypeToken;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class UpdateApiClientTypeAdapterFactoryTest {
    @Test
    public void create_should_return_correct_values() {
        UpdateApiClientTypeAdapterFactory factory = new UpdateApiClientTypeAdapterFactory();
        TypeToken<Object> token1 = TypeToken.get(Object.class);
        TypeToken<Release> token2 = TypeToken.get(Release.class);
        assertNull(factory.create(null, token1));
        assertNotNull(factory.create(null, token2));
    }
}
