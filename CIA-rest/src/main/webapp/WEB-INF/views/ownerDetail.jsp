<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="o" tagdir="/WEB-INF/tags" %>

<o:header title=""/>
<c:choose>
    <c:when test="${action eq 'createOwner'}">
        <c:set var="inputName" value="required"/>
        <c:set var="inputEmail" value="required"/>
        <c:set var="inputPhone" value="required"/>
        <c:set var="inputAcc" value="required"/>
        <c:set var="inputAddressStreet" value="required"/>
        <c:set var="inputAddressCity" value="required"/>
        <c:set var="inputAddressPostal" value="required"/>
        <c:set var="inputAddressCountry" value="required"/>
        <c:set var="base64Logo" value="${owner.logoBASE64}" />
    </c:when>
    <c:when test="${action eq 'editOwner'}">
        <c:set var="address" value="${owner.address}"/>
        <c:set var="inputName" value="value=${owner.name} required"/>
        <c:set var="inputEmail" value="value=${owner.email} required"/>
        <c:set var="inputPhone" value="value=${owner.phoneNumber} required"/>
        <c:set var="inputAcc" value="value=${owner.accountNumber} required"/>
        <c:set var="inputAddressStreet" value="value=${address.streetAddress} required"/>
        <c:set var="inputAddressCity" value="value=${address.city} required"/>
        <c:set var="inputAddressPostal" value="value=${address.postCode} required"/>
        <c:set var="inputAddressCountry" value="value=${address.country} required"/>
        <c:set var="base64Logo" value="${owner.logoBASE64}" />
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
    <form class="container details-form" method="POST" accept-charset="UTF-8" enctype="multipart/form-data" action="/accounting/<c:out value='${action}'/>">
        <input type="hidden" name="id" value="${owner.id}"/>
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
        <div class="row mb-3 address">
            <div class="input-group col-md-12">
                <input class="col-md-4 form-control" type="text" placeholder="Street" name="streetAddress" <c:out value="${inputAddressStreet}"/>>
                <input class="col-md-3 form-control" type="text" placeholder="City" name="city" <c:out value="${inputAddressCity}"/>>
                <input class="col-md-2 form-control" type="text" placeholder="Zip" name="postCode" <c:out value="${inputAddressPostal}"/>>
                <input class="col-md-3 form-control" type="text" placeholder="Country" name="country" <c:out value="${inputAddressCountry}"/>>
            </div>
        </div>
        <div class="row mb-3">
            <div class="input-group col-md-6">
                <div class="input-group-prepend">
                    <span class="input-group-text">Account nr.</span>
                </div>
                <input class="form-control" id="logo" type="file" placeholder="Logo" name="logo" accept="image/*">
            </div>
        </div>
        <c:if test="${not empty owner.logoBASE64}">
        <div class="row mb-4">
            <div class="col-md-6 maxh-150">
                <img id="preview" src="<c:out value="${owner.logoBASE64}"/>" />
            </div>
        </div>
        </c:if>
        <div class="form-group row details-btns">
            <input id="submit-edit" type="submit" value="Confirm" class="col-md-3 offset-md-2 btn btn-danger">
            <a href="/accounting/" class="col-md-3 offset-md-2 btn btn-light">Cancel</a>
        </div>
    </form>
</div>
<o:footer script="ownerDetail"/>