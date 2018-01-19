package com.semeshky.kvgspotter.api;

import android.content.Context;

import com.semeshky.kvgspotter.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class KvgApiClientTest {
    private Context context;

    @Before
    public void setup() {
        context = RuntimeEnvironment.application;
        KvgApiClient.init(context);
    }

    @Test
    public void init_should_init_members() {
        KvgApiClient kvgApiClient = new KvgApiClient(context);
        assertNotNull(kvgApiClient.mTrapezeApiClient);
        assertNotNull(kvgApiClient.mUpdateApiClient);
    }

    @Test
    public void getUpdateService_should_not_be_null() {
        assertNotNull(KvgApiClient.getUpdateService());
    }

    @Test
    public void getService_should_not_be_null() {
        assertNotNull(KvgApiClient.getService());
    }
}
