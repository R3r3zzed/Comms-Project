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
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import static org.hamcrest.CoreMatchers.instanceOf;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.RepeatedTest;
//import org.junit.jupiter.api.Test;
import org.junit.Test;
import java.util.Date;
import java.util.Vector;

import org.junit.jupiter.api.BeforeEach;

public class testServerHandler {
    @Test
    public void testServerObject(){
        Server.ServerHandler madison = new Server.ServerHandler(null);
        Server.ServerHandler root = new Server.ServerHandler(null);
        //these are all equal
        assertEquals(Server.ServerHandler.ServerInfo, madison.ServerInfo);
        assertEquals(Server.ServerHandler.ServerInfo, root.ServerInfo);
        assertEquals(madison.ServerInfo, root.ServerInfo);
    }
    
        
    @Test
    public void testSendFile(){
        Server.ServerHandler madison = new Server.ServerHandler(null);

    }

    @Test
    public void testLogOut(){

    }


    @Test
    public void testReceiveNewMessageClass(){

    }


}
