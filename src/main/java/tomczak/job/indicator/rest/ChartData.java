package tomczak.job.indicator.rest;

import javax.inject.Inject;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ChartData {
	public String label = "Europe (EU27)";
	@Inject public double[][] data;
	
	public ChartData() {
	}
}
