<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="o" tagdir="/WEB-INF/tags" %>

<o:header title=""/>
<c:choose>
    <c:when test="${(action eq 'createPerson') or (action eq 'editPerson')}">
        <c:set var="inputParams" value="required"/>
    </c:when>
    <c:when test="${action eq 'deletePerson'}">
        <c:set var="inputParams" value="readonly"/>
    </c:when>
</c:choose>
<div class="jumbotron">
    <c:if test="${not empty message}">
        <o:alert alertType="${alterType}" message="${message}"/>
    </c:if>
    <form class="container details-form" method="POST" action="/accounting/<c:out value='${action}'/>">
        <input type="hidden" name="id" value="${person.id}"/>
        <h3 class="mb-3 text-center"><c:out value="${title}"/></h3>
        <div class="row mb-3">
            <div class="col-md-6 input-group">
                <div class="input-group-prepend">
                    <span class="input-group-text">Name</span>
                </div>
                <input class="form-control" type="text" name="name" value="<c:out value="${person.name}"/>" <c:out value="${inputParams}"/>>
            </div>
            <div class="col-md-6 input-group">
                <div class="input-group-prepend">
                    <span class="input-group-text">Email</span>
                </div>
                <input class="form-control" type="email" name="email" value="<c:out value="${person.email}"/>" <c:out value="${inputParams}"/>>
            </div>
        </div>
        <div class="row mb-3">
            <div class="col-md-6 input-group">
                <div class="input-group-prepend">
                    <span class="input-group-text">Phone</span>
                </div>
                <input class="form-control" type="tel" name="phoneNumber" value="<c:out value="${person.phoneNumber}"/>" <c:out value="${inputParams}"/>>
            </div>
            <div class="col-md-6 input-group">
                <div class="input-group-prepend">
                    <span class="input-group-text">Account nr.</span>
                </div>
                <input class="form-control" type="text" name="accountNumber" value="<c:out value="${person.accountNumber}"/>" <c:out value="${inputParams}"/>>
            </div>
        </div>
        <div class="row mb-3">
            <div class="col-md-12 row">
                <div class="col-md-4">Street</div>
                <div class="col-md-3">City</div>
                <div class="col-md-2">ZIP</div>
                <div class="col-md-3">Country</div>
            </div>
        </div>
        <div class="row mb-4 address">
            <div class="input-group col-md-12">
                <c:set var="address" value="${person.address}"/>
                <input class="col-md-4 form-control" type="text" placeholder="Street" name="streetAddress" value="<c:out value="${address.streetAddress}"/>" <c:out value="${inputParams}"/>>
                <input class="col-md-3 form-control" type="text" placeholder="City" name="city" value="<c:out value="${address.city}"/>" <c:out value="${inputParams}"/>>
                <input class="col-md-2 form-control" type="text" placeholder="Zip" name="postCode" value="<c:out value="${address.postCode}"/>" <c:out value="${inputParams}"/>>
                <input class="col-md-3 form-control" type="text" placeholder="Country" name="country" value="<c:out value="${address.country}"/>" <c:out value="${inputParams}"/>>
            </div>
        </div>
        <div class="form-group row details-btns">
            <input id="submit-edit" type="submit" value="Confirm" class="col-md-3 offset-md-2 btn btn-danger">
            <a href="/accounting/addressBook" class="col-md-3 offset-md-2 btn btn-light">Cancel</a>
        </div>
    </form>
</div>
<o:footer/>