<%@ include file="/WEB-INF/template/include.jsp"%>
<div>
<form method="post" action="approveRestrictedDrugOrders.form" class="box">
    <input type="hidden" name="orderId" value="${orderId}" />
                        <table>
                            <tr><td>Process Restricted Drug Order : </td>
                                <td> <input type="radio" name="processValue" value="approve" checked> Approve</td>
                                <td> <input type="radio" name="processValue" value="reject"> Reject</td>
                            </tr>    
                            <tr><td>Comments :  </td><td valign="bottom"><textarea name="comments" rows="4" cols="30"> </textarea> </td>
                            </tr> 
                            <tr><td><input type="submit" name="CompleteOrder" value="Complete Order"/></td> </tr>                               
                        </table>
</form>
</div>                            