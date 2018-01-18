package com.semeshky.kvgspotter.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
            assertEquals("expected parse: " + input[i], outputs[i], SemVer.parse(input[i]));
        }
    }
}
