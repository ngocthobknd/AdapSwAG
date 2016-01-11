package test.model;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceFactoryImpl;

public class WriteModel {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*
		* Instantiate EcoreFactory
		*/
		EcoreFactory theCoreFactory = EcoreFactory.eINSTANCE;

		/*
		* Create EClass instance to model BookStore class
		*/
		EClass bookStoreEClass = theCoreFactory.createEClass();
		bookStoreEClass.setName("BookStore");

		/*
		* Create EClass instance to model Book class
		*/
		EClass bookEClass = theCoreFactory.createEClass();
		bookEClass.setName("Book");

		/*
		* Instantiate EPackage and provide unique URI
		* to identify this package
		*/
		EPackage bookStoreEPackage = theCoreFactory.createEPackage();
		bookStoreEPackage.setName("BookStorePackage");
		bookStoreEPackage.setNsPrefix("bookStore");
		bookStoreEPackage.setNsURI("http:///com.ibm.dynamic.example.bookstore.ecore");
		/*
		* Instantiate EcorePackage
		*/
		EcorePackage theCorePackage = EcorePackage.eINSTANCE;

		/*
		* Create attributes for BookStore class as specified in the model
		*/
		EAttribute bookStoreOwner = theCoreFactory.createEAttribute();
		bookStoreOwner.setName("owner");
		bookStoreOwner.setEType(theCorePackage.getEString());
		EAttribute bookStoreLocation = theCoreFactory.createEAttribute();
		bookStoreLocation.setName("location");
		bookStoreLocation.setEType(theCorePackage.getEString());
		EReference bookStore_Books = theCoreFactory.createEReference();
		bookStore_Books.setName("books");
		bookStore_Books.setEType(bookEClass);
		bookStore_Books.setUpperBound(EStructuralFeature.UNBOUNDED_MULTIPLICITY);
		bookStore_Books.setContainment(true);

		/*
		* Create attributes for Book class as defined in the model
		*/
		EAttribute bookName = theCoreFactory.createEAttribute();
		bookName.setName("name");
		bookName.setEType(theCorePackage.getEString());
		EAttribute bookISBN = theCoreFactory.createEAttribute();
		bookISBN.setName("isbn");
		bookISBN.setEType(theCorePackage.getEInt());
		/*
		* Add owner, location and books attributes/references
		* to BookStore class
		*/
		bookStoreEClass.getEStructuralFeatures().add(bookStoreOwner);
		bookStoreEClass.getEStructuralFeatures().add(bookStoreLocation);
		bookStoreEClass.getEStructuralFeatures().add(bookStore_Books);

		/*
		* Add name and isbn attributes to Book class
		*/
		bookEClass.getEStructuralFeatures().add(bookName);
		bookEClass.getEStructuralFeatures().add(bookISBN);

		/*
		* Place BookStore and Book classes in bookStoreEPackage
		*/
		bookStoreEPackage.getEClassifiers().add(bookStoreEClass);
		bookStoreEPackage.getEClassifiers().add(bookEClass);
		
		/*
		* Obtain EFactory instance from BookStoreEPackage 
		*/
		EFactory bookFactoryInstance = bookStoreEPackage.getEFactoryInstance();

		/*
		* Create dynamic instance of BookStoreEClass and BookEClass
		*/
		EObject bookObject = bookFactoryInstance.create(bookEClass);
		EObject bookObject1 = bookFactoryInstance.create(bookEClass);
		EObject bookStoreObject = bookFactoryInstance.create(bookStoreEClass);
		/*
		* Set the values of bookStoreObject attributes
		*/
		bookStoreObject.eSet(bookStoreOwner, "David Brown");
		bookStoreObject.eSet(bookStoreLocation, "Street#12, Top Town, NY");
		
		((List) bookStoreObject.eGet(bookStore_Books)).add(bookObject);
		((List) bookStoreObject.eGet(bookStore_Books)).add(bookObject1);

		/*
		* Set the values of bookObject attributes
		*/
		bookObject.eSet(bookName, "Harry Potter and the Deathly Hallows");
		bookObject.eSet(bookISBN, 157221);

		bookObject1.eSet(bookName, "Harry Potter and the Deathly Hallows");
		bookObject1.eSet(bookISBN, 157221);
		/*
		* Read/Get the values of bookStoreObject attributes
		*/
		/*String strOwner =(String)bookStoreObject.eGet(bookStoreOwner);
		String strLocation =(String)bookStoreObject.eGet(bookStoreLocation);*/

		/*
		* Read/Get the values of bookObject attributes
		*/
		/*String strName =(String)bookObject.eGet(bookName);
		Object iISBN = bookObject.eGet(bookISBN);*/
		ResourceSet resourceSet = new ResourceSetImpl();
		/*
		* Register XML Factory implementation using DEFAULT_EXTENSION
		*/
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(
		    "*", new  XMLResourceFactoryImpl());

		/*
		* Create empty resource with the given URI
		*/
		Resource resource = resourceSet.createResource(URI.createURI("library.xml"));

		/*
		* Add bookStoreObject to contents list of the resource 
		*/
		resource.getContents().add(bookStoreObject);

		try{
		    /*
		    * Save the resource
		    */
		      resource.save(null);
		   }catch (IOException e) {
		      e.printStackTrace();
		   }
		Map options = new HashMap();
		options.put(XMLResource.OPTION_SCHEMA_LOCATION, Boolean.TRUE);
		try{
		     resource.save(options);
		   }catch (IOException e) {
		     e.printStackTrace();
		   }
	}

}
