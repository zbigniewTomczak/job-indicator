package tomczak.job.indicator.rest;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ChartData {
	public String label;
	public long[][] data;
	
	public ChartData() {
	}
	
	public ChartData(String label, long[][] data) {
		this.data = data;
		this.label = label;
	}
}
