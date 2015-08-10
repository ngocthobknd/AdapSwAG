package verification;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cvl.Choice;
import cvl.ChoiceResolution;
import cvl.MultiplicityInterval;
import cvl.VInstance;
import cvl.VSpec;
import cvl.VSpecResolution;
import resolution.gui.ResolutionModelGUI;
import variability.gui.VariabilityModelGUI;

public class ResolutionModelVerification {
	/*
	 * verify resolution model
	 * Input: variability model (VM) and resolution model (RM)
	 * Output: true, false
	 * 
	 */
	
	ArrayList<VSpec> vSpecList = new ArrayList<VSpec>();
	ArrayList<VSpecResolution> resolutionList = new ArrayList<VSpecResolution>();
	
	public String messageAlert = "";
	public ResolutionModelVerification(ArrayList<VSpec> vSpecList, ArrayList<VSpecResolution> resolutionList) {
		this.vSpecList = vSpecList;
		this.resolutionList = resolutionList;
	}

	@SuppressWarnings("unused")
	public boolean verifyRM() {
		boolean result = true;
		for (Iterator<VSpecResolution> iter = resolutionList.listIterator(); iter.hasNext();) {
			VSpecResolution vSpecResolution = iter.next();
			VSpec vSpec = vSpecResolution.getResolvedVSpec();
			boolean chk = false;
			for (int i = 0; i < vSpecList.size(); i++) {
				if (vSpecList.get(i).getName().equals(vSpec.getName())) {
					chk = true;
				}
			}
			if (!chk) {
				messageAlert = "the resolution model is not conformed to the variability model";
				System.out.println(messageAlert);
				return false;
			}
		}
		
		for (Iterator<VSpec> iter = vSpecList.listIterator(); iter.hasNext();) {
			VSpec vSpec = iter.next();
			if (vSpec instanceof Choice) {
				Choice vspChoice = (Choice) vSpec;
				List<VSpec> vSpecChilds = new ArrayList<VSpec>();
				List<VSpecResolution> resolvedChilds = new ArrayList<VSpecResolution>();
				ChoiceResolution vSpecResolution = (ChoiceResolution)returnVSpecResolutionByVSpec(vSpec, resolutionList);
				MultiplicityInterval multi = null;
				if (vSpecResolution != null) {
					try {
						multi = vSpec.getGroupMultiplicity();
						vSpecChilds = vSpec.getChild();
						resolvedChilds = vSpecResolution.getChild();
					} catch(Exception eMulti) {System.out.println(eMulti);}
					
					if (!vSpecResolution.isDecision()) {
						if (vSpecResolution.getChild() != null)
							for (int j = 0; j < resolvedChilds.size(); j++) {
								if (resolvedChilds.get(j) instanceof ChoiceResolution) {
									if (((ChoiceResolution)resolvedChilds.get(j)).isDecision()) {
										messageAlert = "decision conflict";
										System.out.println("decision conflict");
										return false;
									}
								}
							}
					} else if (vSpecChilds.size() > 0) {
						int count = 0;
						for (int j = 0; j < vSpecChilds.size(); j++) {
							VSpec vspChild = vSpecChilds.get(j);
							VSpecResolution vSpChildResol = returnVSpecResolutionByVSpec(vspChild, resolutionList);
							if (vSpChildResol instanceof ChoiceResolution) {
								ChoiceResolution choiceResl = (ChoiceResolution)vSpChildResol;
								if (((Choice)vspChild).isIsImpliedByParent()) {
									if (!choiceResl.isDecision()) {
										messageAlert = "error by contraint of isImpliedByparent";
										System.out.println("error by contraint of isImpliedByparent");
										return false;
									} else count +=1;
								} else {
									if (choiceResl.isDecision()) {
										count +=1;
									}
								}
							} else if (vSpChildResol instanceof VInstance) {
								try {
									if (((VInstance)vSpChildResol).getNumber() > 0) {
										count += ((VInstance)resolvedChilds.get(j)).getNumber();
									}
								}catch (Exception e) {}
							} else if (vSpChildResol == null) {
								if (vspChild instanceof Choice)
									if (((Choice)vspChild).isDefaultResolution()) {
										count += 1;
									}
							}
						}
						if (multi != null) {
							int lower = multi.getLower();
							int upper = multi.getUpper();
							if ((count < lower) || (count > upper)) {
								messageAlert = "error by constraint of Multiplicity";
								System.out.println("error by constraint of Multiplicity at "+vSpec.getName());
								return false; 
							}
						}
					} 
				}
			} 
		}
		return result;
	}
	public VSpecResolution returnVSpecResolutionByVSpec(VSpec vSpec, ArrayList<VSpecResolution> resolutionList) {
		
		VSpecResolution vSpecresolution = null;
		for (int i = 0; i < resolutionList.size(); i++) {
			if (resolutionList.get(i).getResolvedVSpec().getName().equals(vSpec.getName())) {
				return resolutionList.get(i);
			}
		}
		return vSpecresolution;
	}
	public static void main (String [] args) {
		VariabilityModelGUI variabilityModel = new VariabilityModelGUI("model//composite2//model.cvl");
		ResolutionModelGUI resolutionModel = new ResolutionModelGUI( "model//composite2//resolution.cvl"); 
		System.out.println(new ResolutionModelVerification(variabilityModel.getVSpecList(), resolutionModel.getVSpecResolutionList()).verifyRM());
	}
	

}
