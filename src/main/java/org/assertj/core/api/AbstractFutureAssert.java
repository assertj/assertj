/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.api;

import java.util.concurrent.Future;

import org.assertj.core.internal.Futures;
import org.assertj.core.util.VisibleForTesting;

public abstract class AbstractFutureAssert<SELF extends AbstractFutureAssert<SELF, ACTUAL, RESULT>, ACTUAL extends Future<RESULT>, RESULT> extends
    AbstractAssert<SELF, ACTUAL> {

  @VisibleForTesting
  Futures futures = Futures.instance();

  protected AbstractFutureAssert(ACTUAL actual, Class<?> selfType) {
    super(actual, selfType);
  }

  /**
   * Verifies that the {@link Future} is cancelled.
   * <p>
   * Example:
   * <pre><code class='java'> ExecutorService executorService = Executors.newSingleThreadExecutor();
   *                                                                        
   * Future&lt;String&gt; future = executorService.submit(new Callable&lt;String&gt;() {
   *   {@literal @}Override                                                            
   *   public String call() throws Exception {                              
   *     return "done";                                                     
   *   }                                                                    
   * });                                                                    
   *                                                                        
   * // assertion will fail:
   * assertThat(future).isCancelled();
   * 
   * // assertion will pass:
   * future.cancel(true);
   * assertThat(future).isCancelled();</code></pre>
   *
   * @return this assertion object.
   *
   * @see Future#isCancelled()
   * @since 2.7.0 / 3.7.0
   */
  public SELF isCancelled() {
    futures.assertIsCancelled(info, actual);
    return myself;
  }

  /**
   * Verifies that the {@link Future} is not cancelled.
   * <p>
   * Example:
   * <pre><code class='java'> ExecutorService executorService = Executors.newSingleThreadExecutor();
   *                                                                        
   * Future&lt;String&gt; future = executorService.submit(new Callable&lt;String&gt;() {
   *   {@literal @}Override                                                            
   *   public String call() throws Exception {                              
   *     return "done";                                                     
   *   }                                                                    
   * });                                                                    
   *                                                                        
   * // assertion will pass:
   * assertThat(future).isNotCancelled();
   *
   * // assertion will fail:
   * future.cancel(true);
   * assertThat(future).isNotCancelled();</code></pre>
   *
   * @return this assertion object.
   *
   * @see Future#isCancelled()
   * @since 2.7.0 / 3.7.0
   */
  public SELF isNotCancelled() {
    futures.assertIsNotCancelled(info, actual);
    return myself;
  }

  /**
   * Verifies that the {@link Future} is done.
   * <p>
   * Example:
   * <pre><code class='java'> ExecutorService executorService = Executors.newSingleThreadExecutor();
   *                                                                        
   * Future&lt;String&gt; future = executorService.submit(new Callable&lt;String&gt;() {
   *   {@literal @}Override                                                            
   *   public String call() throws Exception {                              
   *     return "done";                                                     
   *   }                                                                    
   * });                                                                    
   *                                                                        
   * // assertion will pass:
   * assertThat(future).isDone();
   *
   * future = executorService.submit(new Callable&lt;String&gt;() {
   *   {@literal @}Override                                                            
   *   public String call() throws Exception {                              
   *     Thread.sleep(1000);
   *     return "done";                                                     
   *   }                                                                    
   * });                                                                    
   *                                                                        
   * // assertion will fail:
   * assertThat(future).isDone();</code></pre>
   *
   * @return this assertion object.
   *
   * @see Future#isDone()
   * @since 2.7.0 / 3.7.0
   */
  public SELF isDone() {
    futures.assertIsDone(info, actual);
    return myself;
  }

  /**
   * Verifies that the {@link Future} is not done.
   * <p>
   * Example:
   * <pre><code class='java'> ExecutorService executorService = Executors.newSingleThreadExecutor();
   *                                                                        
   * Future&lt;String&gt; future = executorService.submit(new Callable&lt;String&gt;() {
   *   {@literal @}Override                                                            
   *   public String call() throws Exception {                              
   *     Thread.sleep(1000);
   *     return "done";                                                     
   *   }                                                                    
   * });
   *                                                                     
   * // assertion will pass:
   * assertThat(future).isNotDone();
   *                                                                        
   * future = executorService.submit(new Callable&lt;String&gt;() {
   *   {@literal @}Override                                                            
   *   public String call() throws Exception {                              
   *     return "done";                                                     
   *   }                                                                    
   * });                                                             
   *
   * // assertion will fail:
   * assertThat(future).isNotDone();</code></pre>
   *
   * @return this assertion object.
   *
   * @see Future#isDone()
   * @since 2.7.0 / 3.7.0
   */
  public SELF isNotDone() {
    futures.assertIsNotDone(info, actual);
    return myself;
  }
}
