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
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
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
import cvl.ChoiceResolution;
import cvl.ObjectExistence;
import cvl.ObjectHandle;
import cvl.ObjectSubstitution;
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
		Definition destinationDefinition = fractalFractory.createDefinition();
		destinationDefinition.setName("product"+sourceDefinitionFractal.getName());

		ResolutionTree rs = new ResolutionTree();
		VSpecTree vSpec = new VSpecTree();
		
		//ArrayList<String> decisionList = rs.decisionList;
		ArrayList<VSpec> vSpecList = vSpec.vSpecList;
		ArrayList<VariationPoint> vpList = vSpec.VPList;
		ArrayList<VSpecResolution> resolutionList = rs.resolutionList; 
		
		//verify resolution model
		
		//ResolutionModelVerification RMverification = new ResolutionModelVerification(vSpecList, resolutionList);
		//if ok
		
		List<Component> sourceComponentList = new ArrayList(sourceDefinitionFractal.getSubComponents());
		List<RealizationComponent> realizationComponentList = new ArrayList(sourceDefinitionFractal.getRealizationComponents());
		Component destinationCompnent = fractalFractory.createComponent();
		
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
		
		for (int i = 0; i < resolutionList.size(); i++) {
			int cas = 0;	// cas = 0 : ChoiceResolution
							// cas = 1 : VariableValueAssignment
							
			VSpecResolution vSpecresolution = resolutionList.get(i);
			if (vSpecresolution instanceof ChoiceResolution) cas = 0;
			else if (vSpecresolution instanceof VariableValueAssignment) cas = 1;
			else if (vSpecresolution instanceof VInstance) cas = 2;
		
			if (cas == 0)
				if ((!((ChoiceResolution)vSpecresolution).isDecision()) && (! vSpecList.get(i).getAvailabilityTime().getName().equals("runtime"))) {
					resolutionList.remove(i);
					vSpecList.remove(i);
					//vpList.remove(returnVP(vSpecList.get(i), vpList));
				} else {
					VariationPoint vp = returnVP(vSpecList.get(i).getName(), vpList);
					
					String componentName = "";
					String realizationComponentName = "";
					RealizationComponent realizationComponent = null;
					//if (vp != null) System.out.println(vp.getName());
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
						System.out.println(realizationComponent.getName());
						//add realization component 
						
						destinationDefinition.getRealizationComponents().add(realizationComponent);
					}
					destinationCompnent = returnComponent(componentName, sourceComponentList);
					
					try {
						if ((realizationComponent != null) && (resolutionList.get(i).equals("true"))) {
							Content content = fractalFractory.createContent();
							content.setClass(realizationComponent.getName());
							destinationCompnent.setContent(content);
						
						}
						
					} catch (Exception e) {}
					
					/*
					 * set value of attribute in component
					 */
					try {
						if (!destinationCompnent.getAttribute().isEmpty()) {
							//if it has more one attributes, use a for to get attributes
							for (int j = 0; j < destinationCompnent.getAttribute().size(); j++) {
								VariationPoint vpVariable = returnVP(destinationCompnent.getAttribute().get(j).getName(), vpList);
								int indexOfVSpec = returnIndexofVSpec(vpVariable, vSpecList);
								destinationCompnent.getAttribute().get(j).setValue(((VariableValueAssignment)resolutionList.get(indexOfVSpec)).getValue());
							}
						}
					} catch(Exception e) {
					}
					
					if (destinationCompnent != null)
						destinationDefinition.getSubComponents().add(destinationCompnent);
				}
			}
		/*
		 * TODO: add connection between components - binding
		 * 
		 */
		
		
		List<Binding> bindingList = new ArrayList(sourceDefinitionFractal.getBindings());
		Binding destinationBinding = fractalFractory.createBinding();
		for (int i = 0; i < bindingList.size(); i++) {
			destinationBinding = bindingList.get(i);
			destinationDefinition.getBindings().add(destinationBinding);
		}
		/*
		for (int i = 0; i < vpList.size(); i++) {
			
			VariationPoint vp = vpList.get(i);
			if ((vp instanceof ObjectSubstitution) && 
					(decisionList.get(returnIndexofVSpec(vp, vSpecList)).equals("true"))) {
			
				String oldServer, newServer;
				oldServer = ((ObjectSubstitution) vp).getPlacementObject().getMOFRef().split("\\.")[1];
				newServer = ((ObjectSubstitution) vp).getReplacementObject().getMOFRef().split("\\.")[1];
				
				ArrayList<Binding> bindList = returnBinding(bindingList, oldServer);
				Component newComponentServer = returnComponent(newServer, sourceComponentList);
				//if (newComponentServer  == null) System.out.println("null"+":"+sourceComponentList.size());
				
				for (int j = 0; j < bindList.size(); j++ ) {
					destinationBinding = bindList.get(j);
					destinationBinding.setServer(newServer);
					
					String oldInterface = destinationBinding.getServerInterface().getName(); 
					for (int k = 0; k < newComponentServer.getInterfaces().size(); k++) {
						if (newComponentServer.getInterfaces().get(k).getName().equals(oldInterface)) {
							destinationBinding.setServerInterface(newComponentServer.getInterfaces().get(k));
							break;
						}
					}
					String client = destinationBinding.getClient();
					VariationPoint vpClient = returnVP(client, vpList);
					try {
						if (decisionList.get(returnIndexofVSpec(vpClient, vSpecList)).equals("true")) {
								destinationDefinition.getBindings().add(destinationBinding);
						}
					}catch (Exception e){}
				}
			}
			
			if ((vp instanceof ObjectExistence) && 
					(decisionList.get(returnIndexofVSpec(vp, vSpecList)).equals("true"))) {
				String serverName = ((ObjectExistence) vp).getOptionalObject().getMOFRef().split("\\.")[1];
				ArrayList<Binding> destinationBindingList = returnBinding(bindingList, serverName); 
				//TODO: can be returned a list of binds ????
				for (int j = 0; j < destinationBindingList.size(); j++) {
					destinationBinding = destinationBindingList.get(j);
					
					//System.out.println("ss"+j);
					
					if (destinationBindingList !=null) {
						String client = destinationBinding.getClient();
						//System.out.println(client+"::"+vpList.size());

						VariationPoint vpClient = returnVP(client, vpList);
						//System.out.println(vpClient.getName());

						try{
							if (decisionList.get(returnIndexofVSpec(vpClient, vSpecList)).equals("true")) {
	
								destinationDefinition.getBindings().add(destinationBinding);
							}
						}catch (Exception e1){}
					}
				}
						
				
			}	
		}
		*/
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

	public int returnIndexofVSpec(VariationPoint vp, ArrayList<VSpec> vSpecList) {
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


