package com.semeshky.kvgspotter.adapter;


import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class HomeAdapterDiffUtilCallbackTest {

    @Test
    public void constructor() {
        List<HomeAdapter.ListItem> items1 = new ArrayList<>();
        items1.add(new HomeAdapter.ListItem(0, 1, "rand1"));
        items1.add(new HomeAdapter.ListItem(0, 2, "rand2"));
        items1.add(new HomeAdapter.ListItem(0, 1, "rand3"));
        items1.add(new HomeAdapter.ListItem(0, 2, "rand4"));
        List<HomeAdapter.ListItem> items2 = new ArrayList<>();
        items2.add(new HomeAdapter.ListItem(0, 1, "rand1"));
        items2.add(new HomeAdapter.ListItem(0, 2, "rand2"));
        items2.add(new HomeAdapter.ListItem(0, 1, "rand3"));
        HomeAdapterDiffUtilCallback callback = new HomeAdapterDiffUtilCallback(items1, items2);
        assertEquals(items1, callback.mOldList);
        assertEquals(items2, callback.mNewList);
    }

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

    @Test
    public void areContentsTheSame_should_return_true() {
        List<HomeAdapter.ListItem> items = new ArrayList<>();
        items.add(new HomeAdapter.ListItem(0, 1, "rand1"));
        items.add(new HomeAdapter.ListItem(0, 2, "rand2"));
        items.add(new HomeAdapter.ListItem(0, 1, "rand3"));
        items.add(new HomeAdapter.ListItem(0, 2, "rand4"));
        HomeAdapterDiffUtilCallback callback = new HomeAdapterDiffUtilCallback(items, items);
        assertTrue(callback.areContentsTheSame(0, 0));
        assertTrue(callback.areContentsTheSame(1, 1));
        assertTrue(callback.areContentsTheSame(2, 2));
        assertTrue(callback.areContentsTheSame(3, 3));
    }

    @Test
    public void areContentsTheSame_should_return_false() {
        List<HomeAdapter.ListItem> items = new ArrayList<>();
        items.add(new HomeAdapter.ListItem(0, 1, "rand1"));
        items.add(new HomeAdapter.ListItem(0, 2, "rand2"));
        items.add(new HomeAdapter.ListItem(0, 1, "rand3"));
        items.add(new HomeAdapter.ListItem(0, 2, "rand4"));
        HomeAdapterDiffUtilCallback callback = new HomeAdapterDiffUtilCallback(items, items);
        assertFalse(callback.areContentsTheSame(0, 1));
        assertFalse(callback.areContentsTheSame(1, 2));
        assertFalse(callback.areContentsTheSame(2, 3));
    }
}
