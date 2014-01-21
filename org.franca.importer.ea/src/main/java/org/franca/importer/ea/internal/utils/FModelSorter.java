package org.franca.importer.ea.internal.utils;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.franca.core.franca.FModel;
import org.franca.core.franca.Import;

public class FModelSorter extends FrancaSorter<FModel> {

	private static Logger jlog =  Logger.getLogger(FModelSorter.class.getName());
	
//	private HashMap<FModel, String> containerMap = new HashMap<FModel, String>(); 

	public FModelSorter(List<FModel> toSort) {
		super(toSort);
//		containerMap = _container;
	}

	@Override
	public boolean canSwitch(FModel m) {

		if (m.getImports().size() == 0) {
			return true;
		} else {
			boolean canSwitch = true;
			for (Import i : m.getImports()) {			
				FModel parent = getFModelFromImport(i);
				if (!isReferenceResolved(parent)) {
					canSwitch = false;
				}
			}
			return canSwitch;
		}
	}
	
	/**
	 * Try to retrieve the FModel, where the given Import refers to
	 * It must be contained in one of the lists
	 * @param i The given Import
	 * @return The FModel, that is refered from the iven Import
	 */
	private FModel getFModelFromImport(Import i) {		
		
		for(FModel m: mRemaining) {
			if(modelNameEqualsImportName(m, i)) {
				return m;
			}
		}

		for(FModel m: mSorted) {
			if(modelNameEqualsImportName(m, i)) {
				return m;
			}
		}

		jlog.log(Level.SEVERE, "Can not find parent model for corresponding import "+i.getImportURI());
		return null;
	}

	private boolean modelNameEqualsImportName(FModel m, Import i) {
		
		// Check if model was loaded via fidl file reader
		if(m.eResource() != null) {
			return m.eResource().getURI().lastSegment().equals(i.getImportURI());
		}
		// No, then the franca model was created differently, probably via M2M transformation
		else {
			jlog.log(Level.SEVERE, "Obviously franca model was not loaded via Resource, but probably via M2M transformation");
			return false;
//			String filename = containerMap.get(m)+".fidl";			
//			return filename.equals(i.getImportURI());			
//			String[] tokens = m.getName().split("\\.");
//			return (tokens[tokens.length-1]+".fidl").equals(i.getImportURI());
		}
	}
	
}
