package org.openmrs.module.dominicamodule.extension.html;

import org.openmrs.module.Extension;
import org.openmrs.module.web.extension.PatientDashboardTabExt;

public class PatientDrugsDashboardTabExt extends PatientDashboardTabExt {

    @Override
    public Extension.MEDIA_TYPE getMediaType() {
        return Extension.MEDIA_TYPE.html;
    }

    @Override
    public String getPortletUrl() {
        return "patientDrugs";
    }

    @Override
    public String getRequiredPrivilege() {
        return "Patient Dashboard - View Drugs Section";
    }

    @Override
    public String getTabId() {
        return "PatientDrugs";
    }

    @Override
    public String getTabName() {
        return "Drugs";
    }
}
