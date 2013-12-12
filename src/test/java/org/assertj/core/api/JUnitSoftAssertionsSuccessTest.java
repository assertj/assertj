package org.assertj.core.api;

import org.assertj.core.util.Lists;
import org.junit.Rule;
import org.junit.Test;

public class JUnitSoftAssertionsSuccessTest {

    @Rule
    public final JUnitSoftAssertions softly = new JUnitSoftAssertions();

    @Test
    public void testSuccess() throws Throwable {
        softly.assertThat(1).isEqualTo(1);
        softly.assertThat(Lists.newArrayList(1, 2)).containsOnly(1, 2);
    }

}
