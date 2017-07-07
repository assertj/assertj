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

import static org.assertj.core.util.Preconditions.checkArgument;
import static org.assertj.core.util.Preconditions.checkNotNull;

import java.io.File;
import java.nio.charset.Charset;
import org.assertj.core.api.exception.RuntimeIOException;
import org.assertj.core.internal.Files;
import org.assertj.core.util.CheckReturnValue;
import org.assertj.core.util.VisibleForTesting;

/**
 * Base class for all implementations of assertions for {@link File}s.
 * 
 * @param <SELF> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/1IZIRcY"
 *          target="_blank">Emulating 'self types' using Java Generics to simplify fluent API implementation</a>&quot;
 *          for more details.
 * 
 * @author David DIDIER
 * @author Yvonne Wang
 * @author Alex Ruiz
 * @author Olivier Michallat
 * @author Olivier Demeijer
 * @author Mikhail Mazursky
 * @author Jean-Christophe Gay
 */
public abstract class AbstractFileAssert<SELF extends AbstractFileAssert<SELF>> extends AbstractAssert<SELF, File> {

  @VisibleForTesting
  Files files = Files.instance();

  @VisibleForTesting
  Charset charset = Charset.defaultCharset();

  public AbstractFileAssert(File actual, Class<?> selfType) {
    super(actual, selfType);
  }

  /**
   * Verifies that the actual {@code File} exists, regardless it's a file or directory.
   * <p>
   * Example:
   * <pre><code class='java'> File tmpFile = File.createTempFile(&quot;tmp&quot;, &quot;txt&quot;);
   * File tmpDir = Files.createTempDirectory(&quot;tmpDir&quot;).toFile();
   * 
   * // assertions will pass
   * assertThat(tmpFile).exists();
   * assertThat(tmpDir).exists();
   * 
   * tmpFile.delete();
   * tmpDir.delete();
   *
   * // assertions will fail
   * assertThat(tmpFile).exists();
   * assertThat(tmpDir).exists();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code File} is {@code null}.
   * @throws AssertionError if the actual {@code File} does not exist.
   */
  public SELF exists() {
    files.assertExists(info, actual);
    return myself;
  }

  /**
   * Verifies that the actual {@code File} does not exist.
   * <p>
   * Example:
   * <pre><code class='java'> File parentDir = Files.createTempDirectory(&quot;tmpDir&quot;).toFile();
   * File tmpDir = new File(parentDir, &quot;subDir&quot;);
   * File tmpFile = new File(parentDir, &quot;a.txt&quot;);
   * 
   * // assertions will pass
   * assertThat(tmpDir).doesNotExist();
   * assertThat(tmpFile).doesNotExist();
   * 
   * tmpDir.mkdir();
   * tmpFile.createNewFile();
   * 
   * // assertions will fail
   * assertThat(tmpFile).doesNotExist();
   * assertThat(tmpDir).doesNotExist();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code File} is {@code null}.
   * @throws AssertionError if the actual {@code File} exists.
   */
  public SELF doesNotExist() {
    files.assertDoesNotExist(info, actual);
    return myself;
  }

  /**
   * Verifies that the actual {@code File} is an existing file.
   * <p>
   * Example:
   * <pre><code class='java'> File tmpFile = File.createTempFile(&quot;tmp&quot;, &quot;txt&quot;);
   * 
   * // assertion will pass
   * assertThat(tmpFile).isFile();
   * 
   * tmpFile.delete();
   * File tmpDir = Files.createTempDirectory(&quot;tmpDir&quot;).toFile();
   * 
   * // assertions will fail
   * assertThat(tmpFile).isFile();
   * assertThat(tmpDir).isFile();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code File} is {@code null}.
   * @throws AssertionError if the actual {@code File} is not an existing file.
   */
  public SELF isFile() {
    files.assertIsFile(info, actual);
    return myself;
  }

  /**
   * Verifies that the actual {@code File} is an existing directory.
   * <p>
   * Example:
   * <pre><code class='java'> File tmpDir = Files.createTempDirectory(&quot;tmpDir&quot;).toFile();
   * 
   * // assertion will pass
   * assertThat(tmpDir).isDirectory();
   * 
   * tmpDir.delete();
   * File tmpFile = File.createTempFile(&quot;tmp&quot;, &quot;txt&quot;);
   * 
   * // assertions will fail
   * assertThat(tmpFile).isDirectory();
   * assertThat(tmpDir).isDirectory();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code File} is {@code null}.
   * @throws AssertionError if the actual {@code File} is not an existing file.
   */
  public SELF isDirectory() {
    files.assertIsDirectory(info, actual);
    return myself;
  }

  /**
   * Verifies that the actual {@code File} is an absolute path.
   * <p>
   * Example:
   * <pre><code class='java'> File absoluteFile = File.createTempFile(&quot;tmp&quot;, &quot;txt&quot;);
   * 
   * // assertions will pass
   * assertThat(absoluteFile).isAbsolute();
   * 
   * File relativeFile = new File(&quot;./test&quot;);
   * 
   * // assertion will fail
   * assertThat(relativeFile).isAbsolute();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code File} is {@code null}.
   * @throws AssertionError if the actual {@code File} is not an absolute path.
   */
  public SELF isAbsolute() {
    files.assertIsAbsolute(info, actual);
    return myself;
  }

  /**
   * Verifies that the actual {@code File} is a relative path.
   * <p>
   * Example:
   * <pre><code class='java'> File relativeFile = new File(&quot;./test&quot;);
   * 
   * // assertion will pass
   * assertThat(relativeFile).isRelative();
   * 
   * File absoluteFile = File.createTempFile(&quot;tmp&quot;, &quot;txt&quot;);
   * 
   * // assertion will fail
   * assertThat(absoluteFile).isRelative();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code File} is {@code null}.
   * @throws AssertionError if the actual {@code File} is not a relative path.
   */
  public SELF isRelative() {
    files.assertIsRelative(info, actual);
    return myself;
  }

  /**
   * Verifies that the content of the actual {@code File} is equal to the content of the given one.
   * The charset to use when reading the actual file can be provided with {@link #usingCharset(Charset)} or
   * {@link #usingCharset(String)} prior to calling this method; if not, the platform's default charset (as returned by
   * {@link Charset#defaultCharset()}) will be used.
   * 
   * Examples:
   * <pre><code class="java"> // use the default charset
   * File xFile = Files.write(Paths.get("xfile.txt"), "The Truth Is Out There".getBytes()).toFile();
   * File xFileClone = Files.write(Paths.get("xfile-clone.txt"), "The Truth Is Out There".getBytes()).toFile();
   * File xFileFrench = Files.write(Paths.get("xfile-french.txt"), "La Vérité Est Ailleurs".getBytes()).toFile();
   * // use UTF-8 charset
   * File xFileUTF8 = Files.write(Paths.get("xfile-clone.txt"), Arrays.asList("The Truth Is Out There"), Charset.forName("UTF-8")).toFile();
   * 
   * // The following assertion succeeds (default charset is used):
   * assertThat(xFile).hasSameContentAs(xFileClone);
   * // The following assertion succeeds (UTF-8 charset is used to read xFile):
   * assertThat(xFileUTF8).usingCharset("UTF-8").hasContent(xFileClone);
   * 
   * // The following assertion fails:
   * assertThat(xFile).hasSameContentAs(xFileFrench);</code></pre>
   * 
   * @param expected the given {@code File} to compare the actual {@code File} to.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given {@code File} is {@code null}.
   * @throws IllegalArgumentException if the given {@code File} is not an existing file.
   * @throws AssertionError if the actual {@code File} is {@code null}.
   * @throws AssertionError if the actual {@code File} is not an existing file.
   * @throws RuntimeIOException if an I/O error occurs.
   * @throws AssertionError if the content of the actual {@code File} is not equal to the content of the given one.
   *
   * @deprecated use {@link #hasSameContentAs(File)} instead
   */
  @Deprecated
  public SELF hasContentEqualTo(File expected) {
    return hasSameContentAs(expected);
  }

  /**
   * Verifies that the content of the actual {@code File} is equal to the content of the given one.
   * The charset to use when reading the actual file can be provided with {@link #usingCharset(Charset)} or
   * {@link #usingCharset(String)} prior to calling this method; if not, the platform's default charset (as returned by
   * {@link Charset#defaultCharset()}) will be used.
   * 
   * Examples:
   * <pre><code class="java"> // use the default charset
   * File xFile = Files.write(Paths.get("xfile.txt"), "The Truth Is Out There".getBytes()).toFile();
   * File xFileClone = Files.write(Paths.get("xfile-clone.txt"), "The Truth Is Out There".getBytes()).toFile();
   * File xFileFrench = Files.write(Paths.get("xfile-french.txt"), "La Vérité Est Ailleurs".getBytes()).toFile();
   * // use UTF-8 charset
   * File xFileUTF8 = Files.write(Paths.get("xfile-clone.txt"), Arrays.asList("The Truth Is Out There"), Charset.forName("UTF-8")).toFile();
   * 
   * // The following assertion succeeds (default charset is used):
   * assertThat(xFile).hasSameContentAs(xFileClone);
   * // The following assertion succeeds (UTF-8 charset is used to read xFile):
   * assertThat(xFileUTF8).usingCharset("UTF-8").hasContent(xFileClone);
   * 
   * // The following assertion fails:
   * assertThat(xFile).hasSameContentAs(xFileFrench);</code></pre>
   * 
   * @param expected the given {@code File} to compare the actual {@code File} to.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given {@code File} is {@code null}.
   * @throws IllegalArgumentException if the given {@code File} is not an existing file.
   * @throws AssertionError if the actual {@code File} is {@code null}.
   * @throws AssertionError if the actual {@code File} is not an existing file.
   * @throws RuntimeIOException if an I/O error occurs.
   * @throws AssertionError if the content of the actual {@code File} is not equal to the content of the given one.
   */
  public SELF hasSameContentAs(File expected) {
      files.assertSameContentAs(info, actual, charset, expected, Charset.defaultCharset());
      return myself;
  }

  /**
   * Verifies that the content of the actual {@code File} is the same as the expected one, the expected {@code File} being read with the given charset while
   * the charset used to read the actual path can be provided with {@link #usingCharset(Charset)} or
   * {@link #usingCharset(String)} prior to calling this method; if not, the platform's default charset (as returned by
   * {@link Charset#defaultCharset()}) will be used.
   * <p>
   * Examples:
   * <pre><code class="java"> File fileUTF8 = Files.write(Paths.get("actual"), Collections.singleton("Gerçek"), StandardCharsets.UTF_8).toFile();
   * Charset turkishCharset = Charset.forName("windows-1254");
   * File fileTurkischCharset = Files.write(Paths.get("expected"), Collections.singleton("Gerçek"), turkishCharset).toFile();
   * 
   * // The following assertion succeeds:
   * assertThat(fileUTF8).usingCharset(StandardCharsets.UTF_8).hasSameContentAs(fileTurkischCharset, turkishCharset);
   * 
   * // The following assertion fails:
   * assertThat(fileUTF8).usingCharset(StandardCharsets.UTF_8).hasSameContentAs(fileTurkischCharset, StandardCharsets.UTF_8);</code></pre>
   *
   * @param expected the given {@code File} to compare the actual {@code File} to.
   * @param expectedCharset the {@link Charset} used to read the content of the expected file.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given {@code File} is {@code null}.
   * @throws IllegalArgumentException if the given {@code File} is not an existing file.
   * @throws AssertionError if the actual {@code File} is {@code null}.
   * @throws AssertionError if the actual {@code File} is not an existing file.
   * @throws RuntimeIOException if an I/O error occurs.
   * @throws AssertionError if the content of the actual {@code File} is not equal to the content of the given one.
   */
  public SELF hasSameContentAs(File expected, Charset expectedCharset) {
      files.assertSameContentAs(info, actual, charset, expected, expectedCharset);
      return myself;
  }

  /**
   * Verifies that the binary content of the actual {@code File} is <b>exactly</b> equal to the given one.
   * <p>
   * Example:
   * <pre><code class='java'> File bin = File.createTempFile(&quot;tmp&quot;, &quot;bin&quot;);
   * Files.write(bin.toPath(), new byte[] {1, 1});
   * 
   * // assertion will pass
   * assertThat(bin).hasBinaryContent(new byte[] {1, 1});
   * 
   * // assertions will fail
   * assertThat(bin).hasBinaryContent(new byte[] { });
   * assertThat(bin).hasBinaryContent(new byte[] {0, 0});</code></pre>
   *
   * @param expected the expected binary content to compare the actual {@code File}'s content to.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given content is {@code null}.
   * @throws AssertionError if the actual {@code File} is {@code null}.
   * @throws AssertionError if the actual {@code File} is not an existing file.
   * @throws RuntimeIOException if an I/O error occurs.
   * @throws AssertionError if the content of the actual {@code File} is not equal to the given binary content.
   */
  public SELF hasBinaryContent(byte[] expected) {
    files.assertHasBinaryContent(info, actual, expected);
    return myself;
  }

  /**
   * Specifies the name of the charset to use for text-based assertions on the file's contents.
   * 
   * @param charsetName the name of the charset to use.
   * @return {@code this} assertion object.
   * @throws IllegalArgumentException if the given encoding is not supported on this platform.
   */
  @CheckReturnValue
  public SELF usingCharset(String charsetName) {
    checkArgument(Charset.isSupported(charsetName), "Charset:<'%s'> is not supported on this system", charsetName);
    return usingCharset(Charset.forName(charsetName));
  }

  /**
   * Specifies the charset to use for text-based assertions on the file's contents.
   * 
   * @param charset the charset to use.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given charset is {@code null}.
   */
  @CheckReturnValue
  public SELF usingCharset(Charset charset) {
    this.charset = checkNotNull(charset, "The charset should not be null");
    return myself;
  }

  /**
   * Verifies that the text content of the actual {@code File} is <b>exactly</b> equal to the given one.<br>
   * The charset to use when reading the file should be provided with {@link #usingCharset(Charset)} or
   * {@link #usingCharset(String)} prior to calling this method; if not, the platform's default charset (as returned by
   * {@link Charset#defaultCharset()}) will be used.
   * <p>
   * Example:
   * <pre><code class='java'> // use the default charset
   * File xFile = Files.write(Paths.get(&quot;xfile.txt&quot;), &quot;The Truth Is Out There&quot;.getBytes()).toFile();
   * 
   * // The following assertion succeeds (default charset is used):
   * assertThat(xFile).hasContent(&quot;The Truth Is Out There&quot;);
   * 
   * // The following assertion fails:
   * assertThat(xFile).hasContent(&quot;La Vérité Est Ailleurs&quot;);
   * 
   * // using a specific charset 
   * Charset turkishCharset = Charset.forName(&quot;windows-1254&quot;);
   * 
   * File xFileTurkish = Files.write(Paths.get(&quot;xfile.turk&quot;), Collections.singleton(&quot;Gerçek&quot;), turkishCharset).toFile();
   * 
   * // The following assertion succeeds:
   * assertThat(xFileTurkish).usingCharset(turkishCharset).hasContent(&quot;Gerçek&quot;);
   * 
   * // The following assertion fails :
   * assertThat(xFileTurkish).usingCharset(StandardCharsets.UTF_8).hasContent(&quot;Gerçek&quot;);</code></pre>
   *
   * @param expected the expected text content to compare the actual {@code File}'s content to.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given content is {@code null}.
   * @throws AssertionError if the actual {@code File} is {@code null}.
   * @throws AssertionError if the actual {@code File} is not an existing file.
   * @throws RuntimeIOException if an I/O error occurs.
   * @throws AssertionError if the content of the actual {@code File} is not equal to the given content.
   */
  public SELF hasContent(String expected) {
    files.assertHasContent(info, actual, expected, charset);
    return myself;
  }

  /**
   * Verifies that the actual {@code File} can be modified by the application.
   * <p>
   * Example:
   * <pre><code class='java'> File tmpFile = File.createTempFile(&quot;tmp&quot;, &quot;txt&quot;);
   * File tmpDir = Files.createTempDirectory(&quot;tmp&quot;).toFile();
   * 
   * // assertions will pass
   * assertThat(tmpFile).canWrite();
   * assertThat(tmpDir).canWrite();
   * 
   * tmpFile.setReadOnly();
   * tmpDir.setReadOnly();
   * 
   * // assertions will fail
   * assertThat(tmpFile).canWrite();
   * assertThat(tmpDir).canWrite();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code File} is {@code null}.
   * @throws AssertionError if the actual {@code File} can not be modified by the application.
   */
  public SELF canWrite() {
    files.assertCanWrite(info, actual);
    return myself;
  }

  /**
   * Verifies that the actual {@code File} can be read by the application.
   * <p>
   * Example:
   * <pre><code class='java'> File tmpFile = File.createTempFile(&quot;tmp&quot;, &quot;txt&quot;);
   * File tmpDir = Files.createTempDirectory(&quot;tmp&quot;).toFile();
   * 
   * // assertions will pass
   * assertThat(tmpFile).canRead();
   * assertThat(tmpDir).canRead();
   * 
   * tmpFile.setReadable(false);
   * tmpDir.setReadable(false);
   * 
   * // assertions will fail
   * assertThat(tmpFile).canRead();
   * assertThat(tmpDir).canRead();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code File} is {@code null}.
   * @throws AssertionError if the actual {@code File} can not be read by the application.
   */
  public SELF canRead() {
    files.assertCanRead(info, actual);
    return myself;
  }

  /**
   * Verifies that the actual {@code File} has given parent.
   *
   * <p>
   * Example:
   * <pre><code class='java'> File xFile = new File(&quot;mulder/xFile&quot;);
   * 
   * // assertion will pass
   * assertThat(xFile).hasParent(new File(&quot;mulder&quot;));
   *
   * // assertion will fail
   * assertThat(xFile).hasParent(new File(&quot;scully&quot;));</code></pre>
   * 
   * @param expected the expected parent {@code File}.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the expected parent {@code File} is {@code null}.
   * @throws RuntimeIOException if an I/O error occurs.
   * @throws AssertionError if the actual {@code File} is {@code null}.
   * @throws AssertionError if the actual {@code File} parent is not equal to the expected one.
   * 
   * @see java.io.File#getParentFile() parent definition.
   */
  public SELF hasParent(File expected) {
    files.assertHasParent(info, actual, expected);
    return myself;
  }

  /**
   * Same as {@link #hasParent(java.io.File)} but takes care of converting given {@code String} as {@code File} for you
   * 
   * <p>
   * Example:
   * <pre><code class='java'> File xFile = new File(&quot;mulder/xFile&quot;);
   * 
   * // assertion will pass
   * assertThat(xFile).hasParent(&quot;mulder&quot;);
   *
   * // assertion will fail
   * assertThat(xFile).hasParent(&quot;scully&quot;);</code></pre>
   * 
   * @param expected the expected parent file path.
   * @return {@code this} assertion object.
   */
  public SELF hasParent(String expected) {
    files.assertHasParent(info, actual, expected != null ? new File(expected) : null);
    return myself;
  }

  /**
   * Verifies that the actual {@code File} has given extension.
   * 
   * <p>
   * Example:
   * <pre><code class='java'> File xFile = new File(&quot;xFile.java&quot;);
   * 
   * // assertion will pass
   * assertThat(xFile).hasExtension(&quot;java&quot;);
   * 
   * // assertion will fail
   * assertThat(xFile).hasExtension(&quot;png&quot;);</code></pre>
   * 
   * @param expected the expected extension, it does not contains the {@code '.'}
   * @return {@code this} assertion object.
   * @throws NullPointerException if the expected extension is {@code null}.
   * @throws AssertionError if the actual {@code File} is {@code null}.
   * @throws AssertionError if the actual {@code File} is not a file (ie a directory).
   * @throws AssertionError if the actual {@code File} does not have the expected extension.
   * 
   * @see <a href="http://en.wikipedia.org/wiki/Filename_extension">Filename extension</a>
   */
  public SELF hasExtension(String expected) {
    files.assertHasExtension(info, actual, expected);
    return myself;
  }

  /**
   * Verifies that the actual {@code File} has given name.
   * 
   * <p>
   * Example:
   * <pre><code class='java'> File xFile = new File(&quot;somewhere/xFile.java&quot;);
   * File xDirectory = new File(&quot;somewhere/xDirectory&quot;);
   * 
   * // assertion will pass
   * assertThat(xFile).hasName(&quot;xFile.java&quot;);
   * assertThat(xDirectory).hasName(&quot;xDirectory&quot;);
   * 
   * // assertion will fail
   * assertThat(xFile).hasName(&quot;xFile&quot;);
   * assertThat(xDirectory).hasName(&quot;somewhere&quot;);</code></pre>
   * 
   * @param expected the expected {@code File} name.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the expected name is {@code null}.
   * @throws AssertionError if the actual {@code File} is {@code null}.
   * @throws AssertionError if the actual {@code File} does not have the expected name.
   * 
   * @see java.io.File#getName() name definition.
   */
  public SELF hasName(String expected) {
    files.assertHasName(info, actual, expected);
    return myself;
  }

  /**
   * Verifies that the actual {@code File} does not have a parent.
   * 
   * <p>
   * Example:
   * <pre><code class='java'> File xFile = new File(&quot;somewhere/xFile.java&quot;);
   * File xDirectory = new File(&quot;xDirectory&quot;);
   * 
   * // assertion will pass
   * assertThat(xDirectory).hasNoParent();
   * 
   * // assertion will fail
   * assertThat(xFile).hasNoParent();</code></pre>
   * 
   * @return {@code this} assertion object.
   * 
   * @throws AssertionError if the actual {@code File} is {@code null}.
   * @throws AssertionError if the actual {@code File} has a parent.
   */
  public SELF hasNoParent() {
    files.assertHasNoParent(info, actual);
    return myself;
  }
}
