package transformation;

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
import org.ow2.fractal.f4e.fractal.Component;
import org.ow2.fractal.f4e.fractal.Definition;
import org.ow2.fractal.f4e.fractal.FractalFactory;
import org.ow2.fractal.f4e.fractal.FractalPackage;

import tree.ResolutionTree;
import tree.VSpecTree;
import cvl.ObjectExistence;
import cvl.ObjectHandle;
import cvl.ObjectSubstitution;
import cvl.VSpec;
import cvl.VariationPoint;

public class GeneratingProductArchitecture {
	 
	
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
	
	public void createFractalModel(Definition sourceDefinitionFractal) {
		/*
		 * 
		 */
		FractalPackage.eINSTANCE.eClass();
		FractalFactory fractalFractory = FractalFactory.eINSTANCE;
		Definition destinationDefinition = fractalFractory.createDefinition();
		destinationDefinition.setName("product"+sourceDefinitionFractal.getName());

		ResolutionTree rs = new ResolutionTree();
		VSpecTree vSpec = new VSpecTree();
		
		ArrayList<String> decisionList = rs.decisionList;
		ArrayList<VSpec> vSpecList = vSpec.vSpecList;
		ArrayList<VariationPoint> vpList = vSpec.VPList;

		
		for (int i = 0; i < decisionList.size(); i++) {
			if ((decisionList.get(i)).equals("false") && (! vSpecList.get(i).getAvailabilityTime().getName().equals("runtime"))) {
				//System.out.println("remove"+vSpecList.get(i));
				decisionList.remove(i);
				vSpecList.remove(i);
				//vpList.remove(returnVP(vSpecList.get(i), vpList));
			}
		}
		
		EList<Component> sourceComponentList = sourceDefinitionFractal.getSubComponents();
		Component destinationCompnent = fractalFractory.createComponent();

		
		for (int i = 0; i < decisionList.size(); i++) {
			VariationPoint vp = returnVP(vSpecList.get(i).getName(), vpList);
			
			String componentName = "";
			if (vp != null) System.out.println(vp.getName());
			if (vp instanceof ObjectExistence) {
				ObjectHandle oObject = ((ObjectExistence) vp).getOptionalObject();
				componentName = oObject.getMOFRef().split("\\.")[1];
			} 
	
			else if (vp instanceof ObjectSubstitution) {
				ObjectHandle oPlacementObject = ((ObjectSubstitution) vp).getPlacementObject();
				ObjectHandle oReplacementObject = ((ObjectSubstitution) vp).getReplacementObject();
				componentName = oReplacementObject.getMOFRef().split("\\.")[1];
			}

			destinationCompnent = returnCompponent(componentName, sourceComponentList);
			/*
			 * set value of attribute of component
			 */
			try{
				if (!destinationCompnent.getAttribute().isEmpty()) {
					System.out.println(destinationCompnent.getAttribute() .get(0));
					//destinationCompnent.getAttribute() .get(0).setValue("123");
					VariationPoint vpVariable = returnVP(destinationCompnent.getAttribute().get(0).getName(), vpList);
					int indexOfVSpec = returnIndexofVSpec(vpVariable, vSpecList);
					destinationCompnent.getAttribute() .get(0).setValue(decisionList.get(indexOfVSpec));
				}
			} catch(Exception e) {
			}
			if (destinationCompnent != null)
			destinationDefinition.getSubComponents().add(destinationCompnent);
			
			
			/*
			 * TODO: add connection between components - binding
			 */
		}

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
	public Component returnCompponent(String name, EList<Component> sourceComponentList) {
		Component comp = null;
		for (int i=0; i <sourceComponentList.size(); i++) {
			if (sourceComponentList.get(i).getName().equals(name)) return sourceComponentList.get(i);
		} 
		return comp;
	}
	
	public VariationPoint returnVP(String vSpec, ArrayList<VariationPoint> VPList) {
		VariationPoint vp = null;
		for (int i = 0; i < VPList.size(); i++) {
			if (VPList.get(i).getBindingVSpec().getName().equals(vSpec)) {
				return VPList.get(i);
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
		new GeneratingProductArchitecture().createFractalModel(readArchitecture(""));
	}

}
