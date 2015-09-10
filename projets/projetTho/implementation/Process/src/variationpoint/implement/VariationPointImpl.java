package variationpoint.implement;
import java.io.File;
import java.util.ArrayList;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import variationpoint.api.VariationPointService;
import cvl.VPackage;
import cvl.VPackageable;
import cvl.VSpec;
import cvl.cvlPackage;
import cvl.VariationPoint;
public class VariationPointImpl implements VariationPointService {

	public VariationPointImpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public VPackage getVPackage(String vpFileName) {
		// TODO Auto-generated method stub
		VPackage vPackage = null;
		cvlPackage.eINSTANCE.eClass();
		Resource resource;
		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
		try {
			reg.getExtensionToFactoryMap().put("cvl", new XMIResourceFactoryImpl());
		} catch (Exception e){
		}
		ResourceSet resourceSet = new ResourceSetImpl();
		String filename = new File(vpFileName).getAbsolutePath();
		URI uri = URI.createFileURI(filename);
		resource = resourceSet.getResource(uri, true);
		//get root of variability model 
		vPackage = (VPackage) resource.getContents().get(0);
		return vPackage;
	}

	@Override
	public ArrayList<VariationPoint> getVariationPointList(VPackage vPackage) {
		ArrayList<VariationPoint> vpList = new ArrayList<cvl.VariationPoint>();
		EList<VPackageable> packageElement = vPackage.getPackageElement();
		for (int i = 0; i < packageElement.size(); i++) {
			if (packageElement.get(i) instanceof VariationPoint) {
				VariationPoint vpT = (VariationPoint)packageElement.get(i);
				vpList.add(vpT);
			}
		}
		return vpList;
	}
	public static void main(String arg[]) {
		VariationPointImpl vm = new VariationPointImpl();
		VPackage vpk = vm.getVPackage("model//fractal//variationpoint2.cvl");
		ArrayList<VariationPoint> vsplist = vm.getVariationPointList(vpk);
		for (int i = 0; i < vsplist.size(); i++) {
			System.out.println(vsplist.get(i).getName());
			VSpec svp = vsplist.get(i).getBindingVSpec();
			System.out.println(svp.getName());
		}
	}
}
