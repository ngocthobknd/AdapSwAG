package verification;

import java.util.ArrayList;
import java.util.List;

import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

import org.eclipse.emf.common.util.EList;

import cvl.Choice;
import cvl.ChoiceResolution;
import cvl.MultiplicityInterval;
import cvl.VInstance;
import cvl.VSpec;
import cvl.VSpecResolution;
import tree.ResolutionTree;
import tree.VSpecTree;

public class ResolutionModelVerification {
	/*
	 * verify resolution model
	 * Input: variability model (VM) and resolution model (RM)
	 * Output: true, false
	 * Algorithm:
	 * 	if (VM.size != RM.size) return false;
	 * 	for (i = 0 to VM.size) {
	 *  }
	 * 
	 */
	
	ArrayList<VSpec> vSpecList;
	ArrayList<VSpecResolution> resolutionList;
	public String messageAlert = "";
	public ResolutionModelVerification(ArrayList<VSpec> vSpecList, ArrayList<VSpecResolution> resolutionList) {
		this.vSpecList = vSpecList;
		this.resolutionList = resolutionList;
	}
	@SuppressWarnings("unchecked")
	public boolean verifyRM() {
		boolean result = true;
		if (resolutionList.size() != vSpecList.size()) return false;
		else {
			for (int i = 0; i < vSpecList.size(); i++) {
				if (vSpecList.get(i) instanceof Choice) {
					VSpec vSpec = vSpecList.get(i);
					VSpecResolution vSpecResolution = resolutionList.get(i);
					MultiplicityInterval multi = null;
					List<VSpec> vSpecChilds = new ArrayList<VSpec>();
					List<VSpecResolution> resolvedChilds = new ArrayList<VSpecResolution>();
					
					try {
						multi = vSpec.getGroupMultiplicity();
						vSpecChilds = vSpec.getChild();
						resolvedChilds = vSpecResolution.getChild();
					
						//System.out.println(vSpec.getName() + "+"+ vSpecChilds.size());
						
					} catch(Exception eMulti) {System.out.println(eMulti);}
					
					//1: case of multiplicity : OR and XOR
					if ((multi != null) && (vSpecChilds.size() > 0) && (resolvedChilds.size() > 0) 
							&& ((ChoiceResolution)vSpecResolution).isDecision()){
						int lower = multi.getLower();
						int upper = multi.getUpper();
						int count = 0; 
						//System.out.println(lower);
						for (int j = 0; j < resolvedChilds.size(); j++) {
							if (resolvedChilds.get(j) instanceof ChoiceResolution) {
								
								if (((Choice)vSpecChilds.get(j)).isIsImpliedByParent()) {
									if (!((ChoiceResolution)resolvedChilds.get(j)).isDecision()) {
										messageAlert = "error by contraint of isImpliedByparent";
										System.out.println("error by contraint of isImpliedByparent");
										return false;
									}
								} 
								if (((ChoiceResolution)resolvedChilds.get(j)).isDecision()) {
									count += 1;
								}
							}
							//VClassifier
							else 
								try {
								if (((VInstance)resolvedChilds.get(j)).getNumber() > 0) {
									count += ((VInstance)resolvedChilds.get(j)).getNumber();
								}
								}catch (Exception e) {}
							
						}
						//System.out.println(vSpec.getName() +""+upper+":"+count);
						if ((count < lower) || (count > upper)) {
							messageAlert = "error by constraint of Multiplicity";
							System.out.println("error by constraint of Multiplicity");
							return false; 
						}
					}
					//2: optional or mandatory
					else if ((vSpecChilds.size() > 0) && (resolvedChilds.size() > 0) 
							&& ((ChoiceResolution)vSpecResolution).isDecision()) {
						for (int j = 0; j < vSpecChilds.size(); j++) {
							if (vSpecChilds.get(j) instanceof Choice) {
								if (((Choice)vSpecChilds.get(j)).isIsImpliedByParent()) {
									if (!((ChoiceResolution)resolvedChilds.get(j)).isDecision()) {
										messageAlert = "error by contraint of isImpliedByparent";
										System.out.println("error by contraint of isImpliedByparent");
										return false;
									}
								}
							}
						}	
					}
				}
				else if (vSpecList.get(i) instanceof VInstance) {
					
				}
			}
		}
		
		return result;
	}
	public static void main (String [] args) {
		ResolutionTree rs;
		VSpecTree vSpec;
		rs = new ResolutionTree();
		vSpec = new VSpecTree();
		System.out.println(new ResolutionModelVerification(vSpec.vSpecList, rs.resolutionList).verifyRM());
	}
	

}
