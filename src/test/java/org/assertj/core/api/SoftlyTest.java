package org.assertj.core.api;

import org.assertj.core.util.Lists;
import org.junit.Rule;
import org.junit.Test;

public class SoftlyTest {

    @Rule
    public final Softly softly = new Softly();

    @Test
    public void testSoftly() throws Exception {
        softly.assertThat(1).isEqualTo(1);
        softly.assertThat(Lists.newArrayList(1, 2)).containsOnly(1, 2);
    }
}
