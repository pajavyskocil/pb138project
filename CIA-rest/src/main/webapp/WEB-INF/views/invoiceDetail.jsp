<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="o" tagdir="/WEB-INF/tags" %>

<o:header title="${title}"/>
<c:choose>
    <c:when test="${action eq 'createInvoice'}">
        <c:set var="inputInvoiceType" value="required"/>
        <c:set var="inputIssued" value="max=${dateToday} required"/>
        <c:set var="inputDueTo" value="min=${dateToday} required"/>
        <c:set var="inputInvoicePrice" value="disabled"/>
    </c:when>
    <c:when test="${action eq 'editInvoice'}">
        <c:set var="inputInvoiceType" value="required"/>
        <c:set var="inputIssued" value="max=${dateToday} value=${invoice.issueDate} required"/>
        <c:set var="inputDueTo" value="min=${dateToday} value=${invoice.dueDate} required"/>
        <c:set var="inputItem" value="required"/>
        <c:set var="inputInvoicePrice" value="value=${invoice.price} disabled"/>
    </c:when>
    <c:otherwise>
        <c:set var="inputInvoiceType" value="disabled"/>
        <c:set var="inputIssued" value="value=${invoice.issueDate} disabled"/>
        <c:set var="inputDueTo" value="value=${invoice.dueDate} disabled"/>
        <c:set var="inputItem" value="disabled"/>
        <c:set var="inputInvoicePrice" value="value=${invoice.price} disabled"/>
    </c:otherwise>
</c:choose>

<div class="jumbotron">
    <c:if test="${not empty message}">
        <div class="row mb-2">
            <div class="alert alert-danger alert-dismissible col-md-6 offset-md-3 <c:out value='${alertType}'/>">
                <button class="close" data-dismiss="alert">&times;</button>
                <strong><c:out value="${message}"/></strong>
            </div>
        </div>
    </c:if>
    <form class="container details-form" method="POST" accept-charset="UTF-8" action="/accounting/<c:out value='${action}' />">
        <input type="hidden" name="id" value="<c:out value='${invoice.id}'/>"/>
        <h3 class="mb-3 text-center"><c:out value="${title}"/></h3>
        <div class="row mb-3 mb-2">
            <div class="col-md-8 input-group">
                <div class="input-group-prepend">
                    <span class="input-group-text">Payer/recipient</span>
                </div>
                <c:choose>
                    <c:when test="${invoice.invoiceType eq 'INCOME'}">
                        <c:set var="otherPersonId" value="${invoice.payer.id}"/>
                        <c:set var="otherPersonText" value="${invoice.payer.name} <${invoice.payer.email}>"/>
                    </c:when>
                    <c:when test="${invoice.invoiceType eq 'EXPENSE'}">
                        <c:set var="otherPersonId" value="${invoice.recipient.id}"/>
                        <c:set var="otherPersonText" value="${invoice.recipient.name} <${invoice.recipient.email}>"/>
                    </c:when>
                </c:choose>
                <c:choose>
                    <c:when test="${action eq 'deleteInvoice'}">
                        <input class="form-control" name="secondPerson" id="secondPerson" type="text" value="${otherPersonText}" disabled>
                    </c:when>
                    <c:otherwise>
                        <select class="form-control" id="secondPerson" name="secondPerson" required>
                            <c:forEach var="person" items="${persons}">
                                <c:set var="personText" value="${person.name} <${person.email}>"/>
                                <option value="${person.id}" <c:if test="${person.id eq otherPersonId}">selected</c:if>>
                                    <c:out value='${personText}'/>
                                </option>
                            </c:forEach>
                        </select>
                    </c:otherwise>
                </c:choose>
            </div>
            <div class="col-md-4 input-group">
                <div class="input-group-prepend">
                    <span class="input-group-text">Type</span>
                </div>
                <select class="form-control" name="type" <c:out value="${inputInvoiceType}"/>>
                    <option <c:if test="${(empty invoice.invoiceType) or (invoice.invoiceType eq 'INCOME')}"><c:out value="selected"/></c:if> value="income">Income</option>
                    <option <c:if test="${invoice.invoiceType eq 'EXPENSE'}"><c:out value="selected"/></c:if> value="expense">Expense</option>
                </select>
            </div>
        </div>
        <div class="row mb-3">
            <div class="col-md-6 input-group">
                <div class="input-group-prepend">
                    <span class="input-group-text">Issued on</span>
                </div>
                <input class="form-control" type="date" id="issued" name="issued" <c:out value="${inputIssued}"/>>
            </div>
            <div class="col-md-6 input-group">
                <div class="input-group-prepend">
                    <span class="input-group-text">Due to</span>
                </div>
                <input class="form-control" type="date" id="dueTo" name="dueTo" <c:out value="${inputDueTo}"/>>
            </div>
        </div>
        <div class="row mb-3">
            <div class="col-md-11 row">
                <div class="col-md-3">Name</div>
                <div class="col-md-5">Description</div>
                <div class="col-md-2">Count</div>
                <div class="col-md-1">Price</div>
            </div>
            <div class="col-md-1"><!-- SPACER --></div>
        </div>
        <c:choose>
        <c:when test="${action eq 'createInvoice'}">
            <div class="row mb-3 item-record">
                <div class="input-group col-md-11">
                    <input class="col-md-3 form-control" type="text" placeholder="Name" name="itemName[]" required>
                    <input class="col-md-4 form-control" type="text" placeholder="Description" name="itemDesc[]" required>
                    <input class="col-md-2 form-control item-count" type="number" min="1" placeholder="1" name="itemCount[]" required>
                    <input class="col-md-2 form-control item-price" type="number" step=".01" min="0.01" placeholder="0.01" name="itemPrice[]" required>
                    <div class="col-md-1 no-padd input-group-append">
                        <span class="input-group-text">&euro;</span>
                    </div>
                </div>
                <div class="col-md-1">
                    <span class="btn btn-danger btn-remove-item"><i class="fas fa-minus"></i></span>
                </div>
            </div>
        </c:when>
        <c:otherwise>
            <c:forEach var="item" items="${invoice.items}">
                <div class="row mb-3 item-record">
                    <div class="input-group col-md-11">
                        <input class="col-md-3 form-control" type="text" placeholder="Name" name="itemName[]" value="<c:out value='${item.name}'/>" <c:out value='${inputItem}'/>>
                        <input class="col-md-4 form-control" type="text" placeholder="Description" name="itemDesc[]" value="<c:out value='${item.description}'/>" <c:out value='${inputItem}'/>>
                        <input class="col-md-2 form-control item-count" type="number" min="1" placeholder="1" name="itemCount[]" value="<c:out value='${item.count}'/>" <c:out value='${inputItem}'/>>
                        <input class="col-md-2 form-control item-price" type="number" step=".01" min="0.01" placeholder="0.01" name="itemPrice[]" value="<c:out value='${item.price}'/>" <c:out value='${inputItem}'/>>
                        <div class="col-md-1 no-padd input-group-append">
                            <span class="input-group-text">&euro;</span>
                        </div>
                    </div>
                    <div class="col-md-1 btn-remove">
                        <span class="btn btn-danger btn-remove-item"><i class="fas fa-minus"></i></span>
                    </div>
                </div>
            </c:forEach>
        </c:otherwise>
        </c:choose>
        <div id="items-after" class="row mb-4">
            <div class="col-md-6 input-group">
                <div class="input-group-prepend">
                    <span class="input-group-text">Total</span>
                </div>
                <input class="form-control" type="text" id="price" name="price" <c:out value='${inputInvoicePrice}'/>>
            </div>
            <div class="col-md-1 offset-md-5">
                <span class="btn btn-success" id="btn-add-item"><i class="fas fa-plus"></i></span>
            </div>
        </div>
        <div class="form-group row details-btns">
            <input id="submit-form" type="submit" value="Confirm" class="col-md-3 offset-md-2 btn btn-danger">
            <a href="/accounting/invoices" class="col-md-3 offset-md-2 btn btn-light">Cancel</a>
        </div>
    </form>
</div>
<o:footer script="invoiceDetail"/>