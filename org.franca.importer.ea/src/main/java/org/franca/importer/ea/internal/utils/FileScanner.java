package org.franca.importer.ea.internal.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

public class FileScanner {

	private FilenameFilter mFilter = null;
	private FileFilter dirFilter = new DirectoryFilter();
	
	/**
	 * Constructor
	 * @param filter The given filename filter
	 */
	public FileScanner() {
		mFilter = new FidlFilter();
	}
	
	/**
	 * Retrieves all the the filenames within a given path (an all its subfolders) filtered by 
	 * a given filename filter (that is initialized in the constructor)
	 * @param path The given path, where the scanner starts searching
	 * @return The list of found filenames
	 */
	public List<String> getFiles(String path) {
		
		List<String> fileNames = new Vector<String>();
		
		File root = new File(path);
		System.out.println(root.getAbsolutePath());
		
		if(!root.exists()) {
			throw new IllegalArgumentException("Path "+path+" does not exist");
		}
		
		File[] direcories = root.listFiles(dirFilter);

		for(File f:direcories) {
			fileNames.addAll(getFiles(f.getAbsolutePath()));
		}
				
		File[] relevantFiles = root.listFiles(mFilter);
		
		for(File f:relevantFiles) {
			fileNames.add(f.getAbsolutePath());
		}
			
		
		return fileNames;
	}
			
	class FidlFilter implements FilenameFilter {


		@Override
		public boolean accept(File arg0, String arg1) {
			// TODO Auto-generated method stub
			return arg1.toLowerCase().endsWith(".fidl");
		}
		
	}
	
	/**
	 * An internal class, that implements the directory filter
	 * @author TMosis
	 *
	 */
	class DirectoryFilter implements FileFilter {

		@Override
		public boolean accept(File pathname) {
			// TODO Auto-generated method stub
			return pathname.isDirectory();
		}
		
	}

}
