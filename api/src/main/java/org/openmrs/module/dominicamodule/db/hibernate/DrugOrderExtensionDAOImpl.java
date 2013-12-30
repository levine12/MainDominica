package org.openmrs.module.dominicamodule.db.hibernate;

import java.util.List;
import org.openmrs.module.dominicamodule.db.hibernate.*;
import org.hibernate.SessionFactory;
import org.openmrs.Order;
import org.openmrs.api.context.Context;
import org.openmrs.module.dominicamodule.DrugOrderExtension;
import org.openmrs.module.dominicamodule.db.DrugOrderExtensionDAO;

public class DrugOrderExtensionDAOImpl implements DrugOrderExtensionDAO {

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

    public DrugOrderExtension getDrugOrderExtension(Integer id) {
        return (DrugOrderExtension) sessionFactory.getCurrentSession().get(DrugOrderExtension.class, id);
    }

    public DrugOrderExtension saveDrugOrderExtension(DrugOrderExtension drugOrderExtension) {
        sessionFactory.getCurrentSession().saveOrUpdate(drugOrderExtension);
        return drugOrderExtension;
    }

    public DrugOrderExtension getDrugOrderExtensionByOrderId(Integer id) {
                String query = "from DrugOrderExtension d where d.orderId = '"+id+"'";
		DrugOrderExtension drugOrderExtension = (DrugOrderExtension) sessionFactory.getCurrentSession().createQuery(query).uniqueResult();
		return drugOrderExtension==null ? new DrugOrderExtension() : drugOrderExtension;
    }

//    public List<DrugOrderExtension> getAllDrugOrderExtension() {
//                String query = "from DrugOrderExtension d ";
//		List<DrugOrderExtension> drugOrderExtensionList = (List<DrugOrderExtension>) sessionFactory.getCurrentSession().createQuery(query).uniqueResult();
//		return drugOrderExtensionList;
//    }
//
//    public List<DrugOrderExtension> getOutStandingDrugOrderExtension() {
//        String query = "from DrugOrderExtension d where d.drugOrderStatus=1";
//	List<DrugOrderExtension> drugOrderExtensionList = (List<DrugOrderExtension>) sessionFactory.getCurrentSession().createQuery(query).uniqueResult();
//	return drugOrderExtensionList;
//    }
//
//    public List<DrugOrderExtension> getCompletedDrugOrderExtension() {
//        String query = "from DrugOrderExtension d where d.drugOrderStatus=2";
//	List<DrugOrderExtension> drugOrderExtensionList = (List<DrugOrderExtension>) sessionFactory.getCurrentSession().createQuery(query).uniqueResult();
//	return drugOrderExtensionList;    
//    }
//
//    public List<DrugOrderExtension> getVoidedDrugOrderExtension() {
//        String query = "from DrugOrderExtension d where d.drugOrderStatus=3";
//	List<DrugOrderExtension> drugOrderExtensionList = (List<DrugOrderExtension>) sessionFactory.getCurrentSession().createQuery(query).uniqueResult();
//	return drugOrderExtensionList;   
//    }
//
//    public List<DrugOrderExtension> getDiscontinuedDrugOrderExtension() {
//        String query = "from DrugOrderExtension d where d.drugOrderStatus=4";
//	List<DrugOrderExtension> drugOrderExtensionList = (List<DrugOrderExtension>) sessionFactory.getCurrentSession().createQuery(query).uniqueResult();
//	return drugOrderExtensionList;    
//    }
		
}
