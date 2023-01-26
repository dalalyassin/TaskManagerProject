# Task_ManagerExalt
This project is designed for users who have multiple tasks to manage using SpringBoot framework. 
The user will be able to view, create, edit, update, and delete only their own tasks.

To ensure secure and efficient task management, the following steps have been taken:

1- Login API has been created, which accepts a user's email and password as input.
2- Passwords are encrypted before being stored in the database for added security.
3- Authorization has been implemented for all existing APIs, ensuring that only authenticated users can access them.
4- A logout feature has been added to allow users to logout from all places at once.
5- Validation has been added to prevent time conflicts between tasks for the same user.
6- Pagination and sorting have been implemented to make the response more manageable.
7- Unit tests for all methods in services have been written to ensure they are working as intended.

All endpoints have been tested using postman to ensure they are working as expected.
datebase: MySQL WorkBench 

In summary, this project aims to provide a secure and efficient task management system for users, by implementing best practices such as password encryption, user authorization, comprehensive testing and logout feature, to maintain the user's security and confidentiality.
