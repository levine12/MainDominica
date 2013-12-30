package org.openmrs.module.dominicamodule.db;

import java.util.List;

import org.openmrs.Patient;
import org.openmrs.module.dominicamodule.LabOrder;
import org.openmrs.module.dominicamodule.LabOrderService;

/**
 * This is the DAO interface. This is never used by the developer, but instead
 * the {@link LabOrderService} calls it (and developers call the NoteService)
 */
public interface LabOrderDAO {

	LabOrder getLabOrder(Integer id);

	LabOrder getLabOrderByUuid(String uuid);

	LabOrder saveLabOrder(LabOrder labOrder);
        
        LabOrder getLabOrderByOrderId(Integer id);

        List<LabOrder> getLabOrdersByPatient(Patient patient);
}
