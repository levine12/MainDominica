package org.openmrs.module.dominicamodule.extension.html;

    import org.openmrs.module.Extension;
    import org.openmrs.module.web.extension.PatientDashboardTabExt;

    public class LabsPatientDashboardTab extends PatientDashboardTabExt {

       public Extension.MEDIA_TYPE getMediaType() {
          return Extension.MEDIA_TYPE.html;
       }
       
       @Override
       public String getPortletUrl() {
          return "patientLabs";
       }

       @Override
       public String getRequiredPrivilege() {
          return "Patient Dashboard - View Labs Section";
       }

       @Override
       public String getTabId() {
          return "PatientLabs";
       }

       @Override
       public String getTabName() {
          return "Labs";
       }
       
    }