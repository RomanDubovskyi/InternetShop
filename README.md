# Internet-shop
* [Short Description](#description)
* [Project Structure](#structure)
* [Setup Guide](#setup)

<hr>

# <a name="description"></a>Short description
Main goal of this project was implementing core functions of a online store.
Therefore here is a list of main features presented in the application:
 - for **User** access rights 
    * get list of all items;
    * add/remove item to bucket;
    * complete order;
    * get your orders history;
 - for **Admin** access rights
    * get list of users/items;
    * add/remove user or item;
    
For security purposes regardless of role there is obligatory procedure of login/registration to have
access to main resources of the web-site. It is enhanced as well by using hashing 
of passwords with "salt". This way even breached, users data won't be exposed. 

<hr>

# <a name="structure"></a>Project Structure
* Java 11
* Maven 4.0.0
* javax.servlet-api 3.1.0
* jstl 1.2
* log4j 1.2.17
* maven-checkstyle-plugin
* mysql-connector-java 8.0.19
<hr>

# <a name="setup"></a>Setup Guide
Open the project in your IDE.

Add it as maven project.

Configure Tomcat:
* add artifact
* add sdk 11.0.3

Add sdk 11.0.3 in project structure.

Use file /InternetShop/src/main/resources/init_db.sql to create all the tables required by this app in MySQL database.

At /InternetShop/src/main/java/mate/academy/internetshop/factory/Factory class use username and password for your DB to create a Connection.

Change a path in /InternetShop/src/main/resources/log4j.properties. It has to reach your logFile.

Run the project.

If you first time launch this project: 
 * Run InjectDataController by URL = .../internet_shop_war_exploded/InjectDataController to create default users.

After that you will have access to **Admin**(login: "admin", password "admin") and **User**(login: "user", password: "user") accounts.

<hr>

#Author
[Roman Dubovskyi](https://github.com/RomanDubovskyi)