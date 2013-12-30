package org.openmrs.module.dominicamodule.db;

import java.util.List;
import org.openmrs.module.dominicamodule.db.*;
import org.openmrs.module.dominicamodule.DrugOrderExtension;


/**
 * radiologyResponse-related database functions
 * 
 * @author Cortex
 * @version 1.0
 */
public interface DrugOrderExtensionDAO {

	public DrugOrderExtension getDrugOrderExtension(Integer id);

	public DrugOrderExtension saveDrugOrderExtension(DrugOrderExtension drugOrderExtension);
	
	/**
	 * @param id
	 * @return the study with orderId=id, or new Study() if there is no such study
	 */
	public DrugOrderExtension getDrugOrderExtensionByOrderId(Integer id);
        
//        public List<DrugOrderExtension> getAllDrugOrderExtension();
//        
//        public List<DrugOrderExtension> getOutStandingDrugOrderExtension();
//        
//        public List<DrugOrderExtension> getCompletedDrugOrderExtension();
//        
//        public List<DrugOrderExtension> getVoidedDrugOrderExtension();
//        
//        public List<DrugOrderExtension> getDiscontinuedDrugOrderExtension();
	
}
