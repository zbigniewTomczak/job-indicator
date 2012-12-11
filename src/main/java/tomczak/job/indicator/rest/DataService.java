package tomczak.job.indicator.rest;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import tomczak.job.indicator.ejb.EntityHelper;
import tomczak.job.indicator.helper.Initial;
import tomczak.job.indicator.model.Category;
import tomczak.job.indicator.model.Site;

@Path("/")
@RequestScoped
public class DataService {
	@Inject EntityHelper entityHelper;
	@Inject tomczak.job.indicator.helper.DataProvider dataProvider;
	@Inject @Initial Long siteId;
	
	@GET
	@Path("/data/{id:[0-9][0-9]*}")
	@Produces(MediaType.APPLICATION_JSON)
	public ChartData getData(@PathParam("id") long id) {
		Category category = entityHelper.findById(Category.class, id);
	    if (category == null) {
	    	throw new WebApplicationException(Response.Status.NOT_FOUND);
	    }
	    return new ChartData(category.getName(), dataProvider.getDataForCategoryId(category.getId()));
	}
	
	@GET
	@Path("/data")
	@Produces(MediaType.APPLICATION_JSON)
	public ChartData getRootData() {
		Site site = entityHelper.findById(Site.class, siteId);
	    if (site == null) {
	    	throw new WebApplicationException(Response.Status.NOT_FOUND);
	    }
	    return new ChartData(site.getName(), dataProvider.getDataForSiteId(siteId));
	}
	
}
