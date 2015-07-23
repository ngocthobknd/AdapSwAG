package transformation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.omg.CORBA.ExceptionList;
import org.ow2.fractal.f4e.fractal.AbstractComponent;
import org.ow2.fractal.f4e.fractal.Binding;
import org.ow2.fractal.f4e.fractal.Component;
import org.ow2.fractal.f4e.fractal.Content;
import org.ow2.fractal.f4e.fractal.Definition;
import org.ow2.fractal.f4e.fractal.FractalFactory;
import org.ow2.fractal.f4e.fractal.FractalPackage;
import org.ow2.fractal.f4e.fractal.Interface;
import org.ow2.fractal.f4e.fractal.RealizationComponent;

import tree.ResolutionTree;
import tree.VSpecTree;
import verification.ResolutionModelVerification;
import cvl.Choice;
import cvl.ChoiceResolution;
import cvl.ObjectExistence;
import cvl.ObjectHandle;
import cvl.ObjectSubstitution;
import cvl.ParametricSlotAssignment;
import cvl.VInstance;
import cvl.VSpec;
import cvl.VSpecResolution;
import cvl.VariableValueAssignment;
import cvl.VariationPoint;

public class GeneratingProductArchitecture implements ProductGenerationService {
	
	/*
	 * algorithm for generating product architecture
	 * INPUT: Variability model, Resolution model, Base model
	 * OUTPUT : Product model
	 * Context:
	 * 1. 	Variability model (VM) -> VSpec list (size = n), VPList (size = m)
	 *		Resolution model (RM) -> Decision list (size = n)
	 *  
	 * 2.  Search in VM and RM : for i = 0 to n 
	 * 		if (VM(i).availabilityTime != runtime) and (RM(i).decision = false) reducing VSpecList and DecisionList by remove VM(i) and RM(i)
	 * 		else 
	 * 			search VP that maps to VM(i) 
	 * 				if VP is ObjectExistence  ....
	 * 				if ObjectSubstitution 
	 * 					VP -> MOFRef (component name)
	 * 					Search MOFRef in BM -> component C
	 * 					if C contains attributes
	 * 						search VPs in VPList contain these attributes -> VSpec(k) -> RM(k)
	 * 						v = getValue of RM(k)
	 * 						setAttribute in Component = v
	 * 			add component C to product architecture	 
	 * 						   
	 *			 
	 */
	
	Definition destinationDefinition;
	public GeneratingProductArchitecture() {
		
		
	}
		
	public static Definition readArchitecture(String file) {
		FractalPackage.eINSTANCE.eClass();
		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
		try {
			//registry extent part of model file ex: *.variability
			reg.getExtensionToFactoryMap().put("fractal", new XMIResourceFactoryImpl());
		} catch (Exception e){
		}
		ResourceSet resourceSet = new ResourceSetImpl();
		URI uri = URI.createFileURI("model//architecture.fractal");
		Resource resource = resourceSet.getResource(uri, true);
		//get root of variability model 
		Definition definition = (Definition) resource.getContents().get(0);
		return definition;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void createProductModel(Definition sourceDefinitionFractal) {
	
		FractalPackage.eINSTANCE.eClass();
		FractalFactory fractalFractory = FractalFactory.eINSTANCE;
		destinationDefinition = fractalFractory.createDefinition();
		destinationDefinition.setName("product"+sourceDefinitionFractal.getName());

		ResolutionTree rs = new ResolutionTree();
		VSpecTree vSpec = new VSpecTree();
		
		ArrayList<VSpec> vSpecList = vSpec.vSpecList;
		ArrayList<VariationPoint> vpList = vSpec.VPList;
		ArrayList<VSpecResolution> resolutionList = rs.resolutionList; 
		

		//verify resolution model
		
		//ResolutionModelVerification RMverification = new ResolutionModelVerification(vSpecList, resolutionList);
		//if ok
		
		List<Component> sourceComponentList = new ArrayList(sourceDefinitionFractal.getSubComponents());
		List<RealizationComponent> realizationComponentList = new ArrayList(sourceDefinitionFractal.getRealizationComponents());
		Component destinationCompnent;
		
		//add component to destination model if it has not relation with VP
		/*
		for (int i = 0; i < sourceComponentList.size(); i++) {
			boolean chk = true;
			for (int j = 0; j < vpList.size(); j++) {
				VariationPoint vp = vpList.get(j);
				if (vp instanceof ObjectExistence) {
					ObjectHandle oObject = ((ObjectExistence) vp).getOptionalObject();
					String componentName = oObject.getMOFRef().split("\\.")[1];
					if (componentName.equals(sourceComponentList.get(i).getName())) {
						chk = false;
						break;
					}
				}
			}
			if (chk) destinationDefinition.getSubComponents().add(sourceComponentList.get(i));
		}*/
		
		for (Iterator<VSpecResolution> iter = resolutionList.listIterator(); iter.hasNext();) {
			int cas = 0;	// cas = 0 : ChoiceResolution
							// cas = 1 : VariableValueAssignment
			VSpecResolution vSpecresolution = iter.next();
			if (vSpecresolution instanceof ChoiceResolution) cas = 0;
			else if (vSpecresolution instanceof VariableValueAssignment) cas = 1;
			else if (vSpecresolution instanceof VInstance) cas = 2;
			else cas = 3;

			if (cas == 0) {
				Choice choice = ((ChoiceResolution)vSpecresolution).getResolvedChoice();
				
				if ((!((ChoiceResolution)vSpecresolution).isDecision()) && (! choice.getAvailabilityTime().getName().equals("runtime"))) {
					iter.remove();
					deleteVSpecInVSpecList(choice, vSpecList);
				} else {
					VariationPoint vp = returnVP(choice.getName(), vpList);
					String componentName = "";
					String realizationComponentName = "";
					RealizationComponent realizationComponent = null;
					
					if (vp instanceof ObjectExistence) {
						ObjectHandle oObject = ((ObjectExistence) vp).getOptionalObject();
						componentName = oObject.getMOFRef().split("\\.")[1];
					} 
			
					else if (vp instanceof ObjectSubstitution) { 
						ObjectHandle oPlacementObject = ((ObjectSubstitution) vp).getPlacementObject();
						ObjectHandle oReplacementObject = ((ObjectSubstitution) vp).getReplacementObject();
						
						componentName = oPlacementObject.getMOFRef().split("\\.")[1];
						realizationComponentName = oReplacementObject.getMOFRef().split("\\.")[1];
						
						realizationComponent = returnRealizationComponents(realizationComponentName, realizationComponentList);
						//System.out.println(realizationComponent.getName());
						//add realization component 
						destinationDefinition.getRealizationComponents().add(realizationComponent);
					}
					destinationCompnent = returnComponent(componentName, sourceComponentList);				
					try {
						if ((realizationComponent != null) && (((ChoiceResolution)vSpecresolution).isDecision())) {
							Content content = fractalFractory.createContent();
							content.setClass(realizationComponent.getName());
							destinationCompnent.setContent(content);
						}
					} catch (Exception e) {}
					if (destinationCompnent != null)
						destinationDefinition.getSubComponents().add(destinationCompnent);
				}
			}
			else {
				System.out.println();
			}
		}
		
		/*
		 * set value of attribute in component
		 */
		setAttribute(destinationDefinition, vpList, resolutionList);
		
		/*
		 * TODO: add connection between components - binding
		 * 
		 */
		List<Binding> bindingList = new ArrayList(sourceDefinitionFractal.getBindings());
		addBinding(bindingList, destinationDefinition);
		/*List<Binding> bindingList = new ArrayList(sourceDefinitionFractal.getBindings());
		Binding destinationBinding;
		for (int i = 0; i < bindingList.size(); i++) {
			destinationBinding = bindingList.get(i);
			String client = destinationBinding.getClient();
			String server = destinationBinding.getServer();
			boolean chk1 = false, chk2 = false;
			System.out.println(client+server);
			for (int j = 0; j < destinationDefinition.getSubComponents().size(); j ++) {
				System.out.println(destinationDefinition.getSubComponents().get(j).getName());
				
				if (client.equals(destinationDefinition.getSubComponents().get(j).getName())) chk1 = true;
				if (server.equals(destinationDefinition.getSubComponents().get(j).getName())) chk2 = true;
			}
			if (chk1 && chk2) destinationDefinition.getBindings().add(destinationBinding);
			
		}*/
		
		
		/*
		 * write to file
		 */
		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
		Map<String, Object> m = reg.getExtensionToFactoryMap();
	    m.put("fractal", new XMIResourceFactoryImpl());
	    // Obtain a new resource set
	    ResourceSet resSet = new ResourceSetImpl();
	    // create a resource
	    Resource resource = resSet.createResource(URI.createURI("model//product.fractal"));
	    
	    // Get the first model element and cast it to the right type, in my
	    // example everything is hierarchical included in this first node
	    resource.getContents().add(destinationDefinition);

	    // now save the content.
	    try {
	    	resource.save(Collections.EMPTY_MAP);
	    } catch (IOException e) {
	      // TODO Auto-generated catch block
	    	e.printStackTrace();
	    }
		
	} 
	//for changing implementation of component 
	public boolean addComponents1(Definition def, List<Component> sourceComponentList, 
			List<RealizationComponent> realizationComponentList, ArrayList<VSpec> vSpecList,	
			ArrayList<VariationPoint> vpList, ArrayList<VSpecResolution> resolutionList) {
		return true;
	}
	
	//for composite component
	public void setAttribute(Definition destinationDefinition, ArrayList<VariationPoint> vpList, ArrayList<VSpecResolution> resolutionList ) {
		/*
		 * set value of attribute in component
		 */		
		for (int i = 0; i < vpList.size(); i++) {
			VariationPoint vpVariable = vpList.get(i);
			if (vpVariable instanceof ParametricSlotAssignment) {
				VSpec vspec = vpVariable.getBindingVSpec();
				System.out.println(vspec);		
				VSpecResolution vspecresolution = returnResolutionVSpec(vspec, resolutionList);
				String value = ((VariableValueAssignment)vspecresolution).getValue();
				
				String valueComponentName = ((ParametricSlotAssignment) vpVariable).getSlotOwner().getMOFRef().split("\\.")[1];
				for (int j = 0; j < destinationDefinition.getSubComponents().size(); j++) {
					String str = destinationDefinition.getSubComponents().get(j).getName();
					if (str.equals(valueComponentName))  destinationDefinition.getSubComponents().get(j).getAttribute().get(0).setValue(value);
				}
			}
		}
	}
	
	public boolean addComponents2() {
		return true;
	}
	public boolean addBinding(List<Binding> bindingList, Definition destinationDefinition) {
		//List<Binding> bindingList = new ArrayList(sourceDefinitionFractal.getBindings());
		Binding destinationBinding;// = fractalFractory.createBinding();
		for (int i = 0; i < bindingList.size(); i++) {
			destinationBinding = bindingList.get(i);
			String client = destinationBinding.getClient();
			String server = destinationBinding.getServer();
			boolean chk1 = false, chk2 = false;
			System.out.println(client+server);
			for (int j = 0; j < destinationDefinition.getSubComponents().size(); j ++) {
				System.out.println(destinationDefinition.getSubComponents().get(j).getName());
				
				if (client.equals(destinationDefinition.getSubComponents().get(j).getName())) chk1 = true;
				if (server.equals(destinationDefinition.getSubComponents().get(j).getName())) chk2 = true;
			}
			if (chk1 && chk2) destinationDefinition.getBindings().add(destinationBinding);
			
		}
		return true;
	}
	public void deleteVSpecInVSpecList(VSpec vSpec, ArrayList<VSpec> vSpecList) {
		for (Iterator<VSpec> vs = vSpecList.listIterator(); vs.hasNext(); ) {
		    VSpec vsp = vs.next();
		    if (vsp.getName().equals(vSpec.getName())) {
		       // System.out.println("del:"+vsp.getName());
		    	vs.remove(); 
		        return;
		    }
		}
		    
	}
	public String returnClient(Binding bd, String serverName) {
		String str = "";
		return str; 
	}

	public RealizationComponent returnRealizationComponents(String name, List<RealizationComponent> realizationComponentList) {
		RealizationComponent realizationComponent = null;
		for (int i = 0; i < realizationComponentList.size(); i++) {
			if (name.equals(realizationComponentList.get(i).getName())) return realizationComponentList.get(i);
		}
		return realizationComponent;
	}
	public ArrayList<Binding> returnBinding(List<Binding> bindingList, String serverName) {
		ArrayList<Binding> bd = new ArrayList<Binding>();
		if (bindingList != null)
			for (int i = 0; i < bindingList.size(); i++ ) {
				//System.out.println(bindingList.size()+bindingList.get(i).getServer());
				Binding bind = bindingList.get(i);
				if (bindingList.get(i).getServer().equals(serverName)) {
					bd.add(bind);
				}
			}
		return bd;
	}
	public VSpecResolution returnResolutionVSpec(VSpec vSpec, ArrayList<VSpecResolution> resolutionList) {
		VSpecResolution vSpecresolution = null;
		for (int i = 0; i < resolutionList.size(); i++) {
			if (resolutionList.get(i).getResolvedVSpec().getName().equals(vSpec.getName())) {
				return resolutionList.get(i);
			}
		}
		return vSpecresolution;
	}
	public Component returnComponent(String name, List<Component> sourceComponents) {
		List<Component> listTemp = new ArrayList<Component>(sourceComponents);
		Component component = null;
		for (int i = 0; i < listTemp.size(); i++) {
			if (listTemp.get(i).getName().equals(name)) {
				component = listTemp.get(i);
			}
		} 
		return component;
	}
	
	public VariationPoint returnVP(String vSpec, ArrayList<VariationPoint> vpList) {
		VariationPoint vp = null;
		for (int i = 0; i < vpList.size(); i++) {
			if (vpList.get(i).getBindingVSpec().getName().equals(vSpec)) {
				return vpList.get(i);
			}
		}
		return vp;
	}

	public int returnVSpecOfVP(VariationPoint vp, ArrayList<VSpec> vSpecList) {
		int indexofvSpec = -1;
		
		for (int i = 0; i < vSpecList.size(); i++) {
			if (vSpecList.get(i).getName().equals(vp.getBindingVSpec().getName())) {
				return i;
			}
		}
		
		return indexofvSpec;
	}
	public static void main(String [] args) {
		new GeneratingProductArchitecture().createProductModel(readArchitecture(""));
	}

}


