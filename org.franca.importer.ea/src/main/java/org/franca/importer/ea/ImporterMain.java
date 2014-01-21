package org.franca.importer.ea;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.franca.importer.ea.utils.CommandLineHelper;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class ImporterMain {

	private static Logger jlog = Logger.getLogger(ImporterMain.class.getName());

	public int doImport(String[] args) {

		Injector injector = Guice.createInjector(new CreateOrOverwriteModule());
		ImporterFacade facade = injector.getInstance(ImporterFacade.class);

		CommandLineHelper cliOptions = new CommandLineHelper();
		cliOptions.createOptions();
		cliOptions.parseOptions(args);

		if (cliOptions.getEaProject() != null
				&& cliOptions.getRootPackage() != null
				&& cliOptions.getFrancaPath() != null) {

			// try to open project and find the root Package
			if (!facade.setup(cliOptions.getEaProject(), cliOptions.getRootPackage())) {
				facade.tearDown();
				return -1;
			}
			
			facade.execute(cliOptions.getFrancaPath());

			// tear everything down
			facade.tearDown();

			return 0;
		}

		else {
			return -2;
		}

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		ImporterMain importer = new ImporterMain();

		int result = importer.doImport(args);

		switch (result) {
		case 0:
			jlog.log(Level.INFO, "Import successfully finished");
			break;
		case -1:
			jlog.log(Level.SEVERE, "Failed to open or create project");
			break;
		case -2:
			jlog.log(Level.SEVERE, "Please specify import parameter");
			break;
		default:
			break;
		}

		System.exit(result);

	}

}
