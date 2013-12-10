package org.assertj.core.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runners.model.MultipleFailureException;

public class SoftlyTest {

    //we cannot make it a rule here, because we need to test the failure without this test failing!
    //@Rule
    public final Softly softly = new Softly();

    @Test
    public void testSuccess() throws Throwable {
        softly.assertThat(1).isEqualTo(1);
        softly.assertThat(Lists.newArrayList(1, 2)).containsOnly(1, 2);
        softly.collector.verify();
    }

    @Test
    public void testFail() throws Throwable {
        try {
            softly.assertThat(1).isEqualTo(1);
            softly.assertThat(1).isEqualTo(2);
            softly.assertThat(Lists.newArrayList(1, 2)).containsOnly(1, 3);
            softly.collector.verify();
            fail("Should not reach here");
        } catch (MultipleFailureException e) {
            List<Throwable> failures = e.getFailures();
            assertThat(failures).hasSize(2);
            assertThat(failures.get(0).getMessage()).isEqualTo("expected:<[2]> but was:<[1]>");
            assertThat(failures.get(1).getMessage()).isEqualTo("\n" +
                "Expecting:\n" +
                " <[1, 2]>\n" +
                "to contain only:\n" +
                " <[1, 3]>\n" +
                "elements not found:\n" +
                " <[3]>\n" +
                "and elements not expected:\n" +
                " <[2]>\n");
        }
    }
}
