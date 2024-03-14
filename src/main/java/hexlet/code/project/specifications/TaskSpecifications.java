package hexlet.code.project.specifications;

import hexlet.code.project.model.Label;
import hexlet.code.project.model.Task;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TaskSpecifications {

    public static Specification<Task> titleContainsIgnoreCase(String title) {
        return (root, query, cb) -> {
            if (title == null) {
                return null;
            }
            return cb.like(cb.lower(root.get("name")), "%" + title.toLowerCase() + "%");
        };
    }

    public static Specification<Task> hasAssignee(Long assigneeId) {
        return (root, query, cb) -> assigneeId == null
                ? null
                : cb.equal(root.get("assignee").get("id"), assigneeId);
    }

    public static Specification<Task> hasStatus(String status) {
        return (root, query, cb) -> status == null
                ? null
                : cb.equal(root.get("status").get("name"), status);
    }

    public static Specification<Task> hasLabel(Long labelId) {
        return (root, query, cb) -> {
            if (labelId == null) {
                return null;
            }

            Join<Task, Label> labelsJoin = root.join("labels");
            return cb.equal(labelsJoin.get("id"), labelId);
        };
    }

}
