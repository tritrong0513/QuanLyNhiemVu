import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class TaskRepository {

    private static final String DB_FILE_PATH = "tasks_database.json";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public List<Task> loadTasks() {
        JSONParser parser = new JSONParser();
        List<Task> tasks = new ArrayList<>();
        try (FileReader reader = new FileReader(DB_FILE_PATH)) {
            Object obj = parser.parse(reader);
            if (obj instanceof JSONArray) {
                JSONArray jsonArray = (JSONArray) obj;
                for (Object item : jsonArray) {
                    tasks.add(Task.fromJSONObject((JSONObject) item));
                }
            }
        } catch (IOException | ParseException e) {
            System.err.println("Lỗi khi đọc file database: " + e.getMessage());
            // Trong một ứng dụng thực tế, bạn có thể ném một ngoại lệ tùy chỉnh ở đây
        }
        return tasks;
    }

    public void saveTasks(List<Task> tasks) {
        JSONArray jsonArray = new JSONArray();
        for (Task task : tasks) {
            jsonArray.add(task.toJSONObject());
        }
        try (FileWriter file = new FileWriter(DB_FILE_PATH)) {
            file.write(jsonArray.toJSONString());
            file.flush();
        } catch (IOException e) {
            System.err.println("Lỗi khi ghi vào file database: " + e.getMessage());
            // Trong một ứng dụng thực tế, bạn có thể ném một ngoại lệ tùy chỉnh ở đây
        }
    }
}
