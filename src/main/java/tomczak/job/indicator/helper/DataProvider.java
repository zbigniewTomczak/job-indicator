package tomczak.job.indicator.helper;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.inject.Inject;

import tomczak.job.indicator.ejb.EntriesRepository;
import tomczak.job.indicator.model.Entry;

public class DataProvider {
	@Inject EntriesRepository eRepo;
	public long[][] getDataForCategoryId(Long catId) {
		Set<Entry> list = new HashSet<Entry>(eRepo.getEntriesForCategoryIdWithSubcategories(catId));
		Map<Date, Long> map = new TreeMap<Date, Long>();
		for (Entry e: list) {
			if(map.containsKey(e.getDate())) {
				map.put(e.getDate(), map.get(e.getDate()) + e.getNumber());
			} else {
				map.put(e.getDate(), e.getNumber().longValue());
			}
		}
		
		return produceData(map);
	}
	
	public long[][] getDataForSiteId(Long siteId) {
		Set<Entry> list = new HashSet<Entry>(eRepo.getAllEntriesForSite(siteId));
		Map<Date, Long> map = new TreeMap<Date, Long>();
		for (Entry e: list) {
			if(map.containsKey(e.getDate())) {
				map.put(e.getDate(), map.get(e.getDate()) + e.getNumber());
			} else {
				map.put(e.getDate(), e.getNumber().longValue());
			}
		}
		
		return produceData(map);
	}
	
	private long[][] produceData(Map<Date, Long> map) {
		long [][] data = new long[map.size()][2];
		int i = 0;
		for(Date d: map.keySet()) {
			data[i][0] = d.getTime();
			data[i][1] = map.get(d);
			i++;
		}
		return data;

	}
}
