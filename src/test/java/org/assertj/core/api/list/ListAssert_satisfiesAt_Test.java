package org.assertj.core.api.list;

import org.assertj.core.api.ListAssert;
import org.assertj.core.api.ListAssertBaseTest;
import org.assertj.core.data.Index;

import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.test.TestData.someIndex;
import static org.mockito.Mockito.verify;

public class ListAssert_satisfiesAt_Test extends ListAssertBaseTest {

  private final Index index = someIndex();
  private final Consumer<String> consumer = str -> assertThat(str).isNotEmpty();

  @Override
  protected ListAssert<String> invoke_api_method() {
    return assertions.satisfiesAt(consumer, index);
  }

  @Override
  protected void verify_internal_effects() {
    verify(lists).satisfiesAt(info(), getActual(assertions), consumer, index);
  }
}
