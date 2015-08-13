package generation.productArchitecture;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import base.acme.implement.ACMEImpl;
import resolution.gui.ResolutionModelGUI;
import variability.gui.VariabilityModelGUI;
import ACME.ACMEFactory;
import ACME.Attachment;
import ACME.Binding;
import ACME.ComponentInstance;
import ACME.Connector;
import ACME.Property;
import ACME.Representation;
import cvl.Choice;
import cvl.ChoiceResolution;
import cvl.ObjectExistence;
import cvl.ObjectSubstitution;
import cvl.ParametricSlotAssignment;
import cvl.VSpec;
import cvl.VSpecResolution;
import cvl.VariableValueAssignment;
import cvl.VariationPoint;

public class ACMEGeneration {
	private ArrayList<VSpec> vSpecList = new ArrayList<VSpec>();
	private ArrayList<VariationPoint> vpList = new ArrayList<VariationPoint>();
	private ArrayList<VSpecResolution> vSpecResolutionList = new ArrayList<VSpecResolution>();

	ArrayList<ComponentInstance> componentList = new ArrayList<ComponentInstance>();
	ArrayList<Connector> connectorList = new ArrayList<Connector>();
	ArrayList<Attachment> attachementList = new ArrayList<Attachment>();
	ArrayList<Binding> bindingList = new ArrayList<Binding>();
	
	ACME.System destSystem;
	ACMEFactory acmeFactory;
	
	
	public ACMEGeneration() {
		// TODO Auto-generated constructor stub
	}
	public ACMEGeneration(ArrayList<VSpec> vSpecList,
			ArrayList<VariationPoint> vpList,
			ArrayList<VSpecResolution> vSpecResolutionList,
			ArrayList<ComponentInstance> componentList,
			ArrayList<Connector> connectorList,
			ArrayList<Attachment> attachementList,
			ArrayList<Binding> bindingList) {
		super();
		this.vSpecList = vSpecList;
		this.vpList = vpList;
		this.vSpecResolutionList = vSpecResolutionList;
		this.componentList = componentList;
		this.connectorList = connectorList;
		this.attachementList = attachementList;
		this.bindingList = bindingList;
		
		acmeFactory = ACMEFactory.eINSTANCE;
		destSystem = acmeFactory.createSystem();
		
	}
	public void create(String fileNameDest, String systemName){
		destSystem.setName("product"+systemName);
		ACME.ComponentInstance component = acmeFactory.createComponentInstance();
		
//		createACME(component, vSpecList, vpList, vSpecResolutionList, componentList, connectorList, attachementList, bindingList);
//		destSystem.getComponentDeclaration().addAll(component.getRepresentations().get(0).getSystems().get(0).getComponentDeclaration());
//		destSystem.getConnectorDeclaration().addAll(component.getRepresentations().get(0).getSystems().get(0).getConnectorDeclaration());
//		destSystem.getAttachement().addAll(component.getRepresentations().get(0).getSystems().get(0).getAttachement());

		createACME1(destSystem, component, vSpecList, vpList, vSpecResolutionList, componentList, connectorList, attachementList, bindingList);

		//write to file
		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
		Map<String, Object> m = reg.getExtensionToFactoryMap();
	    m.put("acme", new XMIResourceFactoryImpl());
	    // Obtain a new resource set
	    ResourceSet resSet = new ResourceSetImpl();
	    // create a resource
	    Resource resource = resSet.createResource(URI.createURI(fileNameDest));
	    // Get the first model element and cast it to the right type, in my
	    // example everything is hierarchical included in this first node
	    resource.getContents().add(destSystem);
	    // now save the content.
	    try {
	    	resource.save(Collections.EMPTY_MAP);
	    } catch (IOException e) {
	      // TODO Auto-generated catch block
	    	e.printStackTrace();
	    }
	}

	public void createACME1(ACME.System system_recusif, 
			ACME.Component component,
			ArrayList<VSpec> vSpecList,
			ArrayList<VariationPoint> vpList,
			ArrayList<VSpecResolution> vSpecResolutionList,
			ArrayList<ComponentInstance> componentList,
			ArrayList<Connector> connectorList,
			ArrayList<Attachment> attachmentList,
			ArrayList<Binding> bindingList) {
		//begin procedure
		
		for (int i = 0; i < componentList.size(); i++) {
			VSpec vSpec = returnVSpecByComponentName(componentList.get(i).getName(), vpList, vSpecList);
			VariationPoint vp = returnVPByComponent(componentList.get(i).getName(), vpList);
			if (vSpec instanceof Choice) {
				Choice choice = (Choice) vSpec;
				VSpecResolution vSpecResolution = returnVSpecResolutionByComponentName(componentList.get(i).getName(), vpList, vSpecResolutionList);
				boolean decision = false;
				if (vSpecResolution != null) {
					decision = ((ChoiceResolution)vSpecResolution).isDecision();
				} else if (choice.isDefaultResolution()) {
					decision = choice.isDefaultResolution();
				}
				if( decision || 
						 (vSpec.getAvailabilityTime().toString().equals("runtime"))) {
				
					if (vp instanceof ObjectExistence) {
						ComponentInstance comp = componentList.get(i);
						ComponentInstance comp_temp = acmeFactory.createComponentInstance();
						comp_temp = comp;
		
						if (comp.getRepresentations() != null) {
							
							ArrayList<Representation> repList = new ArrayList<Representation>();
							for (int j = 0; j < comp.getRepresentations().size(); j++) {
								repList.add(comp.getRepresentations().get(j));
							}
							
							for (int j = 0; j < comp.getRepresentations().size(); j++) {
								EList<ACME.System> subSystemList = comp.getRepresentations().get(j).getSystems();
								Representation representation_temp = acmeFactory.createRepresentation();
								if (subSystemList != null) {
									for (int k = 0; k < subSystemList.size(); k++) {
										ACME.System systemSub = subSystemList.get(k);
									
										ArrayList<ACME.ComponentInstance> list = new ArrayList<ACME.ComponentInstance>(); 
										for (int l = 0; l < systemSub.getComponentDeclaration().size(); l++) {
											list.add(systemSub.getComponentDeclaration().get(l));
										}
										
										ArrayList<ACME.Connector> connectorList_Temp = new ArrayList<ACME.Connector>(); 
										for (int l = 0; l < systemSub.getConnectorDeclaration().size(); l++) {
											connectorList_Temp.add(systemSub.getConnectorDeclaration().get(l));
										}
										
										ArrayList<ACME.Attachment> attachmentList_Temp = new ArrayList<ACME.Attachment>(); 
										for (int l = 0; l < systemSub.getAttachement().size(); l++) {
											if (systemSub.getAttachement().get(l) instanceof Attachment) {
												attachmentList_Temp.add((Attachment) systemSub.getAttachement().get(l));
											}
										}
										
										ArrayList<ACME.Binding> bindingList_Temp = new ArrayList<ACME.Binding>(); 
										for (int l = 0; l < systemSub.getAttachement().size(); l++) {
											if (systemSub.getAttachement().get(l) instanceof Binding) {
												bindingList_Temp.add((Binding) systemSub.getAttachement().get(l));
											}
										}
										
										ACME.System sys = acmeFactory.createSystem();
										//sys.setName(systemSub.getName());
										sys = systemSub;
										sys.getComponentDeclaration().removeAll(systemSub.getComponentDeclaration());
										sys.getConnectorDeclaration().removeAll(systemSub.getConnectorDeclaration());
										sys.getBinding().removeAll(systemSub.getBinding());
										sys.getAttachement().removeAll(systemSub.getAttachement());
										
										comp_temp.getRepresentations().removeAll(repList);
										createACME1(sys, comp_temp,
												vSpecList, 
												vpList, 
												vSpecResolutionList, 
												list, 
												connectorList_Temp, 
												attachmentList_Temp, 
												bindingList_Temp);
										
										representation_temp.getSystems().add(sys);
									}
								}
								
								comp_temp.getRepresentations().add(representation_temp);
							}
						}
						//search property
						if (comp.getProperty() != null) {
							for (int j = 0; j < comp.getProperty().size(); j++) {
								Property proper = comp.getProperty().get(j);
								VariationPoint vpT = returnVPByAttribute(proper.getName().toString(), vpList);
								VSpec vsp = returnVSpecByVP(vpT, vSpecList);
								VSpecResolution vspR = returnVSpecResolutionByVSpec(vsp, vSpecResolutionList);
								if (vspR instanceof VariableValueAssignment) {
									String value = ((VariableValueAssignment)vspR).getValue();
									proper.setVal(value);
									comp_temp.getProperty().add(proper);
								}
								 
							}
						}
						
						system_recusif.getComponentDeclaration().add(comp_temp);
						
					} else if (vp instanceof ObjectSubstitution) {
						VSpec parent = (VSpec)vSpec.eContainer();
						VSpecResolution vspr = returnVSpecResolutionByVSpec(parent,vSpecResolutionList);
						if (((ChoiceResolution)vspr).isDecision() || ((Choice)parent).isDefaultResolution()) {
							ObjectSubstitution vpSubT = (ObjectSubstitution)vp;
							String mofRefPlacement = vpSubT.getPlacementObject().getMOFRef();
							String mofRefReplacement = vpSubT.getReplacementObject().getMOFRef();
							String srcComponentName = mofRefPlacement.substring(mofRefPlacement.lastIndexOf(".") + 1);
							String componentName = mofRefReplacement.substring(mofRefReplacement.lastIndexOf(".") + 1);
							ComponentInstance comp_temp = returnComponentByName(componentName, componentList);
							ComponentInstance comp_del = returnComponentByName(srcComponentName, componentList);
							//System.out.println("add:"+comp_temp.getName());
							//System.out.println("del:"+comp_del.getName());
							
							system_recusif.getComponentDeclaration().add(comp_temp);
							system_recusif.getComponentDeclaration().remove(comp_del);
							
							//search property
							if (comp_temp.getProperty() != null) {
								for (int j = 0; j < comp_temp.getProperty().size(); j++) {
									Property proper = comp_temp.getProperty().get(j);
									VariationPoint vpT = returnVPByAttribute(proper.getName().toString(), vpList);
									VSpec vsp = returnVSpecByVP(vpT, vSpecList);
									VSpecResolution vspR = returnVSpecResolutionByVSpec(vsp, vSpecResolutionList);
									if (vspR instanceof VariableValueAssignment) {
										String value = ((VariableValueAssignment)vspR).getValue();
										proper.setVal(value);
										comp_temp.getProperty().add(proper);
									}
									 
								}
							}
							//modify attachment
							for (int j = 0; j < attachmentList.size(); j++) {
								Attachment attach = attachmentList.get(j);
								String componentAttachName = attach.getComp();
								if (componentAttachName.equals(comp_del.getName())) attachmentList.get(j).setComp(comp_temp.getName());
							}
							
							//modify binding
							for (int j = 0; j < bindingList.size(); j++) {
								Binding binding = bindingList.get(j);
								String componentDest = binding.getCompDest();
								String componentSrc = binding.getCompSrc();
								if (componentDest.equals(comp_del.getName())) bindingList.get(j).setCompDest(comp_temp.getName());
								if (componentSrc.equals(comp_del.getName())) bindingList.get(j).setCompSrc(comp_temp.getName());
									
							}
						}
					}
				} 
			}
		}
		
		//add connector
		system_recusif.getConnectorDeclaration().addAll(connectorList);
		//add attachements
		for (int j = 0; j < attachmentList.size(); j++) {
			Attachment attach = attachmentList.get(j);
			String componentAttachName = attach.getComp();
			for (int k = 0; k < system_recusif.getComponentDeclaration().size(); k++) {
				if (system_recusif.getComponentDeclaration().get(k).getName().equals(componentAttachName)) {
				
					system_recusif.getAttachement().add(attach);
				}
			}
		}
		
		//add binding
		for (int j = 0; j < bindingList.size(); j++) {
			Binding binding = bindingList.get(j);
			String componentDest = binding.getCompDest();
			String componentSrc = binding.getCompSrc();
			boolean chk1 = false, chk2 = false;
			
			
			for (int k = 0; k < system_recusif.getComponentDeclaration().size(); k++) {
					if ((system_recusif.getComponentDeclaration().get(k).getName().equals(componentDest)) ||
						(component.getName().equals(componentDest))){
					chk1 = true;;
				}
				if ((system_recusif.getComponentDeclaration().get(k).getName().equals(componentSrc)) ||
						(component.getName().equals(componentSrc)) ){
					chk2 = true;
				}
			}
			Choice vsp1 = (Choice)returnVSpecByComponentName(componentSrc, vpList, vSpecList);
			Choice vsp2 = (Choice)returnVSpecByComponentName(componentDest, vpList, vSpecList);
			ChoiceResolution vspr1 = (ChoiceResolution)returnVSpecResolutionByComponentName(componentSrc, vpList, vSpecResolutionList);
			ChoiceResolution vspr2 = (ChoiceResolution)returnVSpecResolutionByComponentName(componentDest, vpList, vSpecResolutionList);
			boolean decision1 = false, decision2 = false;
			if ( vspr1 != null) decision1 = vspr1.isDecision(); 
			else if (vsp1 != null) decision1 = vsp1.isDefaultResolution(); 
			if ( vspr2 != null) decision2 = vspr2.isDecision(); 
			else if (vsp2 != null) decision2 = vsp2.isDefaultResolution();

			//System.out.println(chk1 +""+ chk2 +""+ decision1+""+ decision2);
			if (chk1 && chk2 && decision1 && decision2) {
				system_recusif.getAttachement().add(binding);
				
			}
		}
		
	}
	public static void main(String args[]) {
		
		VariabilityModelGUI variabilityModel = new VariabilityModelGUI("model//acme//model.cvl");
		ResolutionModelGUI resolutionModel = new ResolutionModelGUI( "model//acme//resolution.cvl"); 
		
		ACMEImpl baseModel = new ACMEImpl();
		ACME.System sys = baseModel.getACMESystem("model//acme//base.acme");
		
		ACMEGeneration acm = new ACMEGeneration(variabilityModel.getVSpecList(), 
				variabilityModel.getVariationPointList(), 
				resolutionModel.getVSpecResolutionList(), 
				baseModel.getParentComponentList(sys), 
				baseModel.getParentConnectorList(sys),
				baseModel.getParentAttchmentList(sys), 
				baseModel.getParentBindingList(sys));
		acm.create("model//acme//gen.acme", sys.getName());
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
			//VSpec parent = (VSpec)vsp.eContainer();
			vspr = returnVSpecResolutionByVSpec(vsp, resolutionList);
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
	public VSpec returnVSpecByComponentName(String componentName, ArrayList<VariationPoint> vpList, ArrayList<VSpec> vSpecList) {
		VSpec vSpec = null;
		VariationPoint vp = returnVPByComponent(componentName, vpList);
		if (vp != null) vSpec = returnVSpecByVP(vp, vSpecList);
		return vSpec;
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

	public ComponentInstance returnComponentByName(String name, ArrayList<ComponentInstance> sourceComponents) {
		ArrayList<ComponentInstance> listTemp = new ArrayList<ComponentInstance>(sourceComponents);
		ComponentInstance component = null;
		for (int i = 0; i < listTemp.size(); i++) {
			if (listTemp.get(i).getName().equals(name)) {
				return listTemp.get(i);
			}
		} 
		return component;
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
	public void createACME(ACME.ComponentInstance component,
			ArrayList<VSpec> vSpecList,
			ArrayList<VariationPoint> vpList,
			ArrayList<VSpecResolution> vSpecResolutionList,
			ArrayList<ComponentInstance> componentList,
			ArrayList<Connector> connectorList,
			ArrayList<Attachment> attachmentList,
			ArrayList<Binding> bindingList) {
		//begin procedure
		ACME.Representation representation = acmeFactory.createRepresentation();
		ACME.System system = acmeFactory.createSystem(); 
		
		for (int i = 0; i < componentList.size(); i++) {
			VSpec vSpec = returnVSpecByComponentName(componentList.get(i).getName(), vpList, vSpecList);
			VariationPoint vp = returnVPByComponent(componentList.get(i).getName(), vpList);
			if (vSpec instanceof Choice) {
				Choice choice = (Choice) vSpec;
				VSpecResolution vSpecResolution = returnVSpecResolutionByComponentName(componentList.get(i).getName(), vpList, vSpecResolutionList);
				boolean decision = false;
				if (vSpecResolution != null) {
					decision = ((ChoiceResolution)vSpecResolution).isDecision();
				} else if (choice.isDefaultResolution()) {
					decision = choice.isDefaultResolution();
				}
				if( decision || 
						 (vSpec.getAvailabilityTime().toString().equals("runtime"))) {
					if (vp instanceof ObjectExistence) {
						ComponentInstance comp = componentList.get(i);
						ComponentInstance comp_temp = acmeFactory.createComponentInstance();
						comp_temp = comp;
						
						System.out.println(comp.getName());
						
						if (comp.getRepresentations() != null) {
							
							for (int j = 0; j < comp.getRepresentations().size(); j++) {
								EList<ACME.System> subSystemList = comp.getRepresentations().get(j).getSystems();
								//Representation representation_temp = acmeFactory.createRepresentation();
								if (subSystemList != null) {
									for (int k = 0; k < subSystemList.size(); k++) {
										ACME.System systemSub = subSystemList.get(k);
										ArrayList<ACME.ComponentInstance> list = new ArrayList<ACME.ComponentInstance>(); 
										
										for (int l = 0; l < systemSub.getComponentDeclaration().size(); l++) {
											list.add(systemSub.getComponentDeclaration().get(l));
										}
										
										ArrayList<ACME.Connector> connectorList_Temp = new ArrayList<ACME.Connector>(); 
										for (int l = 0; l < systemSub.getConnectorDeclaration().size(); l++) {
											connectorList_Temp.add(systemSub.getConnectorDeclaration().get(l));
										}
										
										ArrayList<ACME.Attachment> attachmentList_Temp = new ArrayList<ACME.Attachment>(); 
										for (int l = 0; l < systemSub.getAttachement().size(); l++) {
											if (systemSub.getAttachement().get(l) instanceof Attachment) {
												attachmentList_Temp.add((Attachment) systemSub.getAttachement().get(l));
											}
										}
										
										ArrayList<ACME.Binding> bindingList_Temp = new ArrayList<ACME.Binding>(); 
										for (int l = 0; l < systemSub.getAttachement().size(); l++) {
											if (systemSub.getAttachement().get(l) instanceof Binding) {
												bindingList_Temp.add((Binding) systemSub.getAttachement().get(l));
											}
										}
										
										comp_temp.getRepresentations().removeAll(comp.getRepresentations());
										createACME(comp_temp, 
												vSpecList, 
												vpList, 
												vSpecResolutionList, 
												list, 
												connectorList_Temp, 
												attachmentList_Temp, 
												bindingList_Temp);
									}
								}
								
 							}
						} 
						//add attribute
						if (comp.getProperty() != null) {
							for (int j = 0; j < comp.getProperty().size(); j++) {
								Property proper = comp.getProperty().get(j);
								VariationPoint vpT = returnVPByAttribute(proper.getName().toString(), vpList);
								VSpec vsp = returnVSpecByVP(vpT, vSpecList);
								VSpecResolution vspR = returnVSpecResolutionByVSpec(vsp, vSpecResolutionList);
								if (vspR instanceof VariableValueAssignment) {
									String value = ((VariableValueAssignment)vspR).getValue();
									proper.setVal(value);
									comp_temp.getProperty().add(proper);
								}
								 
							}
						}
						system.getComponentDeclaration().add(comp_temp);
						representation.getSystems().add(system);
						component.getRepresentations().add(representation);
						//comp_temp.getRepresentations().removeAll(comp.getRepresentations());
						//comp_temp.getRepresentations().add(representation);
						//component.getRepresentations().add(comp_temp);
					} else if (vp instanceof ObjectSubstitution) {
						VSpec parent = (VSpec)vSpec.eContainer();
						VSpecResolution vspr = returnVSpecResolutionByVSpec(parent,vSpecResolutionList);
						if (((ChoiceResolution)vspr).isDecision() || ((Choice)parent).isDefaultResolution()) {
							ObjectSubstitution vpSubT = (ObjectSubstitution)vp;
							String mofRefPlacement = vpSubT.getPlacementObject().getMOFRef();
							String mofRefReplacement = vpSubT.getReplacementObject().getMOFRef();
							String srcComponentName = mofRefPlacement.substring(mofRefPlacement.lastIndexOf(".") + 1);
							String componentName = mofRefReplacement.substring(mofRefReplacement.lastIndexOf(".") + 1);
							ComponentInstance comp_temp = returnComponentByName(componentName, componentList);
							ComponentInstance comp_del = returnComponentByName(srcComponentName, componentList);
				
							
							if (comp_temp != null) component.getRepresentations().get(0).getSystems().get(0).getComponentDeclaration().add(comp_temp);
							if (comp_del != null) component.getRepresentations().get(0).getSystems().get(0).getComponentDeclaration().remove(comp_del);
							
							//search property
							if((comp_temp != null) && (comp_temp.getProperty() != null)) {
								for (int j = 0; j < comp_temp.getProperty().size(); j++) {
									Property proper = comp_temp.getProperty().get(j);
									VariationPoint vpT = returnVPByAttribute(proper.getName().toString(), vpList);
									VSpec vsp = returnVSpecByVP(vpT, vSpecList);
									VSpecResolution vspR = returnVSpecResolutionByVSpec(vsp, vSpecResolutionList);
									if (vspR instanceof VariableValueAssignment) {
										String value = ((VariableValueAssignment)vspR).getValue();
										proper.setVal(value);
										comp_temp.getProperty().add(proper);
									}
									 
								}
							}
							//modify attachment
							for (int j = 0; j < attachmentList.size(); j++) {
								Attachment attach = attachmentList.get(j);
								String componentAttachName = attach.getComp();
								if (componentAttachName.equals(comp_del.getName())) attachmentList.get(j).setComp(comp_temp.getName());
							}
							
							//modify binding
							for (int j = 0; j < bindingList.size(); j++) {
								Binding binding = bindingList.get(j);
								String componentDest = binding.getCompDest();
								String componentSrc = binding.getCompSrc();
								if (componentDest.equals(comp_del.getName())) bindingList.get(j).setCompDest(comp_temp.getName());
								if (componentSrc.equals(comp_del.getName())) bindingList.get(j).setCompSrc(comp_temp.getName());
									
							}
						}
					}
				} 
			}
		}
		
		//add connector
		component.getRepresentations().get(0).getSystems().get(0).getConnectorDeclaration().addAll(connectorList);
		//add attachements
		for (int i = 0; i < attachmentList.size(); i++) {
			Attachment attach = attachmentList.get(i);
			String componentAttachName = attach.getComp();
			for (int j = 0; j < component.getRepresentations().get(0).getSystems().get(0).getComponentDeclaration().size(); j++) {
				if (component.getRepresentations().get(0).getSystems().get(0).getComponentDeclaration().get(j).getName().equals(componentAttachName)) {
					component.getRepresentations().get(0).getSystems().get(0).getAttachement().add(attach);
				}
			}
		}
		
		//add binding
		for (int i = 0; i < bindingList.size(); i++) {
			Binding binding = bindingList.get(i);
			String componentDest = binding.getCompDest();
			String componentSrc = binding.getCompSrc();
			boolean chk1 = false, chk2 = false;
			
			ArrayList<ComponentInstance> compI = new ArrayList<ComponentInstance>();
			compI.addAll(component.getRepresentations().get(0).getSystems().get(0).getComponentDeclaration());
			for (int j = 0; j < compI.size(); j++) {
				if ((compI.get(j).getName().equals(componentDest)) ||
						(component.getName().equals(componentDest))){
					chk1 = true;
				}
				if ((compI.get(j).getName().equals(componentSrc)) ||
						(component.getName().equals(componentSrc)) ){
					chk2 = true;
				}
			}
			Choice vsp1 = (Choice)returnVSpecByComponentName(componentSrc, vpList, vSpecList);
			Choice vsp2 = (Choice)returnVSpecByComponentName(componentDest, vpList, vSpecList);
			ChoiceResolution vspr1 = (ChoiceResolution)returnVSpecResolutionByComponentName(componentSrc, vpList, vSpecResolutionList);
			ChoiceResolution vspr2 = (ChoiceResolution)returnVSpecResolutionByComponentName(componentDest, vpList, vSpecResolutionList);
			boolean decision1 = false, decision2 = false;
			if ( vspr1 != null) decision1 = vspr1.isDecision(); 
			else if (vsp1 != null) decision1 = vsp1.isDefaultResolution(); 
			if ( vspr2 != null) decision2 = vspr2.isDecision(); 
			else if (vsp2 != null) decision2 = vsp2.isDefaultResolution();
			
			if (chk1 && chk2 && decision1 && decision2) {
				component.getRepresentations().get(0).getSystems().get(0).getAttachement().add(binding);
			}
		}
	}
}
