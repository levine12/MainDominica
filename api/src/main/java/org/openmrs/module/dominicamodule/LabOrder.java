package org.openmrs.module.dominicamodule;

/**
 *
 * @author blevine
 */
public class LabOrder {

    private int isProcessed;
    private int labSetId;
    private String labTests;
    private String uuid;
    private Integer orderId; // the primary key colum

    public LabOrder() {

    }

    public int getIsProcessed() {
        return isProcessed;
    }

    public void setIsProcessed(int isProcessed) {
        this.isProcessed = isProcessed;
    }

    public int getLabSetId() {
        return labSetId;
    }

    public void setLabSetId(int labSetId) {
        this.labSetId = labSetId;
    }

    public String getLabTests() {
        return labTests;
    }

    public void setLabTests(String labTests) {
        this.labTests = labTests;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

}
