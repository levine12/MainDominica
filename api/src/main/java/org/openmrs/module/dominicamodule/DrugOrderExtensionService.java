/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openmrs.module.dominicamodule;

import org.openmrs.api.OpenmrsService;
import org.openmrs.module.dominicamodule.db.DrugOrderExtensionDAO;
import org.openmrs.module.dominicamodule.db.GenericDAO;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author akhil
 */

    
@Transactional
public interface DrugOrderExtensionService extends OpenmrsService {

    public void setDoedao(DrugOrderExtensionDAO dao);
    
    public Object get(String query,boolean unique);
    
    public DrugOrderExtension getDrugOrderExtension(Integer id);

    public DrugOrderExtension saveDrugOrderExtension(DrugOrderExtension os);

    public DrugOrderExtension getDrugOrderExtensionByOrderId(Integer id);

    public GenericDAO db();
    

}

