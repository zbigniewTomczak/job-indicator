package tomczak.job.indicator.test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
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
import tomczak.job.indicator.helper.Resources;
import tomczak.job.indicator.model.Category;
import tomczak.job.indicator.model.Entry;
import tomczak.job.indicator.model.Site;


@RunWith(Arquillian.class)
public class EntityHelperTest {

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
	
	@Inject EntityHelper entityHelper;
	
	@Test
	public void testGetAll() {
		assertEquals(799,entityHelper.getAll(Category.class).size());
	}
	
	@Test
	public void testGetTotalQuantity() {
		assertEquals(799L, entityHelper.getTotalQuantity(Category.class).longValue());
	}
}
