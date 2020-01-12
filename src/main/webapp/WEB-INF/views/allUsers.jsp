<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>

<jsp:useBean id="users" scope="request" type="java.util.List<mate.academy.internetshop.model.User>"/>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>All Users</title>
</head>
<body>
<h1>All Users Panel </h1>
<p></p>
<a href="/internet_shop_war_exploded/servlet/registration"/><button>ADD USER</button> </a>
<p>Users:</p>
<table border="1">
    <tr>
        <th>Login</th>
        <th>ID</th>
        <th>Name</th>
        <th>Surname</th>
        <th>DELETE</th>
    </tr>
    <c:forEach var="user" items="${users}" >
        <tr>
            <td>
                <c:out value="${user.login}" />
            </td>
            <td>
                <c:out value="${user.userId}" />
            </td>
            <td>
                <c:out value="${user.name}" />
            </td>
            <td>
                <c:out value="${user.surname}" />
            </td>
            <td>
                <a href="/internet_shop_war_exploded/servlet/deleteUser?user_id=${user.userId}"/><button>DELETE</button></a>
            </td>
        </tr>
    </c:forEach>
</table>
<p><button onclick="location.href='/internet_shop_war_exploded/servlet/main_menu'" type="button">Back to menu</button></p>
</body>
</html>
