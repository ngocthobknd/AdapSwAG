package test.model;
import org.eclipse.emf.common.util.EList;

import cvl.*;
public class ReadVSpecTree {
	public ReadVSpecTree() {

	}
	EList<VSpec> getChild(VSpec vSpec) {
		EList<VSpec> vSpecList = null;
		for (int i=0; i<vSpec.getChild().size(); i++) {
			vSpecList.add(vSpec.getChild().get(i));
		}
		return vSpecList;
	}
}
