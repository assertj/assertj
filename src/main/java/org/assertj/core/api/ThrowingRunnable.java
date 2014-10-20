package org.assertj.core.api;

/**
 * Models a Runnable that can throw a Throwable when executed.
 */
public interface ThrowingRunnable {
  void run() throws Throwable;
}
