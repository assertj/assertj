package org.assertj.core.api;

/**
 * Provides a {@link Assert} for the current object.
 * 
 * <p>Used to map an object to its Assert without having to create a new "Assertions" class.</p>
 * 
 * Usage: <pre><code class='java'>
 * public class Button implements AssertProvider<ButtonAssert> {
 *   public ButtonAssert assertThat() { 
 *     return new ButtonAssert(this); 
 *   } 
 * }
 * 
 * public class ButtonAssert extends Assert<ButtonAssert, Button> {
 *   public ButtonAssert containsText(String text) {
 *     ...
 *   }
 * }
 * 
 * void testMethod() {
 *   Button button = ...;
 *   // First option
 *   Assertions.assertThat(button).containsText("Test");
 *   // Second option
 *   button.assertThat().containsText("Test");
 * }
 * </code></pre>
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
   * @return the assert object for use in cunjunction with {@link Assertions#assertThat(AssertProvider)}
   */
  A assertThat();

}
