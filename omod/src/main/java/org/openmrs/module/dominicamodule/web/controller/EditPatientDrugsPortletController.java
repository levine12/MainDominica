/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openmrs.module.dominicamodule.web.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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
import org.openmrs.Person;
import org.openmrs.User;
import org.openmrs.activelist.ActiveListItem;
import org.openmrs.activelist.Allergy;
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
import org.openmrs.validator.DrugOrderValidator;
import org.openmrs.validator.OrderValidator;
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
public class EditPatientDrugsPortletController {
    
    OrderType drugOrderType = new OrderType(OpenmrsConstants.ORDERTYPE_DRUG);
    
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
        binder.registerCustomEditor(Double.class, new CustomNumberEditor(
                Double.class, true));
        binder.registerCustomEditor(User.class, new UserEditor());
        binder.registerCustomEditor(Patient.class, new PatientEditor());
        binder.registerCustomEditor(Encounter.class, new EncounterEditor());
        binder.registerCustomEditor(Drug.class, new DrugEditor());
    }
    
    ModelAndView populate(ModelAndView mav,DrugOrder order, DrugOrderExtension drugOrderExtension,Integer patientId){
            System.out.println("Here in populate ");
            mav.addObject("order", order);
            mav.addObject("drugOrderExtension", drugOrderExtension);
            mav.addObject("patientid", patientId);
            String[] dUnits = forSelect(DurationUnit.class);
            mav.addObject("dUnits", dUnits);
        
            mav.addObject("n_dUnits", dUnits.length-2);    
            List<String> freq = Frequency.getAllFullNames();
            mav.addObject("freq", freq);
            mav.addObject("n_freq", freq.size());
            //System.out.println("Frequency : "+freq.size());
            String[] units = forSelect(Units.class);
            mav.addObject("units", units);
            mav.addObject("n_units", units.length-2);
            //System.out.println("Dose Units : "+units.length);
            List<String> route = RouteOfAdministration.getAllFullNames();
            mav.addObject("route", route);
            mav.addObject("n_route", route.size());
            //System.out.println("Routes : "+route.size());
            Boolean renewReadOnly=false;
            if (drugOrderExtension.getRenewedOrderId()!=null){
                renewReadOnly=true;                
            }
            mav.addObject("renewReadOnly",renewReadOnly);
            
            Boolean orderEditable=true;
            if (drugOrderExtension.getDrugOrderStatus()!=null)
                    if (DrugOrderStatus.values()[drugOrderExtension.getDrugOrderStatus()]==DrugOrderStatus.FILLED
                        || DrugOrderStatus.values()[drugOrderExtension.getDrugOrderStatus()]==DrugOrderStatus.RENEWEDFILLED
                        || DrugOrderStatus.values()[drugOrderExtension.getDrugOrderStatus()]==DrugOrderStatus.REJECTED
                        || DrugOrderStatus.values()[drugOrderExtension.getDrugOrderStatus()]==DrugOrderStatus.REJECTEDPHARMACIST
                        || DrugOrderStatus.values()[drugOrderExtension.getDrugOrderStatus()]==DrugOrderStatus.COMPLETED){
                        orderEditable=false;
                    }
            mav.addObject("orderEditable", orderEditable);
            mav.addObject("today", Context.getDateFormat().format(new Date()));            
            mav.addObject("allergyList",getAllergensForPatient(patientId));
            System.out.println("Here in populate 2");
            return mav;        
    }
    
    Date getExpireDateFromDuration(Date date,DrugOrderExtension doe){
            Calendar cal = Calendar.getInstance();  
            cal.setTime(date);  
            switch (DurationUnit.values()[doe.getDurationUnit()]){
                case hours :  cal.add(Calendar.HOUR, doe.getDuration());
                    break;
                case days : cal.add(Calendar.DAY_OF_YEAR, doe.getDuration());
                        break;                   
                case weeks:cal.add(Calendar.WEEK_OF_YEAR, doe.getDuration());
                    break;
                case months :cal.add(Calendar.MONTH, doe.getDuration());
                    break;
                case years :cal.add(Calendar.YEAR, doe.getDuration());
                    break;
                        
                            
            }            
            return cal.getTime();
    }
    
    String parseDisplayDate (Date oldDate){
        String newForm="dd/MM/yyyy";
        //String oldForm="yyyy-MM-dd HH:mm:ss.S";
        
        SimpleDateFormat sdf=new SimpleDateFormat(newForm);
        String newDate=sdf.format(oldDate);
        return newDate;
    }
    
    String getAllergensForPatient(Integer patientID){
        Patient patient=Context.getPatientService().getPatient(patientID);
        List<ActiveListItem> allAllergyItems = Context.getActiveListService().getActiveListItems((Person) patient, Allergy.ACTIVE_LIST_TYPE);
        List<Allergy> allergies = new Vector<Allergy>();
        for (ActiveListItem item : allAllergyItems) {
            allergies.add((Allergy) item);
        }
              
        String concatenatedAllergyList=new String();
        int count=0;
        //List<Allergy> active = new ArrayList<Allergy>();
        for (Allergy item : allergies) {
            if (item.getEndDate() == null) {
        //        System.out.println("EditPatientDrugPortletController active allergy: " + item.getAllergen().getName().getName());
                concatenatedAllergyList=concatenatedAllergyList.concat(item.getAllergen().getName().getName());
                count++;
                if (allergies.size()!=count){
                    concatenatedAllergyList=concatenatedAllergyList.concat(",");
                }
            } 
        }
        
        System.out.println("Allergies : "+concatenatedAllergyList);
        return concatenatedAllergyList;
    }
  
     private List<List<Allergy>> separate(List<Allergy> ls) {
        List<Allergy> active = new ArrayList<Allergy>();
        List<Allergy> removed = new ArrayList<Allergy>();

        for (Allergy item : ls) {
            if (item.getEndDate() == null) {
                System.out.println("EditPatientDrugPortletController active allergy: " + item.getAllergen().getName().getName());
                active.add(item);
            } else {
                System.out.println("EditPatientDrugsPortletController ended allergy: " + item.getAllergen().getName().getName());
                removed.add(item);
            }
        }

        List<List<Allergy>> items = new ArrayList<List<Allergy>>(2);
        items.add(active);
        items.add(removed);
        return items;
    }
        
    @RequestMapping(value = "/module/dominicamodule/editpatientdrugs.form")
    ModelAndView getDrugOrderDetails(@RequestParam(value = "orderId", required = false) Integer orderId,
                                     @RequestParam(value = "patientId", required = false) Integer patientId,
                                     @RequestParam(value = "renew", required = false) String renew){
        
       System.out.println("Here in edit controller :  "+ orderId);
        if (orderId==null){
            ModelAndView mav = new ModelAndView("/module/dominicamodule/editPatientDrugs");            
            DrugOrder drugOrder= new  DrugOrder();
            //drugOrder.setStartDate(new Date());
            mav.addObject("orderStartDate","");
            return populate(mav,drugOrder,new DrugOrderExtension(),patientId);        
        }
        System.out.println("Here in edit controller after new order setup :  "+ orderId);
        if (renew.compareTo("true")==0){
            System.out.println("Here in edit controller inside renew order setup :  "+ orderId);
            ModelAndView mav = new ModelAndView("/module/dominicamodule/editPatientDrugs");        
            DrugOrder oldOrder=(DrugOrder)Context.getOrderService().getOrder(orderId,DrugOrder.class);      
            DrugOrderExtension oldDoe=service().getDrugOrderExtensionByOrderId(orderId);
            DrugOrder drugOrder= new  DrugOrder();
            DrugOrderExtension drugOrderExtension= new DrugOrderExtension();
            
            drugOrderExtension.setRenewedOrderId(orderId);
            drugOrderExtension.setRenewOrderDate(new Date());
            drugOrderExtension.setDuration(oldDoe.getDuration());
            drugOrderExtension.setDurationUnit(oldDoe.getDurationUnit());
            drugOrderExtension.setRouteOfAdministration(oldDoe.getRouteOfAdministration());
            
            drugOrder.setStartDate(oldOrder.getStartDate());            
            drugOrder.setConcept(oldOrder.getConcept());
            drugOrder.setDose(oldOrder.getDose());
            drugOrder.setUnits(oldOrder.getUnits());
            drugOrder.setFrequency(oldOrder.getFrequency());
            drugOrder.setQuantity(oldOrder.getQuantity());
            drugOrder.setInstructions(oldOrder.getInstructions());
                                    
            patientId=oldOrder.getPatient().getId();
            System.out.println("Here in edit controller inside renew order setup end :  "+ orderId);
            mav.addObject("orderStartDate",parseDisplayDate(drugOrder.getStartDate()));
            return populate(mav,drugOrder,drugOrderExtension,patientId);
        }
        System.out.println("Here in edit controller after renew order setup :  "+ orderId);
        DrugOrder order=(DrugOrder)Context.getOrderService().getOrder(orderId,DrugOrder.class);      
        DrugOrderExtension drugOrderExtension= service().getDrugOrderExtensionByOrderId(orderId);
        
        patientId=order.getPatient().getId();
        ModelAndView mav = new ModelAndView("/module/dominicamodule/editPatientDrugs");        
        System.out.println("Here in edit controller after edit order setup before populate :  "+ orderId);
        mav.addObject("orderStartDate",order.getStartDate());
        return populate(mav, order, drugOrderExtension,patientId);                
    }
    
    @RequestMapping(value = "/module/dominicamodule/editpatientdrugs.form",method = RequestMethod.POST,params="saveOrder")
    public ModelAndView postSave(HttpServletRequest request,
                                @ModelAttribute("order") DrugOrder order, BindingResult oErrors,
                                @ModelAttribute("drugOrderExtension") DrugOrderExtension drugOrderExtension, BindingResult doeErrors,
                                @RequestParam(value = "orderId",required = false) Integer orderID,
                                @RequestParam(value = "doeId",required = false) Integer doeID) {
        Integer patientId = Integer.valueOf(request.getParameter("patientId"));        
//        Integer orderId = Integer.valueOf(request.getParameter("orderId"));
//        Integer doeId = Integer.valueOf(request.getParameter("doeId"));
        Patient p = Context.getPatientService().getPatient(patientId);
        System.out.println("Here in controller post edit/add method : OrderId : "+order.getId()+ " ExtensionId : "+ drugOrderExtension.getId());
        if (doeID!=null && orderID!=null){
            order.setId(orderID);
            drugOrderExtension.setId(doeID);
        }
        
        new OrderValidator().validate(order, oErrors);
        new DrugOrderValidator().validate(order, oErrors);
        if (doeErrors.hasErrors() || oErrors.hasErrors()){
            System.out.println("Here before checks");
        }        
        if (order == null) {            
            System.out.println("In Pst =Null");
        } else {
            try {
                 if (request.getParameter("saveOrder")!=null) {
                        //order.setUnits(request.getParameter("units"));
                        System.out.println("Drug Name: " + order.getConcept().getName().getName());                        
                        String startdate=request.getParameter("orderStartDate");
                        System.out.println("From param start date : "+startdate);
                        Date orderStartDate=null;
          //              if (drugOrderExtension.getRenewedOrderId()==null){
                        orderStartDate= new SimpleDateFormat("dd/MM/yyyy",Locale.ENGLISH).parse(startdate); 
           //             }
           //             else
           //             {
           //                 orderStartDate= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S",Locale.ENGLISH).parse(startdate); 
           //             }
                        order.setStartDate(orderStartDate);                
                        System.out.println("Start Date: " + order.getStartDate());
                        System.out.println("Dose : " + order.getDose());
                        System.out.println("Units: " + order.getUnits());
                        System.out.println("Frequency: " + order.getFrequency());
                        System.out.println("Instructions: " + order.getInstructions());
                        System.out.println("route: " + RouteOfAdministration.values()[drugOrderExtension.getRouteOfAdministration()]);
                            System.out.println("Duration time: " + drugOrderExtension.getDuration() +" " +DurationUnit.values()[drugOrderExtension.getDurationUnit()]);
                        //System.out.println("Encounter : " + order.getEncounter().getEncounterDatetime().toString());
                        System.out.println("Patient: " + patientId);                        
                        order.setPatient(p);
                        order.setOrderType(drugOrderType);                        
                        order.setOrderer(Context.getAuthenticatedUser());
                        Date date=order.getStartDate();
                        order.setAutoExpireDate(getExpireDateFromDuration(order.getStartDate(),drugOrderExtension));
                        
                        Order order2=Context.getOrderService().saveOrder(order);                        
                        System.out.println("abcd : here2");                        
                        if (order.getConcept().getDescription().getDescription().contains("Dominica restricted drug")){
                            if (drugOrderExtension.getRenewedOrderId()==null)
                                drugOrderExtension.setDrugOrderStatus(DrugOrderStatus.AWAITINGAPPROVAL.ordinal());
                            else
                                drugOrderExtension.setDrugOrderStatus(DrugOrderStatus.RENEWEDAWAITINGAPPROVAL.ordinal());
                            
                        }
                        else {
                            if (drugOrderExtension.getRenewedOrderId()==null)
                                drugOrderExtension.setDrugOrderStatus(DrugOrderStatus.NOAPPROVALNEEDED.ordinal());
                            else
                                drugOrderExtension.setDrugOrderStatus(DrugOrderStatus.RENEWEDNOAPPROVALNEEDED.ordinal());
                        }                        

                        drugOrderExtension.setOrderId(order2.getOrderId());    
                        System.out.println("In edit tttyyy controller : with orderID:"+drugOrderExtension.getOrderId()+" "+drugOrderExtension.getId()+ " "+order.getId());
                        service().saveDrugOrderExtension(drugOrderExtension);                        
                        request.getSession().setAttribute(WebConstants.OPENMRS_MSG_ATTR,"Order.drug.saved");
                }
                
            } catch (Exception e) {
                System.out.println("*****Could not save order******: " + e);
                request.getSession().setAttribute(WebConstants.OPENMRS_MSG_ATTR,"Error Saving Order : Please FIll all the fields.");
            }

        }
        List<DrugOrder> orders = new Vector<DrugOrder>();
        orders = Context.getOrderService().getDrugOrdersByPatient(p);
        for (Order ord : orders) {
            System.out.println("ORDER: " + ord.getConcept().getName().getName());
        }     
                
        return new ModelAndView("redirect:/patientDashboard.form?patientId="+patientId);

    }
    
    @RequestMapping(value = "/module/dominicamodule/editpatientdrugs.form",method = RequestMethod.POST,params="voidDrugOrder")
    public ModelAndView postVoid(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("order") DrugOrder order, BindingResult oErrors) {
        Integer patientId = Integer.valueOf(request.getParameter("patientId"));
        Patient p = Context.getPatientService().getPatient(patientId);
        System.out.println("In post Void order controller method.");
        new OrderValidator().validate(order, oErrors);
        if (request.getParameter("voidDrugOrder")!=null)
                {
                    Integer orderID=Integer.valueOf(request.getParameter("orderId"));
                    DrugOrder order_void=Context.getOrderService().getOrder(orderID,DrugOrder.class);
                    System.out.println("Voiding Order :"+order.getId()+" Void Reason: "+order.getVoidReason());
                    Context.getOrderService().voidOrder(order_void, order.getVoidReason());
                    DrugOrderExtension doe = service().getDrugOrderExtensionByOrderId(order.getId());
                    doe.setDrugOrderStatus(DrugOrderStatus.VOIDED.ordinal());
                    service().saveDrugOrderExtension(doe);
                    request.getSession().setAttribute(WebConstants.OPENMRS_MSG_ATTR,"Order.voidedSuccessfully");
                }
        List<DrugOrder> orders = new Vector<DrugOrder>();
        orders = Context.getOrderService().getDrugOrdersByPatient(p);
                
        return new ModelAndView("redirect:/patientDashboard.form?patientId="+patientId);
        
    }
    
    @RequestMapping(value = "/module/dominicamodule/editpatientdrugs.form",method = RequestMethod.POST,params="discontinueOrder")
    public ModelAndView postDiscontinue(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("order") DrugOrder order, BindingResult oErrors) {
        Integer patientId = Integer.valueOf(request.getParameter("patientId"));
        Patient p = Context.getPatientService().getPatient(patientId);
        System.out.println("In post Discontinue order controller method.");
        new OrderValidator().validate(order, oErrors);
        if (request.getParameter("discontinueOrder")!=null)
                {
                    Integer orderID=Integer.valueOf(request.getParameter("orderId"));
                    DrugOrder order_disc=Context.getOrderService().getOrder(orderID,DrugOrder.class);
                    System.out.println("Discontinue Order :"+order.getId() + " Discontinued Date : " +order.getDiscontinuedDate() + " Discontinued Reason : " + order.getDiscontinuedReason().getName().getName() );
                    Context.getOrderService().discontinueOrder(order_disc,  order.getDiscontinuedReason(),order.getDiscontinuedDate());                        
                    DrugOrderExtension doe = service().getDrugOrderExtensionByOrderId(order.getId());
                    doe.setDrugOrderStatus(DrugOrderStatus.DISCONTINUED.ordinal());
                    service().saveDrugOrderExtension(doe);
                    request.getSession().setAttribute(WebConstants.OPENMRS_MSG_ATTR,"Order.discontinuedSuccessfully");
                }
        List<DrugOrder> orders = new Vector<DrugOrder>();
        orders = Context.getOrderService().getDrugOrdersByPatient(p);
                
        return new ModelAndView("redirect:/patientDashboard.form?patientId="+patientId);
        
    }
    
    @RequestMapping(value = "/module/dominicamodule/editpatientdrugs.form",method = RequestMethod.POST,params="undiscontinueOrder")
    public ModelAndView postUnDiscontinue(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("order") DrugOrder order, BindingResult oErrors) {
        Integer patientId = Integer.valueOf(request.getParameter("patientId"));
        Patient p = Context.getPatientService().getPatient(patientId);
        System.out.println("In post Undiscontinue order controller method.");
        new OrderValidator().validate(order, oErrors);
        if (request.getParameter("undiscontinueOrder")!=null)
                {
                    Integer orderID=Integer.valueOf(request.getParameter("orderId"));
                    DrugOrder order_undisc=Context.getOrderService().getOrder(orderID,DrugOrder.class);
                    System.out.println("Undiscontinue Order :"+order.getId()); 
                    Context.getOrderService().undiscontinueOrder(order_undisc);
                    DrugOrderExtension doe = service().getDrugOrderExtensionByOrderId(order.getId());
                    doe.setDrugOrderStatus(DrugOrderStatus.UNDISCONTINUED.ordinal());
                    service().saveDrugOrderExtension(doe);
                    request.getSession().setAttribute(WebConstants.OPENMRS_MSG_ATTR,"Order.undiscontinuedSuccessfully");
                }
        List<DrugOrder> orders = new Vector<DrugOrder>();
        orders = Context.getOrderService().getDrugOrdersByPatient(p);
                
        return new ModelAndView("redirect:/patientDashboard.form?patientId="+patientId);
        
    }

}
