package org.assertj.core.api;

/**
 * A marker interface that can be used to adapt your test code to the assertion-style.
 * <p>
 * Consider the following class:
 * 
 * <pre>
 * public class ServerSocketAssertion implements AutoAssert {
 *   private final ServerSocket socket;
 * 
 *   public ServerSocketAssertion(ServerSocket socket) {
 *     this.socket = socket;
 *   }
 * 
 *   public ServerSocketAssert isConnectedTo(int port) {
 *     assertThat(socket.isBound()).isTrue();
 *     assertThat(socket.getLocalPort()).isEqualTo(port);
 *     assertThat(socket.isClosed()).isFalse();
 *     return this;
 *   }
 * }
 * </pre>
 * 
 * You could then use <code>assertThat(serverSocket).isConnectedTo(80);</code> where <code>serverSocket</code> is an
 * object of <code>ServerSocketAssertion</code>.
 * 
 * @author Christian Rösch
 */
public interface AutoAssert {
}
