spring-resultset-rest-service
=============================

Goal is to create a rest service for any SQL. kind of having a jdbc resultset converted into JSON response, without making use of POJO or ORM mapping framework.

Any SQl and resultset can be serviced as Rest JSON.

This Uses Jackson's JSONSerializer and Object Mapper to Serialize SqlResultset into JSON. And with help of ObjectMapper configuration in Spring for this serializer now any resultset can be served as Rest Json Service.
