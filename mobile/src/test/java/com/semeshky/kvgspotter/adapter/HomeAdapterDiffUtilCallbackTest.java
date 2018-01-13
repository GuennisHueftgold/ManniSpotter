package com.semeshky.kvgspotter.adapter;


import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class HomeAdapterDiffUtilCallbackTest {

    @Test
    public void areItemsTheSame_should_return_true() {
        List<HomeAdapter.ListItem> items = new ArrayList<>();
        items.add(new HomeAdapter.ListItem(0, 1, "rand1"));
        items.add(new HomeAdapter.ListItem(0, 2, "rand2"));
        items.add(new HomeAdapter.ListItem(0, 1, "rand3"));
        items.add(new HomeAdapter.ListItem(0, 2, "rand4"));
        HomeAdapterDiffUtilCallback callback = new HomeAdapterDiffUtilCallback(items, items);
        assertTrue(callback.areItemsTheSame(0, 0));
        assertTrue(callback.areItemsTheSame(1, 1));
        assertTrue(callback.areItemsTheSame(0, 2));
        assertTrue(callback.areItemsTheSame(1, 3));
    }

    @Test
    public void areItemsTheSame_should_return_false() {
        List<HomeAdapter.ListItem> items = new ArrayList<>();
        items.add(new HomeAdapter.ListItem(0, 1, "rand1"));
        items.add(new HomeAdapter.ListItem(0, 2, "rand2"));
        items.add(new HomeAdapter.ListItem(0, 1, "rand3"));
        items.add(new HomeAdapter.ListItem(0, 2, "rand4"));
        HomeAdapterDiffUtilCallback callback = new HomeAdapterDiffUtilCallback(items, items);
        assertFalse(callback.areItemsTheSame(0, 1));
        assertFalse(callback.areItemsTheSame(1, 2));
        assertFalse(callback.areItemsTheSame(2, 3));
    }
}
