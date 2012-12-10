package tomczak.job.indicator.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ejb.Singleton;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import tomczak.job.indicator.ejb.EntityHelper;
import tomczak.job.indicator.ejb.EntriesUpdater;
import tomczak.job.indicator.model.Category;
import tomczak.job.indicator.model.Entry;
import tomczak.job.indicator.model.Site;

@ApplicationScoped @Singleton
public class DataAnalyzer {

	@Inject EntityHelper eh;
	@Inject EntriesUpdater eu;
	
	final String format = "%s\\s*\\(\\d+\\)";
	
	//TODO liquidate checked exceptions
	public void updateDataForSite(Long siteId) throws MalformedURLException, IOException {
		Site site = eh.findById(Site.class, siteId);
		if (site == null) {
			throw new IllegalArgumentException(""+siteId);
		}
		
		for(Category c: site.getCategories()) {
			updateDataForCategory2(c);
		}
	}
	
	private void updateDataForCategory2(Category category) throws MalformedURLException, IOException {
		if (category.getUrl() == null || category.getUrl().isEmpty()) {
			return;
		}
		
		if (category.getChildCategories().size() == 0) {
			updateEntriesData(category.getUrl(), category);
		} else {
			List<Category> list = new ArrayList<Category>(category.getChildCategories());
			Iterator<Category> iter = list.iterator();
			while(iter.hasNext()) {
				Category c = iter.next();
				if (c.getUrl() != null && !c.getUrl().isEmpty()) {
					updateDataForCategory2(c);
					iter.remove();
				}
			}
			updateEntriesData(category.getUrl(), list.toArray(new Category[]{}));
		}
	}
	
	private void updateEntriesData(String url, Category... categories) throws MalformedURLException, IOException {
		if (categories.length == 0) {
			return;
		}
		
		BufferedReader bufferedReader = null;
		StringBuffer content = new StringBuffer();
		String line;
		List<Entry> entries = new ArrayList<Entry> ();
		try {
			bufferedReader = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
			while((line = bufferedReader.readLine()) != null) {
				content.append(line);
			}
		} finally {
			if(bufferedReader != null) {
				bufferedReader.close();
			}
		}
		for (Category c: categories) {
			Pattern pattern = Pattern.compile(String.format(format, Pattern.quote(c.getName())));
			Matcher matcher = pattern.matcher(content);
			Integer number = null;
			if (matcher.find()) {
				Scanner scanner = new Scanner(matcher.group());
				scanner.useDelimiter(Pattern.compile("(.*\\()|(\\))"));
				if (scanner.hasNextInt()) {
					number = scanner.nextInt();
				}
			}
			Entry entry = new Entry();
			entry.setCategory(c);
			entry.setDate(new Date()); //TODO change to injected field
				
			if (number != null) {
				entry.setNumber(number);
			} else {
				entry.setNumber(0);
			}
			entries.add(entry);
		}
		eu.addEntries(entries.toArray(new Entry[]{}));
	}

	@Deprecated()
	/**
	 * Highly inefficient
	 */
	private void updateDataForCategory(Long categoryId) {
		Category category = eh.findById(Category.class, categoryId);
		if (category.getChildCategories() != null && category.getChildCategories().size() > 0) {
			updateDataForCategory(category.getId());
		}
		
		String categoryUrl = getCategoryUrl(category);
		if (categoryUrl == null || categoryUrl.isEmpty()) {
			return;
		}
		
		Pattern pattern = Pattern.compile(String.format(format, Pattern.quote(category.getName())));
		Integer number = null;
		BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new InputStreamReader(new URL(categoryUrl).openStream()));
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				Matcher matcher = pattern.matcher(line);
				if (matcher.find()) {
					Scanner scanner = new Scanner(matcher.group());
					scanner.useDelimiter(Pattern.compile("(.*\\()|(\\))"));
					if (scanner.hasNextInt()) {
						number = scanner.nextInt();
					}
					break;
				}
			}
		} catch (MalformedURLException e) {
			// TODO: handle exception
		} catch (IOException e) {
			// TODO: handle exception
		} finally {
			if(bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		Entry entry = new Entry();
		entry.setCategory(category);
		entry.setDate(new Date()); //TODO change to injected field
			
		if (number != null) {
			entry.setNumber(number);
		} else {
			entry.setNumber(0);
		}
		eu.addEntries(entry);

	}

	private String getCategoryUrl(Category category) {
		if (category.getUrl() != null && !category.getUrl().isEmpty()) {
			return category.getUrl();
		} else if (category.getParentCategory() != null) {
			return getCategoryUrl(category.getParentCategory());
		}
		
		return null;
	}
}
