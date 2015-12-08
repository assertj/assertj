package org.assertj.core.util.diff;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DiffTest extends TestCase {

    public void testDiff_Insert() {
        final Patch<String> patch = DiffUtils.diff(Arrays.asList("hhh"), Arrays.asList("hhh", "jjj", "kkk"));
        assertNotNull(patch);
        assertEquals(1, patch.getDeltas().size());
        final Delta<String> delta = patch.getDeltas().get(0);
        assertEquals(InsertDelta.class, delta.getClass());
        assertEquals(new Chunk<String>(1, Collections.<String> emptyList()), delta.getOriginal());
        assertEquals(new Chunk<String>(1, Arrays.asList("jjj", "kkk")), delta.getRevised());
    }

    public void testDiff_Delete() {
        final Patch<String> patch = DiffUtils.diff(Arrays.asList("ddd", "fff", "ggg"), Arrays.asList("ggg"));
        assertNotNull(patch);
        assertEquals(1, patch.getDeltas().size());
        final Delta<String> delta = patch.getDeltas().get(0);
        assertEquals(DeleteDelta.class, delta.getClass());
        assertEquals(new Chunk<String>(0, Arrays.asList("ddd", "fff")), delta.getOriginal());
        assertEquals(new Chunk<String>(0, Collections.<String> emptyList()), delta.getRevised());
    }

    public void testDiff_Change() {
        final List<String> changeTest_from = Arrays.asList("aaa", "bbb", "ccc");
        final List<String> changeTest_to = Arrays.asList("aaa", "zzz", "ccc");

        final Patch<String> patch = DiffUtils.diff(changeTest_from, changeTest_to);
        assertNotNull(patch);
        assertEquals(1, patch.getDeltas().size());
        final Delta<String> delta = patch.getDeltas().get(0);
        assertEquals(ChangeDelta.class, delta.getClass());
        assertEquals(new Chunk<String>(1, Arrays.asList("bbb")), delta.getOriginal());
        assertEquals(new Chunk<String>(1, Arrays.asList("zzz")), delta.getRevised());
    }

    public void testDiff_EmptyList() {
        final Patch<String> patch = DiffUtils.diff(new ArrayList<String>(), new ArrayList<String>());
        assertNotNull(patch);
        assertEquals(0, patch.getDeltas().size());
    }

    public void testDiff_EmptyListWithNonEmpty() {
        final Patch<String> patch = DiffUtils.diff(new ArrayList<String>(), Arrays.asList("aaa"));
        assertNotNull(patch);
        assertEquals(1, patch.getDeltas().size());
        final Delta<String> delta = patch.getDeltas().get(0);
        assertEquals(InsertDelta.class, delta.getClass());
    }
}
