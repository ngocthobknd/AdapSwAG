package variationpoint.api;

import java.util.ArrayList;

import cvl.VPackage;
import cvl.VariationPoint;

public interface VariationPointService {
	public VPackage getVPackage(String vpFileName);
	public ArrayList<VariationPoint> getVariationPointList(VPackage vPackage);
}
