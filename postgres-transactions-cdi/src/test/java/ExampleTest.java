import com.arjuna.ats.arjuna.coordinator.TransactionReaper;
import com.johnreah.cdi_jta_helper.persistence.H2DataSourceWrapper;
import com.johnreah.postgres.cdi.SampleEntity;
import com.johnreah.postgres.cdi.SampleService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.*;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(ExampleTestExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ExampleTest {

//	@Inject
//	BeanManager beanManager;
//
//	@Inject
//	H2DataSourceWrapper datasource;

	@Inject
	UserTransaction ut;

	@Inject
	EntityManager em;

	@Inject
	EntityManager em2;

	@Inject
	SampleService sampleService;

	@AfterAll
	private void shutdownTest(){
		TransactionReaper.terminate(true);
	}

	@BeforeEach
	public void beforeEach() {
		sampleService.deleteAllSamples();
	}

	@Test
	@Order(value = 0)
	void testScopeActive(BeanManager beanManager) {
	    assertTrue(beanManager.getContext(SessionScoped.class).isActive());
	  }

	@Test
	@Order(value = 1)
	public void testCreateManual(SampleService sampleService) throws SystemException, NotSupportedException, HeuristicRollbackException, HeuristicMixedException, RollbackException {
		long countBefore = sampleService.countSamples();

	    // manual transaction handling by injecting the user-transaction
		ut.begin();
		em.joinTransaction();
		SampleEntity sampleEntity = new SampleEntity(123, "one-two-three");
		em.persist(sampleEntity);
	    ut.commit();

		ut.begin();
		em2.joinTransaction();
		SampleEntity se2 = new SampleEntity(456, "four-five-six");
		em2.persist(se2);
        ut.commit();

		assertEquals(2, sampleService.countSamples() - countBefore, "Should be 2 rows created");
	}

	@Test
	@Order(value = 2)
	public void testCreateMultipleNonTransactional(SampleService sampleService) {
		long countBefore = sampleService.countSamples();
		Map<Integer, String> newSampleValues = new HashMap<Integer, String>() {{
			put(1, "one");
			put(2, "two");
			put(3, "three");
		}};
		sampleService.createSamplesNonTransactional(newSampleValues);
		assertEquals(3, sampleService.countSamples() - countBefore, "Should be 0 rows created");
	}

    @Test
    @Order(value = 3)
	public void testCreateMultipleTransactional(SampleService sampleService) {
		long countBefore = sampleService.countSamples();
		Map<Integer, String> newSampleValues = new HashMap<Integer, String>() {{
			put(1, "one");
			put(2, "two");
			put(3, "three");
		}};
		sampleService.createSamplesTransactional(newSampleValues);
		assertEquals(3, sampleService.countSamples() - countBefore, "Should be 3 rows created");
	}

    @Test
    @Order(value = 4)
	public void testCreateMultipleNonTransactionalWithError(SampleService sampleService) {
		long countBefore = sampleService.countSamples();
		try {
			Map<Integer, String> newSampleValues = new HashMap<Integer, String>() {{
				put(1, "one");
				put(2, "two");
				put(3, "two");
			}};
			sampleService.createSamplesNonTransactional(newSampleValues);
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertEquals(2, sampleService.countSamples() - countBefore, "Should be 2 rows created");
	}

    @Test
    @Order(value = 5)
	public void testCreateMultipleTransactionalWithError(SampleService sampleService) {
		long countBefore = sampleService.countSamples();
		try {
			Map<Integer, String> newSampleValues = new HashMap<Integer, String>() {{
				put(1, "one");
				put(2, "two");
				put(3, "two");
			}};
			sampleService.createSamplesTransactional(newSampleValues);
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertEquals(0, sampleService.countSamples() - countBefore, "Should be 0 rows created");
	}

}
