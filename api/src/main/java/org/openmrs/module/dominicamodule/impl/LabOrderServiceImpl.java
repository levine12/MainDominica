package org.openmrs.module.dominicamodule.impl;

import java.util.List;
import org.openmrs.module.dominicamodule.LabOrderService;
import org.openmrs.module.dominicamodule.db.LabOrderDAO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Patient;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.dominicamodule.DrugOrderExtension;
import org.openmrs.module.dominicamodule.DrugOrderExtensionService;
import org.openmrs.module.dominicamodule.LabOrder;
import org.openmrs.module.dominicamodule.db.DrugOrderExtensionDAO;
import org.openmrs.module.dominicamodule.db.GenericDAO;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author levine
 */
public class LabOrderServiceImpl extends BaseOpenmrsService implements LabOrderService {

    private LabOrderDAO dao;

    private static final Log log = LogFactory.getLog(LabOrderServiceImpl.class);

    public LabOrderDAO getDao() {
        return dao;
    }

    public static Log getLog() {
        return log;
    }

    public void setDao(LabOrderDAO dao) {
        this.dao = dao;
    }

    @Transactional(readOnly = true)
    public LabOrder getLabOrderByUuid(String uuid) {
        return dao.getLabOrderByUuid(uuid);
    }

    @Transactional(readOnly = true)
    public LabOrder getLabOrder(Integer id) {
        return dao.getLabOrder(id);
    }

    @Transactional
    public LabOrder saveLabOrder(LabOrder doe) {
        return dao.saveLabOrder(doe);
    }

    @Transactional(readOnly = true)
    public LabOrder getLabOrderByOrderId(Integer id) {
        return dao.getLabOrderByOrderId(id);
    }

    @Transactional(readOnly = true)
    public List<LabOrder> getLabOrdersByPatient(Patient patient) {
        return dao.getLabOrdersByPatient(patient);
    }
}
