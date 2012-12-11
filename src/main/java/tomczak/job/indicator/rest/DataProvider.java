package tomczak.job.indicator.rest;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

//@Path("/data")
@RequestScoped
public class DataProvider {
	ChartData data;
	
	
	public DataProvider() {
		//this.data = new ChartData("koko", produceData());
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ChartData getData() {
		return data;
	}
	
	//@javax.enterprise.inject.Produces
	public static double[][] produceData() {
		double[][] data = new double[10][];
		data[0] = new double[]{1999, 3.0};
		data[1] = new double[]{2000, 3.9};
		data[2] = new double[]{2001, 2.0};
		data[3] = new double[]{2002, 1.2};
		data[4] = new double[]{2003, 1.3};
		data[5] = new double[]{2004, 2.5};
		data[6] = new double[]{2005, 2.0};
		data[7] = new double[]{2006, 3.1};
		data[8] = new double[]{2007, 2.9};
		data[9] = new double[]{2008, 0.9};
		return data;
	}
}
