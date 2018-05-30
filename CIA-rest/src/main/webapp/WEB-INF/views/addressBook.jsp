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
    <div class="row searchbar mb-3">
        <div class="searchbar-input col-md-4 offset-md-4">
            <input class="form-control" type="text" id="filter" placeholder="Filter records">
        </div>
        <a href="/accounting/createPerson" class="col-md-1 offset-md-3">
            <span class="btn btn-success"><i class="fas fa-plus"></i></span>
        </a>
    </div>
    <div class="row">
        <table class="table">
            <thead>
                <tr>
                    <th>Name</th>
                    <th>E-mail</th>
                    <th>Phone nr.</th>
                    <th class="width-100 text-center">Edit</th>
                    <th class="width-100 text-center">Delete</th>
                    <th class="width-100 text-center">Details</th>
                </tr>
            </thead>
            <tbody>
            <c:forEach var="person" items="${persons}">
                <tr class="record">
                    <td class="hidden id"><c:out value="${person.id}"/></td>
                    <td class="name"><c:out value="${person.name}" /></td>
                    <td class="email"><c:out value="${person.email}" /></td>
                    <td class="phone"><c:out value="${person.phoneNumber}" /></td>
                    <td class="hidden accNr"><c:out value="${person.accountNumber}" /></td>
                    <td class="hidden street"><c:out value="${person.address.streetAddress}" /></td>
                    <td class="hidden city"><c:out value="${person.address.city}" /></td>
                    <td class="hidden zip"><c:out value="${person.address.postCode}" /></td>
                    <td class="hidden country"><c:out value="${person.address.country}" /></td>
                    <td class="width-100 text-center">
                        <a href="/accounting/editPerson?id=<c:out value='${person.id}'/>">
                            <i class="fas fa-edit"></i>
                        </a>
                    </td>
                    <td class="width-100 text-center">
                        <a href="/accounting/deletePerson?id=<c:out value='${person.id}'/>">
                            <i class="fas fa-ban"></i>
                        </a>
                    </td>
                    <td class="more width-100 text-center"><i class="fas fa-info"></i></td>
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
        <h3 class="mb-3 text-center" id="details-header"></h3>
        <div class="row mb-3">
            <div class="col-md-6 input-group">
                <div class="input-group-prepend">
                    <span class="input-group-text">Name</span>
                </div>
                <div id="name" class="form-control"></div>
            </div>
            <div class="col-md-6 input-group">
                <div class="input-group-prepend">
                    <span class="input-group-text">Email</span>
                </div>
                <div id="email" class="form-control"></div>
            </div>
        </div>
        <div class="row mb-3">
            <div class="col-md-6 input-group">
                <div class="input-group-prepend">
                    <span class="input-group-text">Phone</span>
                </div>
                <div id="phone" class="form-control"></div>
            </div>
            <div class="col-md-6 input-group">
                <div class="input-group-prepend">
                    <span class="input-group-text">Account nr.</span>
                </div>
                <div id="accNr" class="form-control"></div>
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
                <div id="street" class="col-md-4 form-control"></div>
                <div id="city" class="col-md-3 form-control"></div>
                <div id="zip" class="col-md-2 form-control"></div>
                <div id="country" class="col-md-3 form-control"></div>
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
<o:footer script="addressBook"/>