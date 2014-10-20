package org.assertj.core.api.throwable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionThrownBy;
import static org.assertj.core.api.Fail.failBecauseExceptionWasNotThrown;

import java.util.concurrent.Callable;

import org.junit.Test;

public class ThrowableAssert_built_from_Callable_Test {

  @Test
  public void should_build_ThrowableAssert_with_runtime_exception_thrown_by_runnable_code() {
	// check that actual exception is the one thrown by Callable<Void>#run
	assertThatExceptionThrownBy(new Callable<Void>() {
	  @Override
	  public Void call() {
		throw new IllegalArgumentException("something was wrong");
	  }
	}).isInstanceOf(IllegalArgumentException.class).hasMessage("something was wrong");
  }

  @Test
  public void should_build_ThrowableAssert_with_throwable_thrown_by_runnable_code() {
	assertThatExceptionThrownBy(new Callable<Void>() {
	  @Override
	  public Void call() throws Exception {
		throw new Exception("something was wrong");
	  }
	}).isInstanceOf(Exception.class).hasMessage("something was wrong");
  }

  @Test
  public void should_fail_if_nothing_is_thrown_by_runnable_code() throws Exception {
	try {
	  assertThatExceptionThrownBy(new Callable<Void>() {
		@Override
		public Void call() {
		  // no exception
		  return null;
		}
	  });
	  failBecauseExceptionWasNotThrown(AssertionError.class);
	} catch (AssertionError e) {
	  assertThat(e).hasMessage("Expecting code to throw an exception.");
	}
  }

}
