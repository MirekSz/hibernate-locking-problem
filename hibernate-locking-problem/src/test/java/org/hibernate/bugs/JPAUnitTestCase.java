package org.hibernate.bugs;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.bugs.model.Operator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**@formatter:off
 * This template demonstrates how to develop a test case for Hibernate ORM,
 * using the Java Persistence API.
 */
public class JPAUnitTestCase {

	private EntityManagerFactory entityManagerFactory;

	@Before
	public void init() {
		entityManagerFactory = Persistence.createEntityManagerFactory("templatePU");
	}

	@After
	public void destroy() {
		entityManagerFactory.close();
	}

	// Entities are auto-discovered, so just add them anywhere on class-path
	// Add your tests, using standard JUnit.
	@Test
	public void hhh123Test() throws Exception {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		addTwoOperators(entityManager);
		
		entityManager.clear();
		entityManager.getTransaction().begin();

		Session session = (Session) entityManager.getDelegate();
		Operator mikeSimpleLoad = (Operator) session.load(Operator.class, 1L); //no sql simple proxy crreation
		
		Operator johnLoadWithLock = (Operator) session.load(Operator.class, 2L, LockMode.PESSIMISTIC_WRITE); 
		// this produces in console select operator0_.id as id1_0_0_, operator0_.name as name2_0_0_ from Operator operator0_ where operator0_.id in (?, ?) for update

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	private void addTwoOperators(EntityManager entityManager) {
		entityManager.getTransaction().begin();

		Operator mike = new Operator();
		mike.setId(1L);
		mike.setName("Mike");
		entityManager.persist(mike);

		Operator john = new Operator();
		john.setId(2L);
		john.setName("John");
		entityManager.persist(john);

		entityManager.getTransaction().commit();
	}
}
