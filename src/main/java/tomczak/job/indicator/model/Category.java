package tomczak.job.indicator.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Category implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	private String name;

	@ManyToOne(fetch=FetchType.LAZY)
	private Category parentCategory;
	
	@OneToMany(mappedBy="parentCategory", fetch=FetchType.LAZY)
	private List<Category> childCategories;
	
	@ManyToOne
	private Site site;
	
	private String url;
	
	private static final long serialVersionUID = 1L;

	
	@Override
	public String toString() {
		return name != null ? name : super.toString();
	}
	
	public Category() {
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
	public Category getParentCategory() {
		return parentCategory;
	}
	public void setParentCategory(Category parentCategory) {
		this.parentCategory = parentCategory;
	}
	public Site getSite() {
		return site;
	}
	public void setSite(Site site) {
		this.site = site;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public List<Category> getChildCategories() {
		return childCategories;
	}
	public void setChildCategories(List<Category> childCategories) {
		this.childCategories = childCategories;
	}   
   
}
