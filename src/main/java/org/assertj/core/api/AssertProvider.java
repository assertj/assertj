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

/**
 * Provides a {@link Assert} for the current object.
 * 
 * <p>Used to map an object to its Assert without having to create a new "Assertions" class.</p>
 * 
 * Usage:
 * <pre><code class='java'> public class Button implements AssertProvider&lt;ButtonAssert&gt; {
 *   public ButtonAssert assertThat() { 
 *     return new ButtonAssert(this); 
 *   } 
 * }
 * 
 * public class ButtonAssert extends Assert&lt;ButtonAssert, Button&gt; {
 *   public ButtonAssert containsText(String text) {
 *     ...
 *   }
 * }
 * 
 * void testMethod() {
 *   Button button = ...;
 *   
 *   // First option
 *   Assertions.assertThat(button).containsText("Test");
 *   
 *   // Second option
 *   button.assertThat().containsText("Test");
 * }</code></pre>
 * 
 * @param <A>
 *          the type of the assert (not typed - to allow any kind of assert)
 * 
 * @author Tobias Liefke
 */
public interface AssertProvider<A> {

  /**
   * Returns the associated {@link Assert} for this object.
   * 
   * @return the assert object for use in conjunction with {@link Assertions#assertThat(AssertProvider)}
   */
  A assertThat();

}
