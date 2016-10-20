
package eu.vilaca.remote;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import eu.vilaca.remote.handlers.Handler;
import eu.vilaca.remote.handlers.RemoteHandler;
import eu.vilaca.remote.server.*;

public class Deputy {

    private static final Map<String, Object> list = new HashMap<>(); 
    
    public static void register(Object obj, Class<? extends Object> class1) {        
        list.put(class1.getCanonicalName(), obj);
    }

    public static Object create(Class<? extends Object> class1) throws InstantiationException, IllegalAccessException {

        return Proxy.newProxyInstance(
                class1.getClassLoader(),
                new Class[] { class1 },
                new Handler(list));
    }
        
    public static Object useShared(Class<? extends Object> class1) throws InstantiationException, IllegalAccessException {

        return Proxy.newProxyInstance(
                class1.getClassLoader(),
                new Class[] { class1 },
                new RemoteHandler(class1));
    }
         
    public static DeputyServer shared() {
        return new DeputyServer();
    }
}
