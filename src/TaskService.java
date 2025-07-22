import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class TaskService {

    private final TaskRepository taskRepository;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final Set<String> VALID_PRIORITIES = new HashSet<>(Arrays.asList("Thấp", "Trung bình", "Cao"));

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    /**
     * Thêm nhiệm vụ mới vào hệ thống.
     *
     * @param title Tiêu đề nhiệm vụ.
     * @param description Mô tả nhiệm vụ.
     * @param dueDateStr Ngày đến hạn (định dạng YYYY-MM-DD).
     * @param priorityLevel Mức độ ưu tiên ("Thấp", "Trung bình", "Cao").
     * @return Đối tượng Task đã thêm, hoặc null nếu có lỗi validation hoặc trùng lặp.
     */
    public Task addTask(String title, String description, String dueDateStr, String priorityLevel) {
        // Bước 1: Validation đầu vào (KISS)
        if (!isValidTitle(title)) {
            System.out.println("Lỗi: Tiêu đề không được để trống.");
            return null;
        }
        LocalDate dueDate = parseAndValidateDueDate(dueDateStr);
        if (dueDate == null) {
            System.out.println("Lỗi: Ngày đến hạn không hợp lệ. Vui lòng sử dụng định dạng YYYY-MM-DD.");
            return null;
        }
        if (!isValidPriority(priorityLevel)) {
            System.out.println("Lỗi: Mức độ ưu tiên không hợp lệ. Vui lòng chọn từ: Thấp, Trung bình, Cao.");
            return null;
        }

        // Bước 2: Tải dữ liệu hiện có (DRY - thông qua Repository)
        List<Task> tasks = taskRepository.loadTasks();

        // Bước 3: Kiểm tra trùng lặp (DRY, KISS)
        Task potentialNewTask = new Task(null, title, description, dueDate, priorityLevel, "Chưa hoàn thành", null, null); // Tạo tạm để kiểm tra equals
        if (isDuplicateTask(potentialNewTask, tasks)) {
            System.out.println(String.format("Lỗi: Nhiệm vụ '%s' đã tồn tại với cùng ngày đến hạn.", title));
            return null;
        }

        // Bước 4: Tạo đối tượng Task mới
        String taskId = UUID.randomUUID().toString(); // Giữ UUID vì tính duy nhất mạnh mẽ
        Task newTask = new Task(
            taskId,
            title,
            description,
            dueDate,
            priorityLevel,
            "Chưa hoàn thành", // Trạng thái mặc định
            LocalDateTime.now(),
            LocalDateTime.now()
        );

        // Bước 5: Thêm nhiệm vụ và lưu dữ liệu (DRY - thông qua Repository)
        tasks.add(newTask);
        taskRepository.saveTasks(tasks);

        System.out.println(String.format("Đã thêm nhiệm vụ mới thành công với ID: %s", taskId));
        return newTask;
    }

    // Phương thức trợ giúp: Kiểm tra tiêu đề hợp lệ
    private boolean isValidTitle(String title) {
        return title!= null &&!title.trim().isEmpty();
    }

    // Phương thức trợ giúp: Phân tích và kiểm tra ngày đến hạn hợp lệ
    private LocalDate parseAndValidateDueDate(String dueDateStr) {
        if (dueDateStr == null |

| dueDateStr.trim().isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(dueDateStr, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    // Phương thức trợ giúp: Kiểm tra mức độ ưu tiên hợp lệ (KISS)
    private boolean isValidPriority(String priorityLevel) {
        return VALID_PRIORITIES.contains(priorityLevel);
    }

    // Phương thức trợ giúp: Kiểm tra nhiệm vụ trùng lặp (DRY, KISS)
    private boolean isDuplicateTask(Task newTask, List<Task> existingTasks) {
        for (Task existingTask : existingTasks) {
            // So sánh dựa trên tiêu đề và ngày đến hạn (đã override equals trong Task)
            if (existingTask.equals(newTask)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String args) {
        TaskRepository repository = new TaskRepository();
        TaskService manager = new TaskService(repository);

        System.out.println("\n--- Thêm nhiệm vụ hợp lệ ---");
        manager.addTask(
            "Mua sách",
            "Sách Công nghệ phần mềm.",
            "2025-07-20",
            "Cao"
        );

        System.out.println("\n--- Thêm nhiệm vụ trùng lặp (minh họa DRY) ---");
        manager.addTask(
            "Mua sách",
            "Sách Công nghệ phần mềm.",
            "2025-07-20",
            "Cao"
        );

        System.out.println("\n--- Thêm nhiệm vụ với tiêu đề rỗng ---");
        manager.addTask(
            "",
            "Nhiệm vụ không có tiêu đề.",
            "2025-07-22",
            "Thấp"
        );

        System.out.println("\n--- Thêm nhiệm vụ với ngày đến hạn không hợp lệ ---");
        manager.addTask(
            "Kiểm tra báo cáo",
            "Kiểm tra lỗi chính tả.",
            "2025/07/23", // Định dạng sai
            "Trung bình"
        );

        System.out.println("\n--- Thêm nhiệm vụ với mức độ ưu tiên không hợp lệ ---");
        manager.addTask(
            "Gửi email",
            "Gửi báo cáo cuối kỳ.",
            "2025-07-24",
            "Rất Cao" // Ưu tiên không hợp lệ
        );

        System.out.println("\n--- Thêm nhiệm vụ hợp lệ khác ---");
        manager.addTask(
            "Học tiếng Anh",
            "Luyện nghe 30 phút.",
            "2025-07-25",
            "Thấp"
        );
    }
}
