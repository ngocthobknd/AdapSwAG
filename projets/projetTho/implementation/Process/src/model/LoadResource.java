package model;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import cvl.cvlPackage;

public class LoadResource {
	Resource resource;
	public LoadResource() {
		cvlPackage.eINSTANCE.eClass();
		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
		try {
			//registry extent part of model file ex: *.variability
			reg.getExtensionToFactoryMap().put("cvl", new XMIResourceFactoryImpl());
		} catch (Exception e){
		}
		ResourceSet resourceSet = new ResourceSetImpl();
		URI uri = URI.createFileURI("model//model.cvl");
		resource = resourceSet.getResource(uri, true);
		EcoreUtil.resolveAll(resourceSet);
	}
}