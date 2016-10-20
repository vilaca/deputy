package eu.vilaca.remote;

import org.junit.Test;

import eu.vilaca.remote.server.DeputyServer;
import org.junit.Assert;

public class ProofOfConceptTest {

    @Test
    public void testShared() throws InstantiationException, IllegalAccessException {

        DeputyServer server = Deputy.shared();

        server.getHandler().registerSingleton(new SumImpl(), Sum.class);
        server.getHandler().registerSingleton(new MultImpl(), Mult.class);

        Thread t = new Thread(server);
        t.start();

        Sum sum = (Sum) Deputy.useShared(Sum.class);

        Assert.assertEquals(7, sum.calc(3, 4));

        Assert.assertEquals(70, sum.calc(30, 40));

        Mult mult = (Mult) Deputy.useShared(Mult.class);

        Assert.assertEquals(12, mult.calc(3, 4));

        Assert.assertEquals(25, mult.calc(5, 5));

    }

    @Test
    public void testLocal() throws InstantiationException, IllegalAccessException {

        Deputy.register(new SumImpl(), Sum.class);
        Deputy.register(new MultImpl(), Mult.class);

        Sum sum = (Sum) Deputy.create(Sum.class);

        Assert.assertEquals(7, sum.calc(3, 4));

        Assert.assertEquals(70, sum.calc(30, 40));

        Mult mult = (Mult) Deputy.create(Mult.class);

        Assert.assertEquals(12, mult.calc(3, 4));

        Assert.assertEquals(25, mult.calc(5, 5));
    }

}
