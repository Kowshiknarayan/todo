import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class TaskManager {
    private List<Task> tasks = new ArrayList<>();
    private Stack<TaskManagerMemento> history = new Stack<>();
    private boolean hasTasks = false;

    public void addTask(Task task) {
        saveState();
        tasks.add(task);
        hasTasks = true;
    }

    public void deleteTask(String name) throws IllegalStateException {
        if (tasks.isEmpty()) {
            throw new IllegalStateException("No tasks available to delete. Add a task first.");
        }
        saveState();
        tasks.removeIf(task -> task.getname().equals(name));
    }

    public void markTaskCompleted(String name) throws IllegalStateException {
        if (tasks.isEmpty()) {
            throw new IllegalStateException("No tasks available to mark as completed. Add a task first.");
        }
        saveState();
        for (Task task : tasks) {
            if (task.getname().equals(name)) {
                task.markCompleted();
                break;
            }
        }
    }

    public List<Task> viewTasks() {
        return tasks;
    }

    public List<Task> getPendingTasks() {
        List<Task> pendingTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (!task.isCompleted()) {
                pendingTasks.add(task);
            }
        }
        return pendingTasks;
    }

    public List<Task> getCompletedTasks() {
        List<Task> completedTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.isCompleted()) {
                completedTasks.add(task);
            }
        }
        return completedTasks;
    }

    private void saveState() {
        List<Task> tasksCopy = new ArrayList<>();
        for (Task task : tasks) {
            Task.TaskBuilder builder = new Task.TaskBuilder(task.getname());
            builder.dueDate(task.getDueDate());
            builder.tags(task.getTags());
            Task copiedTask = builder.build();
            if (task.isCompleted()) {
                copiedTask.markCompleted();
            }
            tasksCopy.add(copiedTask);
        }
        history.push(new TaskManagerMemento(tasksCopy));
    }

    public void undo() throws IllegalStateException {
        if (!hasTasks) {
            throw new IllegalStateException("No actions to undo. Add a task first.");
        }
        if (!history.isEmpty()) {
            tasks = history.pop().getSavedTasks();
            System.out.println("Undo performed.");
        } else {
            System.out.println("Nothing to undo.");
        }
    }
}
