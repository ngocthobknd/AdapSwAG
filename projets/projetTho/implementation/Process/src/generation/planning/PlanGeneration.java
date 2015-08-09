package generation.planning;

import java.util.ArrayList;

import cvl.VSpec;
import cvl.VSpecResolution;
import cvl.VariationPoint;

public class PlanGeneration {
	/*
	 * generate reconfiguration plan
	 * a reconfiguration plan consists of 
	 * 		1. start new component (C) if existing, if not, create new component
	 * 		2. passivate components (A) whose connections touch components (B) that will stop
	 * 		(3) transfer data from B to C
	 * 		3. stop components (B)
	 * 		4. connect A to C
	 * 		5. activate A
	 * 
	 */
	
	ArrayList<VSpec> vSpecList;
	ArrayList<VSpecResolution> sourceResolutionList;
	ArrayList<VSpecResolution> destinationResolutionList;
	ArrayList<VariationPoint> vpList;
	public PlanGeneration(ArrayList<VSpec> vSpecList,
			ArrayList<VSpecResolution> sourceResolutionList,
			ArrayList<VSpecResolution> destinationResolutionList,
			ArrayList<VariationPoint> vPList) {
		super();
		this.vSpecList = vSpecList;
		this.sourceResolutionList = sourceResolutionList;
		this.destinationResolutionList = destinationResolutionList;
		this.vpList = vPList;
	}
	public PlanGeneration() {
		super();
	}
	
	
}
