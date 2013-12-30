package org.openmrs.module.dominicamodule.web.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.openmrs.Concept;
import org.openmrs.Drug;
import org.openmrs.DrugOrder;
import org.openmrs.Encounter;
import org.openmrs.Order;
import org.openmrs.OrderType;

import org.openmrs.Patient;
import org.openmrs.User;
import org.openmrs.api.APIException;
import org.openmrs.api.OrderService;
import org.openmrs.api.context.Context;
import org.openmrs.module.dominicamodule.DrugOrderExtension;
import org.openmrs.module.dominicamodule.DrugOrderExtension.DrugOrderStatus;
import org.openmrs.module.dominicamodule.DrugOrderExtension.DurationUnit;
import org.openmrs.module.dominicamodule.DrugOrderExtension.Frequency;
import org.openmrs.module.dominicamodule.DrugOrderExtension.RouteOfAdministration;
import org.openmrs.module.dominicamodule.DrugOrderExtension.Units;
import org.openmrs.module.dominicamodule.DrugOrderExtensionService;
import org.openmrs.propertyeditor.ConceptEditor;
import org.openmrs.propertyeditor.DrugEditor;
import org.openmrs.propertyeditor.EncounterEditor;
import org.openmrs.propertyeditor.OrderTypeEditor;
import org.openmrs.propertyeditor.PatientEditor;
import org.openmrs.propertyeditor.UserEditor;
import org.openmrs.util.OpenmrsConstants;
import org.openmrs.web.WebConstants;
import org.springframework.beans.propertyeditors.CustomBooleanEditor;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
//import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class PatientDrugsPortletController {

    OrderType drugOrderType = new OrderType(OpenmrsConstants.ORDERTYPE_DRUG);
    
    static DrugOrderExtensionService service() {
		return Context.getService(DrugOrderExtensionService.class);
	}

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

    @RequestMapping(method = RequestMethod.GET)
    protected ModelAndView get(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("Here in controller get method.");

        Integer patientId = Integer.valueOf(request.getParameter("patientId"));
        Patient p = Context.getPatientService().getPatient(patientId);
        /*
         ModelAndView mav = new ModelAndView("/module/dominicamodule/portlets/patientDrugs");
         mav.addObject("order", order);
         return mav;
         */

        List<DrugOrder> orders = new Vector<DrugOrder>();
        List<DrugOrderExtension> drugOrderExtensions =new Vector<DrugOrderExtension>();
        List<String> duration=new Vector<String>();
        List<DurationUnit> durationUnits=new Vector<DurationUnit>();
        List<String> route=new Vector<String>();
        List<String> pharmacist=new Vector<String>();
        List<String> drugstatus=new Vector<String>();
        List<String> frequency=new Vector<String>();
        List<Units> units=new Vector<Units>();
        List<String> approved = new Vector<String>();
        List<String> pharmInstructions = new Vector<String>();
        List<String> approvedInstructions = new Vector<String>();
        List<String> qtyDispensed = new Vector<String>();
        
        orders = Context.getOrderService().getDrugOrdersByPatient(p);
        
        for (DrugOrder ord : orders) {
            System.out.println("ORDER: " + ord.getConcept().getName().getName());
            drugOrderExtensions.add(service().getDrugOrderExtensionByOrderId(ord.getOrderId()));
            frequency.add(Frequency.getAllFullNames().get(Integer.valueOf(ord.getFrequency())));
            units.add(Units.values()[Integer.valueOf(ord.getUnits())]);
        }
        
        for (DrugOrderExtension doe : drugOrderExtensions){
            duration.add(doe.getDuration().toString());
            
            durationUnits.add(DurationUnit.values()[doe.getDurationUnit()]);            
            route.add(RouteOfAdministration.getAllFullNames().get(doe.getRouteOfAdministration()));
            
            drugstatus.add(DrugOrderStatus.getAllFullNames().get(doe.getDrugOrderStatus()));
            if (doe.getPharmacist()!=null){
                    pharmacist.add(doe.getPharmacist().getPersonName().getFullName() + " on " + doe.getPharmacistDrugDispensedDate()); 
                    pharmInstructions.add(doe.getPharmacistInstruction());
                    if (doe.getPharmacistDrugDispensedQuantity()!=null)
                        qtyDispensed.add(doe.getPharmacistDrugDispensedQuantity().toString());
                    else 
                        qtyDispensed.add("");
            }
            else{
                   pharmacist.add("");          
                   pharmInstructions.add("");
                   qtyDispensed.add("");
            }
            if (doe.getApprovedByUser()!=null){
                approved.add(doe.getApprovedByUser().getPersonName().getFullName() + " on " +doe.getApprovedDate());
                approvedInstructions.add(doe.getApprovingUserInstructions());
            }
            else{
                   approved.add("NA");
                   approvedInstructions.add("NA");
            }
        }
////        
        ModelMap model = new ModelMap();
        model.put("orders", orders);
        model.put("drugOrderExtensions", drugOrderExtensions);
        DrugOrder order2 = new DrugOrder();
        model.put("order", order2);
        model.put("patientId",patientId);
        model.put("duration", duration);
        model.put("durationUnits", durationUnits);
        model.put("route", route);
        model.put("pharmacist", pharmacist);
        model.put("drugstatus",drugstatus);
        model.put("frequency",frequency);
        model.put("units",units);
        model.put("approved",approved);
        model.put("approvedInstructions",approvedInstructions);
        model.put("pharmacistInstructions", pharmInstructions);
        model.put("qtyDispensed",qtyDispensed);
        return new ModelAndView("/module/dominicamodule/portlets/patientDrugs", model);

    }

//    @ModelAttribute("order")
//    protected DrugOrder getDrugOrder(HttpServletRequest request){
//        return (DrugOrder)request.getAttribute("order"); 
//    }
//    
//    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
//        ModelMap model = new ModelMap();
//       // request.
//        String patientIdStr = (String) request.getParameter("patientId");
//        System.out.println("PatiendID : "+patientIdStr);
//        DrugOrder order=getDrugOrder(request);
//        if (request.getMethod().compareTo("GET")==0){
//            return get();
//        }
//        else
//        {
//            return post(order);
//        }
//        
////        //Integer patientId = (Integer) request.getAttribute("patientId");
////        String patientIdStr = (String) request.getParameter("patientId");
////        Integer patientId = Integer.valueOf(patientIdStr);
////        Patient p = Context.getPatientService().getPatient(patientId);
////        System.out.println("PatientDrugsPortletController handleRequest **************************\n"
////                + "Patient id: " + patientIdStr);
////        
////        System.out.println("ORDER\n" +
////                "concept_id: " + request.getParameter("concept_id_other") + "\n" +
////                "instructions: " + request.getParameter("instructions") + "\n" +
////                "startDate: " + request.getParameter("startDate") + "\n" +
////                "autoExpireDate: " + request.getParameter("autoExpireDate") + "\n" +
////                "encounter_id_selection: " + request.getParameter("encounter_id_selection") + "\n" +
////                "encounter_id: " + request.getParameter("encounter_id") + "\n" +
////                "dose: " + request.getParameter("dose") + "\n" +
////                "units: " + request.getParameter("units"));
////        model.put("patient", p);
////       
////        /*
////        ArrayList<String> notes = new ArrayList<String>();
////        
////        It works with a few notes; also works with no notes -it just doesn't go through loop
////        notes.add(patientIdStr);
////        notes.add("abc");
////        */
////        
////        List<DrugOrder> orders = new Vector<DrugOrder>();
////        
////        // Following works for no orders, but I get an error from entering a new order
////        // still places order since when I go to manage drug order I see prior orders
////        orders = Context.getOrderService().getDrugOrdersByPatient(p);
////        
////        model.put("orders",orders);
////        return new ModelAndView("/module/dominicamodule/portlets/patientDrugs", model);
//    }
    @RequestMapping(method = RequestMethod.POST,params="saveOrder")
    public ModelAndView postSave(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("order") DrugOrder order) {
        Integer patientId = Integer.valueOf(request.getParameter("patientId"));
        Patient p = Context.getPatientService().getPatient(patientId);
        System.out.println("Here in co  ntroller post method.");
        if (order == null) {            
            System.out.println("In Pst =Null");
        } else {
            try {
//                if (request.getParameter("voidDrugOrder")!=null)
//                {
//                    Integer orderID=Integer.valueOf(request.getParameter("orderId"));
//                    DrugOrder order_void=Context.getOrderService().getOrder(orderID,DrugOrder.class);
//                    System.out.println("Voiding Order :"+orderID+" Void Reason: "+order.getVoidReason());
//                    Context.getOrderService().voidOrder(order_void, order.getVoidReason());
//                    request.getSession().setAttribute(WebConstants.OPENMRS_MSG_ATTR,"Order.voidedSuccessfully");
//                }
                 if (request.getParameter("saveOrder")!=null) {
                        order.setUnits(request.getParameter("units"));
                        System.out.println("Concept: " + order.getConcept().getId());
                        System.out.println("Start Date: " + order.getStartDate());
                        System.out.println("Dose : " + order.getDose());
                        System.out.println("Units: " + order.getUnits());
                        System.out.println("Frequency: " + order.getFrequency());
                        System.out.println("Instructions: " + order.getInstructions());
                            System.out.println("Duration time: " + request.getParameter("durationperiod"));
                        //System.out.println("Encounter : " + order.getEncounter().getEncounterDatetime().toString());
                        System.out.println("Patient: " + patientId);
                        order.setDiscontinuedReasonNonCoded(request.getParameter("durationperiod"));
                        order.setPatient(p);
                        order.setOrderType(drugOrderType);
                        order.setOrderer(Context.getUserContext().getAuthenticatedUser());
                        Context.getOrderService().saveOrder(order);
                }
                
            } catch (Exception e) {
                System.out.println("*****Could not save order******: " + e);
            }

        }

        List<DrugOrder> orders = new Vector<DrugOrder>();

////        
////        // Following works for no orders, but I get an error from entering a new order
////        // still places order since when I go to manage drug order I see prior orders
        orders = Context.getOrderService().getDrugOrdersByPatient(p);
        for (Order ord : orders) {
            System.out.println("ORDER: " + ord.getConcept().getName().getName());
        }
////        
        
        
        ModelMap model = new ModelMap();
        model.put("orders", orders);
        DrugOrder order2 = new DrugOrder();
        model.put("order", new DrugOrder());  
//        ModelAndView mav=new ModelAndView();
//        mav.addObject("orders", orders);
//        mav.addObject("order", new DrugOrder());
//        mav.setViewName("redirect:/patientDashboard.form?patientId="+patientId);
//        return mav;
        return new ModelAndView("/module/dominicamodule/portlets/patientDrugs", model);
        //return get(request,response);
        /*
         ModelAndView mav = new ModelAndView("/module/dominicamodule/portlets/patientDrugs");
         DrugOrder order2 = new DrugOrder();
         //mav.addObject("order", order2);
         mav.addObject("orders", orders);
         */

        //return mav;
    }

//    @RequestMapping(method = RequestMethod.POST,params="voidDrugOrder")
//    public ModelAndView postVoid(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("order") DrugOrder order) {
//        Integer patientId = Integer.valueOf(request.getParameter("patientId"));
//        Patient p = Context.getPatientService().getPatient(patientId);
//        System.out.println("In post Void order controller method.");
//        if (request.getParameter("voidDrugOrder")!=null)
//                {
//                    Integer orderID=Integer.valueOf(request.getParameter("orderId"));
//                    DrugOrder order_void=Context.getOrderService().getOrder(orderID,DrugOrder.class);
//                    System.out.println("Voiding Order :"+orderID+" Void Reason: "+order.getVoidReason());
//                    Context.getOrderService().voidOrder(order_void, order.getVoidReason());
//                    request.getSession().setAttribute(WebConstants.OPENMRS_MSG_ATTR,"Order.voidedSuccessfully");
//                }
//        List<DrugOrder> orders = new Vector<DrugOrder>();
//        orders = Context.getOrderService().getDrugOrdersByPatient(p);
//        
//        ModelMap model = new ModelMap();
//        model.put("orders", orders);
//        DrugOrder order2 = new DrugOrder();
//        model.put("order", new DrugOrder());  
//        return new ModelAndView("/module/dominicamodule/portlets/patientDrugs", model);
//        
//    }
    
}
