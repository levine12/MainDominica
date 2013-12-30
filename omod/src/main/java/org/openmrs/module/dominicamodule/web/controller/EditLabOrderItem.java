/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openmrs.module.dominicamodule.web.controller;

import org.openmrs.Concept;

/**
 *
 * @author levine
 */
public class EditLabOrderItem {
    
    private String labTestConceptName;
    private int labTestConceptId;
    
    private boolean isChecked;
    
    private String labSetName;
    private int labSetConceptId;
    
    private int orderId;


    public EditLabOrderItem(int orderId, Concept labSet, Concept labTestConcept,boolean isChecked) {
        this.orderId = orderId;
        this.labSetName = labSet.getName().getName();
        this.labSetConceptId = labSet.getConceptId();
        this.labTestConceptName = labTestConcept.getName().getName();
        this.labTestConceptId = labTestConcept.getConceptId();
        this.isChecked = isChecked;
    }
    
    public String toString() {
        return "order id: " +orderId + " lab set name: " + labSetName +  " Test Name: " + labTestConceptName + "  Checked? " + isChecked;
    }

    public int getOrderId() {
        return orderId;
    }
    

    public String getLabTestConceptName() {
        return labTestConceptName;
    }

    public int getLabTestConceptId() {
        return labTestConceptId;
    }

    public boolean isIsChecked() {
        return isChecked;
    }

    public String getLabSetName() {
        return labSetName;
    }

    public int getLabSetConceptId() {
        return labSetConceptId;
    }
            
}
