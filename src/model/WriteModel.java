package model;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceFactoryImpl;

import eu.telecombretagne.variability.Association;
import eu.telecombretagne.variability.DynamicVariationPoint;
import eu.telecombretagne.variability.StaticVariationPoint;
import eu.telecombretagne.variability.Variability;
import eu.telecombretagne.variability.VariabilityFactory;
import eu.telecombretagne.variability.VariabilityPackage;
import eu.telecombretagne.variability.Variant;
import eu.telecombretagne.variability.VariationPoint;
import eu.telecombretagne.variability.impl.VariabilityFactoryImpl;
import eu.telecombretagne.variability.impl.VariationPointImpl;
import eu.telecombretagne.variability.provider.VariationPointItemProvider;
import eu.telecombretagne.variability.util.VariabilityAdapterFactory;
import utils.*;
public class WriteModel {
	public Resource resource;
	public Variability variability;

	@SuppressWarnings({ "unchecked", "rawtypes", "rawtypes" })
	public WriteModel(String inputFile, String outputFile, List selectedVariants){
	
		VariabilityPackage.eINSTANCE.eClass();
		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
		try {
			//registry extent part of model file ex: *.variability
			reg.getExtensionToFactoryMap().put("variability", new XMIResourceFactoryImpl());
		} catch (Exception e){		
		}
		ResourceSet resourceSet = new ResourceSetImpl();
		// path to instance model file
		URI uri = URI.createFileURI(inputFile);
		resource = resourceSet.getResource(uri, true);
		
		variability = (Variability) resource.getContents().get(0);
		
		
		// get list variation points
		EList <VariationPoint> variationPoints = variability.getVariationPoints();
		
		VariabilityFactory variabilityCoreFactory = VariabilityFactory.eINSTANCE;

		
		VariabilityPackage variabilityEPackage = variabilityCoreFactory.getVariabilityPackage();
		
		Variability variabilityObject = VariabilityFactory.eINSTANCE.createVariability();
		
		EReference variabilityReference =  variabilityEPackage.getVariability_VariationPoints();
		
				
		for (VariationPoint vp : variationPoints) {
			VariationPoint variationPointObject = VariabilityFactory.eINSTANCE.createVariationPoint();
			VariationPoint dynamicVariationPointObject = VariabilityFactory.eINSTANCE.createDynamicVariationPoint();
			VariationPoint staticVariationPointObject = VariabilityFactory.eINSTANCE.createStaticVariationPoint();
			
			EReference variationPointReference = variabilityEPackage.getVariationPoint_AssociationType();
			if (vp instanceof DynamicVariationPoint) {
				((List) variabilityObject.eGet(variabilityReference)).add(dynamicVariationPointObject);
				dynamicVariationPointObject.setName(vp.getName());
				dynamicVariationPointObject.setRationale(vp.getRationale());
			}
			else if (vp instanceof StaticVariationPoint) {
				((List) variabilityObject.eGet(variabilityReference)).add(staticVariationPointObject);
				staticVariationPointObject.setName(vp.getName());
				staticVariationPointObject.setRationale(vp.getRationale());
			}
				
			else {
				((List) variabilityObject.eGet(variabilityReference)).add(variationPointObject);
				variationPointObject.setName(vp.getName());
				variationPointObject.setRationale(vp.getRationale());
			}
			
			
			
			
			//variationPointObject.setName(vp.getName());
			
			EList <Association> associations = vp.getAssociationType();
			for (Association ass : associations) {
				String str = ass.eClass().getName();
				Association associationObject;
				Dependances depen = Dependances.valueOf(str);
				switch (depen) {
					case Optional:
						 associationObject = VariabilityFactory.eINSTANCE.createOptional();
						break;
					case Mandatory:
						associationObject = VariabilityFactory.eINSTANCE.createMandatory();
						break;
					case Alternative:
						associationObject = VariabilityFactory.eINSTANCE.createAlternative();
						break;
					default:  
						associationObject = VariabilityFactory.eINSTANCE.createAssociation();
						break;
				}
					
				if (vp instanceof DynamicVariationPoint) {
					((List) dynamicVariationPointObject.eGet(variationPointReference)).add(associationObject);
				} else if (vp instanceof StaticVariationPoint) {
					((List) staticVariationPointObject.eGet(variationPointReference)).add(associationObject);
				} else
					((List) variationPointObject.eGet(variationPointReference)).add(associationObject);
				
				
				System.out.println(ass.eClass().getName());
				
				EReference associationReference = variabilityEPackage.getAssociation_Variants();
				EList <Variant> variants = ass.getVariants();
				for (Variant v : variants) {
					Variant variantObject = VariabilityFactory.eINSTANCE.createVariant();
					((List) associationObject.eGet(associationReference)).add(variantObject);
					if (StringHandle.checkStringInList(v.getName(), selectedVariants)) {
						variantObject.setName(v.getName());
						System.out.println("--"+v.getName());
					}
				}
			}
		}
		
		
		/*
		* Read/Get the values of bookObject attributes
		*/
		ResourceSet newResourceSet = new ResourceSetImpl();
		newResourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(
		    "*", new  XMLResourceFactoryImpl());

		Resource newResource = newResourceSet.createResource(URI.createURI(outputFile));
		newResource.getContents().add(variabilityObject);

		try{
		    /*
		    * Save the resource
		    */
			newResource.save(null);
		   }catch (IOException e) {
		      e.printStackTrace();
		   }
		/*
		* Save the resource using OPTION_SCHEMA_LOCATION save option toproduce 
		* xsi:schemaLocation attribute in the document
		*/
		
		
		
		
	}
	public enum Dependances {

		Optional,
		Mandatory,
		Alternative
	  }
	
	public static void main (String arg[]){
		List<String> strArray= new ArrayList<String>();
		String a="RMI";
		strArray.add(a);
		
		
		WriteModel tb = new WriteModel("My.variability","library.xmi", strArray);
	}

}
