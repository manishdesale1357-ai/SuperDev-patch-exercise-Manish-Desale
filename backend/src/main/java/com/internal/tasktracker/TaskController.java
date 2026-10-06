
package com.internal.tasktracker;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class TaskController {

    private final TaskRepository taskRepository;

    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @GetMapping("/api/tasks")
    public ResponseEntity<?> searchTasks(
            @RequestParam(required = false, defaultValue = "") String q,
            @RequestParam(required = false) String status,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int pageSize) {

        // Validate pagination parameters
        if (page < 1) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Page must be greater than or equal to 1"));
        }

        if (pageSize < 1) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Page size must be greater than 0"));
        }

        // Normalize query input
        String query = q == null ? "" : q.trim();
        String searchTerm = "%" + query.toLowerCase() + "%";

        // Parse status filter
        String normalizedStatus = null;

        if (status != null && !status.isBlank()) {
            try {
                normalizedStatus =
                        TaskStatus.valueOf(status.trim().toUpperCase()).name();
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Invalid status: " + status));
            }
        }

        // Log request information
        System.out.println("[TaskController] q=\"" + query
                + "\" status=" + normalizedStatus
                + " page=" + page
                + " pageSize=" + pageSize);

        // Fetch matching tasks
        List<Task> allResults =
                taskRepository.searchTasks(searchTerm, normalizedStatus);

        // Calculate pagination indexes
        int start = (page - 1) * pageSize;
        int end = Math.min(start + pageSize, allResults.size());

        List<Task> pageResults =
                (start < allResults.size())
                        ? allResults.subList(start, end)
                        : Collections.emptyList();

        // Build response
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("items", pageResults);
        response.put("total", allResults.size());
        response.put("page", page);
        response.put("pageSize", pageSize);

        return ResponseEntity.ok(response);
    }
}

