package com.semeshky.kvgspotter.util;

import org.junit.Test;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class SemVerTest {

    @Test
    public void parse_should_work_correctly() {
        final String[] input = {
                "v1.5.2",
                "v1.5.2-appendix",
                "v0.3.21-appendix",
                "v9.2-appendix",
                "1.5.2",
                "1.5.2-appendix",
                "0.3.21-appendix",
                "9.2-appendix",
        };
        final SemVer[] outputs = {
                new SemVer(1, 5, 2, null),
                new SemVer(1, 5, 2, "appendix"),
                new SemVer(0, 3, 21, "appendix"),
                new SemVer(9, 2, 0, "appendix"),
                new SemVer(1, 5, 2, null),
                new SemVer(1, 5, 2, "appendix"),
                new SemVer(0, 3, 21, "appendix"),
                new SemVer(9, 2, 0, "appendix")
        };
        for (int i = 0; i < input.length; i++) {
            final SemVer testSemVer = SemVer.parse(input[i]);
            assertEquals(testSemVer.getMajor(), outputs[i].getMajor());
            assertEquals(testSemVer.getMinor(), outputs[i].getMinor());
            assertEquals(testSemVer.getPatch(), outputs[i].getPatch());
            assertEquals(testSemVer.getAppendix(), outputs[i].getAppendix());
        }
    }


    @Test(expected = RuntimeException.class)
    public void parse_should_throw_error() {
        SemVer.parse("some_random_string");
    }

    @Test
    public void isNewer_should_return_true() {
        SemVer semVer1 = new SemVer(3, 3, 3);
        assertTrue(semVer1.isNewer(new SemVer(3, 3, 1)));
        assertTrue(semVer1.isNewer(new SemVer(3, 1, 3)));
        assertTrue(semVer1.isNewer(new SemVer(1, 3, 3)));
        assertTrue(semVer1.isNewer(new SemVer(2, 3, 10)));
        assertTrue(semVer1.isNewer(new SemVer(2, 10, 1)));
    }

    @Test
    public void isNewer_should_return_false() {
        SemVer semVer1 = new SemVer(3, 3, 3);
        assertFalse(semVer1.isNewer(semVer1));
        assertFalse(semVer1.isNewer(new SemVer(3, 3, 4)));
        assertFalse(semVer1.isNewer(new SemVer(3, 4, 3)));
        assertFalse(semVer1.isNewer(new SemVer(4, 3, 3)));
        assertFalse(semVer1.isNewer(new SemVer(3, 4, 2)));
        assertFalse(semVer1.isNewer(new SemVer(4, 3, 2)));
        assertFalse(semVer1.isNewer(new SemVer(4, 2, 3)));
        assertFalse(semVer1.isNewer(new SemVer(4, 2, 2)));
    }

    @Test
    public void equals_should_return_true() {
        SemVer semVer1 = new SemVer(2, 3, 4);
        SemVer semVer2 = new SemVer(2, 3, 4, "appendix");
        assertTrue(semVer1.equals(semVer1));
        assertTrue(semVer2.equals(semVer2));
        assertTrue(semVer1.equals(new SemVer(2, 3, 4)));
        assertTrue(semVer2.equals(new SemVer(2, 3, 4, "appendix")));
    }

    @Test
    public void equals_should_return_false() {
        SemVer semVer = new SemVer(2, 3, 4);
        assertFalse(semVer.equals(new SemVer(1, 3, 4)));
        assertFalse(semVer.equals(new SemVer(2, 1, 4)));
        assertFalse(semVer.equals(new SemVer(2, 3, 1)));
        assertFalse(semVer.equals(new SemVer(2, 3, 4, "appendix")));
        assertFalse(semVer.equals(null));
        assertFalse(semVer.equals(new Object()));
    }

    @Test
    public void hashCode_should_return_equal() {
        SemVer semVer1 = new SemVer(2, 3, 4);
        SemVer semVer2 = new SemVer(2, 3, 4, "append");
        assertEquals(semVer1.hashCode(), new SemVer(2, 3, 4).hashCode());
        assertEquals(semVer2.hashCode(), new SemVer(2, 3, 4, "append").hashCode());
    }

    @Test
    public void hashCode_should_return_not_equal() {
        SemVer semVer = new SemVer(2, 3, 4);
        assertNotEquals(semVer.hashCode(), new SemVer(2, 3, 1).hashCode());
        assertNotEquals(semVer.hashCode(), new SemVer(2, 1, 4).hashCode());
        assertNotEquals(semVer.hashCode(), new SemVer(1, 3, 4).hashCode());
        assertNotEquals(semVer.hashCode(), new SemVer(2, 3, 4, "appe").hashCode());
    }

    @Test
    public void toString_should_not_return_null() {
        final SemVer semVer1 = new SemVer(2, 3, 4);
        assertThat(semVer1.toString(), instanceOf(String.class));
        assertEquals("SemVer{2.3.4}", semVer1.toString());
        final SemVer semVer2 = new SemVer(2, 3, 4, "appendix");
        assertThat(semVer2.toString(), instanceOf(String.class));
        assertEquals("SemVer{2.3.4-appendix}", semVer2.toString());

    }
}
