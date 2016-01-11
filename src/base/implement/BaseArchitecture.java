package base.implement;

import java.util.ArrayList;

import fractal.fractal.*;
import ACME.Attachment;
import ACME.ComponentInstance;
import ACME.Connector;
import ACME.System;
import base.api.BaseArchitectureService;

public class BaseArchitecture implements BaseArchitectureService{

	public BaseArchitecture() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Definition getArchitectureDefinition(String file) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Component> getComponentList(Definition definition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Binding> getBindingList(Definition definition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Component> getParentComponentList(Definition definition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Binding> getParentBindingList(Definition definition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public System getACMESystem(String file) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<ComponentInstance> getParentComponentList(System sys) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Connector> getParentConnectorList(System sys) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Attachment> getParentAttchmentList(System sys) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<ACME.Binding> getParentBindingList(System sys) {
		// TODO Auto-generated method stub
		return null;
	}

}
