package org.assertj.core.groups;

public class TripleTuple<S, T, U> {

	private final S first;
    private final T second;
    private final U third;

    public TripleTuple(S first, T second, U third) {

        this.first = first;

        this.second = second;
        this.third = third;
    }

    public S getFirst() {

        return first;
    }

    public T getSecond() {

        return second;
    }

    public U getThird() {

        return third;
    }

    @Override
	public String toString() {

		return "{" + first + ", " + second +  ", " + third + '}';
	}

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final TripleTuple<?, ?, ?> that = (TripleTuple<?, ?, ?>) o;

        if (first != null ? !first.equals(that.first) : that.first != null) {
            return false;
        }
        if (second != null ? !second.equals(that.second) : that.second != null) {
            return false;
        }
        return !(third != null ? !third.equals(that.third) : that.third != null);

    }

    @Override
    public int hashCode() {

        int result = first != null ? first.hashCode() : 0;
        result = 31 * result + (second != null ? second.hashCode() : 0);
        result = 31 * result + (third != null ? third.hashCode() : 0);
        return result;
    }
}
