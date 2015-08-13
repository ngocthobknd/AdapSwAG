/**
 * 
 */
package base.fractalADL.implement;

import java.util.ArrayList;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.ow2.fractal.f4e.fractal.Binding;
import org.ow2.fractal.f4e.fractal.Component;
import org.ow2.fractal.f4e.fractal.Definition;
import org.ow2.fractal.f4e.fractal.FractalPackage;

import base.api.BaseArchitectureService;
import ACME.Attachment;
import ACME.ComponentInstance;
import ACME.Connector;


/**
 * @author hnt
 *
 */
public class FractalADLImpl implements BaseArchitectureService {

	/**
	 * 
	 */
	public FractalADLImpl() {
	}
	public Definition getArchitectureDefinition(String file) {
		FractalPackage.eINSTANCE.eClass();
		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
		Definition definition = null;
		try {
			//registry extent part of model file ex: *.variability
			reg.getExtensionToFactoryMap().put("fractal", new XMIResourceFactoryImpl());
			URI uri = URI.createFileURI(file);
			ResourceSet resourceSet = new ResourceSetImpl();
			Resource resource = resourceSet.getResource(uri, true);
			
			//get root of variability model 
			definition = (Definition) resource.getContents().get(0);
		} catch (Exception e){
		}
		
		return definition;
	}

	public ArrayList<Component> getSubComponent(Component component) {
		ArrayList<Component> subComponentList = new ArrayList<Component>();
		subComponentList.add(component);
		for (int i = 0; i < component.getSubComponents().size(); i++) {
			subComponentList.addAll(getSubComponent(component.getSubComponents().get(i)));
		}
		return subComponentList;
	}
	public ArrayList<Component> getComponentList(Definition definition) {
		ArrayList<Component> componentList = new ArrayList<Component>();
		for (int i = 0; i < definition.getSubComponents().size(); i ++) {
			componentList.addAll(getSubComponent(definition.getSubComponents().get(i)));
		}
		return componentList;
	}
	public ArrayList<Component> getParentComponentList(Definition definition) {
		ArrayList<Component> componentList = new ArrayList<Component>();
		componentList.addAll(definition.getSubComponents());
		return componentList;
	}
	public ArrayList<Binding> getBindingList(Definition definition) {
		ArrayList<Binding> bindingList = new ArrayList<Binding>();
		for (int i = 0; i < definition.getBindings().size(); i++) {
			bindingList.add(definition.getBindings().get(i));
		}
		return bindingList;
	}
	public ArrayList<Binding> getParentBindingList(Definition definition) {
		ArrayList<Binding> bindingList = new ArrayList<Binding>();
		bindingList.addAll(definition.getBindings());
		
		return bindingList;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BaseArchitectureService m = new FractalADLImpl();
		Definition def = m.getArchitectureDefinition("model//composite2//architecture.fractal");
		ArrayList<Component> compList = m.getComponentList(def);
		for (int i = 0; i < compList.size(); i++) {
			System.out.println(compList.get(i).getName());
		}

	}
	@Override
	public ACME.System getACMESystem(String file) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ArrayList<ComponentInstance> getParentComponentList(ACME.System sys) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ArrayList<Connector> getParentConnectorList(ACME.System sys) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ArrayList<Attachment> getParentAttchmentList(ACME.System sys) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ArrayList<ACME.Binding> getParentBindingList(ACME.System sys) {
		// TODO Auto-generated method stub
		return null;
	}

}
