package org.assertj.core.internal;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;

import static org.assertj.core.util.Arrays.isArray;

public class RecursiveHelper {
  public static boolean isContainer(Object o) {
    return o instanceof Iterable ||
           o instanceof Map ||
           o instanceof Optional ||
           o instanceof AtomicReference ||
           o instanceof AtomicReferenceArray ||
           o instanceof AtomicBoolean ||
           o instanceof AtomicInteger ||
           o instanceof AtomicIntegerArray ||
           o instanceof AtomicLong ||
           o instanceof AtomicLongArray ||
           isArray(o);
  }
}
