package ru.pas_zhukov;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        DataProcessor processor = new DataProcessor(5);

        for (int i = 0; i < 100; i++) {
            processor.submitTask(Arrays.asList(1, 2, 3, 4, 5));
        }

        // Ожидание завершения всех задач
        while (processor.getActiveTaskCount() > 0) {
            System.out.println("Active tasks: " + processor.getActiveTaskCount());
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Main thread interrupted");
            }
        }

        // Вывод результатов
        for (int i = 1; i <= 100; i++) {
            String taskName = "task" + i;
            processor.getTaskResult(taskName).ifPresent(result ->
                    System.out.println(taskName + " result: " + result)
            );
        }

        processor.shutdown();
    }
}