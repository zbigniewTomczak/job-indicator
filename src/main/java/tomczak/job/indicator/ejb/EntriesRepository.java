package tomczak.job.indicator.ejb;

import java.util.Collections;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import tomczak.job.indicator.model.Category;
import tomczak.job.indicator.model.Entry;

@Stateless
public class EntriesRepository {

	@Inject EntityManager em;
	
	public List<Entry> getEntriesForCategory(Category category) {
		if (category == null || category.getId() == null) {
			return Collections.emptyList();
		}
		
		TypedQuery<Entry> query = em.createNamedQuery(Entry.SELECT_BY_CATEGORY, Entry.class);
		query.setParameter("catId", category.getId());
		return query.getResultList();
	}
	
	public List<Entry> getAllEntriesForSite(Long siteId) {
		TypedQuery<Entry> query = em.createNamedQuery(Entry.SELECT_BY_SITE, Entry.class);
		query.setParameter("siteId", siteId);
		return query.getResultList();
	}

}
