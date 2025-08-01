package com.scope;

public class ProgressReporter {
    private int total = 0;
    private int count = 0;

    public void setTotal(int total) {
        this.total = total;
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
