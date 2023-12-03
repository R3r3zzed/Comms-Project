import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Date;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.junit.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;


public class testServerHandler {
    @Test
    void testServerObject(){
        Server.ServerHandler madison = new Server.ServerHandler(null);
        Server.ServerHandler root = new Server.ServerHandler(null);
        //these are all equal
        assertEquals(Server.ServerHandler.ServerInfo, madison.ServerInfo);
        assertEquals(Server.ServerHandler.ServerInfo, root.ServerInfo);
        assertEquals(madison.ServerInfo, root.ServerInfo);
    }
    
    DataOutputStream fakeOutputEndPoint;
    DataInputStream fakeInputEndPoint;
    @Before
    void initFile() throws IOException {
        //set up fake backend to catch file object sent by the ServerHandler
        PipedInputStream pipeInput = new PipedInputStream();     
        fakeInputEndPoint = new DataInputStream(pipeInput);

        fakeOutputEndPoint = new DataOutputStream(fakeOutputEndPoint);
        BufferedOutputStream out = new BufferedOutputStream(  new PipedOutputStream(new PipedInputStream()));
        // Here you will have to mock the output somehow inside your 
        //target object.
        targetObject.setOutputStream(out);
        }
    @Test
    void testSendFile(){
        Server.ServerHandler madison = new Server.ServerHandler(null);

        //send the files to the fake end point
        sendFile.

        //Invoke the target method
        targetObject.targetMethod();

        //Check that the correct data has been written correctly in 
        //the output stream reading it from the plugged input stream

        int bytes = 0;
        FileOutputStream fileOutputStream = new FileOutputStream(fileName);
 
        long size = dataInputStream.readLong(); // read file size
        byte[] buffer = new byte[4 * 1024];
        while (size > 0 && (bytes = dataInputStream.read(buffer, 0, (int)Math.min(buffer.length, size))) != -1) {
            // Here we write the file using write method
            fileOutputStream.write(buffer, 0, bytes);
            size -= bytes; // read upto file size
        }
        // Here we received file
        System.out.println("File is Received");


        Assert.assertEquals("something you expects", fakeInputEndPoint.);
    }

    @Test
    void testLoogOut(){

    }


    @Test
    void testReceiveNewMessageClass(){

    }






}
