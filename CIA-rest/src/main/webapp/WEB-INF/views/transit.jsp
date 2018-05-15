<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="o" tagdir="/WEB-INF/tags" %>

<o:header title="${title}" />
<div class="container">
    <div class="row align-mid">
        <div class="col-md-2 offset-md-2 ico-big">
            <a href="/app/invoices">
                <i class="fas fa-receipt"></i>
                <span>Invoices</span>
            </a>
        </div>
        <div class="col-md-2 offset-md-1 ico-big">
            <a href="/app/addressBook">
                <i class="fas fa-address-card"></i>
                <span>Address Book</span>
            </a>
        </div>
        <div class="col-md-2 offset-md-1 ico-big">
            <a href="/app/statistics">
                <i class="fas fa-chart-pie"></i>
                <span>Statistics</span>
            </a>
        </div>
    </div>
</div>
<o:footer />
