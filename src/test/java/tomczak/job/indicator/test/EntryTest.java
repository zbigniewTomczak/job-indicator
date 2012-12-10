package tomczak.job.indicator.test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import tomczak.job.indicator.ejb.EntityHelper;
import tomczak.job.indicator.ejb.EntriesUpdater;
import tomczak.job.indicator.helper.DataRetrievalJob;
import tomczak.job.indicator.helper.Resources;
import tomczak.job.indicator.model.Category;
import tomczak.job.indicator.model.Entry;
import tomczak.job.indicator.model.Site;


@RunWith(Arquillian.class)
public class EntryTest {

	@Deployment
	public static Archive<?> createTestArchive() {
		WebArchive wa= ShrinkWrap.create(WebArchive.class, "test.war")
				.addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
				//.addAsResource("import.sql", "import.sql")
				.addAsWebInfResource("test-ds.xml")
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
				.addPackage(EntriesUpdater.class.getPackage())
				.addPackage(Entry.class.getPackage())
				.addPackage(Resources.class.getPackage());
		System.out.println(wa.toString(true));
		return wa;
	}
	
	@Inject EntriesUpdater entriesUpdater;
	@Inject EntityHelper ef;
	@Inject DataRetrievalJob job;	
	
	@Test
	public void testTimerJob() throws MalformedURLException, IOException {
		job.populateData();
	}
	
	@Test
	public void testEntry() {
		
		assertNotNull(entriesUpdater);
		
		Site site = new Site();
		site.setDomain("http://example.com");
		site.setName("Example");
		ef.persist(site);
		assertNotNull(site.getId());
		
		Category category = new Category();
		category.setSite(site);
		category.setName("Category1");
		ef.persist(category);
		assertNotNull(category.getId());
		
		Entry entry = new Entry();
		entry.setCategory(category);
		entry.setDate(new Date());
		entry.setNumber(37);
		entriesUpdater.addEntries(entry);
		assertNotNull(entry.getId());
		
		Long entryId = entry.getId();
		entry.setNumber(40);
		entriesUpdater.addEntries(entry);
		assertEquals(entryId, entry.getId());
		
		entry = ef.findById(Entry.class, entryId);
		assertEquals(40, entry.getNumber().intValue());
		
		entry = new Entry();
		entry.setCategory(category);
		entry.setDate(new Date());
		entry.setNumber(45);
		entriesUpdater.addEntries(entry);
		entry = ef.findById(Entry.class, entryId);
		assertEquals(45, entry.getNumber().intValue());
	}
}
