package tomczak.job.indicator.ejb;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Singleton;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import tomczak.job.indicator.model.Entry;

@ApplicationScoped @Singleton
public class EntriesUpdater {

	@Inject EntityManager em;
	
	@Inject Logger logger;
	
	public void addEntries(Entry... entries) {
		for(Entry e: entries) {
			if(neededAnUpdate(e)) {
				continue;
			}
			
			em.persist(e);
		}
		//TODO fire event ?
	}

	private boolean neededAnUpdate(Entry e) {
		Entry persistedEntry = getCorrectedEntry(e);
		if (persistedEntry != null) {
			persistedEntry.setDate(e.getDate());
			persistedEntry.setNumber(e.getNumber());
			//em.merge(persistedEntry); //Merge not needed cause we under transaction
			return true;
		}
		
		return false;
	}

	private Entry getCorrectedEntry(Entry entry) {
		if (entry.getCategory() != null && entry.getCategory().getId() != null && entry.getDate() != null) {
			TypedQuery<Entry> query = em.createNamedQuery(Entry.SELECT_BY_DATE_AND_CATEGORY, Entry.class);
			query.setParameter("catId", entry.getCategory().getId());
			query.setParameter("date", entry.getDate());
			List<Entry> resultList = query.getResultList();

			if (resultList.size() == 1) {
				return resultList.get(0);
			} else if (resultList.size() > 1) {
				for (Entry e: resultList) {
				  logger.warning("Duplicate Entry: " + e);
				}
			}
		}
		return null;
	}

}
