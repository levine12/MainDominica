/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openmrs.module.dominicamodule.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.dominicamodule.DrugOrderExtension;
import org.openmrs.module.dominicamodule.DrugOrderExtensionService;
import org.openmrs.module.dominicamodule.db.DrugOrderExtensionDAO;
import org.openmrs.module.dominicamodule.db.GenericDAO;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author akhil
 */
public class DrugOrderExtensionServiceImpl extends BaseOpenmrsService implements DrugOrderExtensionService  {

    
    private GenericDAO gdao;
    private DrugOrderExtensionDAO doedao;
    
    private static final Log log=LogFactory.getLog(DrugOrderExtensionServiceImpl.class);

    public GenericDAO getGdao() {
        return gdao;
    }

    public DrugOrderExtensionDAO getDoedao() {
        return doedao;
    }

    public void setGdao(GenericDAO gdao) {
        this.gdao = gdao;
    }

    public void setDoedao(DrugOrderExtensionDAO doedao) {
        this.doedao = doedao;
    }
                  
    @Override
    public Object get(String query,boolean unique)
	{
		return gdao.get(query,unique);
	}
	
    public GenericDAO db(){return gdao;}
    
    @Transactional(readOnly=true)
    public DrugOrderExtension getDrugOrderExtension(Integer id) {
        return doedao.getDrugOrderExtension(id);
    }

    @Transactional
    public DrugOrderExtension saveDrugOrderExtension(DrugOrderExtension doe) {
        return doedao.saveDrugOrderExtension(doe);
    }

    @Transactional(readOnly=true)
    public DrugOrderExtension getDrugOrderExtensionByOrderId(Integer id) {
        return doedao.getDrugOrderExtensionByOrderId(id);
    }
    
}
