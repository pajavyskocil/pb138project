<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="o" tagdir="/WEB-INF/tags" %>

<o:header title="${title}"/>
<div class="jumbotron">
    <form class="container details-form" method="POST" accept-charset="UTF-8" enctype="multipart/form-data" action="/accounting/<c:out value='${action}'/>">
        <input type="hidden" name="id" value="${owner.id}"/>
        <h3 class="mb-3 text-center"><c:out value="${title}"/></h3>
        <div class="row mb-3">
            <div class="col-md-6 input-group">
                <div class="input-group-prepend">
                    <span class="input-group-text">Name</span>
                </div>
                <input class="form-control" type="text" name="name" value="<c:out value="${owner.name}"/>" required>
            </div>
            <div class="col-md-6 input-group">
                <div class="input-group-prepend">
                    <span class="input-group-text">Email</span>
                </div>
                <input class="form-control" type="email" name="email" value="<c:out value="${owner.email}"/>" required>
            </div>
        </div>
        <div class="row mb-3">
            <div class="col-md-6 input-group">
                <div class="input-group-prepend">
                    <span class="input-group-text">Phone</span>
                </div>
                <input class="form-control" type="text" name="phoneNumber" placeholder="+000 123 456 789" title="In format +000 123 456 789" pattern="\+[0-9]{3} [0-9]{3} [0-9]{3} [0-9]{3}" value="<c:out value="${owner.phoneNumber}"/>" required>
            </div>
            <div class="col-md-6 input-group">
                <div class="input-group-prepend">
                    <span class="input-group-text">Account nr.</span>
                </div>
                <input class="form-control" type="text" name="accountNumber" placeholder="0000123456/7890" title="In format 0000123456/7890" pattern="[0-9]{10}/[0-9]{4}" value="<c:out value="${owner.accountNumber}"/>" required>
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
                <c:set var="address" value="${owner.address}"/>
                <input class="col-md-4 form-control" type="text" placeholder="Street" name="streetAddress" value="<c:out value="${address.streetAddress}"/>" required>
                <input class="col-md-3 form-control" type="text" placeholder="City" name="city" value="<c:out value="${address.city}"/>" required>
                <input class="col-md-2 form-control" type="text" placeholder="Zip" name="postCode" value="<c:out value="${address.postCode}"/>" required>
                <input class="col-md-3 form-control" type="text" placeholder="Country" name="country" value="<c:out value="${address.country}"/>" required>
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