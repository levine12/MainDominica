package org.openmrs.module.dominicamodule.extension.html;

    import org.openmrs.module.Extension;
    import org.openmrs.module.web.extension.PatientDashboardTabExt;

    public class AllergiesPatientDashboardTab extends PatientDashboardTabExt {

       public Extension.MEDIA_TYPE getMediaType() {
          return Extension.MEDIA_TYPE.html;
       }
       
       @Override
       public String getPortletUrl() {
          return "patientAllergies";
       }

       @Override
       public String getRequiredPrivilege() {
          return "Patient Dashboard - View Allergies Section";
       }

       @Override
       public String getTabId() {
          return "PatientAllergies";
       }

       @Override
       public String getTabName() {
          return "Allergies";
       }
       
    }