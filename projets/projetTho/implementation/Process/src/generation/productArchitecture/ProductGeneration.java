package generation.productArchitecture;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.ow2.fractal.f4e.fractal.Binding;
import org.ow2.fractal.f4e.fractal.Component;
import org.ow2.fractal.f4e.fractal.Definition;
import org.ow2.fractal.f4e.fractal.FractalFactory;
import org.ow2.fractal.f4e.fractal.Interface;

import base.gui.BaseArchitectureGUI;
import resolution.gui.ResolutionModelGUI;
import variability.gui.VariabilityModelGUI;
import cvl.Choice;
import cvl.ChoiceResolution;
import cvl.FragmentSubstitution;
import cvl.ObjectExistence;
import cvl.ObjectHandle;
import cvl.ObjectSubstitution;
import cvl.ParametricSlotAssignment;
import cvl.VSpec;
import cvl.VSpecResolution;
import cvl.VariableValueAssignment;
import cvl.VariationPoint;

public class ProductGeneration {

	private ArrayList<VSpec> vSpecList = new ArrayList<VSpec>();
	private ArrayList<VariationPoint> vpList = new ArrayList<VariationPoint>();
	private ArrayList<VSpecResolution> vSpecResolutionList = new ArrayList<VSpecResolution>();

	private Definition destDefinition;
	private FractalFactory fractalADLFractory;
	
	private ArrayList<Component> destinationComponentList = new ArrayList<Component>();
	private ArrayList<Component> sourceComponentList = new ArrayList<Component>();
	private ArrayList<Binding> sourceBindingList = new ArrayList<Binding>();
	
	public ProductGeneration(ArrayList<Component> sourceComponentList,
			ArrayList<Binding> sourceBindingList,
			ArrayList<VSpec> vSpeclist,
			ArrayList<VariationPoint> vpList,
			ArrayList<VSpecResolution> vSpecResolutionList ) {
		this.sourceComponentList = sourceComponentList;
		this.sourceBindingList = sourceBindingList;
		this.vSpecList = vSpeclist;
		this.vpList = vpList;
		this.vSpecResolutionList = vSpecResolutionList;
	}
	public void createProductModel(Definition sourceDefinitionFractal, String createdFileName) {
		//FractalPackage.eINSTANCE.eClass();
		fractalADLFractory = FractalFactory.eINSTANCE;
		destDefinition = fractalADLFractory.createDefinition();
		destDefinition.setName("product"+sourceDefinitionFractal.getName());

		addComponents(destDefinition, sourceComponentList, vSpecList, vpList, vSpecResolutionList);
		
		addBinding(destDefinition, destinationComponentList, sourceBindingList, vpList, vSpecResolutionList);
		/*
		 * write to file
		 */
		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
		Map<String, Object> m = reg.getExtensionToFactoryMap();
	    m.put("fractal", new XMIResourceFactoryImpl());
	    // Obtain a new resource set
	    ResourceSet resSet = new ResourceSetImpl();
	    // create a resource
	    Resource resource = resSet.createResource(URI.createURI(createdFileName));
	    // Get the first model element and cast it to the right type, in my
	    // example everything is hierarchical included in this first node
	    resource.getContents().add(destDefinition);
	    // now save the content.
	    try {
	    	resource.save(Collections.EMPTY_MAP);
	    } catch (IOException e) {
	      // TODO Auto-generated catch block
	    	e.printStackTrace();
	    }
//	    System.out.println("4"+sourceDefinitionFractal.getSubComponents().size());
//	    System.out.println("4"+sourceComponentList.size());
	} 
	public boolean addComponents(Definition destinationDefinition, 
			ArrayList<Component> srcComponentList, 
			ArrayList<VSpec> vSpecList,	
			ArrayList<VariationPoint> vpList, 
			ArrayList<VSpecResolution> vSpecResolutionList) {
		//begin procedure
		ArrayList<Component> srcComponentListTemp = new ArrayList<>(srcComponentList);
		ArrayList<VariationPoint> vpList_temp = new ArrayList<>(vpList);
		for (Iterator<VSpec> iter = vSpecList.listIterator(); iter.hasNext();) {

			Component destinationCompnent = null;
			VSpec vSpec = iter.next();
			if (vSpec instanceof Choice) {
				Choice choice = (Choice) vSpec;
				VSpecResolution vSpecResolution = returnVSpecResolutionByVSpec(vSpec, vSpecResolutionList);
				boolean decision = false;
				if (vSpecResolution != null) {
					decision = ((ChoiceResolution)vSpecResolution).isDecision();
				} else if (choice.isDefaultResolution()) {
					decision = choice.isDefaultResolution();
				}
				if (decision || vSpec.getAvailabilityTime().toString().equals("runtime")) {
					VariationPoint vp = returnVPByVSpecName(vSpec.getName(), vpList_temp);
					String componentName;
					if (vp instanceof ObjectExistence) {
						ObjectExistence vpObjE = (ObjectExistence) vp;
						ObjectHandle objHandle = vpObjE.getOptionalObject();
						componentName = objHandle.getMOFRef().substring(objHandle.getMOFRef().lastIndexOf(".") + 1);
						
						Component tempComponent = returnComponentByName(componentName, srcComponentListTemp);	
						if (tempComponent.getSubComponents().isEmpty()) {
							Component componentA = setAttributeInComponent(tempComponent, vpList_temp, vSpecResolutionList);
							destinationCompnent = componentA;
						} else {
							Component component = fractalADLFractory.createComponent();
							component.setName(tempComponent.getName());
							component.getInterfaces().addAll(tempComponent.getInterfaces());
							
							ArrayList<Component> compList = new ArrayList<Component>();
							List<Component> eComponentList = tempComponent.getSubComponents();
							for (int i = 0; i < eComponentList.size(); i++) {
								compList.add(eComponentList.get(i));
							}
							for (int i = 0; i < compList.size(); i++) {
								Component subComponentTemp = compList.get(i);
								//System.out.println(i+":"+subComponentTemp.getName());	
								VariationPoint vpTemp = returnVPByComponent(subComponentTemp.getName(), vpList_temp);
								
								deleteVPInVPList(vpTemp, vpList_temp);
								try {
									
									VSpec vSpecTemp = returnVSpecByVP(vpTemp, vSpecList);
									VSpecResolution vSpecResolutionTemp = returnVSpecResolutionByVSpec(vSpecTemp, vSpecResolutionList);
									if (vSpecTemp.getAvailabilityTime().toString().equals("runtime") 
											|| ((ChoiceResolution)vSpecResolutionTemp).isDecision()) {
										if ((subComponentTemp.getAttributesController() != null) && (!subComponentTemp.getAttributesController().getAttributes().isEmpty())) {
											Component componentA = setAttributeInComponent(subComponentTemp, vpList_temp, vSpecResolutionList);
											component.getSubComponents().add(componentA); ////////////////////
											destinationComponentList.add(componentA);
										} else {
											component.getSubComponents().add(subComponentTemp); /////////////////
											destinationComponentList.add(compList.get(i));
										}
								}
								}catch (Exception e) {System.out.println("s:"+e);}
								
							}
							destinationCompnent = component;
						}
					}
					else if (vp instanceof ObjectSubstitution) { 
						ObjectSubstitution vpSubT = (ObjectSubstitution) vp;
						String mofRefPlacement = vpSubT.getPlacementObject().getMOFRef();
						String mofRefReplacement = vpSubT.getReplacementObject().getMOFRef();
						String srcComponentName = mofRefPlacement.substring(mofRefPlacement.lastIndexOf(".") + 1);
						//Component temp_source = returnComponentByName(srcComponentName, sourceComponentList);
						componentName = mofRefReplacement.substring(mofRefReplacement.lastIndexOf(".") + 1);
						destinationCompnent = returnComponentByName(componentName, srcComponentListTemp);
					
						if (decision) {
							for (int i = 0; i < sourceBindingList.size(); i++) {
								String client = sourceBindingList.get(i).getClient();
								String server = sourceBindingList.get(i).getServer();
								Interface interfaceClient = sourceBindingList.get(i).getClientInterface();
								Interface interfaceServer = sourceBindingList.get(i).getServerInterface();
								if (client.equals(srcComponentName)) {
									sourceBindingList.get(i).setClient(componentName);
									for (int j = 0; j < destinationCompnent.getInterfaces().size(); j++ ) {
										if (destinationCompnent.getInterfaces().get(j).getRole().getName().equals("client") && 
												interfaceClient.getName().equals(destinationCompnent.getInterfaces().get(j).getName())) {
											sourceBindingList.get(i).setClientInterface(destinationCompnent.getInterfaces().get(j));
										}
									}
								}
								if (server.equals(srcComponentName)) {
									sourceBindingList.get(i).setServer(componentName);
									for (int j = 0; j < destinationCompnent.getInterfaces().size(); j++ ) {
										if (destinationCompnent.getInterfaces().get(j).getRole().getName().equals("server") && 
												interfaceServer.getName().equals(destinationCompnent.getInterfaces().get(j).getName())) {
											sourceBindingList.get(i).setServerInterface(destinationCompnent.getInterfaces().get(j));
										}
									}
								}
							}
						}
					} else if (vp instanceof FragmentSubstitution) { //VClassifier
						//clone component
					}
				} else {
					System.out.println(vSpec.getName() + " is not configured for product");
				}
			}
			if (destinationCompnent != null) {
				destinationDefinition.getSubComponents().add(destinationCompnent); /////////////////////
				//System.out.println("add:"+destinationCompnent.getName());
				destinationComponentList.add(destinationCompnent); ///////////////////////
			}
		}
		//System.out.println("4:"+definition.getSubComponents().size());
		return true;
	}
	public Component setAttributeInComponent(Component component, ArrayList<VariationPoint> vpList, ArrayList<VSpecResolution> resolutionList ) {
		/*
		 * set value of attribute in component
		 */		
		for (int i = 0; i < component.getAttributesController().getAttributes().size(); i++) {
			VariationPoint vp = returnVPByAttribute(component.getAttributesController().getAttributes().get(i).getName(), vpList);
			if (vp != null) {
				VSpec vspec = (vp).getBindingVSpec();
				VSpecResolution vspecresolution = returnVSpecResolutionByVSpec(vspec, resolutionList);
				String value = ((VariableValueAssignment)vspecresolution).getValue();
				component.getAttributesController().getAttributes().get(i).setValue(value);
				
			}
		}
		return component;
	}

	
	public boolean addBinding(Definition destinationDefinition, ArrayList<Component> destinationComponentList, List<Binding> bindingList,
			ArrayList<VariationPoint> vpList, ArrayList<VSpecResolution> resolutionList) {
		Binding destinationBinding;// = fractalFractory.createBinding();
		for (int i = 0; i < bindingList.size(); i++) {
			destinationBinding = bindingList.get(i);
			String client = destinationBinding.getClient();
			String server = destinationBinding.getServer();
			boolean chk1 = false, chk2 = false;
			
			for (int j = 0; j < destinationComponentList.size(); j ++) {
				if (client.equals(destinationComponentList.get(j).getName())) chk1 = true;
				if (server.equals(destinationComponentList.get(j).getName())) chk2 = true;
			}
			
			ChoiceResolution vspr1 = (ChoiceResolution)returnVSpecResolutionByComponentName(client, vpList, resolutionList);
			ChoiceResolution vspr2 = (ChoiceResolution)returnVSpecResolutionByComponentName(server, vpList, resolutionList);
			//if ((vspr1 != null) && (vspr2 != null)) System.out.println(chk1 +""+ chk2 +""+ vspr1.isDecision() +""+ vspr2.isDecision());
			if (chk1 && chk2 && vspr1.isDecision() && vspr2.isDecision()) {
				destinationDefinition.getBindings().add(destinationBinding);
			}
		}
		return true;
	}
	public VSpecResolution returnVSpecResolutionByComponentName(String componentName,	
			ArrayList<VariationPoint> vpList, ArrayList<VSpecResolution> resolutionList) {
		VSpecResolution vspr = null;
		
		VariationPoint vp = returnVPByComponent(componentName, vpList);
		if (vp != null) 
		if (vp instanceof ObjectExistence) {
			VSpec vsp = vp.getBindingVSpec();
			vspr = returnVSpecResolutionByVSpec(vsp, resolutionList);
			
		} 
		else if (vp instanceof ObjectSubstitution) {
			VSpec vsp = vp.getBindingVSpec();
			//if vp is objectsubtitution, we need find the parent of a Vspec in Vspec tree  
			VSpec parent = (VSpec)vsp.eContainer();
			vspr = returnVSpecResolutionByVSpec(parent, resolutionList);
		}
		
		return vspr;
	}
	public VSpecResolution returnVSpecResolutionByVSpec(VSpec vSpec, ArrayList<VSpecResolution> resolutionList) {
		
		VSpecResolution vSpecresolution = null;
		for (int i = 0; i < resolutionList.size(); i++) {
			if (resolutionList.get(i).getResolvedVSpec().getName().equals(vSpec.getName())) {
				return resolutionList.get(i);
			}
		}
		return vSpecresolution;
	}
	public VariationPoint returnVPByComponent (String componentName, ArrayList<VariationPoint> vpList) {
		VariationPoint vp = null;
		for (int i = 0; i < vpList.size(); i++) {
			if (vpList.get(i) instanceof ObjectExistence) {
				String mofReference = ((ObjectExistence)vpList.get(i)).getOptionalObject().getMOFRef();
				String strcomponentName = mofReference.substring(mofReference.lastIndexOf(".") + 1);
				if (strcomponentName.equals(componentName)) {
					return vpList.get(i); 
				}
			} else if (vpList.get(i) instanceof ObjectSubstitution) {
				
				String strPlacementName = ((ObjectSubstitution)vpList.get(i)).getPlacementObject().getMOFRef().split("\\.")[1];
				String strReplacementName = ((ObjectSubstitution)vpList.get(i)).getReplacementObject().getMOFRef().split("\\.")[1];
				
				if (strPlacementName.equals(componentName) || strReplacementName.equals(componentName) ) {
					//System.out.println(vpList.get(i));
					return vpList.get(i); 
				}
			}
		}
		return vp;
	}
	public VariationPoint returnVPByAttribute (String attributeName, ArrayList<VariationPoint> vpList) {
		VariationPoint vp = null;
		for (int i = 0; i < vpList.size(); i++) {
			if (vpList.get(i) instanceof ParametricSlotAssignment) {
				String mofReference = ((ParametricSlotAssignment)vpList.get(i)).getSlotOwner().getMOFRef();
				String strAttributeName = mofReference.substring(mofReference.lastIndexOf(".") + 1);
				if (strAttributeName.equals(attributeName)) {
					return vpList.get(i); 
				}
			
			}
		}
		return vp;
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
	public void deleteVPInVPList(VariationPoint vp, ArrayList<VariationPoint> vpList) {
		for (Iterator<VariationPoint> v = vpList.listIterator(); v.hasNext(); ) {
			VariationPoint vsp = v.next();
		    if (vsp.getName().equals(vp.getName())) {
		       // System.out.println("del:"+vsp.getName());
		    	v.remove(); 
		        return;
		    }
		}
		    
	}
//	public RealizationComponent returnRealizationComponents(String name, List<RealizationComponent> realizationComponentList) {
//		RealizationComponent realizationComponent = null;
//		for (int i = 0; i < realizationComponentList.size(); i++) {
//			if (name.equals(realizationComponentList.get(i).getName())) return realizationComponentList.get(i);
//		}
//		return realizationComponent;
//	}
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
	
	public Component returnComponentByName(String name, ArrayList<Component> sourceComponents) {
		ArrayList<Component> listTemp = new ArrayList<Component>(sourceComponents);
		Component component = null;
		for (int i = 0; i < listTemp.size(); i++) {
			if (listTemp.get(i).getName().equals(name)) {
				return listTemp.get(i);
			}
		} 
		return component;
	}
	
	public VariationPoint returnVPByVSpecName(String vSpec, ArrayList<VariationPoint> vpList) {
		VariationPoint vp = null;
		for (int i = 0; i < vpList.size(); i++) {
			if (vpList.get(i).getBindingVSpec().getName().equals(vSpec)) {
				return vpList.get(i);
			}
		}
		return vp;
	}

	public VSpec returnVSpecByVP(VariationPoint vp, ArrayList<VSpec> vSpecList) {
		VSpec vSpec = null;
		
		for (int i = 0; i < vSpecList.size(); i++) {
			if (vSpecList.get(i).getName().equals(vp.getBindingVSpec().getName())) {
				return vSpecList.get(i);
			}
		}
		
		return vSpec;
	}
	
	public void addDisconnectedComponent(Definition destinationDefinition, List<Component> sourceComponentList, 
			ArrayList<VSpec> vSpecList,	
			ArrayList<VariationPoint> vpList, ArrayList<VSpecResolution> resolutionList) {
		//add component to destination model if it has not relation with VP
		// if search component MOF is not in VPs, add this component to product	
		for (int i = 0; i < sourceComponentList.size(); i++) {
			boolean chk = true;
			for (int j = 0; j < vpList.size(); j++) {
				VariationPoint vp = vpList.get(j);
				if (vp instanceof ObjectExistence) {
					String oObject = ((ObjectExistence) vp).getOptionalObject().getMOFRef();
					String componentName = oObject.substring(oObject.lastIndexOf(".") + 1);
					if (componentName.equals(sourceComponentList.get(i).getName())) {
						chk = false;
						break;
					}
				}
			}
			if (chk) destinationDefinition.getSubComponents().add(sourceComponentList.get(i));
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BaseArchitectureGUI baseModel = new BaseArchitectureGUI("model//primitive//architecture.fractal");
		VariabilityModelGUI variabilityModel = new VariabilityModelGUI("model//primitive//model.cvl");
		ResolutionModelGUI resolutionModel = new ResolutionModelGUI( "model//primitive//resolution.cvl"); 
		//BaseArchitectureGUI product = new BaseArchitectureGUI(productModelFileName);
		
	//	Definition definition = baseModel.getArchitectureDefinition("model//composite2//architecture.fractal");
		
//		ProductGeneration generateProduct = new ProductGeneration( 
//				baseModel.getComponentList(definition),
//				baseModel.getBindingList(definition), 
//				variabilityModel.getVSpecList(), 
//				variabilityModel.getVariationPointList(),
//				resolutionModel.getVSpecResolutionList());
//		
		//generateProduct.createProductModel(definition, productModelFileName);
		//generateProduct.createProductModel(generateProduct.readArchitecture(""));
		String baseModelFileName =  baseModel.baseModelFileName;
		int i = baseModelFileName.lastIndexOf("//");
		String header = baseModelFileName.substring(0, i);
		String footer =  baseModelFileName.substring(i + 2);
		String productModelFileName = header + "//" + "generated" + footer;
		
		//Definition definition = baseModel.getArchitectureDefinition();
	//	generateProduct.createProductModel(definition, productModelFileName);
	}

}
