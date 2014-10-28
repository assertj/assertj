package org.assertj.core.api;

/**
 * A marker interface that can be used to wrap your assertion within assertThat method for better readability.
 * <p>
 * Consider the following MyButton and MyButtonAssert classes:
 * 
 * <pre><code class='java'>
 * public class MyButton extends JButton {
 *
 *   private boolean blinking;
 *
 *   public boolean isBlinking() { return this.blinking; }
 *
 *   public void setBlinking(boolean blink) { this.blinking = blink; }
 *
 * }
 *
 * private static class MyButtonAssert implements AssertDelegateTarget {
 *
 *   private MyButton button;
 *   MyButtonAssert(MyButton button) { this.button = button; }
 *
 *   void isBlinking() {
 *     // standard assertion from core Assertions.assertThat
 *     assertThat(button.isBlinking()).isTrue();
 *   }
 *
 *   void isNotBlinking() {
 *     // standard assertion from core Assertions.assertThat
 *     assertThat(button.isBlinking()).isFalse();
 *   }
 * }
 * </code></pre>
 *
 * As MyButtonAssert implements AssertDelegateTarget, you can use <code>assertThat(buttonAssert).isBlinking();</code>
 * instead of <code>buttonAssert.isBlinking();</code> to have easier to read assertions.
 *
 * <pre><code class='java'>
 * {@literal @}Test
 * public void AssertDelegateTarget_example() {
 *
 *   MyButton button = new MyButton();
 *   MyButtonAssert buttonAssert = new MyButtonAssert(button);
 *
 *   // you can encapsulate MyButtonAssert assertions methods within assertThat
 *   assertThat(buttonAssert).isNotBlinking(); // same as : buttonAssert.isNotBlinking();
 *
 *   button.setBlinking(true);
 *
 *   assertThat(buttonAssert).isBlinking(); // same as : buttonAssert.isBlinking();
 * }
 * </code></pre>
 *
 * @author Christian Rösch
 */
public interface AssertDelegateTarget {
}
