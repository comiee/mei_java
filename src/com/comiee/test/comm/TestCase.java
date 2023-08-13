package com.comiee.test.comm;

public class TestCase {
    protected void assertEqual(Object a, Object b) {
        if (!a.equals(b)) {
            throw new AssertionError("预期：" + a + "实际：" + b);
        } else {
            System.out.println("比较一致：" + a);
        }
    }
}
