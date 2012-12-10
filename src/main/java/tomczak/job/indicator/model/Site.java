package tomczak.job.indicator.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Site implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	@Column(unique=true, nullable=false)
	private String name;
	@Column(unique=true, nullable=false)
	private String domain;
	
	@OneToMany(mappedBy="site")
	private List<Category> categories;
	
	private static final long serialVersionUID = 1L;

	public String toString() {
		return name != null ? name : super.toString();
	}

	public Site(Site site) {
		setName(site.getName());
		setDomain(site.getDomain());
	}
	
	public Site() {
		super();
	}   
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}   
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}   
	public String getDomain() {
		return this.domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}
	public List<Category> getCategories() {
		return categories;
	}
	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}
   
}
