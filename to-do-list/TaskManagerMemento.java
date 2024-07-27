import java.util.ArrayList;
import java.util.List;

public class TaskManagerMemento {
    private List<Task> tasks;

    public TaskManagerMemento(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    public List<Task> getSavedTasks() {
        return tasks;
    }
}
