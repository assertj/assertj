package org.assertj.core.api;

import org.assertj.core.util.Lists;
import org.junit.Rule;
import org.junit.Test;

public class JUnitSoftAssertionsSuccessTest {

    @Rule
    public final JUnitSoftAssertions softly = new JUnitSoftAssertions();

    @Test
    public void all_assertions_should_pass() throws Throwable {
        softly.assertThat(1).isEqualTo(1);
        softly.assertThat(Lists.newArrayList(1, 2)).containsOnly(1, 2);
    }

}
