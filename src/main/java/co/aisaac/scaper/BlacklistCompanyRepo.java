package co.aisaac.scaper;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlacklistCompanyRepo extends CrudRepository<BlacklistCompany, Long> {
    List<BlacklistCompany> findAll();
}
