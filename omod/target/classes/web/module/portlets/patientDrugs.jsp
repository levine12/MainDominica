<%@ include file="/WEB-INF/template/include.jsp"%>
<openmrs:htmlInclude file="/scripts/calendar/calendar.js" />
<!--<openmrs:htmlInclude file="/scripts/jquery/jquery.min.js" />
<openmrs:htmlInclude file="/scripts/calendar/calendar.js" />-->

<spring:hasBindErrors name="order">
    <openmrs:message code="fix.error"/>
    <br />
</spring:hasBindErrors>


<c:if test="${fn:length(orders) == 0}">
    <!-- There are no notes in the "notes" variable -->
    <br><b>NO ORDERS</b></br>
</c:if>


<span class="boxHeader"><openmrs:message code="Order.drug.list.title"/></span>
<div id="drugTab" class="box">
    <table cellpadding="5">
        <tr>
            <th>Edit.</th>
            <th></th>
            <th> Drug Name</th>
            <th> <openmrs:message code="general.dateStart"/> </th>
        <th> Duration </th>
        <th> Dose </th>
        <th> <openmrs:message code="DrugOrder.units"/> </th>
        <th> <openmrs:message code="DrugOrder.frequency"/> </th>
        <th> <openmrs:message code="DrugOrder.quantity"/> </th>
        <th> <openmrs:message code="general.instructions" /> </th>
        <th> Route of Administration </th>
        <th> Drug Order Status </th>
        <th> Approving User </th>
        <th> Approving User Instructions </th>
        <th> Date Order Filled by Pharmacist </th>
        <th> Quantity Dispensed </th>
        <th> Pharmacist's Instructions </th>
        </tr>

        <c:forEach var="ord" items="${orders}" varStatus="count">
            <tr>
                <td><a href="module/dominicamodule/editpatientdrugs.form?orderId=${ord.orderId}&renew=false" class="drugOrderList">${count.count}</a></td>
                <td><a href="module/dominicamodule/editpatientdrugs.form?orderId=${ord.orderId}&renew=true" class="drugOrderList"><button type="button">Renew</button></a></td>
                <td>${ord.concept.name}</td>
                <td><openmrs:formatDate date="${ord.startDate}" format="dd/MM/yyyy" /></td>
            <td>${duration[count.count-1]} ${durationUnits[count.count-1]}</td>   <!-- duration e.g. 5 weeks -->
            <td>${ord.dose}</td>
                    <!-- CONCEPT ${ord.concept}<br> -->
            <td>${units[count.count-1]}</td>
            <td>${frequency[count.count-1]} </td>
            <td>${ord.quantity} </td>
            <td>${ord.instructions} </td>
            <td>${route[count.count-1]}</td>
            <td>${drugstatus[count.count-1]}</td>
            <td>${approved[count.count-1]}</td>
            <td>${approvedInstructions[count.count-1]}</td>
            <td> ${pharmacist[count.count-1]} </td>
            <td> ${qtyDispensed[count.count-1]}</td>
            <td>${pharmacistInstructions[count.count-1]}</td>
            </tr>

        </c:forEach>
    </table>
</div>            
<p><a href="module/dominicamodule/editpatientdrugs.form?patientId=${patientId}" class="newDrugOrder">Add New Drug Order</a>
    <input type="button" id="closeForm" value="Close" /></p>

<div id="drugOrderForm"></div>
    
<script type="text/javascript">
    var $j=jQuery.noConflict();                
        $j(document).ready(function() {
                $j('#closeForm').hide();
                $j('.drugOrderList').click(function(e) {
                         e.preventDefault();
                         var url=$j(this).attr('href');                            
                         $j('#drugOrderForm').load(url);
                         $j('#closeForm').show();
                         $j('#drugOrderForm').show();
                });     
                $j('.newDrugOrder').click(function(e) {
                         e.preventDefault();
                         var url=$j(this).attr('href');                            
                         $j('#drugOrderForm').load(url);
                         $j('#closeForm').show();
                         $j('#drugOrderForm').show();
                });
                $j('#closeForm').click(function(e){
                    $j('#drugOrderForm').hide();
                    $j('#closeForm').hide();
                });
                
                $('#orderForm').on('submit', function() {
                            // check validation
                            var valid=validateForm();

                            });

                            function validateForm(){
                                var dur=$j('#duration').val();
                                alert("check "+dur);
                                return true;
                            }
  });  
</script>    
