# CubicFox trial task
This MAVEN project is for testing the [jsonplaceholder's users](https://jsonplaceholder.typicode.com/users) endpoint.  

The application implements the following conditions:
* Check the endpoint is available
* Examines that if calling multiple times this end point we got the same count in the response
* Verify the given email addresses. If not valid we don't save the record.
* It uses the standard logger. So you can modify what would you want to see in the log file.  
