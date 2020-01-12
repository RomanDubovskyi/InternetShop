<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="bucket" scope="request" type="mate.academy.internetshop.model.Bucket"/>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Your Bucket</title>
</head>
<body>
<h1>This is your Bucket</h1>
<table border="1">
    <tr>
        <th>Item Name</th>
        <th>Item Price</th>
        <th>Remove From Bucket</th>
    </tr>
    <c:forEach var="item" items="${bucket.items}" >
        <tr>
            <td>
                <c:out value="${item.name}" />
            </td>
            <td>
                <c:out value="${item.price}" />
            </td>
            <td>
                <a href="${pageContext.request.contextPath}/servlet/deleteItemFromBucket?bucket_id=${bucket.bucketId}&item_id=${item.id}"><button>Remove</button></a>
            </td>
        </tr>
    </c:forEach>
</table>
<p><a href="completeOrder?bucket_id=${bucket.bucketId}"><button>Complete Order</button></a></p>
<p><button onclick="location.href='/internet_shop_war_exploded/servlet/main_menu'" type="button">Back to menu</button></p>
</body>
</html>
