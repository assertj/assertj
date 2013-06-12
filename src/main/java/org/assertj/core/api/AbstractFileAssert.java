/*
 * Created on Jan 28, 2011
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright @2011-2012 the original author or authors.
 */
package org.assertj.core.api;

import java.io.File;
import java.nio.charset.Charset;

import org.assertj.core.internal.Files;
import org.assertj.core.util.FilesException;
import org.assertj.core.util.VisibleForTesting;


/**
 * Base class for all implementations of assertions for {@link File}s.
 * @param <S> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/anMa4g"
 *          target="_blank">Emulating 'self types' using Java Generics to simplify fluent API implementation</a>&quot;
 *          for more details.
 *
 * @author David DIDIER
 * @author Yvonne Wang
 * @author Alex Ruiz
 * @author Olivier Michallat
 * @author Olivier Demeijer
 * @author Mikhail Mazursky
 */
public abstract class AbstractFileAssert<S extends AbstractFileAssert<S>> extends AbstractAssert<S, File> {

	@VisibleForTesting
	Files files = Files.instance();

	@VisibleForTesting
	Charset charset = Charset.defaultCharset();

	protected AbstractFileAssert(File actual, Class<?> selfType) {
		super(actual, selfType);
	}

	/**
	 * Verifies that the actual {@code File} exists, regardless it's a file or directory.
	 * @return {@code this} assertion object.
	 * @throws AssertionError if the actual {@code File} is {@code null}.
	 * @throws AssertionError if the actual {@code File} does not exist.
	 */
	public S exists() {
		files.assertExists(info, actual);
		return myself;
	}

	/**
	 * Verifies that the actual {@code File} does not exist.
	 * @return {@code this} assertion object.
	 * @throws AssertionError if the actual {@code File} is {@code null}.
	 * @throws AssertionError if the actual {@code File} exists.
	 */
	public S doesNotExist() {
		files.assertDoesNotExist(info, actual);
		return myself;
	}

	/**
	 * Verifies that the actual {@code File} is an existing file.
	 * @return {@code this} assertion object.
	 * @throws AssertionError if the actual {@code File} is {@code null}.
	 * @throws AssertionError if the actual {@code File} is not an existing file.
	 */
	public S isFile() {
		files.assertIsFile(info, actual);
		return myself;
	}

	/**
	 * Verifies that the actual {@code File} is an existing directory.
	 * @return {@code this} assertion object.
	 * @throws AssertionError if the actual {@code File} is {@code null}.
	 * @throws AssertionError if the actual {@code File} is not an existing file.
	 */
	public S isDirectory() {
		files.assertIsDirectory(info, actual);
		return myself;
	}

	/**
	 * Verifies that the actual {@code File} is an absolute path.
	 * @return {@code this} assertion object.
	 * @throws AssertionError if the actual {@code File} is {@code null}.
	 * @throws AssertionError if the actual {@code File} is not an absolute path.
	 */
	public S isAbsolute() {
		files.assertIsAbsolute(info, actual);
		return myself;
	}

	/**
	 * Verifies that the actual {@code File} is a relative path.
	 * @return {@code this} assertion object.
	 * @throws AssertionError if the actual {@code File} is {@code null}.
	 * @throws AssertionError if the actual {@code File} is not a relative path.
	 */
	public S isRelative() {
		files.assertIsRelative(info, actual);
		return myself;
	}

	/**
	 * Verifies that the content of the actual {@code File} is equal to the content of the given one.
	 * @param expected the given {@code File} to compare the actual {@code File} to.
	 * @return {@code this} assertion object.
	 * @throws NullPointerException if the given {@code File} is {@code null}.
	 * @throws IllegalArgumentException if the given {@code File} is not an existing file.
	 * @throws AssertionError if the actual {@code File} is {@code null}.
	 * @throws AssertionError if the actual {@code File} is not an existing file.
	 * @throws FilesException if an I/O error occurs.
	 * @throws AssertionError if the content of the actual {@code File} is not equal to the content of the given one.
	 */
	public S hasContentEqualTo(File expected) {
		files.assertEqualContent(info, actual, expected);
		return myself;
	}

	/**
	 * Verifies that the binary content of the actual {@code File} is <b>exactly</b> equal to the given one.
	 * @param expected the expected binary content to compare the actual {@code File}'s content to.
	 * @return {@code this} assertion object.
	 * @throws NullPointerException if the given content is {@code null}.
	 * @throws AssertionError if the actual {@code File} is {@code null}.
	 * @throws AssertionError if the actual {@code File} is not an existing file.
	 * @throws FilesException if an I/O error occurs.
	 * @throws AssertionError if the content of the actual {@code File} is not equal to the given binary content.
	 */
	public S hasBinaryContent(byte[] expected) {
		files.assertHasBinaryContent(info, actual, expected);
		return myself;
	}

	/**
	 * Specifies the name of the charset to use for text-based assertions on the file's contents.
	 *
	 * @param charsetName the name of the charset to use.
	 * @return  {@code this} assertion object.
	 * @throws IllegalArgumentException if the given encoding is not supported on this platform.
	 */
	public S usingCharset(String charsetName) {
		if (!Charset.isSupported(charsetName)) throw new IllegalArgumentException(String.format("Charset:<'%s'> is not supported on this system", charsetName));
		return usingCharset(Charset.forName(charsetName));
	}

	/**
	 * Specifies the charset to use for text-based assertions on the file's contents.
	 *
	 * @param charset the charset to use.
	 * @return  {@code this} assertion object.
	 * @throws NullPointerException if the given charset is {@code null}.
	 */
	public S usingCharset(Charset charset) {
		if (charset == null) throw new NullPointerException("The charset should not be null");
		this.charset = charset;
		return myself;
	}

	/**
	 * Verifies that the text content of the actual {@code File} is <b>exactly</b> equal to the given one.<br/>
	 * The charset to use when reading the file should be provided with {@link #usingCharset(Charset)} or
	 * {@link #usingCharset(String)} prior to calling this method; if not, the platform's default charset (as returned by
	 * {@link Charset#defaultCharset()}) will be used.
	 * @param expected the expected text content to compare the actual {@code File}'s content to.
	 * @return {@code this} assertion object.
	 * @throws NullPointerException if the given content is {@code null}.
	 * @throws AssertionError if the actual {@code File} is {@code null}.
	 * @throws AssertionError if the actual {@code File} is not an existing file.
	 * @throws FilesException if an I/O error occurs.
	 * @throws AssertionError if the content of the actual {@code File} is not equal to the given binary content.
	 */
	public S hasContent(String expected) {
		files.assertHasContent(info, actual, expected, charset);
		return myself;
	}

	/**
	 *
	 * Verifies that the actual {@code File} can be modified by the application.
	 * @return {@code this} assertion object.
	 * @throws AssertionError if the actual {@code File} is {@code null}.
	 * @throws AssertionError if the actual {@code File} can not be modified by the application.
	 */
	public S canWrite() {
		files.assertCanWrite(info, actual);
		return myself;
	}

	/**
	 *
	 * Verifies that the actual {@code File} can be read by the application.
	 * @return {@code this} assertion object.
	 * @throws AssertionError if the actual {@code File} is {@code null}.
	 * @throws AssertionError if the actual {@code File} can not be read by the application.
	 */
	public S canRead() {
		files.assertCanRead(info, actual);
		return myself;
	}
}
