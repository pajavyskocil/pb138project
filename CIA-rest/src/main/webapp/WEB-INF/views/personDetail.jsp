<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="o" tagdir="/WEB-INF/tags" %>

<o:header title=""/>
<c:choose>
    <c:when test="${action eq 'createPerson'}">
        <c:set var="inputName" value="required"/>
        <c:set var="inputEmail" value="required"/>
        <c:set var="inputPhone" value="required"/>
        <c:set var="inputAcc" value="required"/>
        <c:set var="inputAddressStreet" value="required"/>
        <c:set var="inputAddressCity" value="required"/>
        <c:set var="inputAddressPostal" value="required"/>
        <c:set var="inputAddressCountry" value="required"/>
    </c:when>
    <c:when test="${action eq 'editPerson'}">
        <c:set var="address" value="${person.address}"/>
        <c:set var="inputName" value="value=${person.name} required"/>
        <c:set var="inputEmail" value="value=${person.email} required"/>
        <c:set var="inputPhone" value="value=${person.phoneNumber} required"/>
        <c:set var="inputAcc" value="value=${person.accountNumber} required"/>
        <c:set var="inputAddressStreet" value="value=${address.streetAddress} required"/>
        <c:set var="inputAddressCity" value="value=${address.city} required"/>
        <c:set var="inputAddressPostal" value="value=${address.postCode} required"/>
        <c:set var="inputAddressCountry" value="value=${address.country} required"/>
    </c:when>
    <c:when test="${action eq 'deletePerson'}">
        <c:set var="address" value="${person.address}"/>
        <c:set var="inputName" value="value=${person.name} readonly"/>
        <c:set var="inputEmail" value="value=${person.email} disabled"/>
        <c:set var="inputPhone" value="value=${person.phoneNumber} disabled"/>
        <c:set var="inputAcc" value="value=${person.accountNumber} disabled"/>
        <c:set var="inputAddressStreet" value="value=${address.streetAddress} disabled"/>
        <c:set var="inputAddressCity" value="value=${address.city} disabled"/>
        <c:set var="inputAddressPostal" value="value=${address.postCode} disabled"/>
        <c:set var="inputAddressCountry" value="value=${address.country} disabled"/>
    </c:when>
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
    <form class="container details-form" method="POST" accept-charset="UTF-8" action="/accounting/<c:out value='${action}'/>">
        <input type="hidden" name="id" value="${person.id}"/>
        <h3 class="mb-3 text-center"><c:out value="${title}"/></h3>
        <div class="row mb-3">
            <div class="col-md-6 input-group">
                <div class="input-group-prepend">
                    <span class="input-group-text">Name</span>
                </div>
                <input class="form-control" type="text" name="name" <c:out value="${inputName}"/>>
            </div>
            <div class="col-md-6 input-group">
                <div class="input-group-prepend">
                    <span class="input-group-text">Email</span>
                </div>
                <input class="form-control" type="email" name="email" <c:out value="${inputEmail}"/>>
            </div>
        </div>
        <div class="row mb-3">
            <div class="col-md-6 input-group">
                <div class="input-group-prepend">
                    <span class="input-group-text">Phone</span>
                </div>
                <input class="form-control" type="text" name="phoneNumber" <c:out value="${inputPhone}"/>>
            </div>
            <div class="col-md-6 input-group">
                <div class="input-group-prepend">
                    <span class="input-group-text">Account nr.</span>
                </div>
                <input class="form-control" type="text" name="accountNumber" <c:out value="${inputAcc}"/>>
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
                <input class="col-md-4 form-control" type="text" placeholder="Street" name="streetAddress" <c:out value="${inputAddressStreet}"/>>
                <input class="col-md-3 form-control" type="text" placeholder="City" name="city" <c:out value="${inputAddressCity}"/>>
                <input class="col-md-2 form-control" type="text" placeholder="Zip" name="postCode" <c:out value="${inputAddressPostal}"/>>
                <input class="col-md-3 form-control" type="text" placeholder="Country" name="country" <c:out value="${inputAddressCountry}"/>>
            </div>
        </div>
        <div class="form-group row details-btns">
            <input id="submit-edit" type="submit" value="Confirm" class="col-md-3 offset-md-2 btn btn-danger">
            <a href="/accounting/addressBook" class="col-md-3 offset-md-2 btn btn-light">Cancel</a>
        </div>
    </form>
</div>
<o:footer/>