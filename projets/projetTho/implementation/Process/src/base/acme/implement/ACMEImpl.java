package base.acme.implement;

import java.util.ArrayList;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import base.implement.BaseArchitecture;
import ACME.ACMEPackage;
import ACME.Attachment;
import ACME.Binding;
import ACME.Component;
import ACME.ComponentInstance;
import ACME.Connector;
import ACME.Link;
import ACME.Representation;

public class ACMEImpl extends BaseArchitecture {// implements BaseArchitectureService {

	public ACMEImpl() {
		// TODO Auto-generated constructor stub
	}
	public ACME.System getACMESystem(String file) {
		ACME.System sys = null;
		ACMEPackage.eINSTANCE.eClass();
		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
		try {
			//registry extent part of model file ex: *.variability
			reg.getExtensionToFactoryMap().put("acme", new XMIResourceFactoryImpl());
			URI uri = URI.createFileURI(file);
			ResourceSet resourceSet = new ResourceSetImpl();
			Resource resource = resourceSet.getResource(uri, true);
			
			//get root of variability model 
			sys = (ACME.System) resource.getContents().get(0);
		} catch (Exception e){
		}
		return sys;
	}
	public ArrayList<ComponentInstance> getParentComponentList(ACME.System sys){
		ArrayList<ComponentInstance> componentList = new ArrayList<ComponentInstance>();
		componentList.addAll(sys.getComponentDeclaration());
		return componentList;
	}
	public ArrayList<ComponentInstance> getComponentList(ACME.System sys) {
		ArrayList<ComponentInstance> componentList = new ArrayList<ComponentInstance>();
		componentList.addAll(sys.getComponentDeclaration());
		for (int i = 0; i < sys.getComponentDeclaration().size(); i++) {
			Component component = sys.getComponentDeclaration().get(i);
			if (component.getRepresentations() != null) {
				for (int j = 0;j < component.getRepresentations().size(); j++) {
					Representation representation = component.getRepresentations().get(j);
					if (representation.getSystems() != null) {
						for (int k = 0; k < representation.getSystems().size(); k++) {
							ACME.System system = representation.getSystems().get(k);
							componentList.addAll(getComponentList(system));
						}
					}
				}
			}
		}
		return componentList;
	}
	public ArrayList<Connector> getConnectorList(ACME.System sys) {
		ArrayList<Connector> connectorList = new ArrayList<Connector>();
		connectorList.addAll(sys.getConnectorDeclaration());
		for (int i = 0; i < sys.getComponentDeclaration().size(); i++) {
			Component component = sys.getComponentDeclaration().get(i);
			if (component.getRepresentations() != null) {
				for (int j = 0;j < component.getRepresentations().size(); j++) {
					Representation representation = component.getRepresentations().get(j);
					if (representation.getSystems() != null) {
						for (int k = 0; k < representation.getSystems().size(); k++) {
							ACME.System system = representation.getSystems().get(k);
							connectorList.addAll(getConnectorList(system));
						}
					}
				}
			}
		}
		return connectorList;
	}
	public ArrayList<Connector> getParentConnectorList(ACME.System sys) {
		ArrayList<Connector> connectorList = new ArrayList<Connector>();
		connectorList.addAll(sys.getConnectorDeclaration());
		return connectorList;
	}
	public ArrayList<Attachment> getAttchmentList(ACME.System sys) {
		ArrayList<Attachment> attachmentList = new ArrayList<Attachment>();
		EList<Link> link = sys.getAttachement();
		for (int i = 0; i < link.size(); i++) {
			if (link.get(i) instanceof Attachment) {
				Attachment att = (Attachment)link.get(i);
				attachmentList.add(att);
			}
		}
		
		for (int i = 0; i < sys.getComponentDeclaration().size(); i++) {
			Component component = sys.getComponentDeclaration().get(i);
			if (component.getRepresentations() != null) {
				for (int j = 0;j < component.getRepresentations().size(); j++) {
					Representation representation = component.getRepresentations().get(j);
					if (representation.getSystems() != null) {
						for (int k = 0; k < representation.getSystems().size(); k++) {
							ACME.System system = representation.getSystems().get(k);
							attachmentList.addAll(getAttchmentList(system));
						}
					}
				}
			}
		}
		return attachmentList;
	}
	public ArrayList<Attachment> getParentAttchmentList(ACME.System sys) {
		ArrayList<Attachment> attachmentList = new ArrayList<Attachment>();
		EList<Link> link = sys.getAttachement();
		for (int i = 0; i < link.size(); i++) {
			if (link.get(i) instanceof Attachment) {
				Attachment att = (Attachment)link.get(i);
				attachmentList.add(att);
			}
		}
		
		return attachmentList;
	}
	public ArrayList<Binding> getBindingList(ACME.System sys) {
		ArrayList<Binding> bindingList = new ArrayList<Binding>();
		EList<Link> link = sys.getAttachement();
		for (int i = 0; i < link.size(); i++) {
			if (link.get(i) instanceof Binding) {
				Binding att = (Binding)link.get(i);
				bindingList.add(att);
			}
		}
		for (int i = 0; i < sys.getComponentDeclaration().size(); i++) {
			Component component = sys.getComponentDeclaration().get(i);
			if (component.getRepresentations() != null) {
				for (int j = 0;j < component.getRepresentations().size(); j++) {
					Representation representation = component.getRepresentations().get(j);
					if (representation.getSystems() != null) {
						for (int k = 0; k < representation.getSystems().size(); k++) {
							ACME.System system = representation.getSystems().get(k);
							bindingList.addAll(getBindingList(system));
						}
					}
				}
			}
		}
		return bindingList;
	}
	public ArrayList<ACME.Binding> getParentBindingList(ACME.System sys) {
		ArrayList<ACME.Binding> bindingList = new ArrayList<ACME.Binding>();
		EList<Link> link = sys.getAttachement();
		for (int i = 0; i < link.size(); i++) {
			if (link.get(i) instanceof ACME.Binding) {
				ACME.Binding att = (ACME.Binding)link.get(i);
				bindingList.add(att);
			}
		}
		
		return bindingList;
	}
	public static void main(String args[]) {
		ACMEImpl acm = new ACMEImpl();
		ACME.System sys = acm.getACMESystem("model//acme//base.acme");
		ArrayList<ComponentInstance> list = acm.getComponentList(sys);
		for (int i = 0; i < list.size(); i++) {
			System.out.println("component:"+list.get(i).getName());
		}
		ArrayList<Connector> connectorlist = acm.getConnectorList(sys);
		for (int i = 0; i < connectorlist.size(); i++) {
			System.out.println("connector: "+connectorlist.get(i).getName());
		}
		
		ArrayList<Attachment> attachmentList = acm.getAttchmentList(sys);
		for (int i = 0; i < attachmentList.size(); i++) {
			System.out.println("attachment: "+attachmentList.get(i).getComp());
		}
		
		ArrayList<Binding> bindingList = acm.getBindingList(sys);
		for (int i = 0; i < bindingList.size(); i++) {
			System.out.println("binding: "+bindingList.get(i).getCompDest());
		}
	}
	
}
