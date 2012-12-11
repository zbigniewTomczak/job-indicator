package tomczak.job.indicator.view;

import java.io.IOException;
import java.net.MalformedURLException;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.TreeNode;

import tomczak.job.indicator.helper.DataRetrievalJob;

@Model
public class CategoriesTreeBean {
	@Inject
	private TreeNode root;

	private TreeNode selectedNode;

	@PostConstruct
	@SuppressWarnings("unused")
	private void init() {
		selectedNode = root.getChildCount() > 0 ? root.getChildren().get(0) : null;
	}

	public void onNodeSelect(NodeSelectEvent event) {
		//TODO fire event event.getTreeNode().getData()
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Selected", event.getTreeNode().toString());  
		  
        FacesContext.getCurrentInstance().addMessage(null, message); 
        selectedNode = event.getTreeNode();
	}

	public TreeNode getRoot() {
		return root;
	}

	public void setRoot(TreeNode root) {
		this.root = root;
	}

	public TreeNode getSelectedNode() {
		return selectedNode;
	}

	public void setSelectedNode(TreeNode selectedNode) {
		this.selectedNode = selectedNode;
	}

	@Inject DataRetrievalJob job;
	public void create() {
		try {
			job.populateData();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
