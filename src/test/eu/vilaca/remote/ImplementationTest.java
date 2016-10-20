package eu.vilaca.remote;

import static org.junit.Assert.*;

import org.junit.Test;

import eu.vilaca.remote.server.DeputyServer;

public class ImplementationTest {

    @Test
    public void test() {

        DeputyServer server = Deputy.shared();

        Thread t = new Thread(server);
        t.start();

    }

}
