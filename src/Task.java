import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import org.json.simple.JSONObject;

public class Task {
    private String id;
    private String title;
    private String description;
    private LocalDate dueDate;
    private String priority;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdatedAt;

    // Constructor
    public Task(String id, String title, String description, LocalDate dueDate, String priority, String status, LocalDateTime createdAt, LocalDateTime lastUpdatedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.priority = priority;
        this.status = status;
        this.createdAt = createdAt;
        this.lastUpdatedAt = lastUpdatedAt;
    }

    // Factory method from JSONObject
    public static Task fromJSONObject(JSONObject jsonObject) {
        String id = (String) jsonObject.get("id");
        String title = (String) jsonObject.get("title");
        String description = (String) jsonObject.get("description");
        LocalDate dueDate = LocalDate.parse((String) jsonObject.get("due_date"), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String priority = (String) jsonObject.get("priority");
        String status = (String) jsonObject.get("status");
        LocalDateTime createdAt = LocalDateTime.parse((String) jsonObject.get("created_at"), DateTimeFormatter.ISO_DATE_TIME);
        LocalDateTime lastUpdatedAt = LocalDateTime.parse((String) jsonObject.get("last_updated_at"), DateTimeFormatter.ISO_DATE_TIME);
        return new Task(id, title, description, dueDate, priority, status, createdAt, lastUpdatedAt);
    }

    // Convert to JSONObject
    public JSONObject toJSONObject() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        jsonObject.put("title", title);
        jsonObject.put("description", description);
        jsonObject.put("due_date", dueDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        jsonObject.put("priority", priority);
        jsonObject.put("status", status);
        jsonObject.put("created_at", createdAt.format(DateTimeFormatter.ISO_DATE_TIME));
        jsonObject.put("last_updated_at", lastUpdatedAt.format(DateTimeFormatter.ISO_DATE_TIME));
        return jsonObject;
    }

    // Getters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public LocalDate getDueDate() { return dueDate; }
    public String getPriority() { return priority; }
    public String getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getLastUpdatedAt() { return lastUpdatedAt; }

    // Setters
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public void setPriority(String priority) { this.priority = priority; }
    public void setStatus(String status) { this.status = status; }
    public void setLastUpdatedAt(LocalDateTime lastUpdatedAt) { this.lastUpdatedAt = lastUpdatedAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null |

| getClass()!= o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(title, task.title) &&
               Objects.equals(dueDate, task.dueDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, dueDate);
    }

    @Override
    public String toString() {
        return "Task{" +
               "id='" + id + '\'' +
               ", title='" + title + '\'' +
               ", dueDate=" + dueDate +
               ", priority='" + priority + '\'' +
               ", status='" + status + '\'' +
               '}';
    }
}
