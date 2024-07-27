import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static TaskManager taskManager = new TaskManager();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            runApplication();
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    private static void runApplication() {
        while (true) {
            try {
                displayMenu();
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        addTask();
                        break;
                    case 2:
                        markTaskCompleted();
                        break;
                    case 3:
                        deleteTask();
                        break;
                    case 4:
                        handleViewTasks();
                        break;
                    case 5:
                        try {
                            taskManager.undo();
                        } catch (IllegalStateException e) {
                            System.out.println(e.getMessage());
                        }
                        break;
                    case 6:
                        System.out.println("Exiting the application...");
                        return;
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    private static void displayMenu() {
        System.out.println("1. Add Task");
        System.out.println("2. Mark Task Completed");
        System.out.println("3. Delete Task");
        System.out.println("4. View Tasks");
        System.out.println("5. Undo");
        System.out.println("6. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void addTask() {
        try {
            System.out.println("Enter task name:");
            String name = scanner.nextLine().trim(); 
            if (name.isEmpty()) {
                System.out.println("Task name cannot be empty. Task not added.");
                return;
            }

            String dueDateString = "";
            Date dueDate = null;
            while (dueDate == null) {
                System.out.println("Enter due date (yyyy-MM-dd) or press Enter to skip:");
                dueDateString = scanner.nextLine().trim();
                if (dueDateString.isEmpty()) {
                    break; 
                }

                try {
                    dueDate = new SimpleDateFormat("yyyy-MM-dd").parse(dueDateString);
                } catch (ParseException e) {
                    System.out.println("Invalid date format. Please enter date in yyyy-MM-dd format.");
                }
            }

            System.out.println("Enter tags or press Enter to skip:");
            String tags = scanner.nextLine();

            Task task = new Task.TaskBuilder(name)
                    .dueDate(dueDate)
                    .tags(tags)
                    .build();
            taskManager.addTask(task);
            System.out.println("Task added.");
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    private static void markTaskCompleted() {
        List<Task> tasks = taskManager.viewTasks();
        if (tasks.isEmpty()) {
            System.out.println("No tasks available to mark as completed. Add a task first.");
            return;
        }

        System.out.println("Tasks:");
        tasks.forEach(System.out::println);

        System.out.println("Enter task name to mark as completed:");
        String name = scanner.nextLine().trim();
        boolean found = false;
        for (Task task : tasks) {
            if (task.getname().equals(name)) {
                if (task.isCompleted()) {
                    System.out.println("Task is already marked as completed.");
                } else {
                    taskManager.markTaskCompleted(name);
                    System.out.println("Task marked as completed.");
                }
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("Task not found with name: " + name);
        }
    }

    private static void deleteTask() {
        List<Task> tasks = taskManager.viewTasks();
        if (tasks.isEmpty()) {
            System.out.println("No tasks available to delete. Add a task first.");
            return;
        }

        System.out.println("Tasks:");
        tasks.forEach(System.out::println);

        System.out.println("Enter task name to delete:");
        String name = scanner.nextLine().trim();
        boolean found = false;
        for (Task task : tasks) {
            if (task.getname().equals(name)) {
                taskManager.deleteTask(name);
                System.out.println("Task deleted.");
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("Task not found with name: " + name);
        }
    }

    private static void handleViewTasks() {
        System.out.println("Select filter:");
        System.out.println("1. View All Tasks");
        System.out.println("2. View Pending Tasks");
        System.out.println("3. View Completed Tasks");

        int choice = Integer.parseInt(scanner.nextLine());
        switch (choice) {
            case 1:
                viewTasks("all");
                break;
            case 2:
                viewTasks("pending");
                break;
            case 3:
                viewTasks("completed");
                break;
            default:
                System.out.println("Invalid choice. Try again.");
        }
    }

    private static void viewTasks(String filter) {
        List<Task> tasks;

        switch (filter) {
            case "pending":
                tasks = taskManager.getPendingTasks();
                break;
            case "completed":
                tasks = taskManager.getCompletedTasks();
                break;
            case "all":
            default:
                tasks = taskManager.viewTasks();
                break;
        }

        if (tasks.isEmpty()) {
            System.out.println("No tasks found.");
        } else {
            tasks.forEach(System.out::println);
        }
    }
}
