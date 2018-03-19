package org.assertj.core.internal.lists;

import static org.assertj.core.data.Index.atIndex;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.Lists.newArrayList;

import java.util.List;
import java.util.function.Consumer;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.api.Assertions;
import org.assertj.core.data.Index;
import org.assertj.core.internal.ListsBaseTest;
import org.junit.Test;

public class Lists_satisfiesAt_Test extends ListsBaseTest {

  private final AssertionInfo info = someInfo();
  private final Consumer<String> requirements = str -> Assertions.assertThat(str).isEqualTo("Luke");
  private final Index index = atIndex(1);
  private final List<String> actual = newArrayList("Leia", "Luke", "Yoda");

  @Test
  public void should_pass_if_actual_match_requirements() {
    lists.satisfiesAt(info, actual, requirements, index);
  }

  @Test
  public void should_fail_if_actual_dont_match_requirements() {
    thrown.expectAssertionError("expected:<\"[Luke]\"> but was:<\"[Yoda]\">");
    lists.satisfiesAt(info, actual, requirements, atIndex(2));
  }

  @Test
  public void should_fail_if_index_is_out_of_bound() {
    thrown.expectIndexOutOfBoundsException("Index should be between <0> and <2> (inclusive,) but was:%n <3>");
    lists.satisfiesAt(info, actual, requirements, atIndex(3));
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(shouldNotBeNull());
    lists.satisfiesAt(info, null, requirements, index);
  }

  @Test
  public void should_fail_if_requirements_are_null() {
    thrown.expectNullPointerException("The Consumer<? super T> expressing the assertions requirements must not be null");
    lists.satisfiesAt(info, actual, null, index);
  }

  @Test
  public void should_fail_if_index_is_null() {
    thrown.expectNullPointerException("Index should not be null");
    lists.satisfiesAt(info, actual, requirements, null);
  }
}
