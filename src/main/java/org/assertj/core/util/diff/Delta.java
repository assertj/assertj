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
package org.assertj.core.util.diff;

import static org.assertj.core.util.Preconditions.checkArgument;

import java.util.List;

import org.assertj.core.configuration.ConfigurationProvider;

/**
 * Initially copied from https://code.google.com/p/java-diff-utils/.
 * <p>
 * Describes the delta between original and revised texts.
 * 
 * @author <a href="dm.naumenko@gmail.com">Dmitry Naumenko</a>
 * @param <T> The type of the compared elements in the 'lines'.
 */
public abstract class Delta<T> {

  public static final String DEFAULT_END = "]";
  public static final String DEFAULT_START = "[";
  
  /** The original chunk. */
  private Chunk<T> original;

  /** The revised chunk. */
  private Chunk<T> revised;

  /**
   * Specifies the type of the delta.
   *
   */
  public enum TYPE {
    /** A change in the original. */
    CHANGE,
    /** A delete from the original. */
    DELETE,
    /** An insert into the original. */
    INSERT
  }

  /**
   * Construct the delta for original and revised chunks
   * 
   * @param original Chunk describing the original text. Must not be {@code null}.
   * @param revised Chunk describing the revised text. Must not be {@code null}.
   */
  public Delta(Chunk<T> original, Chunk<T> revised) {
    checkArgument(original != null, "original must not be null");
    checkArgument(revised != null, "revised must not be null");
    this.original = original;
    this.revised = revised;
  }

  /**
   * Verifies that this delta can be used to patch the given text.
   * 
   * @param target the text to patch.
   * @throws IllegalStateException if the patch cannot be applied.
   */
  public abstract void verify(List<T> target) throws IllegalStateException;

  /**
   * Applies this delta as the patch for a given target
   * 
   * @param target the given target
   * @throws IllegalStateException if {@link #verify(List)} fails
   */
  public abstract void applyTo(List<T> target) throws IllegalStateException;

  /**
   * Returns the type of delta
   * @return the type enum
   */
  public abstract TYPE getType();

  /**
   * @return The Chunk describing the original text.
   */
  public Chunk<T> getOriginal() {
    return original;
  }

  /**
   * @return The Chunk describing the revised text.
   */
  public Chunk<T> getRevised() {
    return revised;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((original == null) ? 0 : original.hashCode());
    result = prime * result + ((revised == null) ? 0 : revised.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    @SuppressWarnings("rawtypes")
    Delta other = (Delta) obj;
    if (original == null) {
      if (other.original != null)
        return false;
    } else if (!original.equals(other.original))
      return false;
    if (revised == null) {
      if (other.revised != null)
        return false;
    } else if (!revised.equals(other.revised))
      return false;
    return true;
  }

  public int lineNumber() {
    return getOriginal().getPosition() + 1;
  }

  @Override
  public String toString() {
    return ConfigurationProvider.CONFIGURATION_PROVIDER.representation().toStringOf(this);
  }

}
