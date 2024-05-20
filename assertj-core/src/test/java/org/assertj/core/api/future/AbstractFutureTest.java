/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.api.future;

import static java.lang.String.format;

import java.lang.Thread.UncaughtExceptionHandler;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

abstract class AbstractFutureTest {
  protected ExecutorService executorService;
  private final ThreadFactory factory;

  AbstractFutureTest() {
    factory = new FutureTestThreadFactory();
  }

  @BeforeEach
  void beforeEach() {
    executorService = Executors.newSingleThreadExecutor(factory);
  }

  @AfterEach
  void afterEach() {
    executorService.shutdownNow();
  }

  private class FutureTestThreadFactory implements ThreadFactory, UncaughtExceptionHandler {
    private final Logger logger;
    private final AtomicInteger count;

    private FutureTestThreadFactory() {
      logger = LoggerFactory.getLogger(AbstractFutureTest.this.getClass());
      count = new AtomicInteger(0);
    }

    @Override
    public Thread newThread(Runnable runnable) {
      Thread thread = new Thread(runnable);
      thread.setName(AbstractFutureTest.this.getClass().getName() + "." + count.incrementAndGet());
      thread.setDaemon(true);
      thread.setPriority(Thread.MAX_PRIORITY);
      thread.setUncaughtExceptionHandler(this);
      thread.setContextClassLoader(Thread.currentThread().getContextClassLoader());
      return thread;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
      logger.info(
                  ex,
                  () -> format("Thread %s [%s] threw an exception", thread.getName(), thread.getId()));
    }
  }

  protected static <U> CompletableFuture<U> completedFutureAfter(U value, long sleepDuration, ExecutorService service) {
    CompletableFuture<U> completableFuture = new CompletableFuture<>();
    service.submit(() -> {
      Thread.sleep(sleepDuration);
      completableFuture.complete(value);
      return null;
    });
    return completableFuture;
  }

  protected static <U> CompletableFuture<U> completedFutureAfter(U value, Duration sleepDuration, ExecutorService service) {
    return completedFutureAfter(value, sleepDuration.toMillis(), service);
  }
}
