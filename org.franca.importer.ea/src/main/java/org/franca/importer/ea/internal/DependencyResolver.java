package org.franca.importer.ea.internal;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.franca.core.franca.FArrayType;
import org.franca.core.franca.FEnumerationType;
import org.franca.core.franca.FField;
import org.franca.core.franca.FInterface;
import org.franca.core.franca.FStructType;
import org.franca.core.franca.FTypeRef;
import org.franca.core.franca.FUnionType;
import org.franca.importer.ea.ImportProcessor;
import org.franca.importer.ea.internal.utils.EAConstants;
import org.franca.importer.ea.internal.utils.EARepositoryAccessor;
import org.franca.importer.ea.internal.utils.ElementContainer;
import org.franca.importer.ea.internal.utils.EAConstants.ECardinality;
import org.franca.importer.ea.internal.utils.EAConstants.EConnectionType;
import org.franca.importer.ea.internal.utils.EAConstants.EConnectorDirection;
import org.franca.importer.ea.internal.utils.EAConstants.EStereoType;
import org.sparx.Attribute;
import org.sparx.Connector;
import org.sparx.ConnectorEnd;
import org.sparx.Element;

public class DependencyResolver extends ImportProcessorDecorator {

	private static Logger jlog =  Logger.getLogger(DependencyResolver.class.getName());	
	
	public DependencyResolver(ImportProcessor lProcessor) {
		super(lProcessor);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Attribute handleField(Element parent, FField src) {
		
		Attribute field = processor.handleField(parent, src);
		
		Element type = EARepositoryAccessor.INSTANCE.findElement(field.GetType());
		
		if(type != null) {
			Connector conn = null;				
			
			if(field.GetIsCollection()) {						
				conn = createConnector(field.GetName(), parent, type,  
						EConnectorDirection.SOURCE_TO_DEST, 
						EConnectionType.STRUCTURE,
						ECardinality.ZERO_TO_INFINITE, null);
			}
			else {
				conn = createConnector(field.GetName(), parent, type,  
						EAConstants.EConnectorDirection.SOURCE_TO_DEST, 
						EConnectionType.STRUCTURE, 
						null,null);							
			}	
			
			parent.GetConnectors().Refresh();

		}		
		
		return field;
	}

	
	
	@Override
	public Attribute handleMapKey(Element parent, FTypeRef src) {
		
		Attribute mapKey = processor.handleMapKey(parent, src);
		
		Element type = EARepositoryAccessor.INSTANCE.findElement(mapKey.GetType());
		
		if(type != null) {
			Connector conn = null;				
			
			conn = createConnector(mapKey.GetName(), parent, type,  
					EAConstants.EConnectorDirection.SOURCE_TO_DEST, 
					EConnectionType.MAP, 
					null,null);							
			
			parent.GetConnectors().Refresh();

		}		
				
		return mapKey;
	}

	@Override
	public Attribute handleMapValue(Element parent, FTypeRef src) {
		
		Attribute mapValue = processor.handleMapValue(parent, src);
		
		Element type = EARepositoryAccessor.INSTANCE.findElement(mapValue.GetType());
		
		if(type != null) {
			Connector conn = null;				
			
			conn = createConnector(mapValue.GetName(), parent, type,  
					EAConstants.EConnectorDirection.SOURCE_TO_DEST, 
					EConnectionType.MAP, 
					null,null);							
			
			parent.GetConnectors().Refresh();

		}		
		
		return mapValue;
	}

	
	
	@Override
	public Element handleArray(ElementContainer<?> parent, FArrayType src) {
		
		Element array = processor.handleArray(parent, src);
		
		String elementType = array.GetGenlinks().split("=")[1].replaceFirst(";", "");
		Element type = EARepositoryAccessor.INSTANCE.findElement(elementType);
		
		if(type != null) {
			 createConnector("", array, type,  
						EAConstants.EConnectorDirection.DEST_TO_SOURCE, 
						EAConstants.EConnectionType.ARRAY, 
						null, EStereoType.ARRAY_OF);						
		}
		
		array.GetConnectors().Refresh();
		
		return array;
	}

	@Override
	public Element handleStructure(ElementContainer<?> parent, FStructType src) {
		
		Element struct = processor.handleStructure(parent, src);
				
		if(src.getBase() != null) {
			Element superStruct = EARepositoryAccessor.INSTANCE.findElement(src.getBase().getName());
			
			createConnector("", superStruct, struct, 
					EConnectorDirection.SOURCE_TO_DEST, EConnectionType.INHERITANCE, 
					null, EStereoType.INHERITANCE);
						
			superStruct.GetConnectors().Refresh();
									
		}
				
		return struct;
	}
			
		
	@Override
	public Element handleUnion(ElementContainer<?> parent, FUnionType src) {
		
		Element union = processor.handleUnion(parent, src);

		if(src.getBase() != null) {
			Element superUnion = EARepositoryAccessor.INSTANCE.findElement(src.getBase().getName());
			
			createConnector("", superUnion, union, 
					EConnectorDirection.SOURCE_TO_DEST, EConnectionType.INHERITANCE, 
					null, EStereoType.INHERITANCE);
						
			superUnion.GetConnectors().Refresh();
									
		}
		
		return union;
	}

	@Override
	public Element handleEnumeration(ElementContainer<?> parent,
			FEnumerationType src) {
		
		Element enumeration = processor.handleEnumeration(parent, src);

		if(src.getBase() != null) {
			Element superEnum = EARepositoryAccessor.INSTANCE.findElement(src.getBase().getName());
			
			createConnector("", superEnum, enumeration, 
					EConnectorDirection.SOURCE_TO_DEST, EConnectionType.INHERITANCE, 
					null, EStereoType.INHERITANCE);
						
			superEnum.GetConnectors().Refresh();
									
		}

		return enumeration;
	}

	@Override
	public Element handleInterface(ElementContainer<?> parent, FInterface src) {
		
		Element _interface = processor.handleInterface(parent, src);
		
		if(EAConstants.GENERATE_BROADCAST.equals("true")) {
			// Check if we have broadcast-interface relation ship
			if(_interface.GetName().endsWith(EAConstants.CLIENTINTERFACE_SUFFIX)) {
				// element with same name, but without CLIENTINTERFACE_SUFFIX should exist
				Element bI = EARepositoryAccessor.INSTANCE.findElement(_interface.GetName().replace(EAConstants.CLIENTINTERFACE_SUFFIX,""));
				if(bI != null) {					
					 createConnector("", _interface, bI,  
								EAConstants.EConnectorDirection.UNSPECIFIED, 
								EAConstants.EConnectionType.BROADCAST,
								null,
								EStereoType.BROADCAST_DEPENDENCY);						
				}			
				_interface.GetConnectors().Refresh();					
			}
//			else {
//				jlog.log(Level.SEVERE, "Client-Interface without corresponding Master-interface found: "+_interface.GetName());
//			}			
		}
		
		if(src.getBase() != null) {
			Element superInterface = EARepositoryAccessor.INSTANCE.findElement(src.getBase().getName());
			
			createConnector("", superInterface, _interface, 
					EConnectorDirection.SOURCE_TO_DEST, EConnectionType.INHERITANCE, 
					null, EStereoType.INHERITANCE);
						
			superInterface.GetConnectors().Refresh();
			
		}
		
		return _interface;
	}

	private Connector createConnector(String name, Element supplier, Element client, 
			EConnectorDirection direction,
			EConnectionType type,
			ECardinality cardinality,
			EStereoType stereotype) {

		Connector conn = supplier.GetConnectors().AddNew("", "");																		
		
		//Element supplier = mConnectors.get(getTypeName(src.getType()));
		conn.SetClientID(client.GetElementID());
		conn.SetSupplierID(supplier.GetElementID());
		conn.SetDirection(direction.getName());
		conn.SetType(type.getName());
		if(stereotype != null) {
			conn.SetStereotype(stereotype.getName());
		}
		
		if(type.getName().equals(EConnectionType.MAP.getName())) {
			conn.SetSubtype("Strong");
		}
		
		conn.SetName(name);
		
		//check cardinality
		if(cardinality != null) {
			ConnectorEnd end = conn.GetClientEnd();
			end.SetCardinality(cardinality.getName());			
		}
		
		conn.Update();
				
		StringBuffer buf = new StringBuffer("Id: ");
		buf.append(conn.GetConnectorID()).append(", Name: ").append(conn.GetName()).
		append(", Type: ").append(conn.GetType()).
		append(", Direction: ").append(conn.GetDirection()).append(", ClientId: ").
		append(conn.GetClientID()).append(", SupplierId: ").append(conn.GetSupplierID());
		
		jlog.log(Level.FINE, "Created connector: "+buf.toString());
		
		return conn;
	}	
	
}
