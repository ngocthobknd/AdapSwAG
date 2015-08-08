package variability.api;

import java.util.ArrayList;

import cvl.VPackage;
import cvl.VSpec;
import cvl.VariationPoint;

public interface VariabilityModelService {
	public VPackage getVPackage(String variabilityModelFileName);
	public VSpec getVSpecTreeRoot(VPackage vPackage);
	public ArrayList<VSpec> getVSpecList(VPackage vPackage);
	public ArrayList<VariationPoint> getVariationPointList(VPackage vPackage);
}
