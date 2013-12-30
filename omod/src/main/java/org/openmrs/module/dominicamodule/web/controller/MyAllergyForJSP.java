package org.openmrs.module.dominicamodule.web.controller;

import java.util.Date;
import org.openmrs.Concept;
import org.openmrs.activelist.ActiveListItem;
import org.openmrs.activelist.Allergy;

/**
 *
 * @author levine
 */
public class MyAllergyForJSP {
    private Concept allergen;
    private Date allergyDate;
    private Concept reaction;
    private String severity;
    private int allergyId;
    
    public MyAllergyForJSP(ActiveListItem allergyItem) {
        Allergy allergy = (Allergy)allergyItem;
        this.allergen = allergy.getAllergen();
        this.allergyDate = allergy.getDateCreated();
        this.reaction = allergy.getReaction();
        if (allergy.getSeverity() != null) {
            this.severity = allergy.getSeverity().toString();
        } else {
            System.out.println("**********SEVERITY IS NULL");
        }
        this.allergyId = allergy.getActiveListId();
    }

    public int getAllergyId() {
        return allergyId;
    }

    public Concept getAllergen() {
        return allergen;
    }

    public Date getAllergyDate() {
        return allergyDate;
    }

    public Concept getReaction() {
        return reaction;
    }

    public String getSeverity() {
        return severity;
    }
    
    
}
