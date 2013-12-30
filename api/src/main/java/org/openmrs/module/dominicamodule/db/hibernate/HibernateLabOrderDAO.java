package org.openmrs.module.dominicamodule.db.hibernate;

import java.util.List;
import org.hibernate.Criteria;
import org.openmrs.module.dominicamodule.db.hibernate.*;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.openmrs.Order;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.dominicamodule.DrugOrderExtension;
import org.openmrs.module.dominicamodule.LabOrder;
import org.openmrs.module.dominicamodule.Note;
import org.openmrs.module.dominicamodule.db.DrugOrderExtensionDAO;
import org.openmrs.module.dominicamodule.db.LabOrderDAO;

public class HibernateLabOrderDAO implements LabOrderDAO {

    private SessionFactory sessionFactory;

    /**
     * This is a Hibernate object. It gives us metadata about the currently
     * connected database, the current session, the current db user, etc. To
     * save and get objects, calls should go through
     * sessionFactory.getCurrentSession() <br/>
     * <br/>
     * This is called by Spring. See the /metadata/moduleApplicationContext.xml
     * for the "sessionFactory" setting. See the applicationContext-service.xml
     * file in CORE openmrs for where the actual "sessionFactory" object is
     * first defined.
     *
     * @param sessionFactory
     */
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public LabOrder getLabOrder(Integer id) {
        return (LabOrder) sessionFactory.getCurrentSession().get(LabOrder.class, id);
    }

    public LabOrder saveLabOrder(LabOrder labOrder) {
        sessionFactory.getCurrentSession().saveOrUpdate(labOrder);
        return labOrder;
    }

    public LabOrder getLabOrderByOrderId(Integer id) {
                String query = "from LabOrder d where d.orderId = '"+id+"'";
		LabOrder labOrder = (LabOrder) sessionFactory.getCurrentSession().createQuery(query).uniqueResult();
		return labOrder==null ? new LabOrder() : labOrder;
    }

    public LabOrder getLabOrderByUuid(String uuid) {
               String query = "from LabOrder d where d.uid = '"+uuid+"'";
		LabOrder labOrder = (LabOrder) sessionFactory.getCurrentSession().createQuery(query).uniqueResult();
		return labOrder==null ? new LabOrder() : labOrder;
    }

    public List<LabOrder> getLabOrdersByPatient(Patient patient) {
                String query = "from LabOrder d where d.patient_id = '"+patient.getPatientId() + "'";
		List<LabOrder> labOrders = (List<LabOrder>) sessionFactory.getCurrentSession().createQuery(query);
		return labOrders;
    }

}
