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
package org.assertj.core.util;

import static java.lang.String.format;
import static org.assertj.core.util.Lists.newArrayList;
import static org.assertj.core.util.Preconditions.checkArgument;

import java.util.List;

/**
 * Utility methods related to {@code String}s.
 * 
 * @author Alex Ruiz
 */
public final class Strings {
  /**
   * Indicates whether the given {@code String} is {@code null} or empty.
   * 
   * @param s the {@code String} to check.
   * @return {@code true} if the given {@code String} is {@code null} or empty, otherwise {@code false}.
   */
  public static boolean isNullOrEmpty(String s) {
    return s == null || s.length() == 0;
  }

  /**
   * Returns the given {@code String} surrounded by single quotes, or {@code null} if the given {@code String} is
   * {@code null}.
   * 
   * @param s the given {@code String}.
   * @return the given {@code String} surrounded by single quotes, or {@code null} if the given {@code String} is
   *         {@code null}.
   */
  public static String quote(String s) {
    return s != null ? concat("'", s, "'") : null;
  }

  /**
   * Returns the given object surrounded by single quotes, only if the object is a {@code String}.
   * 
   * @param o the given object.
   * @return the given object surrounded by single quotes, only if the object is a {@code String}.
   * @see #quote(String)
   */
  public static Object quote(Object o) {
    return o instanceof String ? quote(o.toString()) : o;
  }

  /**
   * Concatenates the given objects into a single {@code String}. This method is more efficient than concatenating using
   * "+", since only one <code>{@link StringBuilder}</code> is created.
   * 
   * @param objects the objects to concatenate.
   * @return a {@code String} containing the given objects.
   */
  public static String concat(Object... objects) {
    if (Arrays.isNullOrEmpty(objects)) {
      return null;
    }
    StringBuilder b = new StringBuilder();
    for (Object o : objects) {
      b.append(o);
    }
    return b.toString();
  }

  /**
   * Format with {@link String#format(String, Object...)} the given message iif some args have been given otherwise juts
   * return the message.
   * 
   * @param message the string to format
   * @param args args used to format the message, can be null or empty
   * @return the formatted string if any args were given
   */
  public static String formatIfArgs(String message, Object... args) {
    return Arrays.isNullOrEmpty(args)
            // here we need to format %n but not other % since we do not have arguments. %% is formatted to % so
            // replacing % into %% and then format it will have no effect. Nevertheless, we want to format %n
            // correctly so we replace all % to %% except if they are followed by a 'n'.
            ? format(message.replaceAll("%([^n])","%%$1"))
            : format (message, args);
  }

  /**
   * Joins the given {@code String}s using a given delimiter. The following example illustrates proper usage of this
   * method:
   * <pre><code class='java'> Strings.join(&quot;a&quot;, &quot;b&quot;, &quot;c&quot;).with(&quot;|&quot;);</code></pre>
   * 
   * which will result in the {@code String} <code>"a|b|c"</code>.
   * 
   * @param strings the {@code String}s to join.
   * @return an intermediate object that takes a given delimiter and knows how to join the given {@code String}s.
   * @see StringsToJoin#with(String)
   */
  public static StringsToJoin join(String... strings) {
    return new StringsToJoin(strings);
  }

  /**
   * Joins the given {@code Object}s using a given delimiter. The following example illustrates proper usage of this
   * method:
   * <pre><code class='java'> Strings.join(new ArrayList(&quot;a&quot;, &quot;b&quot;, &quot;c&quot;)).with(&quot;|&quot;);</code></pre>
   * 
   * which will result in the {@code String} <code>"a|b|c"</code>.
   * 
   * @param toStringable the {@code Object}s to join.
   * @return an intermediate object that takes a given delimiter and knows how to join the given {@code Object}s.
   * @see StringsToJoin#with(String)
   */
  public static StringsToJoin join(Iterable<?> toStringable) {
    List<String> strings = newArrayList();
    for (Object o : toStringable) {
      strings.add(String.valueOf(o));
    }
    return new StringsToJoin(strings.toArray(new String[strings.size()]));
  }

  /**
   * Knows how to join {@code String}s using a given delimiter.
   * 
   * @see Strings#join(String[])
   */
  public static class StringsToJoin {

    /** The {@code String}s to join. */
    private final String[] strings;

    /**
     * Creates a new <code>{@link StringsToJoin}</code>.
     * 
     * @param strings the {@code String}s to join.
     */
    StringsToJoin(String... strings) {
      this.strings = strings;
    }

    /**
     * Specifies the delimiter to use to join {@code String}s.
     * 
     * @param delimiter the delimiter to use.
     * @return the {@code String}s joined using the given delimiter.
     */
    public String with(String delimiter) {
      return with(delimiter, null);
    }

    /**
     * Specifies the delimiter to use to join {@code String}s and the esapce String to wrap strings with.
     * 
     * @param delimiter the delimiter to use.
     * @param escapeString the String wrapper to use.
     * @return the {@code String}s joined using the given delimiter.
     */
    public String with(String delimiter, String escapeString) {
      checkArgument(delimiter != null, "Delimiter should not be null");
      if (Arrays.isNullOrEmpty(strings)) {
        return "";
      }
      String escape = escapeString == null ? "" : escapeString;
      StringBuilder b = new StringBuilder();
      int stringCount = strings.length;
      for (int i = 0; i < stringCount; i++) {
        String s = strings[i];
        if (s != null) {
          b.append(escape);
          b.append(s);
          b.append(escape);
        }
        if (i < stringCount - 1) {
          b.append(delimiter);
        }
      }
      return b.toString();
    }
  }

  /**
   * Appends a given {@code String} to the given target, only if the target does not end with the given {@code String}
   * to append. The following example illustrates proper usage of this method:
   * <pre><code class='java'> Strings.append(&quot;c&quot;).to(&quot;ab&quot;);
   * Strings.append(&quot;c&quot;).to(&quot;abc&quot;);</code></pre>
   * 
   * resulting in the {@code String} <code>"abc"</code> for both cases.
   * 
   * @param toAppend the {@code String} to append.
   * @return an intermediate object that takes the target {@code String} and knows to append the given {@code String}.
   * @see StringToAppend#to(String)
   */
  public static StringToAppend append(String toAppend) {
    return new StringToAppend(toAppend);
  }

  /**
   * Knows how to append a given {@code String} to the given target, only if the target does not end with the given
   * {@code String} to append.
   */
  public static class StringToAppend {
    private final String toAppend;

    StringToAppend(String toAppend) {
      this.toAppend = toAppend;
    }

    /**
     * Appends the {@code String} specified in the constructor to the {@code String} passed as argument.
     * 
     * @param s the target {@code String}.
     * @return a {@code String} containing the target {@code String} with the given {@code String} to append added to
     *         the end.
     */
    public String to(String s) {
      if (!s.endsWith(toAppend)) {
        return concat(s, toAppend);
      }
      return s;
    }
  }

  private Strings() {
  }
}
