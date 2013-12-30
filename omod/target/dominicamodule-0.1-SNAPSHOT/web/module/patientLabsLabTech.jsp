<%@ include file="/WEB-INF/template/include.jsp"%>

<%@ include file="/WEB-INF/template/header.jsp"%>

<!-- module/dominicamodule/patientLabsLabTech.form  -->

<openmrs:htmlInclude file="/scripts/jquery/jquery.min.js" />
<openmrs:htmlInclude file="/dwr/engine.js" ></openmrs:htmlInclude>
<openmrs:htmlInclude file="/dwr/util.js" ></openmrs:htmlInclude>

<script src="<openmrs:contextPath/>/dwr/interface/DWRMyConceptSetService.js"></script>



<spring:hasBindErrors name="order">
    <openmrs:message code="fix.error"/>
    <br />
</spring:hasBindErrors>


<SCRIPT LANGUAGE="JavaScript">

    $j(document).ready(function() {

        $j("button[name='fillOrder']").click(function() {

            var orderId = this.value;
            document.getElementById("orderId").value = orderId;
            var x;
            var r = confirm("Confirm Lab Order Processing");
            if (r == true)
            {
                           document.forms["myform"].submit();
            }
 
        });
    });

</SCRIPT>

<span class="boxHeader">Current Lab Orders</span>
<div class="box">
    <form method="post" id="myform">
        <table cellpadding="5" >
            <tr>
                <th></th>
                <th>Patient</th>
                <th> Ordering Physician</th>
                <th> Date </th>
                <th> Instructions </th>
                <th> Lab Set </th>
                <th> Lab Tests </th>
            </tr>
            <c:forEach var="laborder" items="${current_orders}">
                <tr>
                    <td><button type="button" name="fillOrder"  type="submit" value="${laborder.orderId}" />Process</button></td> 
                    <td>${laborder.patient.givenName} &nbsp;${laborder.patient.familyName}</td>
                    <td>${laborder.orderingPhysician.givenName} &nbsp; ${laborder.orderingPhysician.familyName}</td>
                    <td>${laborder.orderDate}</td>
                    <td>${laborder.orderInstructions} </td>
                    <td> <b>${laborder.labSet.name}</b></td>
                    <td>
                <c:forEach var="labtest" items="${laborder.labTests}">
                    ${labtest.name}, &nbsp; &nbsp;
                </c:forEach>
                </td>
                </tr>
            </c:forEach>
        </table>
        <input type="hidden" id="orderId" name="orderId" />  
    </form>
</div>
<br><br>


<%@ include file="/WEB-INF/template/footer.jsp" %>