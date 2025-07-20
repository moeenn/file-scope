package com.crawler;

public class ProgressReporter {
    private int total = 0;
    private int count = 0;

    public void increment() {
        total++;
        count = total;
    }

    public void increment(int n) {
        total += n;
        count = total;
    }

    public int getPending() {
        return count;
    }

    public void decrement() {
        count--;
        System.out.printf("\rPending: %02d / %02d", count, total);
    }
}
