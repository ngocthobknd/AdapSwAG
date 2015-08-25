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
import org.ow2.fractal.f4e.fractal.Attribute;
import org.ow2.fractal.f4e.fractal.Binding;
import org.ow2.fractal.f4e.fractal.Component;
import org.ow2.fractal.f4e.fractal.Definition;
import org.ow2.fractal.f4e.fractal.FractalFactory;
import org.ow2.fractal.f4e.fractal.Interface;

import resolution.gui.ResolutionModelGUI;
import variability.gui.VariabilityModelGUI;
import base.gui.BaseArchitectureGUI;
import cvl.Choice;
import cvl.ChoiceResolution;
import cvl.ObjectExistence;
import cvl.ObjectSubstitution;
import cvl.ParametricSlotAssignment;
import cvl.VSpec;
import cvl.VSpecResolution;
import cvl.VariableValueAssignment;
import cvl.VariationPoint;

public class FractalGeneration {
	private ArrayList<VSpec> vSpecList = new ArrayList<VSpec>();
	private ArrayList<VariationPoint> vpList = new ArrayList<VariationPoint>();
	private ArrayList<VSpecResolution> vSpecResolutionList = new ArrayList<VSpecResolution>();

	private Definition destDefinition;
	private FractalFactory fractalADLFractory;
	
	private ArrayList<Component> sourceComponentList = new ArrayList<Component>();
	private ArrayList<Binding> sourceBindingList = new ArrayList<Binding>();
	public FractalGeneration() {
		// TODO Auto-generated constructor stub
	}
	public FractalGeneration(ArrayList<VSpec> vSpecList,
			ArrayList<VariationPoint> vpList,
			ArrayList<VSpecResolution> vSpecResolutionList,
			ArrayList<Component> sourceComponentList,
			ArrayList<Binding> sourceBindingList) {
		super();
		this.vSpecList = vSpecList;
		this.vpList = vpList;
		this.vSpecResolutionList = vSpecResolutionList;
		this.sourceComponentList = sourceComponentList;
		this.sourceBindingList = sourceBindingList;
		fractalADLFractory = FractalFactory.eINSTANCE;
		destDefinition = fractalADLFractory.createDefinition();
	}
	public void create(String createdFileName, String name) {
		// TODO Auto-generated method stub
		destDefinition.setName("product"+name);
		
		Component component_temp = fractalADLFractory.createComponent();
		createFractal(component_temp, 
				vSpecList, 
				vpList, 
				vSpecResolutionList, 
				sourceComponentList, 
				sourceBindingList);
		destDefinition.getSubComponents().addAll(component_temp.getSubComponents());
		destDefinition.getBindings().addAll(component_temp.getBindings());
		
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
	}
	public void createFractal(Component component, 
			ArrayList<VSpec> vSpecList,
			ArrayList<VariationPoint> vpList,
			ArrayList<VSpecResolution> vSpecResolutionList,
			ArrayList<Component> sourceComponentList,
			ArrayList<Binding> sourceBindingList) {
		for (int i = 0; i < sourceComponentList.size(); i++ ){
			VSpec vSpec = returnVSpecByComponentName(sourceComponentList.get(i).getName(), vpList, vSpecList);
			
			VariationPoint vp = returnVPByComponent(sourceComponentList.get(i).getName(), vpList);
			if (vSpec instanceof Choice) {
				Choice choice = (Choice) vSpec;
				VSpecResolution vSpecResolution = returnVSpecResolutionByComponentName(sourceComponentList.get(i).getName(), vpList, vSpecResolutionList);
				boolean decision = false;
				if (vSpecResolution != null) {
					decision = ((ChoiceResolution)vSpecResolution).isDecision();
				} else if (choice.isDefaultResolution()) {
					decision = choice.isDefaultResolution();
				}
				if( decision || 
						(vSpec.getAvailabilityTime().toString().equals("runtime"))) {
					if (vp instanceof ObjectExistence) {
						Component comp = sourceComponentList.get(i);
						Component comp_temp = fractalADLFractory.createComponent();
						comp_temp = comp;
						if (comp.getSubComponents().size() > 0) {
							ArrayList<Component> subComponentList = new ArrayList<Component>();
							subComponentList.addAll(comp.getSubComponents());
							ArrayList<Binding> subBindingList = new ArrayList<Binding>();
							for (int j = 0; j < comp.getBindings().size(); j++) {
								subBindingList.add(comp.getBindings().get(j)); 
							}
							comp_temp.getSubComponents().removeAll(comp.getSubComponents());
							comp_temp.getBindings().removeAll(comp.getBindings());
							
							createFractal(comp_temp, 
									vSpecList,
									vpList,
									vSpecResolutionList,
									subComponentList,
									subBindingList);
						}
						//add attribute
						if (comp.getAttributesController() != null) {
							for (int j = 0; j < comp.getAttributesController().getAttributes().size(); j++) {
								Attribute attribute = comp.getAttributesController().getAttributes().get(j);
								String attributeName = attribute.getName();
								
								VariationPoint vpT = returnVPByAttribute(attributeName, vpList);
								VSpec vsp = null; 
								if (vpT != null) vsp = returnVSpecByVP(vpT, vSpecList);
								VSpecResolution vspR = null;
								if (vsp != null) vspR = returnVSpecResolutionByVSpec(vsp, vSpecResolutionList);
								if (vspR instanceof VariableValueAssignment) {
									String value = ((VariableValueAssignment)vspR).getValue();
									attribute.setValue(value);
									comp_temp.getAttributesController().getAttributes().add(attribute);
								}
							}
							
						}
						//System.out.println(comp_temp.getName());
						component.getSubComponents().add(comp_temp);
					} else if (vp instanceof ObjectSubstitution) {
						VSpec parent = (VSpec)vSpec.eContainer();
						VSpecResolution vspr = returnVSpecResolutionByVSpec(parent,vSpecResolutionList);
						if (((ChoiceResolution)vspr).isDecision() || ((Choice)parent).isDefaultResolution()) {
							ObjectSubstitution vpSubT = (ObjectSubstitution)vp;
							String mofRefPlacement = vpSubT.getPlacementObject().getMOFRef();
							String mofRefReplacement = vpSubT.getReplacementObject().getMOFRef();
							String srcComponentName = mofRefPlacement.substring(mofRefPlacement.lastIndexOf(".") + 1);
							String componentName = mofRefReplacement.substring(mofRefReplacement.lastIndexOf(".") + 1);
							
							Component comp_temp = returnComponentByName(componentName, sourceComponentList);
							Component comp_del = returnComponentByName(srcComponentName, sourceComponentList);
							//add component to C
							if (comp_temp != null) component.getSubComponents().add(comp_temp);
							if (comp_del != null) component.getSubComponents().remove(comp_del);
							//add attribute
							if (comp_temp != null)
							if ((comp_temp != null) && (comp_temp.getAttributesController() != null)){
								for (int j = 0; j < comp_temp.getAttributesController().getAttributes().size(); j++) {
									Attribute attribute = comp_temp.getAttributesController().getAttributes().get(j);
									String attributeName = attribute.getName();
									VariationPoint vpT = returnVPByAttribute(attributeName, vpList);
									VSpec vsp = returnVSpecByVP(vpT, vSpecList);
									VSpecResolution vspR = returnVSpecResolutionByVSpec(vsp, vSpecResolutionList);
									if (vspR instanceof VariableValueAssignment) {
										String value = ((VariableValueAssignment)vspR).getValue();
										attribute.setValue(value);
										comp_temp.getAttributesController().getAttributes().add(attribute);
									}
								}
							}
							//modify binding
							for (int j = 0; j < sourceBindingList.size(); j++) {
								Binding destBinding = sourceBindingList.get(j);
								String client = destBinding.getClient();
								String server = destBinding.getServer();
								
								if (client.equals(comp_del.getName())) {
									sourceBindingList.get(j).setClient(comp_temp.getName());
									for (int k = 0; k < comp_temp.getInterfaces().size(); k++) {
										Interface itf = comp_temp.getInterfaces().get(k);
										if (itf.getName().equals(destBinding.getClientInterface().getName())) {
											destBinding.setClientInterface(itf);
										}
									}
								}
								if (server.equals(comp_del.getName())) {
									sourceBindingList.get(j).setServer(comp_temp.getName());
									for (int k = 0; k < comp_temp.getInterfaces().size(); k++) {
										Interface itf = comp_temp.getInterfaces().get(k);
										if (itf.getName().equals(destBinding.getServerInterface().getName())) {
											destBinding.setServerInterface(itf);
										}
									}
								}
							}
						}
					}
					
				}
			}			
		}
		//add binding
		
		for (int i = 0; i < sourceBindingList.size(); i++) {
			Binding destBinding = sourceBindingList.get(i);
			String client = destBinding.getClient();
			String server = destBinding.getServer();
			boolean chk1 = false, chk2 = false;
			
			for (int j = 0; j < component.getSubComponents().size(); j ++) {
				if ( (client.equals(component.getSubComponents().get(j).getName())) 
						|| (client.equals(component.getName())))chk1 = true;
				if ( (server.equals(component.getSubComponents().get(j).getName())) ||
						(server.equals(component.getName()))) chk2 = true;
			}
			Choice vsp1 = (Choice)returnVSpecByComponentName(client, vpList, vSpecList);
			Choice vsp2 = (Choice)returnVSpecByComponentName(server, vpList, vSpecList);
			ChoiceResolution vspr1 = (ChoiceResolution)returnVSpecResolutionByComponentName(client, vpList, vSpecResolutionList);
			ChoiceResolution vspr2 = (ChoiceResolution)returnVSpecResolutionByComponentName(server, vpList, vSpecResolutionList);
			boolean decision1 = false, decision2 = false;
			if ( vspr1 != null) decision1 = vspr1.isDecision(); 
			else  if (vsp1 != null) decision1 = vsp1.isDefaultResolution(); 
			if ( vspr2 != null) decision2 = vspr2.isDecision(); 
			else if (vsp2 != null) decision2 = vsp2.isDefaultResolution();
			
			//if ((vspr1 != null) && (vspr2 != null)) System.out.println(chk1 +""+ chk2 +""+ vspr1.isDecision() +""+ vspr2.isDecision());
			if (chk1 && chk2 && decision1 && decision2) {
				component.getBindings().add(destBinding);
			}
		}
		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BaseArchitectureGUI baseModel = new BaseArchitectureGUI("model//fractal//architecture.fractal");
		VariabilityModelGUI variabilityModel = new VariabilityModelGUI("model//fractal//model.cvl");
		ResolutionModelGUI resolutionModel = new ResolutionModelGUI( "model//fractal//resolution.cvl"); 
		//BaseArchitectureGUI product = new BaseArchitectureGUI(productModelFileName);
		Definition definition = baseModel.getDefinition();
		FractalGeneration generateProduct = new FractalGeneration(variabilityModel.getVSpecList(),
				variabilityModel.getVariationPointList(),
				resolutionModel.getVSpecResolutionList(),
				baseModel.getParentComponentList(definition), 
				baseModel.getParentBindingList(definition));
		
		//generateProduct.createProductModel(definition, productModelFileName);
		//generateProduct.createProductModel(generateProduct.readArchitecture(""));
		String baseModelFileName =  baseModel.baseModelFileName;
		int i = baseModelFileName.lastIndexOf("//");
		String header = baseModelFileName.substring(0, i);
		String footer =  baseModelFileName.substring(i + 2);
		String productModelFileName = header + "//" + "generated" + footer;
		
		//Definition definition = baseModel.getArchitectureDefinition();
		generateProduct.create(productModelFileName, definition.getName());
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
			
				String strPlacementName = ((ObjectSubstitution)vpList.get(i)).getPlacementObject().getMOFRef();
				String strReplacementName = ((ObjectSubstitution)vpList.get(i)).getReplacementObject().getMOFRef();
				strPlacementName = strPlacementName.substring(strPlacementName.lastIndexOf(".") + 1);
				strReplacementName = strReplacementName.substring(strReplacementName.lastIndexOf(".") + 1);
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
			try {
				if (vSpecList.get(i).getName().equals(vp.getBindingVSpec().getName())) {
					return vSpecList.get(i);
				}
			}catch (Exception e) {
				System.out.println(vSpecList.get(i)+"::::::::"+vp.getBindingVSpec());
			}
		}
		return vSpec;
	}
	public VSpec returnVSpecByComponentName(String componentName, ArrayList<VariationPoint> vpList, ArrayList<VSpec> vSpecList) {
		VSpec vSpec = null;
		VariationPoint vp = returnVPByComponent(componentName, vpList);
		if (vp != null) vSpec = returnVSpecByVP(vp, vSpecList);
		return vSpec;
	}
	
}
