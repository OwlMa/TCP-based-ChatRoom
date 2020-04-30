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
