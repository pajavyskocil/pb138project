<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="o" tagdir="/WEB-INF/tags" %>

<o:header title="${title}" />
<o:navbar title="${title}" />
<main class="container">
    <div class="row section-title mb-4 mt-3">
        <h2 class="col-md-12"><c:out value="${title}"/></h2>
    </div>
    <c:if test="${not empty message}">
        <div class="row mb-2">
            <div class="alert alert-success alert-dismissible col-md-6 offset-md-3 <c:out value='${alertType}'/>">
                <button class="close" data-dismiss="alert">&times;</button>
                <strong><c:out value="${message}"/></strong>
            </div>
        </div>
    </c:if>
    <form method="get" action="/app/invoices" class="mb-3">
        <div class="row mb-3">
            <div class="list-type-select-input input-group col-md-7">
                <div class="input-group-prepend">
                    <span class="input-group-text">Show invoices</span>
                </div>
                <select id="list-type" class="form-control" name="listingType">
                    <option value="all">All</option>
                    <option value="dates">Between dates</option>
                    <option value="type">Of type</option>
                    <option value="user">Of user and between dates</option>
                    <option value="typeAndDate">Of type and between dates</option>
                    <option value="userAndType">Of user, type and between dates</option>
                </select>
                <div class="input-group-append">
                    <input class="input-group-btn btn btn-warning" type="submit" value="Filter">
                </div>
            </div>
            <a href="/app/createInvoice" class="col-md-1 offset-md-4 btn btn-success"><i class="fas fa-plus"></i></a>
        </div>
        <div class="row mb-4">
            <div id="listOldest" class="input-hdn col-md-3 mb-3">
                <div class="input-group">
                    <div class="input-group-prepend">
                        <span class="input-group-text">Oldest</span>
                    </div>
                    <input class="form-control" name="oldest" type="date">
                </div>
            </div>
            <div id="listNewest" class="input-hdn col-md-3 input-group mb-3">
                <div class="input-group">
                    <div class="input-group-prepend">
                        <span class="input-group-text">Newest</span>
                    </div>
                    <input class="form-control" name="newest" type="date">
                </div>
            </div>
            <div id="listType" class="input-hdn col-md-4 input-group mb-3">
                <div class="input-group">
                    <div class="input-group-prepend">
                        <span class="input-group-text">Type</span>
                    </div>
                    <select class="form-control" name="listType">
                        <option value="expense">Expense</option>
                        <option value="income">Income</option>
                    </select>
                </div>
            </div>
            <div id="listPerson" class="input-hdn col-md-4 input-group">
                <div class="input-group-prepend">
                    <span class="input-group-text">Person</span>
                </div>
                <select class="form-control" name="personId">
                    <c:forEach var="person" items="${persons}">
                        <option value="<c:out value='${person.id}'/>"><c:out value="${person.name} <${person.email}>"/></option>
                    </c:forEach>
                </select>
            </div>
        </div>
    </form>
    <div class="row">
        <table class="table">
            <thead>
                <tr>
                    <th>Id</th>
                    <th>Type</th>
                    <th>Payer</th>
                    <th>Recipient</th>
                    <th>Cost</th>
                    <th class="w-100">Edit</th>
                    <th class="w-100">Delete</th>
                    <th class="w-100">Details</th>
                </tr>
            </thead>
            <tbody>
            <c:forEach var="invoice" items="${invoices}">
                <tr class="record">
                    <c:set var="items" value="${invoice.items}"/>
                    <td class="id"><c:out value="${invoice.id}" /></td>
                    <td class="type"><c:out value="${invoice.invoiceType}"/></td>
                    <td class="payer"><c:out value="${invoice.payer.name} <${invoice.payer.email}>"/></td>
                    <td class="recipient"><c:out value="${invoice.recipient.name} <${invoice.recipient.email}>"/></td>
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
                    <td class="w-100 text-center"><a href="/app/editInvoice?id=<c:out value='${invoice.id}'/>"><i class="fas fa-edit"></i></a></td>
                    <td class="w-100 text-center"><a href="/app/deleteInvoice?id=<c:out value='${invoice.id}'/>"><i class="fas fa-ban"></i></a></td>
                    <td class="more w-100 text-center"><i class="fas fa-info"></i></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</main>

<div class="jumbotron" id="details">
    <form class="container details-form" method="GET" action="">
        <div class="row">
            <div class="col-md-1 offset-md-11">
                <i id="details-close" class="fas fa-times"></i>
            </div>
        </div>
        <h3 id="details-header" class="mb-3 text-center"></h3>
        <div class="row mb-3">
            <div class="col-md-6 input-group">
                <div class="input-group-prepend">
                    <span class="input-group-text">Payer</span>
                </div>
                <input id="payer" class="form-control" name="payer" type="text" disabled>
            </div>
            <div class="col-md-6 input-group">
                <div class="input-group-prepend">
                    <span class="input-group-text">Recipient</span>
                </div>
                <input id="recipient" class="form-control" name="recipient" type="text" disabled>
            </div>
        </div>
        <div class="row mb-3">
            <div class="col-md-6 input-group">
                <div class="input-group-prepend">
                    <span class="input-group-text">Issued</span>
                </div>
                <input class="form-control" type="date" id="issued" name="issued" disabled>
            </div>
            <div class="col-md-6 input-group">
                <div class="input-group-prepend">
                    <span class="input-group-text">Due to</span>
                </div>
                <input class="form-control" type="date" id="dueTo" name="dueTo" disabled>
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
        <div id="items-after" class="row mb-4">
            <div class="col-md-6 input-group">
                <div class="input-group-prepend">
                    <span class="input-group-text">Total</span>
                </div>
                <input id="price" class="form-control" type="text" name="price" disabled>
            </div>
            <div class="col-md-6 input-group">
                <div class="input-group-prepend">
                    <span class="input-group-text">Type</span>
                </div>
                <input id="type" class="form-control" type="text" name="type" disabled/>
            </div>
        </div>
        <div class="form-group row details-btns">
            <input id="submit-edit" type="submit" value="Edit" class="col-md-3 offset-md-2 btn btn-danger">
            <input id="submit-delete" type="submit" value="Delete" class="col-md-3 offset-md-2 btn btn-danger">
        </div>
    </form>
</div>

<o:footer script="invoices"/>

