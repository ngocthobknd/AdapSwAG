package generation;

import java.util.ArrayList;
import java.util.List;

import org.ow2.fractal.f4e.fractal.Binding;
import org.ow2.fractal.f4e.fractal.Component;
import org.ow2.fractal.f4e.fractal.Definition;

import cvl.VSpec;
import cvl.VSpecResolution;
import cvl.VariationPoint;

public interface ProductGenerationService {
	
	public boolean addComponents1(Definition destinationDefinition, 
			List<Component> sourceComponentList, 
			ArrayList<VSpec> vSpecList,	
			ArrayList<VariationPoint> vpList, 
			ArrayList<VSpecResolution> resolutionList);
	public boolean addBinding(Definition destinationDefinition, 
			ArrayList<Component> destinationComponentList, 
			List<Binding> bindingList,
			ArrayList<VariationPoint> vpList,
			ArrayList<VSpecResolution> resolutionList);
}
