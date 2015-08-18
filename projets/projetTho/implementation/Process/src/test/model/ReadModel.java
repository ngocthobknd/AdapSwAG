//package test.model;
//import org.eclipse.emf.common.util.EList;
//import org.eclipse.emf.common.util.URI;
//import org.eclipse.emf.ecore.resource.Resource;
//import org.eclipse.emf.ecore.resource.ResourceSet;
//import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
//import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
//import cvl.*;
//
//import eu.telecombretagne.variability.Association;
//import eu.telecombretagne.variability.DynamicVariationPoint;
//import eu.telecombretagne.variability.Variant;
//import eu.telecombretagne.variability.VariationPoint;
//import eu.telecombretagne.variability.Variability;
//import eu.telecombretagne.variability.VariabilityPackage;
//
//public class ReadModel {
//	private Resource resource;
//	public Variability variability;
//	@SuppressWarnings("unchecked")
//	public ReadModel(String fileInput) {
//		VariabilityPackage.eINSTANCE.eClass();
//		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
//		try {
//			//registry extent part of model file ex: *.variability
//			reg.getExtensionToFactoryMap().put("variability", new XMIResourceFactoryImpl());
//		} catch (Exception e){
//			
//		}
//		ResourceSet resourceSet = new ResourceSetImpl();
//		URI uri = URI.createFileURI(fileInput);
//		resource = resourceSet.getResource(uri, true);
//		//get root of variability model 
//		variability = (Variability) resource.getContents().get(0);
//		// get list variation points
//		EList <VariationPoint> variationPoints = variability.getVariationPoints();
//		System.out.println("in ReadModel.java");
//		for (VariationPoint vp : variationPoints) {
//			System.out.println(vp.getName());
//			if (vp instanceof DynamicVariationPoint) {
//				System.out.println("---dynamic");
//			}
//			EList <Association> associations = vp.getAssociationType();
//			for (Association ass : associations) {
//				// get variant of variation point
//				EList <Variant> variants = ass.getVariants();
//				for (Variant v : variants) {
//					System.out.println("--"+v.getName());
//				}
//			}
//		}
//	}
//	
//	public static void main(String arg[]) {
//		ReadModel rm = new ReadModel("My.variability");
//	}
//
//}
