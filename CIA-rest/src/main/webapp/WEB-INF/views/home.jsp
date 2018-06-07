<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="o" tagdir="/WEB-INF/tags" %>

<o:header title="${title}" />
<div class="container">
    <div class="align-mid">
        <c:if test="${not empty message}">
            <o:alert alertType="${alertType}" message="${message}"/>
        </c:if>
        <div class="row mb-5 mt-4 text-center">
            <h1 class="col-md-12 home-title"><c:out value="${title}" /></h1>
        </div>
        <div class="row mb-2">
            <c:choose>
                <c:when test="${not hasOwner}">
                    <div class="col-md-4 offset-md-4 ico-big">
                        <a href="/accounting/createOwner">
                            <i class="fas fa-user"></i>
                            <span>Create owner</span>
                        </a>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="col-md-3 ico-big">
                        <a href="/accounting/editOwner">
                            <i class="fas fa-user-edit"></i>
                            <span>Edit owner</span>
                        </a>
                    </div>
                    <div class="col-md-3 ico-big">
                        <a href="/accounting/invoices">
                            <i class="fas fa-receipt"></i>
                            <span>Invoices</span>
                        </a>
                    </div>
                    <div class="col-md-3 ico-big">
                        <a href="/accounting/addressBook">
                            <i class="fas fa-address-book"></i>
                            <span>Address Book</span>
                        </a>
                    </div>
                    <div class="col-md-3 ico-big">
                        <a href="/accounting/statistics">
                            <i class="fas fa-chart-pie"></i>
                            <span>Statistics</span>
                        </a>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>
<o:footer />
