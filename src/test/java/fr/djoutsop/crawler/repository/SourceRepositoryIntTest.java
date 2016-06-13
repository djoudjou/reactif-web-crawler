package fr.djoutsop.crawler.repository;

import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.*;

import fr.djoutsop.crawler.WebCrawlerConfiguration;
import fr.djoutsop.crawler.entity.Source;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WebCrawlerConfiguration.class)
@WebAppConfiguration
@IntegrationTest
@Transactional
public class SourceRepositoryIntTest {
	@Inject
	private SourceRepository sourceRepository;

	@Test
	public void saveValidSource_ShouldPersist() {
		// Given
		Source source_ft = new Source("fairy tail", "fairy tail url");
		Source source_dg = new Source("dgray man", "DGray Man url");

		// When
		sourceRepository.save(source_ft);
		sourceRepository.save(source_dg);

		// Then
		List<Source> result = sourceRepository.findAll();
		assertThat(result, containsInAnyOrder(source_ft, source_dg));
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	public void saveWithExistingName_ShouldThrowDataIntegrityViolationException() {
		// Given
		Source source_ft = new Source("fairy tail", "fairy tail url");
		Source source_dg = new Source("fairy tail", "DGray Man url");

		// When
		sourceRepository.save(source_ft);
		
		sourceRepository.save(source_dg);

		// Then
	}
	
	
	@Test(expected = DataIntegrityViolationException.class)
	public void saveWithExistingUrl_ShouldThrowDataIntegrityViolationException() {
		// Given
		Source source_ft = new Source("name1", "url1");
		Source source_dg = new Source("name2", "url1");

		// When
		sourceRepository.save(source_ft);
		sourceRepository.save(source_dg);

		// Then
	}
}
