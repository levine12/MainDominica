/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openmrs.module.dominicamodule;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.openmrs.User;

/**
 *
 * @author akhil
 */
public class DrugOrderExtension {
        
    public DrugOrderExtension(Integer id, String uid, Integer orderId, Integer drugOrderStatus, Date approvedDate, User approvedByUser, Integer routeOfAdministration, Integer duration, Integer durationUnit, String pharmacistInstruction, Integer pharmacistDrugDispensedQuantity, User pharmacist, Date pharmacistDrugDispensedDate, Integer renewedOrderId, String renewReason, Date renewOrderDate, String approvingUserInstructions) {
        this.id = id;
        this.uid = uid;
        this.orderId = orderId;
        this.drugOrderStatus = drugOrderStatus;
        this.approvedDate = approvedDate;
        this.approvedByUser = approvedByUser;
        this.routeOfAdministration = routeOfAdministration;
        this.duration = duration;
        this.durationUnit = durationUnit;
        this.pharmacistInstruction = pharmacistInstruction;
        this.pharmacistDrugDispensedQuantity = pharmacistDrugDispensedQuantity;
        this.pharmacist = pharmacist;
        this.pharmacistDrugDispensedDate = pharmacistDrugDispensedDate;
        this.renewedOrderId = renewedOrderId;
        this.renewReason = renewReason;
        this.renewOrderDate = renewOrderDate;
        this.approvingUserInstructions = approvingUserInstructions;
    }
    
    public enum DrugOrderStatus {
           
		APPROVED("Approved"),
                AWAITINGAPPROVAL("Awaiting Approval"),
                NOAPPROVALNEEDED("No Approval Needed"),
                REJECTED("Rejected By Supervising Physician"),
                REJECTEDPHARMACIST("Rejected by Pharmacist"),                
                DISCONTINUED("Discontinued"),
                COMPLETED("Completed"),
                UNDISCONTINUED("Undiscontinued"),
                VOIDED("Voided"),
                FILLED("Order Filled"),
                RENEWEDNOAPPROVALNEEDED("Order Renewed : No Approval Needed"),
                RENEWEDAWAITINGAPPROVAL("Order Renewed : Awaiting Approval"),
                RENEWEDAPPROVED("Order Renewed : Aprroved"),
                RENEWEDREJECTED("Order Renewed : Rejected By Supervising Physician"),
                RENEWEDREJECTEDPHARMACIST("Order Renewed : Rejected By Pharmacist"),
                RENEWEDFILLED("Order Renewed : Filled ");
                
                final private String fullName;
                DrugOrderStatus (String fullname){
                    this.fullName=fullname;
                }
                public String getFullName(){
                    return this.fullName;
                }
                public static List<String> getAllFullNames()
                {
                    List<String> fullNameList=new ArrayList<String>();
                    for (DrugOrderStatus d:DrugOrderStatus.values())
                        fullNameList.add(d.getFullName());
                    return fullNameList;
                }
	}
    
    public enum RouteOfAdministration {
        ORALLY("Orally"),
        SUBCUTANEOUSLY("Subcutaneously"),
        INTRAMUSCULARLY("Intramuscularly"),
        INTRAVENOUSLY("Intravenously"),
        RECTALLY("Rectally"),
        INTRARECTALLY("Intrathecally"),
        PEREYE("Per Eye"),
        PEREAR("Per Ear"),
        PERNOSTRIL("Per Nostril"),
        TOPICALLY("Topically");
        
        final private String fullName;
                RouteOfAdministration (String fullname){
                    this.fullName=fullname;
                }
                public String getFullName(){
                    return this.fullName;
                }
                public static List<String> getAllFullNames()
                {
                    List<String> fullNameList=new ArrayList<String>();
                    for (RouteOfAdministration d:RouteOfAdministration.values())
                        fullNameList.add(d.getFullName());
                    return fullNameList;
                }
        
    }
    
    public enum DurationUnit {
        hours,days,weeks,months,years;
    }
    
    public enum Units {
        mg,oz,ml,tablets;
    }
           
    
    public enum Frequency {
        	ONCE("Once per Day"),
                TWICE("Twice per day"),
                THRICE("Thrice per Day"),
                DAILY("Daily"),                
                TWICEDAILY("Twice Daily"),
                THRICEDAILY("Three Times Daily"),
                FOURDAILY("Four Times Daily"),
                FIFTEENMINUTES("Every 15 Minutes"),
                THIRTYMINUTES("Every 30 Minutes"),
                HOURLY("Hourly"),
                TWOHOURS("Every 2 Hours"),
                THREEHOURS("Every 3 Hours"),
                EVERYOTHERDAY("Every Other Day"),
                WEEKLY("Weekly"),
                MONTHLY("Monthly"),
                TWELVEWEEKS("Every 12 Weeks");
                
                final private String fullName;
                Frequency (String fullname){
                    this.fullName=fullname;
                }
                public String getFullName(){
                    return this.fullName;
                }
                public static List<String> getAllFullNames()
                {
                    List<String> fullNameList=new ArrayList<String>();
                    for (Frequency d:Frequency.values())
                        fullNameList.add(d.getFullName());
                    return fullNameList;
                }
    }
    
    Integer id;
    String uid;
    
    Integer orderId;
    
    Integer drugOrderStatus;    
    Date approvedDate;
    User approvedByUser;
    
    Integer routeOfAdministration;
    Integer duration;
    Integer durationUnit;
    
    String pharmacistInstruction;
    Integer pharmacistDrugDispensedQuantity;
    User pharmacist;
    Date pharmacistDrugDispensedDate;
    
    Integer renewedOrderId;
    String renewReason;
    
    Date renewOrderDate;
    String approvingUserInstructions;

    public void setRenewOrderDate(Date renewOrderDate) {
        this.renewOrderDate = renewOrderDate;
    }

    public void setApprovingUserInstructions(String approvingUserInstructions) {
        this.approvingUserInstructions = approvingUserInstructions;
    }

    public Date getRenewOrderDate() {
        return renewOrderDate;
    }

    public String getApprovingUserInstructions() {
        return approvingUserInstructions;
    }
    

    public DrugOrderExtension(){
        super();
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public void setDrugOrderStatus(Integer drugOrderStatus) {
        this.drugOrderStatus = drugOrderStatus;
    }

    public void setApprovedDate(Date approvedDate) {
        this.approvedDate = approvedDate;
    }

    public void setApprovedByUser(User approvedByUser) {
        this.approvedByUser = approvedByUser;
    }

    public void setRouteOfAdministration(Integer routeOfAdministration) {
        this.routeOfAdministration = routeOfAdministration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public void setDurationUnit(Integer durationUnit) {
        this.durationUnit = durationUnit;
    }

    public void setPharmacistInstruction(String pharmacistInstruction) {
        this.pharmacistInstruction = pharmacistInstruction;
    }

    public void setPharmacistDrugDispensedQuantity(Integer pharmacistDrugDispensedQuantity) {
        this.pharmacistDrugDispensedQuantity = pharmacistDrugDispensedQuantity;
    }

    public void setPharmacist(User pharmacist) {
        this.pharmacist = pharmacist;
    }

    public void setPharmacistDrugDispensedDate(Date pharmacistDrugDispensedDate) {
        this.pharmacistDrugDispensedDate = pharmacistDrugDispensedDate;
    }

    public void setRenewedOrderId(Integer renewedOrderId) {
        this.renewedOrderId = renewedOrderId;
    }

    public void setRenewReason(String renewReason) {
        this.renewReason = renewReason;
    }
    
    
    public Integer getId() {
        return id;
    }

    public String getUid() {
        return uid;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public Integer getDrugOrderStatus() {
        return drugOrderStatus;
    }

    public Date getApprovedDate() {
        return approvedDate;
    }

    public User getApprovedByUser() {
        return approvedByUser;
    }

    public Integer getRouteOfAdministration() {
        return routeOfAdministration;
    }

    public Integer getDuration() {
        return duration;
    }

    public Integer getDurationUnit() {
        return durationUnit;
    }

    public String getPharmacistInstruction() {
        return pharmacistInstruction;
    }

    public Integer getPharmacistDrugDispensedQuantity() {
        return pharmacistDrugDispensedQuantity;
    }

    public User getPharmacist() {
        return pharmacist;
    }

    public Date getPharmacistDrugDispensedDate() {
        return pharmacistDrugDispensedDate;
    }

    public Integer getRenewedOrderId() {
        return renewedOrderId;
    }

    public String getRenewReason() {
        return renewReason;
    }
    
    
}
