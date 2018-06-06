<%@ tag pageEncoding="UTF-8" %>
<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="invoices" required="true" type="java.util.Collection" %>
<%@ attribute name="invoicesType" required="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="row table-title mb-2">
    <div class="col-md-1 table-hider">
        <i class="btn btn-primary fas fa-angle-down" data-toggle="collapse" data-target="#<c:out value="${invoicesType}"/>-collapse"></i>
    </div>
    <h3><c:out value="${invoicesType}"/></h3>
</div>
<div class="row">
    <table class="table">
        <thead>
        <tr>
            <th>Id</th>
            <th>Type</th>
            <th>Payer / recipient</th>
            <th>Cost</th>
            <th class="width-100 text-center">Details</th>
            <th class="width-100 text-center">Edit</th>
            <th class="width-100 text-center">Delete</th>
        </tr>
        </thead>
        <tbody class="collapse show" id="<c:out value="${invoicesType}"/>-collapse">
        <c:forEach var="invoice" items="${invoices}">
            <tr class="record">
                <c:set var="items" value="${invoice.items}"/>
                <c:choose>
                    <c:when test="${invoice.invoiceType eq 'INCOME'}">
                        <c:set var="secondPersonText" value="${invoice.payer.name} <${invoice.payer.email}>"/>
                    </c:when>
                    <c:when test="${invoice.invoiceType eq 'EXPENSE'}">
                        <c:set var="secondPersonText" value="${invoice.recipient.name} <${invoice.recipient.email}>"/>
                    </c:when>
                </c:choose>
                <td class="id"><c:out value="${invoice.id}" /></td>
                <td class="type"><c:out value="${invoice.invoiceType}"/></td>
                <td class="secondPerson"><c:out value="${secondPersonText}"/></td>
                <td class="price"><c:out value="${invoice.price}"/></td>
                <td class="hidden">
                    <c:forEach var="item" items="${items}">
                        <div class="item">
                            <span class="item-name"><c:out value="${item.name}"/></span>
                            <span class="item-desc"><c:out value="${item.description}"/></span>
                            <span class="item-count"><c:out value="${item.count}"/></span>
                            <span class="item-price"><c:out value="${item.price}"/></span>
                        </div>
                    </c:forEach>
                </td>
                <td class="hidden issued"><c:out value="${invoice.issueDate}"/></td>
                <td class="hidden due-to"><c:out value="${invoice.dueDate}"/></td>
                <td class="width-100 text-center col-bl more"><i class="btn fas fa-info"></i></td>
                <td class="width-100 text-center col-bl"><a href="/accounting/editInvoice?id=<c:out value='${invoice.id}'/>"><i class="btn fas fa-edit"></i></a></td>
                <td class="width-100 text-center col-bl"><a href="/accounting/deleteInvoice?id=<c:out value='${invoice.id}'/>"><i class="btn fas fa-ban"></i></a></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>