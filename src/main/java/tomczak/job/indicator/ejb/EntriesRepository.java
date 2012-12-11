package tomczak.job.indicator.ejb;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import tomczak.job.indicator.model.Category;
import tomczak.job.indicator.model.CategoryVisitor;
import tomczak.job.indicator.model.Entry;

@Stateless
public class EntriesRepository {

	@Inject EntityManager em;
	
	public List<Entry> getEntriesForCategoryId(Long categoryId) {
		if (categoryId == null || em.find(Category.class, categoryId) == null) {
			return Collections.emptyList();
		}
		
		TypedQuery<Entry> query = em.createNamedQuery(Entry.SELECT_BY_CATEGORY, Entry.class);
		query.setParameter("catId", categoryId);
		return query.getResultList();
	}
	
	public List<Entry> getAllEntriesForSite(Long siteId) {
		TypedQuery<Entry> query = em.createNamedQuery(Entry.SELECT_BY_SITE, Entry.class);
		query.setParameter("siteId", siteId);
		return query.getResultList();
	}

	public Collection<Entry> getEntriesForCategoryIdWithSubcategories(Long categoryId) {
		if (categoryId == null || em.find(Category.class, categoryId) == null) {
			return Collections.emptyList();
		}
		
		final Set<Entry> list = new HashSet<Entry>();
		CategoryVisitor visitor = new CategoryVisitor() {
			
			@Override
			public void visit(Category category) {
				list.addAll(getEntriesForCategoryId(category.getId()));
			}
		};
		em.find(Category.class, categoryId).accept(visitor);
		
		return list;
		
	}

}
