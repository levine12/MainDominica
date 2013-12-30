/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.dominicamodule.web.controller;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.Drug;
import org.openmrs.DrugOrder;
import org.openmrs.Encounter;
import org.openmrs.Order;
import org.openmrs.OrderType;
import org.openmrs.Patient;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.module.dominicamodule.DrugOrderExtension;
import org.openmrs.module.dominicamodule.DrugOrderExtension.DrugOrderStatus;
import org.openmrs.module.dominicamodule.DrugOrderExtension.Units;
import org.openmrs.module.dominicamodule.DrugOrderExtensionService;
import org.openmrs.propertyeditor.ConceptEditor;
import org.openmrs.propertyeditor.DrugEditor;
import org.openmrs.propertyeditor.EncounterEditor;
import org.openmrs.propertyeditor.OrderTypeEditor;
import org.openmrs.propertyeditor.PatientEditor;
import org.openmrs.propertyeditor.UserEditor;
import org.springframework.beans.propertyeditors.CustomBooleanEditor;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PharmacyLandingPageController {

    /**
     * Logger for this class and subclasses
     */
    protected final Log log = LogFactory.getLog(getClass());

    /**
     * Success form view name
     */
    private final String SUCCESS_FORM_VIEW = "/module/dominicamodule/pharmacyLandingPage";

    private static List<DrugOrder> filledOrders = new Vector<DrugOrder>();
    private List<DrugOrder> orders = new Vector<DrugOrder>();
    private List<DrugOrderExtension> drugOrderExtensions = new Vector<DrugOrderExtension>();
    
    private List<String> duration=new Vector<String>();
    private List<DrugOrderExtension.DurationUnit> durationUnits=new Vector<DrugOrderExtension.DurationUnit>();
    private List<String> route=new Vector<String>();
    private List<String> pharmacist=new Vector<String>();
    private List<String> drugstatus=new Vector<String>();
    List<String> frequency=new Vector<String>();
    List<Units> units=new Vector<Units>();
    List<String> approved=new Vector<String>();
    List<String> approvingInstructions=new Vector<String>();
    
    static DrugOrderExtensionService service() {
		return Context.getService(DrugOrderExtensionService.class);
	}

    public String[] forSelect(Class<?> c) {
		Field[] f = c.getDeclaredFields();
		String s[] = new String[f.length + 1];
		s[0] = Context.getMessageSourceService().getMessage("general.select");
		for (int i = 0; i < f.length; i++) {
			try
         {
                 s[i+1]=f[i].getName();
	         //s[i + 1] = (String) c.getMethod("string",Integer.class,Boolean.class).invoke(c.newInstance(),new Integer(i),new Boolean(true));
         }
         catch(Exception e)
         {	         
         }
		}
		return s;
	}


    /**
     * This method is called by spring to let it know how to convert from an
     * String patientId to a Patient object. The "showNotes" method takes in a
     * parameter called patient_id which is of type {@link Patient}. <br/>
     * <br/>
     * There are a lot of property editors for openmrs objects in
     * org.openmrs.propertyeditor package.
     *
     * @param wdb a spring class we register "custom editors" with.
     */
    /*
     * This allows the jsp data entry to update the form backing object properties automatically
     * without having to get the info from the form and updating via code
     */
    @InitBinder
    void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(OrderType.class, new OrderTypeEditor());
        binder.registerCustomEditor(Boolean.class, new CustomBooleanEditor("t",
                "f", true));
        binder.registerCustomEditor(Integer.class, new CustomNumberEditor(
                Integer.class, true));
        binder.registerCustomEditor(Concept.class, new ConceptEditor());
        binder.registerCustomEditor(Date.class,
                new CustomDateEditor(Context.getDateFormat(), true));
        binder.registerCustomEditor(User.class, new UserEditor());
        binder.registerCustomEditor(Patient.class, new PatientEditor());
        binder.registerCustomEditor(Encounter.class, new EncounterEditor());
        binder.registerCustomEditor(Drug.class, new DrugEditor());
    }

    @RequestMapping(value = "module/dominicamodule/pharmacyLandingPage.form", method = RequestMethod.GET)
    protected ModelAndView get(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("Here in PharmDispenseController get method.");
        removeFilledOrders();
        
        ModelMap model = new ModelMap();
        model.put("orders", orders);
        model.put("drugOrderExtensions", drugOrderExtensions);
        model.put("duration", duration);
        model.put("durationUnits", durationUnits);
        model.put("route", route);
        model.put("pharmacist", pharmacist);
        model.put("drugstatus",drugstatus);
        model.put("frequency",frequency);
        model.put("units", units);
        model.put("approved",approved);
        model.put("approvingInstructions",approvingInstructions);
        return new ModelAndView("module/dominicamodule/pharmDispenseReturnPrescriptions", model);

    }
    
    @RequestMapping(value="module/dominicamodule/portlets/pharmacistDrugCompletion.form",method=RequestMethod.GET)
    protected ModelAndView getForm(@RequestParam(value = "orderId") Integer orderId){
        ModelAndView mav=new ModelAndView("module/dominicamodule/portlets/pharmacistFillDrugs");
        mav.addObject("orderId", orderId);
        DrugOrder order=Context.getOrderService().getOrder(orderId,DrugOrder.class);
        DrugOrderExtension doe=service().getDrugOrderExtensionByOrderId(orderId);
        mav.addObject("qty",order.getQuantity());
        mav.addObject("drugname", order.getConcept().getName().getName());
        
        mav.addObject("dose",order.getDose().toString()+" "+Units.values()[Integer.valueOf(order.getUnits())] );
        return mav;
    }
    
    private void removeFilledOrders() {
        List<DrugOrder> allOrders = new Vector<DrugOrder>();
        allOrders = Context.getOrderService().getOrders(DrugOrder.class, null, null, null, null, null, null);
        orders.clear();
        drugOrderExtensions.clear();
        duration.clear();
        durationUnits.clear();
        route.clear();
        pharmacist.clear();
        drugstatus.clear();
        units.clear();
        frequency.clear();
        approved.clear();
        approvingInstructions.clear();
        
        for (DrugOrder order : allOrders) {
            DrugOrderExtension doe=new DrugOrderExtension();
            doe= service().getDrugOrderExtensionByOrderId(order.getOrderId());
            DrugOrderStatus dos=DrugOrderStatus.values()[doe.getDrugOrderStatus()];
            if (dos == DrugOrderStatus.APPROVED || dos == DrugOrderStatus.NOAPPROVALNEEDED) {
                orders.add(order);
                drugOrderExtensions.add(doe);
                duration.add(doe.getDuration().toString());
                durationUnits.add(DrugOrderExtension.DurationUnit.values()[doe.getDurationUnit()]);
                route.add(DrugOrderExtension.RouteOfAdministration.getAllFullNames().get(doe.getRouteOfAdministration()));
                pharmacist.add("");
                drugstatus.add(DrugOrderStatus.getAllFullNames().get(doe.getDrugOrderStatus()));
                frequency.add(DrugOrderExtension.Frequency.getAllFullNames().get(Integer.valueOf(order.getFrequency())));
                units.add(DrugOrderExtension.Units.values()[Integer.valueOf(order.getUnits())]);
                if (dos == DrugOrderStatus.APPROVED){
                    approved.add(doe.getApprovedByUser().getPersonName().getFullName() + " on " + doe.getApprovedDate());
                    approvingInstructions.add(doe.getApprovingUserInstructions());
                }
                else
                {
                    approved.add("NA");
                    approvingInstructions.add("NA");
                }
                
            }
        }
        for (Order ord : orders) {
            System.out.println("ORDER: " + ord.getConcept().getName().getName()
                    + ord.getPatient().getGivenName());
        }
    }

    @RequestMapping(value = "module/dominicamodule/pharmacyLandingPage.form", method = RequestMethod.POST)
    public ModelAndView post(@RequestParam(value = "orderId") Integer orderId,
                            @RequestParam(value = "qtyDispensed") Integer qtyDispensed,
                            @RequestParam(value = "pharmInstructions") String pharmInstructions,
                            @RequestParam(value = "processValue") String processValue) {
        Date today = new Date();
        System.out.println("Here in PharmDispenseController post method.");                
        Order order = Context.getOrderService().getOrder(orderId);
        DrugOrderExtension doe=service().getDrugOrderExtensionByOrderId(orderId);

        if (processValue.compareTo("approve")==0){
            doe.setPharmacistDrugDispensedQuantity(qtyDispensed);
            if (doe.getRenewedOrderId()==null)
                                doe.setDrugOrderStatus(DrugOrderStatus.FILLED.ordinal());
                            else
                                doe.setDrugOrderStatus(DrugOrderStatus.RENEWEDFILLED.ordinal());
            
        }
        else
            if (processValue.compareTo("reject")==0){                
                if (doe.getRenewedOrderId()==null)
                                doe.setDrugOrderStatus(DrugOrderStatus.REJECTEDPHARMACIST.ordinal());
                            else
                                doe.setDrugOrderStatus(DrugOrderStatus.RENEWEDREJECTEDPHARMACIST.ordinal());
                
            }
        
        doe.setPharmacistInstruction(pharmInstructions);
        doe.setPharmacistDrugDispensedDate(new Date());
        doe.setPharmacist(Context.getAuthenticatedUser());
        
        
        service().saveDrugOrderExtension(doe);                
        removeFilledOrders();        
        return new ModelAndView("redirect://module/dominicamodule/pharmacyLandingPage.form");

    }

}
