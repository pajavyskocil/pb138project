<%@ tag pageEncoding="UTF-8" %>
<%@ attribute name="script" required="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script src="/resources/js/jquery.min.js"></script>
<script src="/resources/js/bootstrap.min.js"></script>
<c:if test="${not empty script}">
<script src="/resources/js/${script}.js"></script>
</c:if>
</body>
</html>