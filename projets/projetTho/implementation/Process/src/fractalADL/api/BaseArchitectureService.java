package fractalADL.api;

import java.util.ArrayList;

import org.ow2.fractal.f4e.fractal.Binding;
import org.ow2.fractal.f4e.fractal.Component;
import org.ow2.fractal.f4e.fractal.Definition;

public interface BaseArchitectureService {
	public Definition getArchitectureDefinition(String file);
	public ArrayList<Component> getComponentList(Definition definition);
	public ArrayList<Binding> getBindingList(Definition definition);
	public ArrayList<Component> getParentComponentList(Definition definition);
	public ArrayList<Binding> getParentBindingList(Definition definition);
}
