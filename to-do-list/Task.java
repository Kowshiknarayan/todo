import java.util.Date;

public class Task {
    private String name;
    private Date dueDate;
    private boolean isCompleted;
    private String tags;

    private Task(TaskBuilder builder) {
        this.name = builder.name;
        this.dueDate = builder.dueDate;
        this.isCompleted = builder.isCompleted;
        this.tags = builder.tags;
    }

    public static class TaskBuilder {
        private String name;
        private Date dueDate;
        private boolean isCompleted;
        private String tags;

        public TaskBuilder(String name) {
            this.name = name;
        }

        public TaskBuilder dueDate(Date dueDate) {
            this.dueDate = dueDate;
            return this;
        }

        public TaskBuilder tags(String tags) {
            this.tags = tags;
            return this;
        }

        public Task build() {
            return new Task(this);
        }
    }

    public String getname() {
        return name;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void markCompleted() {
        this.isCompleted = true;
    }

    public String getTags() {
        return tags;
    }

    @Override
    public String toString() {
        String status = isCompleted ? "Completed" : "Pending";
        String dueDateString = dueDate != null ? dueDate.toString() : "No due date";
        String tagsString = tags != null ? tags : "No tags";
        return String.format("Task[Name='%s', dueDate='%s', status='%s', tags='%s']", name, dueDateString, status, tagsString);
    }
}
