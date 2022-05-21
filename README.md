# Client-Server
Client-Server application for sending objects from client to server and writing to PostgreDB
### Technolohies
- Java
- PostgreSQL
### How to run
```sh
Firstly to launch class Main.java in Server part. Than to launch class Main.java in Client part
```
### Database description
Connection is established through JDBC. Postgre database initialized with some sample user
#### Account
| ID | USERNAME | BILL_ID | BILL |
| -----------| ------ | ------ | ------ |
| 38 | Lori | 83 | 1000|
### Usage example
Interaction between server and client is conducted through the console:

#### Server side
```
Server starts
Table "Account" is created
Waiting for connection with client to port 9991
```
#### Client side
```
Client is connected
Insert 1 - to make payment or adjustment; 2 - to close the app
```
```
Choose operation: 1 - payment; 2 - adjustment
```
#### Server side
```
Account added to table
```
#### Client side 
- Payment operation
```
Payment: 500 
completed successfully
Current Bill: 500
```
- Adjustment operation
```
Adjustment: 300
Completed successfully
Current Bill: 1300
