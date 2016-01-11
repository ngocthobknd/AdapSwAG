//package test;
//
//import generation.ProductGenerationService;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//
//import org.eclipse.emf.common.util.URI;
//import org.eclipse.emf.ecore.resource.Resource;
//import org.eclipse.emf.ecore.resource.ResourceSet;
//import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
//import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
//import org.ow2.fractal.f4e.fractal.Binding;
//import org.ow2.fractal.f4e.fractal.Component;
//import org.ow2.fractal.f4e.fractal.Content;
//import org.ow2.fractal.f4e.fractal.Definition;
//import org.ow2.fractal.f4e.fractal.FractalFactory;
//import org.ow2.fractal.f4e.fractal.FractalPackage;
//import org.ow2.fractal.f4e.fractal.RealizationComponent;
//
//import test.tree.ResolutionTree;
//import test.tree.VSpecTree;
//import cvl.Choice;
//import cvl.ChoiceResolution;
//import cvl.ObjectExistence;
//import cvl.ObjectHandle;
//import cvl.ObjectSubstitution;
//import cvl.ParametricSlotAssignment;
//import cvl.VSpec;
//import cvl.VSpecResolution;
//import cvl.VariableValueAssignment;
//import cvl.VariationPoint;
//
//public class GeneratingArchitecture {
//	
//	/*
//	 * algorithm for generating product architecture
//	 * INPUT: Variability model, Resolution model, Base model
//	 * OUTPUT : Product model
//	 * Context:
//	 * 1. 	Variability model (VM) -> VSpec list (size = n), VPList (size = m)
//	 *		Resolution model (RM) -> Decision list (size = n)
//	 *  
//	 * 2.  Search in VM and RM : for i = 0 to n 
//	 * 		if (VM(i).availabilityTime != runtime) and (RM(i).decision = false) reducing VSpecList and DecisionList by remove VM(i) and RM(i)
//	 * 		else 
//	 * 			search VP that maps to VM(i) 
//	 * 				if VP is ObjectExistence  ....
//	 * 				if ObjectSubstitution 
//	 * 					VP -> MOFRef (component name)
//	 * 					Search MOFRef in BM -> component C
//	 * 					if C contains attributes
//	 * 						search VPs in VPList contain these attributes -> VSpec(k) -> RM(k)
//	 * 						v = getValue of RM(k)
//	 * 						setAttribute in Component = v
//	 * 			add component C to product architecture	 
//	 * 						   
//	 *			 
//	 */
//	
//	Definition destinationDefinition;
//	FractalFactory fractalFractory;
//	ResolutionTree rs;
//	VSpecTree vSpec;
//	
//	
//	public GeneratingArchitecture() {
//		FractalPackage.eINSTANCE.eClass();
//		fractalFractory = FractalFactory.eINSTANCE;
//		destinationDefinition = fractalFractory.createDefinition();
//		rs = new ResolutionTree("model//primitive//resolution.cvl");
//		vSpec = new VSpecTree("model//primitive//model.cvl");
//		
//	}
//		
//	public static Definition readArchitecture(String file) {
//		FractalPackage.eINSTANCE.eClass();
//		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
//		try {
//			//registry extent part of model file ex: *.variability
//			reg.getExtensionToFactoryMap().put("fractal", new XMIResourceFactoryImpl());
//		} catch (Exception e){
//		}
//		ResourceSet resourceSet = new ResourceSetImpl();
//		URI uri = URI.createFileURI("model//primitive//architecture.fractal");
//		Resource resource = resourceSet.getResource(uri, true);
//		//get root of variability model 
//		Definition definition = (Definition) resource.getContents().get(0);
//		return definition;
//	}
//	
//	@SuppressWarnings({ "unchecked", "rawtypes" })
//	public void createProductModel(Definition sourceDefinitionFractal) {
//		
//		destinationDefinition.setName("product"+sourceDefinitionFractal.getName());
//		ArrayList<VSpec> vSpecList = vSpec.vSpecList;
//		ArrayList<VariationPoint> vpList = vSpec.VPList;
//		ArrayList<VSpecResolution> resolutionList = rs.resolutionList; 
//		
//		List<Component> sourceComponentList = new ArrayList(sourceDefinitionFractal.getSubComponents());
//		//List<RealizationComponent> realizationComponentList = new ArrayList(sourceDefinitionFractal.getRealizationComponents());
//		List<Binding> bindingList = new ArrayList(sourceDefinitionFractal.getBindings());
//		//Component destinationCompnent;
//		
//		
//		
//		//addComponents1(destinationDefinition, sourceComponentList, realizationComponentList, vSpecList, vpList, resolutionList);
//		/*
//		 * set value of attribute in component
//		 */
//		setAttribute(destinationDefinition, vpList, resolutionList);
//
//		
//		//addBinding(destinationDefinition, bindingList,);
//		addBinding(destinationDefinition, bindingList, vpList, resolutionList);
//		/*
//		 * write to file
//		 */
//		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
//		Map<String, Object> m = reg.getExtensionToFactoryMap();
//	    m.put("fractal", new XMIResourceFactoryImpl());
//	    // Obtain a new resource set
//	    ResourceSet resSet = new ResourceSetImpl();
//	    // create a resource
//	    Resource resource = resSet.createResource(URI.createURI("model//product.fractal"));
//	    
//	    // Get the first model element and cast it to the right type, in my
//	    // example everything is hierarchical included in this first node
//	    resource.getContents().add(destinationDefinition);
//
//	    // now save the content.
//	    try {
//	    	resource.save(Collections.EMPTY_MAP);
//	    } catch (IOException e) {
//	      // TODO Auto-generated catch block
//	    	e.printStackTrace();
//	    }
//		
//	} 
//	//add components to product for the first case
////	public boolean addComponents1(Definition destinationDefinition, List<Component> sourceComponentList, 
////			List<RealizationComponent> realizationComponentList, ArrayList<VSpec> vSpecList,	
////			ArrayList<VariationPoint> vpList, ArrayList<VSpecResolution> resolutionList) {
////		
////		Component destinationCompnent;
////		for (Iterator<VSpecResolution> iter = resolutionList.listIterator(); iter.hasNext();) {
////			int cas = 0;	// cas = 0 : ChoiceResolution
////							// cas = 1 : VariableValueAssignment
////			VSpecResolution vSpecresolution = iter.next();
////			if (vSpecresolution instanceof ChoiceResolution) cas = 0;
////			else if (vSpecresolution instanceof VariableValueAssignment) cas = 1;
////			else cas = 2;
////
////			if (cas == 0) {
////				Choice choice = ((ChoiceResolution)vSpecresolution).getResolvedChoice();
////				
////				if ((!((ChoiceResolution)vSpecresolution).isDecision()) && (! choice.getAvailabilityTime().getName().equals("runtime"))) {
////					iter.remove();
////					deleteVSpecInVSpecList(choice, vSpecList);
////				} else {
////					VariationPoint vp = returnVPByVSpecName(choice.getName(), vpList);
////					String componentName = "";
////					String realizationComponentName = "";
////			//		RealizationComponent realizationComponent = null;
////					
////					if (vp instanceof ObjectExistence) {
////						ObjectHandle oObject = ((ObjectExistence) vp).getOptionalObject();
////						componentName = oObject.getMOFRef().split("\\.")[1];
////					} 
////			
////					else if (vp instanceof ObjectSubstitution) { 
////						ObjectHandle oPlacementObject = ((ObjectSubstitution) vp).getPlacementObject();
////						ObjectHandle oReplacementObject = ((ObjectSubstitution) vp).getReplacementObject();
////						
////						componentName = oPlacementObject.getMOFRef().split("\\.")[1];
////						realizationComponentName = oReplacementObject.getMOFRef().split("\\.")[1];
//////						
//////						realizationComponent = returnRealizationComponents(realizationComponentName, realizationComponentList);
//////						//System.out.println(realizationComponent.getName());
//////						//add realization component 
//////						destinationDefinition.getRealizationComponents().add(realizationComponent);
////					}
////					destinationCompnent = returnComponentByName(componentName, sourceComponentList);				
//////					try {
//////						if ((realizationComponent != null) && (((ChoiceResolution)vSpecresolution).isDecision())) {
//////							Content content = fractalFractory.createContent();
//////							content.setClass(realizationComponent.getName());
//////							destinationCompnent.setContent(content);
//////						}
//////					} catch (Exception e) {}
//////					if (destinationCompnent != null)
//////						destinationDefinition.getSubComponents().add(destinationCompnent);
//////				}
////			}
//////			else {
//////				System.out.println();
//////			}
////		}
////		return true;
////	}
//	
//	//for the first case
//	public void setAttribute(Definition destinationDefinition, ArrayList<VariationPoint> vpList, ArrayList<VSpecResolution> resolutionList ) {
//		/*
//		 * set value of attribute in component
//		 */		
//		for (int i = 0; i < vpList.size(); i++) {
//			VariationPoint vpVariable = vpList.get(i);
//			if (vpVariable instanceof ParametricSlotAssignment) {
//				VSpec vspec = vpVariable.getBindingVSpec();
//				//System.out.println(vspec);		
//				VSpecResolution vspecresolution = returnVSpecResolutionByVSpecName(vspec, resolutionList);
//				String value = ((VariableValueAssignment)vspecresolution).getValue();
//				
//				String valueComponentName = ((ParametricSlotAssignment) vpVariable).getSlotOwner().getMOFRef().split("\\.")[1];
//				for (int j = 0; j < destinationDefinition.getSubComponents().size(); j++) {
//					String str = destinationDefinition.getSubComponents().get(j).getName();
//					if (str.equals(valueComponentName))  destinationDefinition.getSubComponents().get(j).getAttribute().get(0).setValue(value);
//				}
//			}
//		}
//	}
//	
//	//for second case
//	public boolean addComponents2() {
//		return true;
//	}
//	
//	
//	public void addDisconnectedComponent(Definition destinationDefinition, List<Component> sourceComponentList, 
//			List<RealizationComponent> realizationComponentList, ArrayList<VSpec> vSpecList,	
//			ArrayList<VariationPoint> vpList, ArrayList<VSpecResolution> resolutionList) {
//		//add component to destination model if it has not relation with VP
//		// if search component MOF is not in VPs, add this component to product	
//		for (int i = 0; i < sourceComponentList.size(); i++) {
//			boolean chk = true;
//			for (int j = 0; j < vpList.size(); j++) {
//				VariationPoint vp = vpList.get(j);
//				if (vp instanceof ObjectExistence) {
//					ObjectHandle oObject = ((ObjectExistence) vp).getOptionalObject();
//					String componentName = oObject.getMOFRef().split("\\.")[1];
//					if (componentName.equals(sourceComponentList.get(i).getName())) {
//						chk = false;
//						break;
//					}
//				}
//			}
//			if (chk) destinationDefinition.getSubComponents().add(sourceComponentList.get(i));
//		}
//	}
//	
//	public boolean addBinding(Definition destinationDefinition, List<Binding> bindingList,
//			ArrayList<VariationPoint> vpList, ArrayList<VSpecResolution> resolutionList) {
//		//List<Binding> bindingList = new ArrayList(sourceDefinitionFractal.getBindings());
//		Binding destinationBinding;// = fractalFractory.createBinding();
//		for (int i = 0; i < bindingList.size(); i++) {
//			destinationBinding = bindingList.get(i);
//			String client = destinationBinding.getClient();
//			String server = destinationBinding.getServer();
//			boolean chk1 = false, chk2 = false;
//			//System.out.println(client+server);
//			for (int j = 0; j < destinationDefinition.getSubComponents().size(); j ++) {
//				//System.out.println(destinationDefinition.getSubComponents().get(j).getName());
//				
//				if (client.equals(destinationDefinition.getSubComponents().get(j).getName())) chk1 = true;
//				if (server.equals(destinationDefinition.getSubComponents().get(j).getName())) chk2 = true;
//			}
//			
//			ChoiceResolution vspr1 = (ChoiceResolution)returnVSpecResolutionByComponentName(client, vpList, resolutionList);
//			ChoiceResolution vspr2 = (ChoiceResolution)returnVSpecResolutionByComponentName(server, vpList, resolutionList);
//			//if ((vspr1 != null) && (vspr2 != null)) System.out.println(chk1 +""+ chk2 +""+ vspr1.isDecision() +""+ vspr2.isDecision());
//			if (chk1 && chk2 && vspr1.isDecision() && vspr2.isDecision()) destinationDefinition.getBindings().add(destinationBinding);
//			
//		}
//		return true;
//	}
//	public VSpecResolution returnVSpecResolutionByComponentName(String componentName,	
//			ArrayList<VariationPoint> vpList, ArrayList<VSpecResolution> resolutionList) {
//		VSpecResolution vspr = null;
//		
//		VariationPoint vp = returnVPByComponent(componentName, vpList);
//		if (vp != null) 
//		if (vp instanceof ObjectExistence) {
//			VSpec vsp = vp.getBindingVSpec();
//			vspr = returnVSpecResolutionByVSpecName(vsp, resolutionList);
//			
//		} else if (vp instanceof ObjectSubstitution) {
//			VSpec vsp = vp.getBindingVSpec();
//			//if vp is objectsubtitution, we need find the parent of a Vspec in Vspec tree  
//			VSpec parent = (VSpec)vsp.eContainer();
//			vspr = returnVSpecResolutionByVSpecName(parent, resolutionList);
//		}
//		
//		return vspr;
//	}
//	public VariationPoint returnVPByComponent (String componentName, ArrayList<VariationPoint> vpList) {
//		VariationPoint vp = null;
//		for (int i = 0; i < vpList.size(); i++) {
//			if (vpList.get(i) instanceof ObjectExistence) {
//				String strcomponentName = ((ObjectExistence)vpList.get(i)).getOptionalObject().getMOFRef().split("\\.")[1];
//				if (strcomponentName.equals(componentName)) {
//					return vpList.get(i); 
//				}
//			} else if (vpList.get(i) instanceof ObjectSubstitution) {
//				String strPlacementName = ((ObjectSubstitution)vpList.get(i)).getPlacementObject().getMOFRef().split("\\.")[1];
//				String strReplacementName = ((ObjectSubstitution)vpList.get(i)).getReplacementObject().getMOFRef().split("\\.")[1];
//				
//				if (strPlacementName.equals(componentName) || strReplacementName.equals(componentName) ) {
//					//System.out.println(vpList.get(i));
//					return vpList.get(i); 
//				}
//			}
//		}
//		return vp;
//	}
//	public void deleteVSpecInVSpecList(VSpec vSpec, ArrayList<VSpec> vSpecList) {
//		for (Iterator<VSpec> vs = vSpecList.listIterator(); vs.hasNext(); ) {
//		    VSpec vsp = vs.next();
//		    if (vsp.getName().equals(vSpec.getName())) {
//		       // System.out.println("del:"+vsp.getName());
//		    	vs.remove(); 
//		        return;
//		    }
//		}
//		    
//	}
//	public RealizationComponent returnRealizationComponents(String name, List<RealizationComponent> realizationComponentList) {
//		RealizationComponent realizationComponent = null;
//		for (int i = 0; i < realizationComponentList.size(); i++) {
//			if (name.equals(realizationComponentList.get(i).getName())) return realizationComponentList.get(i);
//		}
//		return realizationComponent;
//	}
//	public ArrayList<Binding> returnBinding(List<Binding> bindingList, String serverName) {
//		ArrayList<Binding> bd = new ArrayList<Binding>();
//		if (bindingList != null)
//			for (int i = 0; i < bindingList.size(); i++ ) {
//				//System.out.println(bindingList.size()+bindingList.get(i).getServer());
//				Binding bind = bindingList.get(i);
//				if (bindingList.get(i).getServer().equals(serverName)) {
//					bd.add(bind);
//				}
//			}
//		return bd;
//	}
//	public VSpecResolution returnVSpecResolutionByVSpecName(VSpec vSpec, ArrayList<VSpecResolution> resolutionList) {
//		VSpecResolution vSpecresolution = null;
//		for (int i = 0; i < resolutionList.size(); i++) {
//			if (resolutionList.get(i).getResolvedVSpec().getName().equals(vSpec.getName())) {
//				return resolutionList.get(i);
//			}
//		}
//		return vSpecresolution;
//	}
//	public Component returnComponentByName(String name, List<Component> sourceComponents) {
//		List<Component> listTemp = new ArrayList<Component>(sourceComponents);
//		Component component = null;
//		for (int i = 0; i < listTemp.size(); i++) {
//			if (listTemp.get(i).getName().equals(name)) {
//				component = listTemp.get(i);
//			}
//		} 
//		return component;
//	}
//	
//	public VariationPoint returnVPByVSpecName(String vSpec, ArrayList<VariationPoint> vpList) {
//		VariationPoint vp = null;
//		for (int i = 0; i < vpList.size(); i++) {
//			if (vpList.get(i).getBindingVSpec().getName().equals(vSpec)) {
//				return vpList.get(i);
//			}
//		}
//		return vp;
//	}
//
//	public int returnVSpecByVP(VariationPoint vp, ArrayList<VSpec> vSpecList) {
//		int indexofvSpec = -1;
//		
//		for (int i = 0; i < vSpecList.size(); i++) {
//			if (vSpecList.get(i).getName().equals(vp.getBindingVSpec().getName())) {
//				return i;
//			}
//		}
//		
//		return indexofvSpec;
//	}
//	public static void main(String [] args) {
//		new GeneratingArchitecture().createProductModel(readArchitecture(""));
//	}
//
//}
//
//
