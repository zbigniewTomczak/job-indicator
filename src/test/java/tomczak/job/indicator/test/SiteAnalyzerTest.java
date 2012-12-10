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
import org.junit.Ignore;
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
public class SiteAnalyzerTest {

	@Deployment
	public static Archive<?> createTestArchive() {
		WebArchive wa= ShrinkWrap.create(WebArchive.class, "test.war")
				.addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
				.addAsResource("import.sql", "import.sql")
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
	//@Ignore
	public void testSiteDownload() throws MalformedURLException, IOException {
		da.updateDataForSite(1L);
		assertEquals(752, er.getAllEntriesForSite(1L).size());
	}
	
}