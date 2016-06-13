package fr.djoutsop.crawler.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.djoutsop.crawler.entity.Source;

/**
 * Spring Data JPA repository for the User source.
 */
public interface SourceRepository extends JpaRepository<Source, Long> {

	Optional<Source> findOneById(Long sourceId);
	
	Optional<Source> findOneByName(String name);

	@Override
	void delete(Source t);

}
