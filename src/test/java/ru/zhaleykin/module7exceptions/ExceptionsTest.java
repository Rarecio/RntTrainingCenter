package ru.zhaleykin.module7exceptions;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ExceptionsTest {

    @Test(expected = OutOfMemoryError.class)
    public void outOfMemoryErrorWithCollectionsTest() {
        List<Integer> lst = new ArrayList<>(1000000000);
        for (long i = 0; i < 1000000000000000000L; i++) {
            lst.add((int) i);
        }
    }

    @Test(expected = OutOfMemoryError.class)
    public void outOfMemoryErrorWithoutCollectionsTest() {
        StringBuilder s = new StringBuilder();
        while(true) {
            s.append("1");
        }
    }

}
