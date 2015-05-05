package org.assertj.core.groups;

public final class TypedTuples {

    public static <S, T, U> TripleTuple<S, T, U> tuple(S first, T second, U third) {

        return new TripleTuple<>(first, second, third);
    }

    private TypedTuples() {

    }
}
