package ru.zhaleykin.module3concurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    public static void main(String[] args) {
        int n = 5;
        List<Philosopher> philosophers = new ArrayList<>(n);
        Lock[] forks = new ReentrantLock[n];
        Semaphore semaphore = new Semaphore(n-1);
        for (int i = 0; i < n; i++) {
            forks[i] = new ReentrantLock();
        }
        for (int i = 0; i < n; i++) {
            philosophers.add(new Philosopher(i+1, forks[i], forks[(i+n+1)%n], semaphore));
        }

        philosophers.forEach(Thread::start);
    }
}
