<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:useBean id="items" scope="request" type="java.util.List<mate.academy.internetshop.model.Item>"/>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>All Items</title>
</head>
<body>
<h1>List of all Items</h1>
<p>Items:</p>
<table border="1">
    <tr>
        <th>ID</th>
        <th>Name</th>
        <th>Price</th>
        <th>Add to Bucket</th>
        <th>Remove from items</th>
    </tr>
    <c:forEach var="item" items="${items}">
        <tr>
            <td>
                <c:out value="${item.id}"/>
            </td>
            <td>
                <c:out value="${item.name}"/>
            </td>
            <td>
                <c:out value="${item.price}"/>
            </td>
            <td>
                <a href="${pageContext.request.contextPath}/servlet/addItemToBucket?item_id=${item.id}"></a>
                <button>Add to Bucket</button>
            </td>
            <td>
                <a href="${pageContext.request.contextPath}/servlet/deleteFromItems?item_id=${item.id}"></a>
                <button>   Remove   </button>
            </td>
        </tr>
    </c:forEach>
</table>
<p>
    <button onclick="location.href='/internet_shop_war_exploded/servlet/main_menu'" type="button">Back to menu</button>
</p>
</body>
</html>
