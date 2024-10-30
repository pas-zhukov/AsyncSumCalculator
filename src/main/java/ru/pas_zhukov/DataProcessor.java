package ru.pas_zhukov;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DataProcessor {

    private final ExecutorService executorService;

    private AtomicInteger tasksCounter = new AtomicInteger(0);
    private AtomicInteger tasksInProgress = new AtomicInteger(0);

    private final Map<String, Integer> results = new HashMap<>();

    public DataProcessor() {
        executorService = Executors.newCachedThreadPool();
    }

    public DataProcessor(Integer threadsCount) {
        this.executorService = Executors.newFixedThreadPool(threadsCount);
    }

    public synchronized void submitTask(List<Integer> numbers) {
        CalculateSumTask task = new CalculateSumTask("task" + tasksCounter.incrementAndGet(), numbers);
        CompletableFuture<Integer> resultFuture = CompletableFuture.supplyAsync(() -> {
            try {
                return task.call();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, executorService);
        tasksInProgress.incrementAndGet();

        resultFuture.thenAccept(result -> {
            synchronized (results) {
                results.put(task.getName(), result);
            }
            tasksInProgress.decrementAndGet();
        });
    }

    public Optional<Integer> getTaskResult(String taskName) {
        synchronized (results) {
            return Optional.ofNullable(results.get(taskName));
        }
    }

    public int getActiveTaskCount() {
        return tasksInProgress.get();
    }

    public void shutdown() {
        executorService.shutdown();
    }
}
