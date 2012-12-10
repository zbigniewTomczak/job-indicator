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
import tomczak.job.indicator.model.Category;

@Path("/data/{id:[0-9][0-9]*}")
@RequestScoped
public class DataService {
	@Inject ChartData data;
	@Inject EntityHelper entityHelper;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ChartData getData(@PathParam("id") long id) {
		Category category = entityHelper.findById(Category.class, id);
	    if (category == null) {
	    	throw new WebApplicationException(Response.Status.NOT_FOUND);
	    }
	    //TODO retrieve real data
		return data;
	}
}
