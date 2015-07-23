package transformation;

import java.util.ArrayList;
import java.util.List;

import org.ow2.fractal.f4e.fractal.Binding;
import org.ow2.fractal.f4e.fractal.Component;
import org.ow2.fractal.f4e.fractal.Definition;
import org.ow2.fractal.f4e.fractal.RealizationComponent;

import cvl.VSpec;
import cvl.VSpecResolution;
import cvl.VariationPoint;

public interface ProductGenerationService {
	
	public boolean addComponents1(Definition destinationDefinition, List<Component> sourceComponentList, 
			List<RealizationComponent> realizationComponentList, ArrayList<VSpec> vSpecList,	
			ArrayList<VariationPoint> vpList, ArrayList<VSpecResolution> resolutionList);
	void setAttribute(Definition destinationDefinition, ArrayList<VariationPoint> vpList, ArrayList<VSpecResolution> resolutionList );
	public boolean addBinding(Definition destinationDefinition, List<Binding> bindingList);
}
