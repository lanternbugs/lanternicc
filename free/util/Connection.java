/**
 * The utillib library.
 * More information is available at http://www.jinchess.com/.
 * Copyright (C) 2002 Alexander Maryanovsky.
 * All rights reserved.
 *
 * The utillib library is free software; you can redistribute
 * it and/or modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 *
 * The utillib library is distributed in the hope that it will
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser
 * General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with utillib library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package free.util;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;


/**
 * An abstract base class for managers of a connection to a TCP/IP, session based server.
 * A <code>Connection</code> has 4 major states: Unconnected, Connecting, Connected and LoggedIn which are specified by
 * the  {@link #isConnecting()}, {@link #isConnected()} and {@link #isLoggedIn()} methods.
 */

public abstract class Connection{
  
  
  
  /**
   * The username requested by the user. This may be different from the actual username assigned by the server.
   */
  
  private final String requestedUsername;
  
  
  
  /**
   * The password provided by the user.
   */
  
  private final String password;
  
  
  
  /**
   * The thread that connects to the server and then runs the read-loop, <code>null</code> until a connection attempt
   * is initiated.
   */
  
  private Thread readerThread = null;
  
  
  
  /**
   * The socket to the server, <code>null</code> when not connected.
   */

  private Socket socket;
  
  
  
  /**
   * The username assigned to us by the server. This is <code>null</code> until we are logged in.
   */
   
  private String username = null;
  
  
  
  /**
   * Creates a new <code>Connection</code> with the specified requested username and password. Both the username and the
   * password are kept merely as a convenience for subclasses - this class does not use them in any manner other than
   * returning them from {@link #getRequestedUsername()} and {@link getPassword()}. Consequently they may both be
   * <code>null</code>.
   */
  
  public Connection(String requestedUsername, String password){
    this.requestedUsername = requestedUsername;
    this.password = password;
  }
  
  
  
  /**
   * Createa a new <code>Connection</code> object. The {@link #getRequestedUsername()} and {@link #getPassword()} of the
   * new <code>Connection</code> object will return <code>null</code>.
   */
  
  public Connection(){
    this(null, null);
  }
  
  
  
  /**
   * Returns the username requested by the user.
   */
  
  protected String getRequestedUsername(){
    return requestedUsername;
  }
  
  
  
  /**
   * Returns the password specified by the user.
   */
  
  protected String getPassword(){
    return password;
  }



  /**
   * Initiates a connection to the specified hostname on the specified port. The actual connecting is done
   * asynchronously.
   */

  public synchronized void initiateConnect(final String hostname, final int port){
    readerThread = new Thread("ReaderThread"){
      public void run(){
        try{
          if (connect(hostname, port))
            readerLoop();
        } finally{
            synchronized(Connection.this){
              if (readerThread == Thread.currentThread())
                readerThread = null;
            }
          }
      }
    };
    readerThread.setPriority(Thread.MAX_PRIORITY);
    readerThread.start();
  }
  
  
  
  /**
   * Connects to the server. Returns whether successful.
   */
  
  private boolean connect(String hostname, int port){
    try{
      Socket tmpSocket = connectImpl(hostname, port);
      
      synchronized(this){
        socket = tmpSocket;
      }
      
      execRunnable(new SafeRunnable(){
        public void safeRun(){
          handleConnected();
        }
      });

      return true;
    } catch (IOException e){
        // This may not be true if the connection was closed while we were
        // sitting in connectImpl, connecting to the server
        if (readerThread == Thread.currentThread())
          handleConnectingFailed(e);
        return false;
      }
  }
  
  
  
  /**
   * Creates and connects a socket to the specified hostname on the specified port. Note that this method is called from
   * the constructor, so an implementation may not depend on the subclass's constructor having finished. The default
   * implementation returns a plain <code>java.net.Socket</code>. 
   */
  
  protected Socket connectImpl(String hostname, int port) throws IOException{
    return new Socket(hostname, port);
  }
  
  
  
  /**
   * Returns the <code>OutputStream</code> that sends data to the server.  
   */
  
  public synchronized OutputStream getOutputStream() throws IOException{
    if (!isConnected())
      throw new IllegalStateException("Not connected");
    
    return socket.getOutputStream();
  }
  
  
  
  /**
   * Returns the host we're connected to.
   */
  
  public synchronized InetAddress getHost(){
    if (!isConnected())
      throw new IllegalStateException("Not connected");
    
    return socket.getInetAddress();
  }
  
  
  
  /**
   * Returns the port we're connected on.
   */
  
  public synchronized int getPort(){
    if (!isConnected())
      throw new IllegalStateException("Not connected");

    return socket.getPort();
  }
  
  
  
  /**
   * A loop which reads data from the server.
   */
  
  private void readerLoop(){
    try{
      InputStream in = createInputStream(socket.getInputStream());
      while (true){
        Object message = readMessage(in);
        
        if (message == null) // Clean exit
          break;
        
        execRunnable(new MessageDispatcher(message));
      }
      connectionInterrupted(null);
    } catch (IOException e){
        connectionInterrupted(e);
      }
  }
  
  
  
  /**
   * Creates the <code>InputStream</code> that will be passed to {@link #readMessage(InputStream)}. This allows
   * implementations to wrap the input stream in other input streams, which are useful for parsing their specific
   * protocol. This method is invoked once per socket, meaning that the same <code>InputStream</code> is reused for all
   * invocations of <code>readMessage</code>. Subclasses are encouraged to wrap the argument in at least
   * {@link BufferedInputStream}.
   * <p>The default implementation simply returns the argument. 
   */
  
  protected InputStream createInputStream(InputStream in){
    return in;
  }
  
  
  
  /**
   * Reads and returns a single message from the server. Returns <code>null</code> if an EOF is read from the server.
   */
  
  protected abstract Object readMessage(InputStream inputStream) throws IOException;
  
  
  
  /**
   * Invoked when an interruption occurs in the communication with the server. The specified exception is the one that
   * was thrown, which may be <code>null</code> if we were disconnected cleanly from the server (end of stream was
   * reached). Note that client code is also expected to invoke this method if, for example, an
   * <code>IOException</code> is thrown while trying to send data to the server.
   */
  
  protected final synchronized void connectionInterrupted(final IOException exception){
    if (!isConnected()) // Just ignore because we may be called twice. For example, if sending fails, this method is
      return;           // invoked and closes the connection. Then the reader thread fails and invokes it again.
    
    try{
      socket.close(); // Closing the socket causes a SocketException to be thrown in the reader thread
      socket = null;
      username = null;
      
      execRunnable(new SafeRunnable(){
        public void safeRun(){
          handleDisconnection(exception);
        }
      });
    } catch (IOException e){
        e.printStackTrace(); // Nothing else to do
      }
  }

  
  
  /**
   * Returns whether we are currently attempting to, but not yet connected to the server.
   */
  
  public final synchronized boolean isConnecting(){
    return (readerThread != null) && !isConnected();
  }
  
  
  
  /**
   * Returns whether we are connected to the server.
   */
  
  public final synchronized boolean isConnected(){
    return socket != null;
  }
  
  
  
  /**
   * Returns whether this <code>Connection</code> is already logged in.
   */
  
  public final synchronized boolean isLoggedIn(){
    return username != null;
  }
  
  
  
  /**
   * Returns the username assigned to us by the server on login.
   */
  
  public final synchronized String getUsername(){
    if (!isLoggedIn())
      throw new IllegalStateException("Not yet logged in");
    
    return username;
  }
  
  
  
  /**
   * Initiates the login procedure. This method returns immediately, without blocking.
   */
  
  public synchronized final void initiateLogin(){
    if (!isConnected())
      throw new IllegalStateException("Not connected");
    if (isLoggedIn())
      throw new IllegalStateException("Already logged in");
    
    sendLoginSequence();
  }
  
  
  
  /**
   * Sends the login sequence to the server via the specified output stream. This method must not block.
   * 
   * @param out The <code>OutputStream</code> to write the login sequence into.
   * @param username The requested username.
   * @param password The password for the requested username.
   * 
   * @throws IOException If an I/O error occurs while sending the login sequence.
   */
  
  protected abstract void sendLoginSequence();
  
  
  
  /**
   * Closes this connection. If the connection is currently attempting to connect, the attempt is aborted.
   * If the connection is connected, disconnection is initiated. Otherwise, the call is ignored.
   * The actual disconnection is performed asynchronously.
   */
  
  public synchronized void close(){
    if (isConnected())
      connectionInterrupted(null);
    else if (isConnecting()){
      readerThread.interrupt();
      readerThread = null;
    }
  }
  
  
  
  /**
   * Client code (specifically {@link #handleMessage(Object)} invokes this method when it receives a message from the
   * server acknowledging login.
   * 
   * @param username The username assigned to us by the server. May not be <code>null</code>.
   */
  
  protected synchronized final void loginSucceeded(String username){
    if (username == null)
      throw new IllegalArgumentException("username may not be null");
    if (isLoggedIn())
      throw new IllegalStateException("Already logged in");
    
    this.username = username;
    
    // We are already in the client thread, so there's no need to use execRunnable
    handleLoginSucceeded();
  }
  
  
  
  /**
   * Client code (specifically {@link #handleMessage(Object)} invokes this method when it receives a message from the
   * server indicating that it denied to log us in.
   * 
   * @param reason The reason why the server denied to log us in. May be <code>null</code>.
   */
  
  protected synchronized final void loginFailed(String reason){
    if (isLoggedIn())
      throw new IllegalStateException("Already logged in");
      
    // We are already in the client thread, so there's no need to use execRunnable
    handleLoginFailed(reason);
    
    close();
  }
  
  

  /**
   * Any invocation of client code (all the <code>handleXXX</code> methods) from the thread that reads data from the
   * server is done via this method. This allows clients to specify which thread they want to handle connection events
   * in, which is especially useful for Swing/AWT based applications. The default implementation simply invokes the
   * runnable, so the event handling is done in the reader thread.
   */

  protected void execRunnable(Runnable runnable){
    runnable.run();
  }
  
  
  
  /**
   * Invoked when a connection to the server is established. The default implementation invokes
   * {@link #initiateLogin()}.
   */
  
  protected void handleConnected(){
    initiateLogin();
  }
  
  
  
  /**
   * Invoked when the attempt to connect to the server fails. The default implementation does nothing.
   * 
   * @param exception The <code>IOException</code> thrown when attempting to connect.
   */
  
  protected void handleConnectingFailed(IOException exception){
    
  }
  
  
  
  /**
   * Invoked when a message arrives from the server. The specified message is an object returned by the
   * {@link #readMessage(InputStream)} method, so an implementation can cast it to the appropriate type.
   * It is up to this method to recognize when the server acknowledges or denies login and invoke the
   * {@link #loginSucceeded(String)} or {@link #loginFailed(String)} methods accordingly.
   */
  
  protected abstract void handleMessage(Object message);
  
  
  
  /**
   * Invoked when the server acknowledges that we are logged in. The default implementation does nothing.
   * 
   * @param username The username the server assigned us.
   */
  
  protected void handleLoginSucceeded(){
    
  }
  
  
  
  /**
   * Invoked when the server denies to log us in. The default implementation does nothing.
   * 
   * @param reason The reason the server denied to log us in. May be <code>null</code>.
   */
  
  protected void handleLoginFailed(String reason){
    
  }
  
  
  
  /**
   * Invoked when we are disconnected from the server. The specified <code>IOException</code> is the exception which
   * caused us to believe we are disconnected. It is <code>null</code> in the case of a clean disconnection (EOF read
   * from the server). The default implementation does nothing.
   */
  
  protected void handleDisconnection(IOException e){
    
  }
  
  
  
  /**
   * A runnable which invokes the {@link #handleMessage(Object)} method.
   */
  
  private class MessageDispatcher extends SafeRunnable{
    
    
    
    /**
     * The message to handle.
     */
    
    private final Object message;
    
    
    
    /**
     * Creates a new <code>MessageDispatcher</code>.
     */
    
    public MessageDispatcher(Object message){
      this.message = message;
    }
    
    
    
    /**
     * Invokes {@link Connection#handleMessage(Object)} with the message.
     */
    
    public void safeRun(){
      handleMessage(message);
    }
    
    
    
  };
  
  
  
}
