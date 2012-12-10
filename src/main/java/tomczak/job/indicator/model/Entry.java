package tomczak.job.indicator.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

@Entity
@NamedQueries({
		@NamedQuery(name=Entry.SELECT_BY_DATE_AND_CATEGORY,query="SELECT e FROM Entry e WHERE e.date = :date AND e.category.id = :catId"),
		@NamedQuery(name=Entry.SELECT_BY_CATEGORY,query="SELECT e FROM Entry e WHERE e.category.id = :catId"),
		@NamedQuery(name=Entry.SELECT_BY_SITE,query="SELECT e FROM Entry e WHERE e.category.site.id = :siteId")
})
@Table(uniqueConstraints=@UniqueConstraint(columnNames={"category_id", "date"}))
public class Entry implements Serializable {

	public static final String SELECT_BY_DATE_AND_CATEGORY = "Entry.getByDateAndCategory";
	public static final String SELECT_BY_CATEGORY = "Entry.getByCategory";
	public static final String SELECT_BY_SITE = "Entry.getBySite";

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	@ManyToOne
	private Category category;
	
	@Temporal(TemporalType.DATE)
	private Date date;
	
	private Integer number;
	
	@Override
	public String toString() {
		return String.format("(%s, %s, %tF, %s)", id, category, date, number);
	}
	
	private static final long serialVersionUID = 1L;

	public Entry() {
		super();
	}   
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Integer getNumber() {
		return number;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}   

}
