package net;

  import java.net.*;
  import java.net.Socket;
  import java.io.*;
  import java.util.logging.Level;
import java.util.logging.Logger;

import kotlinLabGlobal.Interpreter.GlobalValues;
import static kotlinLabGlobal.Interpreter.GlobalValues.*;

  import static net.NetSVD.*;

// kotlinLabServer performs computations using socket based communication
public class  kotlinLabNet  {
 
 
  
 // reads the array bd from the input stream reader
    public static void readArray(DataInputStream reader, double [] da, int  len)  {
      for (int  k = 0; k<len; k++)
          try {
              da[k] = reader.readDouble();
          } catch (IOException ex) {
              System.out.println("error in readArray");
          }
       
    }         
 
 // reads the array bd from the input stream writer
public static void      readArray(DataInputStream reader, double [][]  da, int Nrows, int  Ncols)   {
     for (int r = 0;  r < Nrows; r++) 
         for (int c = 0; c < Ncols; c++) 
           try {
               da[r][c] = reader.readDouble();
         } catch (IOException ex) {
              System.out.println("error in readArray");
         }
          
         }
           
             

  // reads the array bd from the input stream reader
public static void        writeArray ( DataOutputStream   writer, double []  da,  int  len)   {
         try {
     for (int k = 0; k< len; k++)  
             writer.writeDouble(da[k]);
     writer.flush();
         } catch (IOException ex) {
             System.out.println("error in writeArray");
         }
         
       }        
 
 
 // writes the array bd onto the output stream writer
public static void     writeArray(DataOutputStream  writer,  double [][] bd,  int  Nrows,  int  Ncols)  {
    try {
   for (int r = 0; r <  Nrows; r++) 
       for (int c = 0; c < Ncols; c++) 
            writer.writeDouble(bd[r][c]);
 
      writer.flush();
    } 
    catch (IOException ex) {
                        System.out.println("error in writeArray");
             }
}

// init the kotlinLab server
  public static void  initServer()   {  // init server with a seperate thread
    Thread initThread =   new Thread(() -> {
        try {
              GlobalValues.kotlinLabServerSocket =  new ServerSocket( GlobalValues.kotlinLabServerPort);
            System.out.println("Server Socket created ");
            Socket   saccepted = GlobalValues.kotlinLabServerSocket.accept();   // accept a connection from the client
            System.out.println("Connection on server socket accepted");
            InputStream serverReadStream = saccepted.getInputStream();   // input stream
            OutputStream  serverWriteStream = saccepted.getOutputStream(); // output stream
            DataInputStream reader1 = new DataInputStream(new BufferedInputStream(serverReadStream));
            DataOutputStream writer1 = new DataOutputStream( new BufferedOutputStream(serverWriteStream));
                 
            boolean  hasFinished = false;
            while (!hasFinished) {
                int operationCode = reader1.readInt();
                switch (operationCode) {
                    case  GlobalValues.exitCode:   hasFinished = true; break;
                    case GlobalValues.svdCode:
                        serverCompSVD(reader1, writer1);
                        break;
                    default:  hasFinished = false; break;
                }
            } // hasFinished
        } // try
        catch (Exception e) {
            System.out.println("error in init");
        }
    } // run
    // Runnable
    );
    
  //  System.out.println("attempting to srart server initialization");
    initThread.start();
  }
 
          
      
         
 // init a client that connects to a tremote kotlinLab server
 public static void initClient() {
        try {
            //   System.out.println("before client socket creation");
            GlobalValues.sclient = new Socket(InetAddress.getByName(serverIP), GlobalValues.kotlinLabServerPort);
            //   System.out.println("after client socket creation");
            
            
            GlobalValues.clientReadStream = GlobalValues.sclient.getInputStream();
            GlobalValues.clientWriteStream = GlobalValues.sclient.getOutputStream();
                            
            GlobalValues.reader = new DataInputStream(GlobalValues.clientReadStream);
            GlobalValues.writer = new DataOutputStream(GlobalValues.clientWriteStream);
    } 
        catch (IOException ex) {
            Logger.getLogger(kotlinLabNet.class.getName()).log(Level.SEVERE, null, ex);
        }
 }
 

 
 // a client residing at the same address as the server
public static void initLocalHostClient() {
        try {
          //   System.out.println("before local host client socket creation");
            GlobalValues.sclient = new Socket(InetAddress.getLoopbackAddress(), GlobalValues.kotlinLabServerPort);
           //  System.out.println("after client socket creation");
            
            
            GlobalValues.clientReadStream = GlobalValues.sclient.getInputStream();
            GlobalValues.clientWriteStream = GlobalValues.sclient.getOutputStream();
                            
            GlobalValues.reader = new DataInputStream(GlobalValues.clientReadStream);
            GlobalValues.writer = new DataOutputStream(GlobalValues.clientWriteStream);
    } 
        catch (IOException ex) {
            Logger.getLogger(kotlinLabNet.class.getName()).log(Level.SEVERE, null, ex);
        }
 }
 



  
  
public static void specifyServerIP(String  IPOfkotlinLabServer)  {
    serverIP  = IPOfkotlinLabServer;
    
}
  
  
}
                           
