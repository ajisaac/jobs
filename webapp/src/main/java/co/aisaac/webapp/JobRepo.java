package co.aisaac.webapp;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// todo switch away from JPA
@Repository
public interface JobRepo extends CrudRepository<Job, Long> {

	List<Job> findAll();

	List<Job> findAllByStatusOrderByTitle(String status);
}
