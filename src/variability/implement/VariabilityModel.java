package variability.implement;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import variability.api.VariabilityModelService;
import cvl.VPackage;
import cvl.VPackageable;
import cvl.VSpec;
import cvl.VariationPoint;
import cvl.cvlPackage;

public class VariabilityModel implements VariabilityModelService {

	public VariabilityModel() {
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
	public ArrayList<VariationPoint> getVariationPointList(VPackage vPackage) {
		ArrayList<VariationPoint> vpList = new ArrayList<VariationPoint>();
		EList<VPackageable> packageElement = vPackage.getPackageElement();
		for (int i = 1; i < packageElement.size(); i++) {
			if (packageElement.get(i) instanceof VariationPoint) {
				VariationPoint vpT = (VariationPoint)packageElement.get(i);
				vpList.add(vpT);
			}
			
		}
		return vpList;
	}
	public static void main(String arg[]) {
		VariabilityModelService vm = new VariabilityModel();
		VPackage vpk = vm.getVPackage("model//composite2//model.cvl");
		ArrayList<VSpec> vsplist = vm.getVSpecList(vpk);
		for (int i = 0; i < vsplist.size(); i++) {
			System.out.println(vsplist.get(i).getName());
		}
	}
	

}
