<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:useBean id="orders" scope="request" type="java.util.List<mate.academy.internetshop.model.Order>"/>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Your Order History</title>
</head>
<body>
<h1>Your Orders:</h1>
<table border="1">
    <tr>
        <th>Order ID</th>
        <th>Items</th>
        <th>Delete Order</th>
    </tr>
    <c:forEach var="order" items="${orders}">
        <tr>
            <td>
                <c:out value="${order.orderId}"/>
            </td>
            <td>
                <c:forEach var="item" items="${order.items}">
                    <c:out value="${item}"/>
                </c:forEach>
            </td>
            <td>
                <a href="${pageContext.request.contextPath}/servlet/deleteOrder?order_id=${order.orderId}">
                    <button>Delete</button>
                </a>
            </td>
        </tr>
    </c:forEach>
</table>
<p>
    <button onclick="location.href='/internet_shop_war_exploded/servlet/mainMenu'" type="button">Back to menu</button>
</p>
</body>
</html>
