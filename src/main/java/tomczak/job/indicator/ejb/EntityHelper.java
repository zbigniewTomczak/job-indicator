package tomczak.job.indicator.ejb;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Stateless
public class EntityHelper {

	@Inject
	private EntityManager em;
	
	public <T> T findById(Class<T> c, Long id) {
		return em.find(c, id);
	}
	
	public void persist(Object entity) {
		em.persist(entity);
	}
	
	public <T> List<T> getAll(Class<T> c) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> criteria = cb.createQuery(c);
		Root<T> root = criteria.from(c);
		return em.createQuery(criteria.select(root)).getResultList();
	}
	
	public <T> Long getTotalQuantity(Class<T> c) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> countCriteria = cb.createQuery(Long.class);
		countCriteria.select(cb.count(countCriteria.from(c)));
		// Following line if commented causes [org.hibernate.hql.ast.QuerySyntaxException: Invalid path: 'generatedAlias1.enabled' [select count(generatedAlias0) from xxx.yyy.zzz.Brand as generatedAlias0 where ( generatedAlias1.enabled=:param0 ) and ( lower(generatedAlias1.description) like :param1 )]]
		//em.createQuery(cq);
		//cq.where(pArray);
		return em.createQuery(countCriteria).getSingleResult();
	}
}
