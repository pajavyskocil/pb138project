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
    <form method="get" action="/accounting/invoices" accept-charset="UTF-8" class="mb-3">
        <div class="row mb-3">
            <div class="list-type-select-input input-group col-md-7">
                <div class="input-group-prepend">
                    <span class="input-group-text">Show invoices</span>
                </div>
                <select id="list-type" class="form-control" name="listingType">
                    <option value="all">All</option>
                    <option value="dates">Between dates</option>
                    <option value="type">Of type</option>
                    <option value="user">Of user</option>
                    <option value="userAndDate">Of user and between dates</option>
                    <option value="typeAndDate">Of type and between dates</option>
                    <option value="userAndType">Of user, type and between dates</option>
                </select>
                <div class="input-group-append">
                    <input class="input-group-btn btn btn-warning" type="submit" value="Search">
                </div>
            </div>
            <a href="/accounting/createInvoice" class="col-md-1 offset-md-4">
                <span class="btn btn-success"><i class="fas fa-plus"></i></span>
            </a>
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
            <div id="listPerson" class="input-hdn col-md-4 input-group mb-3">
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
                    <th>Payer / recipient</th>
                    <th>Cost</th>
                    <th class="width-100 text-center">Edit</th>
                    <th class="width-100 text-center">Delete</th>
                    <th class="width-100 text-center">Details</th>
                </tr>
            </thead>
            <tbody>
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
                    <td class="width-100 text-center point"><a href="/accounting/editInvoice?id=<c:out value='${invoice.id}'/>"><i class="fas fa-edit"></i></a></td>
                    <td class="width-100 text-center point"><a href="/accounting/deleteInvoice?id=<c:out value='${invoice.id}'/>"><i class="fas fa-ban"></i></a></td>
                    <td class="more width-100 text-center point"><i class="fas fa-info"></i></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</main>

<div class="jumbotron" id="details">
    <div class="container details-form">
        <div class="row">
            <div class="col-md-1 offset-md-11">
                <i id="details-close" class="point fas fa-times"></i>
            </div>
        </div>
        <h3 id="details-header" class="mb-3 text-center"></h3>
        <div class="row mb-3">
            <div class="col-md-8 input-group">
                <div class="input-group-prepend">
                    <span class="input-group-text">Payer/recipient</span>
                </div>
                <div id="secondPerson" class="form-control"></div>
            </div>
            <div class="col-md-4 input-group">
                <div class="input-group-prepend">
                    <span class="input-group-text">Type</span>
                </div>
                <div id="type" class="form-control"></div>
            </div>
        </div>
        <div class="row mb-3">
            <div class="col-md-6 input-group">
                <div class="input-group-prepend">
                    <span class="input-group-text">Issued on</span>
                </div>
                <div id="issued" class="form-control"></div>
            </div>
            <div class="col-md-6 input-group">
                <div class="input-group-prepend">
                    <span class="input-group-text">Due to</span>
                </div>
                <div id="dueTo" class="form-control"></div>
            </div>
        </div>
        <div class="row mb-3">
            <div class="col-md-11 row">
                <div class="col-md-3">Name</div>
                <div class="col-md-4">Description</div>
                <div class="col-md-2">Count</div>
                <div class="col-md-2">Price</div>
            </div>
            <div class="col-md-1"><!-- SPACER --></div>
        </div>
        <div id="items-after" class="row mb-4">
            <div class="col-md-6 input-group">
                <div class="input-group-prepend">
                    <span class="input-group-text">Total</span>
                </div>
                <div id="price" class="form-control"></div>
            </div>
        </div>
        <form method="GET" accept-charset="UTF-8" action="">
            <input id="id" type="hidden" name="id"/>
            <div class="form-group row details-btns">
                <input type="submit" value="Edit" id="submit-edit" class="col-md-3 offset-md-2 btn btn-danger">
                <input type="submit" value="Delete" id="submit-delete" class="col-md-3 offset-md-2 btn btn-danger">
            </div>
        </form>
    </div>
</div>

<o:footer script="invoices"/>

