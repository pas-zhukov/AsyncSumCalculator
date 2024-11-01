package ru.pas_zhukov;

import java.util.List;
import java.util.concurrent.Callable;

public class CalculateSumTask implements Callable<Integer> {

    private final String name;
    private final List<Integer> elements;

    public CalculateSumTask(String name, List<Integer> elements) {
        this.name = name;
        this.elements = elements;
    }

    @Override
    public Integer call() throws Exception {
        System.out.printf("Task started: %s, Thread name: %s%n", name, Thread.currentThread().getName());
        int sum = 0;
        for (Integer element : elements) {
            sum += element;
            Thread.sleep(200);
        }
        return sum;
    }

    public String getName() {
        return name;
    }
}
