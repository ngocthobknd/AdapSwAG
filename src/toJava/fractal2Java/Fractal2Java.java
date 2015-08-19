package toJava.fractal2Java;
import java.io.FileNotFoundException;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.mwe.core.resources.ResourceLoaderFactory;
import org.eclipse.xpand2.XpandExecutionContextImpl;
import org.eclipse.xpand2.XpandFacade;
import org.eclipse.xpand2.output.Outlet;
import org.eclipse.xpand2.output.Output;
import org.eclipse.xpand2.output.OutputImpl;
import org.eclipse.xtend.typesystem.emf.EmfRegistryMetaModel;
import org.ow2.fractal.f4e.fractal.Definition;
import org.ow2.fractal.f4e.fractal.FractalPackage;
import org.eclipse.emf.mwe.utils.DirectoryCleaner;
public class Fractal2Java {

	public Fractal2Java(String file) {
		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
	    Map<String, Object> factoryMap = reg.getExtensionToFactoryMap();
	    factoryMap.put(Resource.Factory.Registry.DEFAULT_EXTENSION, new XMIResourceFactoryImpl());

	    ResourceSet resourceSet = new ResourceSetImpl();
	    EPackage fractalPackage = FractalPackage.eINSTANCE;
	    resourceSet.getPackageRegistry().put(fractalPackage.getNsURI(), fractalPackage);
	    
	    URI uri = URI.createFileURI(file);
	    Resource resource = resourceSet.getResource(uri, true);
	    Definition definition = (Definition)resource.getContents().get(0);
        

	    //System.out.println(definition.getName());
        //Xpand
        Output out = new OutputImpl();
        out.addOutlet(new Outlet("src-gen"));
        
        //clean src-gen
        DirectoryCleaner clean = new DirectoryCleaner();
        try {
			clean.cleanFolder("src-gen");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
        String templatePath = "toJava::fractal2Java::template::Template::definition";
        XpandExecutionContextImpl execCtx = new XpandExecutionContextImpl(out, null);
        execCtx.registerMetaModel(new EmfRegistryMetaModel());
        XpandFacade facade = XpandFacade.create(execCtx);
        facade.evaluate(templatePath, definition);
	    
        System.out.println("End generate code");
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Fractal2Java("model//primitive//generatedarchitecture.fractal");
	}

}
