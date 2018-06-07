<%@ tag pageEncoding="UTF-8" %>
<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="persons" required="true" type="java.util.Collection" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<form method="get" action="/accounting/invoices">
    <div class="row mb-3">
        <div class="list-type-select-input input-group col-md-7">
            <div class="input-group-prepend">
                <span class="input-group-text">Show invoices</span>
            </div>
            <select id="filter-type" class="form-control" name="listingType">
                <option value="all">All</option>
                <option value="date">Between dates</option>
                <option value="user">Of user</option>
                <option value="userDate">Of user and between dates</option>
            </select>
            <div class="input-group-append">
                <input class="input-group-btn btn btn-primary fa fa-input" type="submit" value="&#xf002;">
            </div>
        </div>
        <div class="col-md-2 offset-md-2 text-right">
            <span class="btn btn-danger" id="export-invoices">Export PDF</span>
        </div>
        <a href="/accounting/createInvoice" class="col-md-1">
            <span class="btn btn-danger"><i class="fas fa-plus"></i></span>
        </a>
    </div>
    <div class="row mb-3">
        <div class="col-md-3">
            <div class="input-group">
                <div class="input-group-prepend">
                    <span class="input-group-text">Oldest</span>
                </div>
                <input id="filter-oldest" class="form-control" name="oldest" type="date" disabled>
            </div>
        </div>
        <div class="col-md-3 input-group">
            <div class="input-group">
                <div class="input-group-prepend">
                    <span class="input-group-text">Newest</span>
                </div>
                <input id="filter-newest"  class="form-control" name="newest" type="date" disabled>
            </div>
        </div>
        <div class="col-md-6 input-group">
            <div class="input-group-prepend">
                <span class="input-group-text">Person</span>
            </div>
            <select  id="filter-personId" class="form-control" name="personId" disabled>
                <c:forEach var="person" items="${persons}">
                    <option value="<c:out value='${person.id}'/>"><c:out value="${person.name} <${person.email}>"/></option>
                </c:forEach>
            </select>
        </div>
    </div>
</form>