package tomczak.job.indicator.ejb;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import tomczak.job.indicator.helper.Initial;
import tomczak.job.indicator.model.Category;
import tomczak.job.indicator.model.Site;

@Stateless
public class CategoryRepository {
	@Inject
	private EntityManager em;
	
	public List<Category> getAllLeafCategories(Long siteId) {
		Site site = em.find(Site.class, siteId);
		if (site == null) {
			return Collections.emptyList();
		}
		
		List<Category> list = site.getCategories();
		Iterator<Category> iter = list.iterator();
		while(iter.hasNext()) {
			if(iter.next().getChildCategories().size() > 0) {
				iter.remove();
			}
		}
		
		return list;
	}
	
	@Produces
	public TreeNode createCategoriesTree(@Initial Long siteId) {
		TreeNode root = new DefaultTreeNode("root",  null);
		Site site = em.find(Site.class, siteId);
		if (site == null) {
			return root;
		}
		
		TreeNode siteNode = new DefaultTreeNode(new Site(site), root);
		List<Category> list = site.getCategories();
		Iterator<Category> iter = list.iterator();
		while(iter.hasNext()) {
			Category category = iter.next();
			if(category.getParentCategory() == null) {
				createCategoriesTree(siteNode, category);
			}
		}
		
		return root;
	}

	private void createCategoriesTree(TreeNode parentNode, Category category) {
		DefaultTreeNode node = new DefaultTreeNode(category, parentNode);
		
		List<Category> list = category.getChildCategories();
		Iterator<Category> iter = list.iterator();
		while(iter.hasNext()) {
			createCategoriesTree(node, iter.next());
		}
	}
}
