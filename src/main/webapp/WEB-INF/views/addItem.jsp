<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add Item</title>
</head>
<body>
<form action="${pageContext.request.contextPath}/servlet/addItem" method="post">
    <div class="container">
        <h1>Add Item</h1>
        <p>Please fill in this form to add an Item.</p>
        <hr>

        <label for="item_name"><b>Name</b></label>
        <input type="text" placeholder="Enter Item name" name="item_name" required>

        <label for="price"><b>Price</b></label>
        <input type="text" placeholder="Enter Price" name="price" required>

        <hr>
        <button type="submit">Add</button>
        <p>
            <button onclick="location.href='/internet_shop_war_exploded/servlet/mainMenu'" type="button">Back to menu
            </button>
        </p>
        <p>
            <button onclick="location.href='/internet_shop_war_exploded/servlet/getAllItems'" type="button">Items List
            </button>
        </p>
    </div>
</form>
</body>
</html>
