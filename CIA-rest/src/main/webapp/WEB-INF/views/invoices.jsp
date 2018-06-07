<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="o" tagdir="/WEB-INF/tags" %>

<o:header title="${title}" />
<o:navbar title="${title}" />
<main class="container">
    <div class="row section-title mb-4 mt-3">
        <h2 class="col-md-12"><c:out value="${title}"/></h2>
    </div>
    <c:if test="${not empty message}">
        <o:alert alertType="${alertType}" message="${message}"/>
    </c:if>
    <o:invoiceFilterForm persons="${persons}"/>
    <o:invoiceTable invoices="${incomes}" invoicesType="Incomes" />
    <o:invoiceTable invoices="${expenses}" invoicesType="Expenses"/>
</main>

<div class="jumbotron" id="export">
    <div class="container export-form">
        <div class="row">
            <div class="col-md-1 offset-md-11">
                <i id="export-close" class="point fas fa-times"></i>
            </div>
        </div>
        <h3 id="export-header" class="mb-3 text-center">Export invoices</h3>
        <form action="/accounting/exportInvoices">
            <div class="row mb-3">
                <div class="col-md-6 input-group">
                    <div class="input-group-prepend">
                        <span class="input-group-text">From date</span>
                    </div>
                    <input id="from" class="form-control" name="from" type="date" required>
                </div>
            </div>
            <div class="row mb-3">
                <div class="col-md-6 input-group">
                    <div class="input-group-prepend">
                        <span class="input-group-text">To date</span>
                    </div>
                    <input id="to" class="form-control" name="to" type="date" required>
                </div>
            </div>
            <div class="row mb-3">
                <input class="form-control btn btn-danger" type="submit" value="Export">
            </div>
        </form>
    </div>
</div>

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
                <div class="input-group-append">
                    <span class="input-group-text">&euro;</span>
                </div>
            </div>
        </div>
        <form method="GET" action="">
            <input id="id" type="hidden" name="id"/>
            <div class="form-group row details-btns">
                <input type="submit" value="Export PDF" id="submit-pdf" class="col-md-2 btn btn-danger">
                <input type="submit" value="Edit" id="submit-edit" class="col-md-3 offset-md-3 btn btn-danger">
                <input type="submit" value="Delete" id="submit-delete" class="col-md-3 offset-md-1 btn btn-danger">
            </div>
        </form>
    </div>
</div>

<o:footer script="invoices"/>

