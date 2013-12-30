/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.dominicamodule.extension.html;

import java.util.List;
import java.util.Map;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.activelist.ActiveListItem;
import org.openmrs.activelist.Allergy;
import org.openmrs.api.context.Context;
import org.openmrs.module.Extension;

public class PatientSummaryExtension extends Extension {

    private String patientId = "";

    public Extension.MEDIA_TYPE getMediaType() {
        return Extension.MEDIA_TYPE.html;
    }

    @Override
    public String getOverrideContent(final String bodyContent) {
        Patient p = Context.getPatientService().getPatient(Integer.valueOf(patientId));
        Person person = p;
        List<ActiveListItem> allAllergies = Context.getActiveListService().getActiveListItems(person, Allergy.ACTIVE_LIST_TYPE);
        System.out.println("********************PatientAllergiesPortletController  Allergies for patient: " + p.getFamilyName());
        String allergies = "<b><font color=\"red\"> ";
        for (ActiveListItem allergyItem : allAllergies) {
            if (allergyItem.getEndDate() == null) {
                Allergy allergy = (Allergy) allergyItem;
                allergies += allergy.getAllergen().getName().getName() + ", ";
                System.out.println("Allergy: " + allergy.getAllergen().getName().getName());
            }
        }
        allergies += "</font></b>";
        //return " &nbsp;<a href='module/basicmodule/patientsummarylink.form?patientId=" + patientId + "' target='_blank'>View Summary</a> ";
        return "Allergies: " + allergies;
    }

    @Override
    public void initialize(final Map<String, String> parameters) {
        patientId = parameters.get("patientId");
    }

}
