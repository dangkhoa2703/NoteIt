# NoteIt
Simple note app to write and share note

# NoteIt

- Description: This is a simple app to take note.


- Features:
	+ Register new user.
	+ User logging with encrypted password(Bcrypt).
  	+ Write notes.
  	+ Edit notes (only author).
  	+ Share notes (only author).
	+ Delete notes (only author).


- Technologies:
	+ Maven: build management and dependencies.
   	+ Spring Boot: Framework for the application.
	+ Spring Security: Authentication.
	+ MySQL: Database.



- Challenges:
	+ How all the technologies work together.
	+ Manage data between users and server.


- Features to implement in the future:
	+ Text input validation.
	+ Authorization.
	+ Add admin authority.
	+ Exception handling.


- How to install and run the app: 
	1. Install IntelliJ IDEA [here](https://www.jetbrains.com/idea/download).
	2. Install MySQL [here](https://dev.mysql.com/downloads/mysql).
	3. Install MySQL Workbench [here](https://dev.mysql.com/downloads/workbench).
 	4. Install Postman [here](https://www.postman.com/downloads/). 
	5. Open MySQL Workbench, login to localhost.
	6. Open File -> Open SQL Script...
	7. Open NoteIt\noteit\sql_script\01-Create_user.sql and execute file (yellow lightning bolt) to create new user.
 	8. Open NoteIt\noteit\sql_script\02-Note_it.sql and execute file to create DB
	9. Open NoteIt in IntelliJ and run the app.
	10. Test the app in Postman


- Table of contents:
	- created Users: username & password\
   		+ HungryRabbit | eatcarrot
  		+ MuscleWhale | 100pushup
 		+ ChillyAlligator | justchilling
		+ FunnyBanana | iamyellow
  		+ SleepyCat | drinkcoffee
	- REST APIs URL:
 		+ login: POST http://localhost:8080/api/v1/auth/authenticate
   		+ register: POST  http://localhost:8080/api/v1/auth/register
  		+ request all note: GET http://localhost:8080/api/v1/noteit
  		+ request one note: GET http://localhost:8080/api/v1/noteit/{noteId}
  		+ create new note: POST http://localhost:8080/api/v1/noteit
  		+ edit a note: PUT http://localhost:8080/api/v1/noteit/{noteId}
  		+ delete a note: DELETE http://localhost:8080/api/v1/noteit/{noteId}
		+ get a list of all user to share a note: GET http://localhost:8080/api/v1/noteit/share/{noteId}
   		+ share a note with one user: PUT http://localhost:8080/api/v1/noteit/{noteId}/{userName} 
