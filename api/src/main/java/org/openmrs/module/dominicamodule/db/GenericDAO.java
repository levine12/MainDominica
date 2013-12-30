package org.openmrs.module.dominicamodule.db;

import org.openmrs.module.dominicamodule.db.*;
import org.hibernate.classic.Session;

public interface GenericDAO {
	public Object get(String query,boolean unique);
	public Session session();
}
