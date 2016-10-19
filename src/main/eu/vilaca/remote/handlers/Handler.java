
package eu.vilaca.remote.handlers;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

public class Handler implements InvocationHandler {

    final Map<String, Object> list;
    
    public Handler (Map<String, Object> list)
    {
        this.list = list;
    }
    
    @Override
    public Object invoke(Object unused, Method m, Object[] params) throws Throwable {
                    
        final String className = m.getDeclaringClass().getCanonicalName();
        Object obj = list.get(className);
        return m.invoke(obj, params);
    }
}
