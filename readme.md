# TCP-Based ChatRoom
*This is a network course project.*

## Server

### start server
1. use the ServerSocket API:
    - This class implements server sockets. 
    - A server socket waits for requests to come in over the network. 
    - It performs some operation based on that request, and then possibly returns a result to the requester.
2. Start server
    - When ServerMain() method is running and we click the start button,the serverSocket will be passed to ServerThread.
3. serverThread
    - The serverSocket call accept() method to listen for a connection to be made to this socket and accepts.
4. ServerCollection
    - The ServerCollection class is used for storing a map to record the username and its Thread.

## Message

### Type
1. addFriendRequest/addFriendResponse:
    - This type is used while client request for adding a friend.
    - For the client, the content should be the name of the friend.
    - For the server response, the content should be **"disagree"** representing that this friend name not exist in the database or is invalid name, or **friend name** if server agree to add this friend into both users' database.
2. loginRequest/loginResponse:
    - This type is used while user login.
    - For the client, the content should be the **username** and **password**.
    - for the server, the content should be flowing if agree: 1. agree 2. user's friend list
    or if disagree: 1. disagree 2. reason(error code: 1 for wrong username and 2 for this username is login).
    
    