package vspectree.implement;
import java.io.File;
import java.util.ArrayList;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import cvl.VPackage;
import cvl.VPackageable;
import cvl.VSpec;
import cvl.cvlPackage;
import vspectree.api.*;
public class VSpecTreeImpl implements VSpecTreeService{

	public VSpecTreeImpl() {
		// TODO Auto-generated constructor stub
	}

	public VPackage getVPackage(String variabilityModelFileName) {
		VPackage vPackage = null;
		cvlPackage.eINSTANCE.eClass();
		Resource resource;
		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
		try {
			reg.getExtensionToFactoryMap().put("cvl", new XMIResourceFactoryImpl());
		} catch (Exception e){
		}
		ResourceSet resourceSet = new ResourceSetImpl();
		String filename = new File(variabilityModelFileName).getAbsolutePath();
		URI uri = URI.createFileURI(filename);
		resource = resourceSet.getResource(uri, true);
		//get root of variability model 
		vPackage = (VPackage) resource.getContents().get(0);
		return vPackage;
	}
	public VSpec getVSpecTreeRoot(VPackage vPackage) {
		EList<VPackageable> packageElement = vPackage.getPackageElement();
		VSpec vSpec = (VSpec) packageElement.get(0);
		return vSpec;
	}
	public ArrayList<VSpec> getChildVSpecList(VSpec vSpec) {
		ArrayList<VSpec> vSpecList = new ArrayList<VSpec>();
		vSpecList.add(vSpec);
		for (int i = 0; i < vSpec.getChild().size(); i++) {
			vSpecList.addAll(getChildVSpecList(vSpec.getChild().get(i)));
		}
		return vSpecList;
	}
	public ArrayList<VSpec> getVSpecList(VPackage vPackage) {
		ArrayList<VSpec> vSpecList = new ArrayList<VSpec>();
		EList<VPackageable> packageElement = vPackage.getPackageElement();
		VSpec vSpec = (VSpec) packageElement.get(0);
		vSpecList = getChildVSpecList(vSpec);
		
		return vSpecList;
	}
}
