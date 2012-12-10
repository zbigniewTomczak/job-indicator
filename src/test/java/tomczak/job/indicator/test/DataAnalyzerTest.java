package tomczak.job.indicator.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.net.MalformedURLException;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import tomczak.job.indicator.ejb.CategoryRepository;
import tomczak.job.indicator.ejb.EntityHelper;
import tomczak.job.indicator.ejb.EntriesRepository;
import tomczak.job.indicator.ejb.EntriesUpdater;
import tomczak.job.indicator.helper.DataAnalyzer;
import tomczak.job.indicator.helper.Resources;
import tomczak.job.indicator.model.Category;
import tomczak.job.indicator.model.Entry;
import tomczak.job.indicator.model.Site;


@RunWith(Arquillian.class)
public class DataAnalyzerTest {

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
	
	@Inject DataAnalyzer da;
	@Inject EntityHelper ef;
	@Inject CategoryRepository cr;
	@Inject EntriesRepository er;
	
	@Test
	public void testGetDataForCategory() throws MalformedURLException, IOException {
		assertNotNull(da);
		
		Site site = new Site();
		site.setDomain("http://example.com");
		site.setName("Example");
		ef.persist(site);
		assertNotNull(site.getId());
		
		Category category = new Category();
		category.setSite(site);
		category.setName("Informatyka/Programowanie");
		category.setUrl("http://www.goldenline.pl/praca/informatyka-programowanie");
		ef.persist(category);
		assertNotNull(category.getId());
		
		Category subcategory = new Category();
		subcategory.setSite(site);
		subcategory.setName("kujawsko-pomorskie");
		subcategory.setParentCategory(category);
		ef.persist(subcategory);
		assertNotNull(category.getId());
		
		da.updateDataForSite(site.getId());
		
		assertEquals(1, er.getEntriesForCategory(subcategory).size());
		assertNotNull(er.getEntriesForCategory(subcategory).get(0).getNumber());
	}
}
