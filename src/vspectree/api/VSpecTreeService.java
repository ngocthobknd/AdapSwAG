package vspectree.api;

import java.util.ArrayList;

import cvl.VPackage;
import cvl.VSpec;

public interface VSpecTreeService {
	public VPackage getVPackage(String variabilityModelFileName);
	public VSpec getVSpecTreeRoot(VPackage vPackage);
	public ArrayList<VSpec> getVSpecList(VPackage vPackage);
}
