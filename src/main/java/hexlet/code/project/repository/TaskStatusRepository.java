package hexlet.code.project.repository;

import hexlet.code.project.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskStatusRepository extends JpaRepository<TaskStatus, Long> {

    Optional<TaskStatus> findByName(String name);

    Optional<TaskStatus> findBySlug(String slug);

}
