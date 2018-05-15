<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="o" tagdir="/WEB-INF/tags" %>

<o:header title="${title}"/>
<c:choose>
    <c:when test="${action eq 'createInvoice'}">
        <c:set var="inputInvoiceType" value="required"/>
        <c:set var="inputDueTo" value="min=${dateToday} required"/>
        <c:set var="inputInvoicePrice" value="disabled"/>
    </c:when>
    <c:when test="${action eq 'editInvoice'}">
        <c:set var="inputInvoiceType" value="required"/>
        <c:set var="inputDueTo" value="min=${dateToday} value=${invoice.dueDate} required"/>
        <c:set var="inputItem" value="required"/>
        <c:set var="inputInvoicePrice" value="value=${invoice.price} disabled"/>
    </c:when>
    <c:otherwise>
        <c:set var="inputInvoiceType" value="readonly"/>
        <c:set var="inputDueTo" value="value=${invoice.dueDate} readonly"/>
        <c:set var="inputItem" value="readonly"/>
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
    <form class="container details-form" method="POST" action="/app/<c:out value='${action}' />">
        <input type="hidden" name="id" value="<c:out value='${invoice.id}'/>"/>
        <h3 class="mb-3 text-center"><c:out value="${title}"/></h3>
        <div class="row mb-3 mb-2">
            <div class="col-md-8 offset-md-2">
                <div id="pbalert" class="alert alert-danger">
                    <strong>Payer and Recipient cannot be the same person!</strong>
                </div>
            </div>
            <div class="col-md-6 input-group">
                <div class="input-group-prepend">
                    <span class="input-group-text">Payer</span>
                </div>
                <c:choose>
                    <c:when test="${action eq 'deleteInvoice'}">
                        <c:set var="personText" value="${invoice.payer.name} <${invoice.payer.email}>"/>
                        <input type="hidden" name="payerId" value="<c:out value='${invoice.payer.id}'/>"/>
                        <input class="form-control" name="payer" id="payer" type="text" value="${personText}" disabled>
                    </c:when>
                    <c:otherwise>
                        <select class="form-control" id="payer" name="payerId" required>
                            <c:forEach var="person" items="${persons}">
                                <c:set var="personText" value="${person.name} <${person.email}>"/>
                                <c:if test="${not empty invoice.payer}">
                                    <c:set var="selected" value="${invoice.payer.id eq person.id}"/>
                                </c:if>
                                <option value="${person.id}" <c:if test="${selected}">selected</c:if>>
                                    <c:out value='${personText}'/>
                                </option>
                            </c:forEach>
                        </select>
                    </c:otherwise>
                </c:choose>
            </div>
            <div class="col-md-6 input-group">
                <div class="input-group-prepend">
                    <span class="input-group-text">Recipient</span>
                </div>
                <c:choose>
                    <c:when test="${action eq 'deleteInvoice'}">
                        <c:set var="personText" value="${invoice.recipient.name} <${invoice.recipient.email}>"/>
                        <input type="hidden" name="recipientId" value="<c:out value='${invoice.recipient.id}'/>"/>
                        <input class="form-control" name="recipient" id="recipient" type="text" value="${personText}" disabled>
                    </c:when>
                    <c:otherwise>
                        <select class="form-control" id="recipient" name="recipientId" required>
                            <c:forEach var="person" items="${persons}">
                                <c:set var="personText" value="${person.name} <${person.email}>"/>
                                <c:if test="${not empty invoice.recipient}">
                                    <c:set var="selected" value="${invoice.recipient.id eq person.id}"/>
                                </c:if>
                                <option value="${person.id}" <c:if test="${selected}">selected</c:if>>
                                    <c:out value='${personText}'/>
                                </option>
                            </c:forEach>
                        </select>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
        <div class="row mb-3">
            <div class="col-md-6 input-group">
                <div class="input-group-prepend">
                    <span class="input-group-text">Type</span>
                </div>
                <select class="form-control" name="type" <c:out value="${inputInvoiceType}"/>>
                    <option <c:if test="${(empty invoice.invoiceType) or (invoice.invoiceType eq 'INCOME')}"><c:out value="selected"/></c:if> value="income">Income</option>
                    <option <c:if test="${invoice.invoiceType eq 'EXPENSE'}"><c:out value="selected"/></c:if> value="expense">Expense</option>
                </select>
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
                <div class="col-md-6">Description</div>
                <div class="col-md-1">Count</div>
                <div class="col-md-2">Price</div>
            </div>
            <div class="col-md-1"><!-- SPACER --></div>
        </div>
        <c:choose>
        <c:when test="${action eq 'createInvoice'}">
            <div class="row mb-3 item-record">
                <div class="input-group col-md-11">
                    <input class="col-md-3 form-control" type="text" placeholder="Name" name="itemName[]" required>
                    <input class="col-md-6 form-control" type="text" placeholder="Description" name="itemDesc[]" required>
                    <input class="col-md-1 form-control" type="text" placeholder="Count" name="itemCount[]" required>
                    <input class="col-md-1 form-control item-price" type="text" placeholder="Price" name="itemPrice[]" required>
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
                        <input class="col-md-6 form-control" type="text" placeholder="Description" name="itemDesc[]" value="<c:out value='${item.description}'/>" <c:out value='${inputItem}'/>>
                        <input class="col-md-1 form-control" type="text" placeholder="Count" name="itemCount[]" value="<c:out value='${item.count}'/>" <c:out value='${inputItem}'/>>
                        <input class="col-md-1 form-control item-price" type="text" placeholder="Price" name="itemPrice[]" value="<c:out value='${item.price}'/>" <c:out value='${inputItem}'/>>
                        <div class="col-md-1 no-padd input-group-append">
                            <span class="input-group-text">&euro;</span>
                        </div>
                    </div>
                    <div class="col-md-1">
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
            <input id="submit-form" type="submit" value="Confirm" class="col-md-3 offset-md-2 btn btn-success">
            <a href="/app/invoices" class="col-md-3 offset-md-2 btn btn-light">Cancel</a>
        </div>
    </form>
</div>
<o:footer script="invoiceDetail"/>