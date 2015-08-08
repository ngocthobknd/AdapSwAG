package resolution.api;

import java.util.ArrayList;

import cvl.VSpecResolution;

public interface ResolutionModelService {
	public VSpecResolution getVSpecResolutionRoot(String resolutionModelFileName);
	public ArrayList<VSpecResolution> getVSPecResolutionList(VSpecResolution vSpecResolutionRoot);
		
}
