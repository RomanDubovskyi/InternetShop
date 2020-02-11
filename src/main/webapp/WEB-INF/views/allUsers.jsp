<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:useBean id="users" scope="request" type="java.util.List<mate.academy.internetshop.model.User>"/>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>All Users</title>
</head>
<body>
<h1>All Users Panel </h1>
<p></p>
<a href="${pageContext.request.contextPath}/registration"></a>
<button>ADD USER</button>
<p>Users:</p>
<table border="1">
    <tr>
        <th>Login</th>
        <th>ID</th>
        <th>Name</th>
        <th>Surname</th>
        <th>DELETE</th>
    </tr>
    <c:forEach var="user" items="${users}">
        <tr>
            <td>
                <c:out value="${user.login}"/>
            </td>
            <td>
                <c:out value="${user.userId}"/>
            </td>
            <td>
                <c:out value="${user.name}"/>
            </td>
            <td>
                <c:out value="${user.surname}"/>
            </td>
            <td>
                <a href="${pageContext.request.contextPath}/servlet/deleteUser?user_id=${user.userId}"></a>
                <button>DELETE</button>
            </td>
        </tr>
    </c:forEach>
</table>
<p>
    <button onclick="location.href='/internet_shop_war_exploded/servlet/mainMenu'" type="button">Back to menu</button>
</p>
</body>
</html>
