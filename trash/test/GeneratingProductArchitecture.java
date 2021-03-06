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
//import org.eclipse.emf.common.util.EList;
//import org.eclipse.emf.common.util.URI;
//import org.eclipse.emf.ecore.resource.Resource;
//import org.eclipse.emf.ecore.resource.ResourceSet;
//import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
//import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
//import org.ow2.fractal.f4e.fractal.Attribute;
//import org.ow2.fractal.f4e.fractal.Binding;
//import org.ow2.fractal.f4e.fractal.Component;
//import org.ow2.fractal.f4e.fractal.Content;
//import org.ow2.fractal.f4e.fractal.Definition;
//import org.ow2.fractal.f4e.fractal.FractalFactory;
//import org.ow2.fractal.f4e.fractal.FractalPackage;
//import org.ow2.fractal.f4e.fractal.Interface;
//import org.ow2.fractal.f4e.fractal.RealizationComponent;
//
//import test.tree.ResolutionTree;
//import test.tree.VSpecTree;
//import cvl.Choice;
//import cvl.ChoiceResolution;
//import cvl.FragmentSubstitution;
//import cvl.ObjectExistence;
//import cvl.ObjectHandle;
//import cvl.ObjectSubstitution;
//import cvl.ParametricSlotAssignment;
//import cvl.VClassifier;
//import cvl.VSpec;
//import cvl.VSpecResolution;
//import cvl.Variable;
//import cvl.VariableValueAssignment;
//import cvl.VariationPoint;
//
//public class GeneratingProductArchitecture implements ProductGenerationService{
//	
//	/*
//	 * algorithm for generating composite architecture
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
//	ArrayList<Component> destinationComponentList = new ArrayList<Component>();
//	
//	ArrayList<Component> sourceComponentList = new ArrayList<Component>();
//	ArrayList<Binding> sourceBindingList = new ArrayList<Binding>();
//	List<Binding> bindingList = new ArrayList<Binding>();
//	
//	ArrayList<VSpec> vSpeclist = new ArrayList<VSpec>();
//	ArrayList<VariationPoint> vpList = new ArrayList<VariationPoint>();
//	ArrayList<VSpecResolution> vSpecResolutionList = new ArrayList<VSpecResolution>();
//	
//	
//	
//	String variabilityModelFileName;// = "model//composite2//model.cvl";
//	String resolutionModelFileName;// = "model//composite2//resolution.cvl";
//	String baseModelFileName;// = "model//composite2//architecture.fractal";
//	String productModelFileName;// = "model//composite2//product.fractal";
//	
//	
//	
//	
//	public GeneratingProductArchitecture(String variabilityModel, String resolutionModel, 
//			String baseModel, String productModel) {
//		this.variabilityModelFileName = variabilityModel;
//		this.resolutionModelFileName = resolutionModel;
//		this.baseModelFileName = baseModel;
//		this.productModelFileName = productModel;
//		FractalPackage.eINSTANCE.eClass();
//		fractalFractory = FractalFactory.eINSTANCE;
//		destinationDefinition = fractalFractory.createDefinition();
//		rs = new ResolutionTree(resolutionModelFileName);
//		vSpec = new VSpecTree(variabilityModelFileName);
//		
//	}
//	public GeneratingProductArchitecture(ArrayList<Component> sourceComponentList,
//			ArrayList<Binding> sourceBindingList,
//			ArrayList<VSpec> vSpeclist,
//			ArrayList<VariationPoint> vpList,
//			ArrayList<VSpecResolution> vSpecResolutionList ) {
//		this.sourceComponentList = sourceComponentList;
//		this.sourceBindingList = sourceBindingList;
//		this.vSpeclist = vSpeclist;
//		this.vpList = vpList;
//		this.vSpecResolutionList = vSpecResolutionList;
//		
//		FractalPackage.eINSTANCE.eClass();
//		fractalFractory = FractalFactory.eINSTANCE;
//		destinationDefinition = fractalFractory.createDefinition();
//		
//	}
//	public Definition readArchitecture(String file) {
//		FractalPackage.eINSTANCE.eClass();
//		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
//		try {
//			//registry extent part of model file ex: *.variability
//			reg.getExtensionToFactoryMap().put("fractal", new XMIResourceFactoryImpl());
//		} catch (Exception e){
//		}
//		ResourceSet resourceSet = new ResourceSetImpl();
//		URI uri = URI.createFileURI(baseModelFileName);
//		Resource resource = resourceSet.getResource(uri, true);
//		//get root of variability model 
//		Definition definition = (Definition) resource.getContents().get(0);
//		return definition;
//	}
//	public void readComponent(Component component) {
//		
//		sourceComponentList.add(component);
//		for (int i = 0; i < component.getSubComponents().size(); i++) {
//			readComponent(component.getSubComponents().get(i));
//		}
//	}
//	@SuppressWarnings({ "unchecked", "rawtypes" })
//	public void createProductModel(Definition sourceDefinitionFractal) {
//		
//		destinationDefinition.setName("product"+sourceDefinitionFractal.getName());
//		ArrayList<VSpec> vSpecList = vSpec.vSpecList;
//		ArrayList<VariationPoint> vpList = vSpec.VPList;
//		ArrayList<VSpecResolution> resolutionList = rs.resolutionList; 
//		
//		for (int i = 0; i < sourceDefinitionFractal.getSubComponents().size(); i ++) {
//			readComponent(sourceDefinitionFractal.getSubComponents().get(i));
//		}
//		//List<RealizationComponent> realizationComponentList = new ArrayList(sourceDefinitionFractal.getRealizationComponents());
//		sourceBindingList = new ArrayList(sourceDefinitionFractal.getBindings());
//		bindingList = new ArrayList(sourceDefinitionFractal.getBindings());
//	
//		addComponents1(destinationDefinition, sourceComponentList, vSpecList, vpList, resolutionList);
//
//		addBinding(destinationDefinition, destinationComponentList, bindingList, vpList, resolutionList);
//		/*
//		 * write to file
//		 */
//		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
//		Map<String, Object> m = reg.getExtensionToFactoryMap();
//	    m.put("fractal", new XMIResourceFactoryImpl());
//	    // Obtain a new resource set
//	    ResourceSet resSet = new ResourceSetImpl();
//	    // create a resource
//	    Resource resource = resSet.createResource(URI.createURI(productModelFileName));
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
//	public boolean addComponents1(Definition destinationDefinition, 
//			List<Component> sourceComponentList, 
//			ArrayList<VSpec> vSpecList,	
//			ArrayList<VariationPoint> vpList, ArrayList<VSpecResolution> resolutionList) {
//		//begin procedure
//		ArrayList<VariationPoint> vpList_temp = new ArrayList<>(vpList);
//		for (Iterator<VSpec> iter = vSpecList.listIterator(); iter.hasNext();) {
//
//			Component destinationCompnent = null;
//			VSpec vSpec = iter.next();
//			if (vSpec instanceof Choice) {
//				Choice choice = (Choice) vSpec;
//				VSpecResolution vSpecResolution = returnVSpecResolutionByVSpec(vSpec, resolutionList);
//				boolean decision = false;
//				if (vSpecResolution != null) {
//					decision = ((ChoiceResolution)vSpecResolution).isDecision();
//				} else if (choice.isDefaultResolution()) {
//					decision = choice.isDefaultResolution();
//				}
//				if (decision || vSpec.getAvailabilityTime().toString().equals("runtime")) {
//					VariationPoint vp = returnVPByVSpecName(vSpec.getName(), vpList_temp);
//					String componentName;
//					if (vp instanceof ObjectExistence) {
//						ObjectExistence vpObjE = (ObjectExistence) vp;
//						ObjectHandle objHandle = vpObjE.getOptionalObject();
//						componentName = objHandle.getMOFRef().substring(objHandle.getMOFRef().lastIndexOf(".") + 1);
//						
//						Component tempComponent = returnComponentByName(componentName, sourceComponentList);	
//						if (tempComponent.getSubComponents().isEmpty()) {
//							Component componentA = setAttributeInComponent(tempComponent, vpList_temp, resolutionList);
//							destinationCompnent = componentA;
//						} else {
//							Component component = fractalFractory.createComponent();
//							component.setName(tempComponent.getName());
//							component.getInterfaces().addAll(tempComponent.getInterfaces());
//							
//							ArrayList<Component> compList = new ArrayList<Component>();
//							EList<Component> eComponentList = tempComponent.getSubComponents();
//							for (int i = 0; i < eComponentList.size(); i++) {
//								compList.add(eComponentList.get(i));
//							}
//							
//							for (int i = 0; i < compList.size(); i++) {
//								Component subComponentTemp = compList.get(i);
//								//System.out.println(i+":"+subComponentTemp.getName());	
//								VariationPoint vpTemp = returnVPByComponent(subComponentTemp.getName(), vpList_temp);
//								
//								deleteVPInVPList(vpTemp, vpList_temp);
//								try {
//									
//									VSpec vSpecTemp = returnVSpecByVP(vpTemp, vSpecList);
//									VSpecResolution vSpecResolutionTemp = returnVSpecResolutionByVSpec(vSpecTemp, resolutionList);
//									if (vSpecTemp.getAvailabilityTime().toString().equals("runtime") || ((ChoiceResolution)vSpecResolutionTemp).isDecision()) {
//										if (!compList.get(i).getAttribute().isEmpty()) {
//											Component componentA = setAttributeInComponent(compList.get(i), vpList_temp, resolutionList);
//											component.getSubComponents().add(componentA);
//											destinationComponentList.add(componentA);
//										} else {
//											component.getSubComponents().add(compList.get(i));
//											//System.out.println("add:"+compList.get(i).getName());
//											destinationComponentList.add(compList.get(i));
//										}
//								}
//								}catch (Exception e) {System.out.println(e);}
//								
//							}
//							destinationCompnent = component;
//						}
//					}
//					else if (vp instanceof ObjectSubstitution) { 
//						ObjectSubstitution vpSubT = (ObjectSubstitution) vp;
//						String mofRefPlacement = vpSubT.getPlacementObject().getMOFRef();
//						String mofRefReplacement = vpSubT.getReplacementObject().getMOFRef();
//						String srcComponentName = mofRefPlacement.substring(mofRefPlacement.lastIndexOf(".") + 1);
//						//Component temp_source = returnComponentByName(srcComponentName, sourceComponentList);
//						
//						componentName = mofRefReplacement.substring(mofRefReplacement.lastIndexOf(".") + 1);
//						destinationCompnent = returnComponentByName(componentName, sourceComponentList);
//					
//						if (decision) {
//							for (int i = 0; i < bindingList.size(); i++) {
//								String client = bindingList.get(i).getClient();
//								String server = bindingList.get(i).getServer();
//								Interface interfaceClient = bindingList.get(i).getClientInterface();
//								Interface interfaceServer = bindingList.get(i).getServerInterface();
//								if (client.equals(srcComponentName)) {
//									bindingList.get(i).setClient(componentName);
//									for (int j = 0; j < destinationCompnent.getInterfaces().size(); j++ ) {
//										if (destinationCompnent.getInterfaces().get(j).getRole().getName().equals("client") && 
//												interfaceClient.getName().equals(destinationCompnent.getInterfaces().get(j).getName())) {
//											bindingList.get(i).setClientInterface(destinationCompnent.getInterfaces().get(j));
//										}
//									}
//								}
//								if (server.equals(srcComponentName)) {
//									bindingList.get(i).setServer(componentName);
//									for (int j = 0; j < destinationCompnent.getInterfaces().size(); j++ ) {
//										if (destinationCompnent.getInterfaces().get(j).getRole().getName().equals("server") && 
//												interfaceServer.getName().equals(destinationCompnent.getInterfaces().get(j).getName())) {
//											bindingList.get(i).setServerInterface(destinationCompnent.getInterfaces().get(j));
//										}
//									}
//								}
//							}
//						}
//					} else if (vp instanceof FragmentSubstitution) { //VClassifier
//						//clone component
//					}
//					
//				} else {
//					System.out.println(vSpec.getName() + " is not configured for product");
//				}
//			}
//			if (destinationCompnent != null) {
//				destinationDefinition.getSubComponents().add(destinationCompnent);
//				//System.out.println("add:"+destinationCompnent.getName());
//				destinationComponentList.add(destinationCompnent);
//			}
//		}
//		return true;
//	}
//	
//	
////	public boolean addComponents1(Definition destinationDefinition, 
////			List<Component> sourceComponentList, 
////			ArrayList<VSpec> vSpecList,	
////			ArrayList<VariationPoint> vpList, ArrayList<VSpecResolution> resolutionList) {
////		
////		ArrayList<VariationPoint> vpList_temp = new ArrayList<>(vpList);
////		for (Iterator<VSpecResolution> iter = resolutionList.listIterator(); iter.hasNext();) {
////
////			Component destinationCompnent = null;
////			VSpecResolution vSpecresolution = iter.next();
////			int cas = 2;	// cas = 0 : ChoiceResolution
////			// cas = 1 : VariableValueAssignment
////			if ((vSpecresolution instanceof ChoiceResolution) && ((ChoiceResolution)vSpecresolution).isDecision()) cas = 0;
////			else if (vSpecresolution instanceof VariableValueAssignment) cas = 1;
////			else cas = 2;
////
////			if (cas == 0) {
////				Choice choice = ((ChoiceResolution)vSpecresolution).getResolvedChoice();
////				if ((!((ChoiceResolution)vSpecresolution).isDecision()) && (! choice.getAvailabilityTime().getName().equals("runtime"))) {
////					iter.remove();
////					deleteVSpecInVSpecList(choice, vSpecList);
////					 //System.out.println(choice.getName());
////				} else {
////					VariationPoint vp = returnVPByVSpecName(choice.getName(), vpList_temp);
////					String componentName = "";
////			
////					if (vp instanceof ObjectExistence) {
////						ObjectHandle oObject = ((ObjectExistence) vp).getOptionalObject();
////						componentName = oObject.getMOFRef().substring(oObject.getMOFRef().lastIndexOf(".") + 1);;
////						
////						Component tempComponent = returnComponentByName(componentName, sourceComponentList);				
////						if (tempComponent.getSubComponents().isEmpty()) {
////							//System.out.println(tempComponent.getName());
////							Component componentA = setAttributeInComponent(tempComponent, vpList_temp, resolutionList);
////							destinationCompnent = componentA;
////						} else {
////							//System.out.println(tempComponent.getName());
////								
////							Component component = fractalFractory.createComponent();
////							component.setName(tempComponent.getName());
////							component.getInterfaces().addAll(tempComponent.getInterfaces());
////							
////							ArrayList<Component> compList = new ArrayList<Component>();
////							EList<Component> eComponentList = tempComponent.getSubComponents();
////							for (int i = 0; i < eComponentList.size(); i++) {
////								compList.add(eComponentList.get(i));
////							}
////							
////							for (int i = 0; i < compList.size(); i++) {
////								Component subComponentTemp = compList.get(i);
////								//System.out.println(i+":"+subComponentTemp.getName());	
////								VariationPoint vpTemp = returnVPByComponent(subComponentTemp.getName(), vpList_temp);
////								
////								deleteVPInVPList(vpTemp, vpList_temp);
////								try {
////									
////									VSpec vSpecTemp = returnVSpecByVP(vpTemp, vSpecList);
////									VSpecResolution vSpecResolutionTemp = returnVSpecResolutionByVSpec(vSpecTemp, resolutionList);
////									if (vSpecTemp.getAvailabilityTime().toString().equals("runtime") || ((ChoiceResolution)vSpecResolutionTemp).isDecision()) {
////										if (!compList.get(i).getAttribute().isEmpty()) {
////											Component componentA = setAttributeInComponent(compList.get(i), vpList_temp, resolutionList);
////											component.getSubComponents().add(componentA);
////											System.out.println("add:"+componentA.getName());
////											destinationComponentList.add(componentA);
////										} else {
////											component.getSubComponents().add(compList.get(i));
////											System.out.println("add:"+compList.get(i).getName());
////											destinationComponentList.add(compList.get(i));
////										}
////								}
////								}catch (Exception e) {System.out.println(e);}
////								
////							}
////							destinationCompnent = component;
////						}
////					} 
////			
////					else if (vp instanceof ObjectSubstitution) { 
////						String mofRefPlacement = ((ObjectSubstitution) vp).getPlacementObject().getMOFRef();
////						String mofRefReplacement = ((ObjectSubstitution) vp).getReplacementObject().getMOFRef();
////					
////						
////						String srcComponentName = mofRefPlacement.substring(mofRefPlacement.lastIndexOf(".") + 1);
////						//Component temp_source = returnComponentByName(srcComponentName, sourceComponentList);
////						
////						componentName = mofRefReplacement.substring(mofRefReplacement.lastIndexOf(".") + 1);
////						destinationCompnent = returnComponentByName(componentName, sourceComponentList);
////					
////						
////						for (int i = 0; i < bindingList.size(); i++) {
////							String client = bindingList.get(i).getClient();
////							String server = bindingList.get(i).getServer();
////							Interface interfaceClient = bindingList.get(i).getClientInterface();
////							Interface interfaceServer = bindingList.get(i).getServerInterface();
////							if (client.equals(srcComponentName)) {
////								bindingList.get(i).setClient(componentName);
////								for (int j = 0; j < destinationCompnent.getInterfaces().size(); j++ ) {
////									if (destinationCompnent.getInterfaces().get(j).getRole().getName().equals("client") && 
////											interfaceClient.getName().equals(destinationCompnent.getInterfaces().get(j).getName())) {
////										bindingList.get(i).setClientInterface(destinationCompnent.getInterfaces().get(j));
////									}
////								}
////							}
////							if (server.equals(srcComponentName)) {
////								bindingList.get(i).setServer(componentName);
////								for (int j = 0; j < destinationCompnent.getInterfaces().size(); j++ ) {
////									if (destinationCompnent.getInterfaces().get(j).getRole().getName().equals("server") && 
////											interfaceServer.getName().equals(destinationCompnent.getInterfaces().get(j).getName())) {
////										bindingList.get(i).setServerInterface(destinationCompnent.getInterfaces().get(j));
////									}
////								}
////							}
////						}
////					}
////					
////					if (destinationCompnent != null) {
////						destinationDefinition.getSubComponents().add(destinationCompnent);
////						System.out.println("add:"+destinationCompnent.getName());
////						destinationComponentList.add(destinationCompnent);
////					}
////				}
////			}
////			else {
////				System.out.println("");
////			}
////		}
////		
////		for (int i = 0; i < bindingList.size(); i++) {
////			System.out.println("bind: "+bindingList.get(i).getServer());
////		}
////		
////		return true;
////	}
//	
//	public Component setAttributeInComponent(Component component, ArrayList<VariationPoint> vpList, ArrayList<VSpecResolution> resolutionList ) {
//		/*
//		 * set value of attribute in component
//		 */		
//		for (int i = 0; i < component.getAttribute().size(); i++) {
//			VariationPoint vp = returnVPByAttribute(component.getAttribute().get(i).getName(), vpList);
//			if (vp != null) {
//				VSpec vspec = (vp).getBindingVSpec();
//				VSpecResolution vspecresolution = returnVSpecResolutionByVSpec(vspec, resolutionList);
//				String value = ((VariableValueAssignment)vspecresolution).getValue();
//				component.getAttribute().get(i).setValue(value);
//				
//			}
//		}
//		return component;
//	}
//	//for the first case
//	public void setAttribute(Definition destinationDefinition, ArrayList<VariationPoint> vpList, ArrayList<VSpecResolution> resolutionList ) {
//		/*
//		 * set value of attribute in component
//		 */		
//		for (int i = 0; i < vpList.size(); i++) {
//			VariationPoint vpVariable = vpList.get(i);
//			if (vpVariable instanceof ParametricSlotAssignment) {
//				VSpec vspec = vpVariable.getBindingVSpec();
//				
//				VSpecResolution vspecresolution = returnVSpecResolutionByVSpec(vspec, resolutionList);
//				String value = ((VariableValueAssignment)vspecresolution).getValue();
//				
//				String mofRef =  ((ParametricSlotAssignment) vpVariable).getSlotOwner().getMOFRef();
//				
//				String valueComponentName = mofRef.substring(mofRef.lastIndexOf(".") + 1);
//				for (int j = 0; j < destinationDefinition.getSubComponents().size(); j++) {
//					//System.out.println(vspecresolution);		
//					String str = destinationDefinition.getSubComponents().get(j).getName();
//					if (str.equals(valueComponentName))  destinationDefinition.getSubComponents().get(j).getAttribute().get(0).setValue(value);
//				}
//			}
//		}
//	}
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
//					String oObject = ((ObjectExistence) vp).getOptionalObject().getMOFRef();
//					String componentName = oObject.substring(oObject.lastIndexOf(".") + 1);
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
//	public boolean addBinding(Definition destinationDefinition, ArrayList<Component> destinationComponentList, List<Binding> bindingList,
//			ArrayList<VariationPoint> vpList, ArrayList<VSpecResolution> resolutionList) {
//		Binding destinationBinding;// = fractalFractory.createBinding();
//		for (int i = 0; i < bindingList.size(); i++) {
//			destinationBinding = bindingList.get(i);
//			String client = destinationBinding.getClient();
//			String server = destinationBinding.getServer();
//			boolean chk1 = false, chk2 = false;
//			
//			for (int j = 0; j < destinationComponentList.size(); j ++) {
//				if (client.equals(destinationComponentList.get(j).getName())) chk1 = true;
//				if (server.equals(destinationComponentList.get(j).getName())) chk2 = true;
//			}
//			
//			ChoiceResolution vspr1 = (ChoiceResolution)returnVSpecResolutionByComponentName(client, vpList, resolutionList);
//			ChoiceResolution vspr2 = (ChoiceResolution)returnVSpecResolutionByComponentName(server, vpList, resolutionList);
//			//if ((vspr1 != null) && (vspr2 != null)) System.out.println(chk1 +""+ chk2 +""+ vspr1.isDecision() +""+ vspr2.isDecision());
//			if (chk1 && chk2 && vspr1.isDecision() && vspr2.isDecision()) {
//				destinationDefinition.getBindings().add(destinationBinding);
//			}
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
//			vspr = returnVSpecResolutionByVSpec(vsp, resolutionList);
//			
//		} else if (vp instanceof ObjectSubstitution) {
//			VSpec vsp = vp.getBindingVSpec();
//			//if vp is objectsubtitution, we need find the parent of a Vspec in Vspec tree  
//			VSpec parent = (VSpec)vsp.eContainer();
//			vspr = returnVSpecResolutionByVSpec(parent, resolutionList);
//		}
//		
//		return vspr;
//	}
//	public VSpecResolution returnVSpecResolutionByVSpec(VSpec vSpec, ArrayList<VSpecResolution> resolutionList) {
//		
//		VSpecResolution vSpecresolution = null;
//		for (int i = 0; i < resolutionList.size(); i++) {
//			if (resolutionList.get(i).getResolvedVSpec().getName().equals(vSpec.getName())) {
//				return resolutionList.get(i);
//			}
//		}
//		return vSpecresolution;
//	}
//	public VariationPoint returnVPByComponent (String componentName, ArrayList<VariationPoint> vpList) {
//		VariationPoint vp = null;
//		for (int i = 0; i < vpList.size(); i++) {
//			if (vpList.get(i) instanceof ObjectExistence) {
//				String mofReference = ((ObjectExistence)vpList.get(i)).getOptionalObject().getMOFRef();
//				String strcomponentName = mofReference.substring(mofReference.lastIndexOf(".") + 1);
//				if (strcomponentName.equals(componentName)) {
//					return vpList.get(i); 
//				}
//			} else if (vpList.get(i) instanceof ObjectSubstitution) {
//				
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
//	public VariationPoint returnVPByAttribute (String attributeName, ArrayList<VariationPoint> vpList) {
//		VariationPoint vp = null;
//		for (int i = 0; i < vpList.size(); i++) {
//			if (vpList.get(i) instanceof ParametricSlotAssignment) {
//				String mofReference = ((ParametricSlotAssignment)vpList.get(i)).getSlotOwner().getMOFRef();
//				String strAttributeName = mofReference.substring(mofReference.lastIndexOf(".") + 1);
//				if (strAttributeName.equals(attributeName)) {
//					return vpList.get(i); 
//				}
//			
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
//	public void deleteVPInVPList(VariationPoint vp, ArrayList<VariationPoint> vpList) {
//		for (Iterator<VariationPoint> v = vpList.listIterator(); v.hasNext(); ) {
//			VariationPoint vsp = v.next();
//		    if (vsp.getName().equals(vp.getName())) {
//		       // System.out.println("del:"+vsp.getName());
//		    	v.remove(); 
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
//	
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
//	public VSpec returnVSpecByVP(VariationPoint vp, ArrayList<VSpec> vSpecList) {
//		VSpec vSpec = null;
//		
//		for (int i = 0; i < vSpecList.size(); i++) {
//			if (vSpecList.get(i).getName().equals(vp.getBindingVSpec().getName())) {
//				return vSpecList.get(i);
//			}
//		}
//		
//		return vSpec;
//	}
//	public static void main(String [] args) {
//		String variabilityModelFileName = "model//primitive//model.cvl";
//		String resolutionModelFileName = "model//primitive//resolution.cvl";
//		String baseModelFileName = "model//primitive//architecture.fractal";
//		String productModelFileName = "model//primitive//product.fractal";
//		GeneratingProductArchitecture q = new GeneratingProductArchitecture(variabilityModelFileName, 
//				resolutionModelFileName, baseModelFileName, 
//				productModelFileName);
//		q.createProductModel(q.readArchitecture(""));
//	}
//
//}
//
//
