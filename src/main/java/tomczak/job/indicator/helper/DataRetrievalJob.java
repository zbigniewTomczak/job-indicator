package tomczak.job.indicator.helper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.New;
import javax.inject.Inject;

import tomczak.job.indicator.ejb.EntityHelper;
import tomczak.job.indicator.model.Entry;
import tomczak.job.indicator.model.Site;

@ApplicationScoped @Singleton
public class DataRetrievalJob {
  
	@Inject @New EntityHelper eh;
	@Inject @New DataAnalyzer da;
	@Inject Logger logger;
	
	@Schedule(hour="16")
	public void populateData() throws MalformedURLException, IOException {
		logger.info("Populating data"); 
		long entriesNumberBefore = eh.getTotalQuantity(Entry.class);
		List<Site> sites = eh.getAll(Site.class);
		for(Site site: sites) {
			da.updateDataForSite(site.getId());
		}
		//change to CDI interceptor?
		logger.info(eh.getTotalQuantity(Entry.class) - entriesNumberBefore + " Entries were added during this job");
	}
	
}
