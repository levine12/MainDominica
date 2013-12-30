<%@ include file="/WEB-INF/template/include.jsp"%>
<div>
<form method="post" action="pharmacyLandingPage.form" class="box">
    <input type="hidden" name="orderId" value="${orderId}" />
                        <table>
                            <tr><td>Drug : </td>
                            <td>${drugname}</td></tr>
                            
                            <tr><td>Dosage : </td>
                            <td>${dose}</td></tr>
                            <tr><td>Process Drug Order : </td>
                                <td> <input type="radio" name="processValue" value="approve" checked> Approve</td>
                                <td> <input type="radio" name="processValue" value="reject"> Reject</td>
                            </tr>    
                            <tr><td>Quantity Dispensed : </td>
                                <td> <openmrs:fieldGen type="java.lang.Integer" formFieldName="qtyDispensed" val="${qty}"/> </td>
                            </tr>    
                            <tr><td>Pharmacist's Instructions : </td>
                                <td valign="bottom"> <textarea name="pharmInstructions" rows="4" cols="30"> </textarea> </td>
                            </tr> 
                            <tr><td><input type="submit" name="CompleteOrder" value="Complete Order"/></td> </tr>                               
                        </table>
</form>
</div>                            