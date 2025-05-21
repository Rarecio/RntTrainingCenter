package ru.zhaleykin.module3concurrency;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;

public class Philosopher extends Thread {
    final int n;
    State state = State.THINKING;

    private final Lock left;
    private final Lock right;

    private final Semaphore semaphore;

    public Philosopher(int n, Lock left, Lock right, Semaphore semaphore) {
        this.n = n;
        this.left = left;
        this.right = right;
        this.semaphore = semaphore;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                System.out.println(this);
                Thread.sleep((long) (Math.random() * 1000));
                semaphore.acquire();
                left.lock();
                try {
                    right.lock();
                    try {
                        state = State.EATING;
                        System.out.println(this);
                        Thread.sleep((long) (Math.random() * 1000));
                        state = State.THINKING;
                    } finally {
                        right.unlock();
                    }
                } finally {
                    left.unlock();
                }
                semaphore.release();

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Override
    public String toString() {
        return "Philosopher " + n + " " + state.toString().toLowerCase();
    }

    public enum State {
        EATING, THINKING
    }
}
