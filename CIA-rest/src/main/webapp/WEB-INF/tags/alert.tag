<%@ tag pageEncoding="UTF-8" %>
<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="alertType" required="true" %>
<%@ attribute name="message" required="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="row mb-3">
    <div class="alert alert-danger alert-dismissible col-md-6 offset-md-3 <c:out value='${alertType}'/>">
        <button class="close" data-dismiss="alert">&times;</button>
        <strong><c:out value="${message}"/></strong>
    </div>
</div>