package eu.vilaca.remote.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

public class RequestHandler extends AbstractHandler {

    private final Map<String, Object[]> list = new HashMap<>();

    public void register(Object obj, Class<? extends Object> class1) {

        for (Method m : class1.getDeclaredMethods()) {
            final String name = class1.getCanonicalName() + "." + m.getName();
            list.put(name, new Object[] { m, obj });
            System.out.println(name);
        }

    }

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        System.out.println(target);
        
        Object[] meta = list.get(target.substring(1));
        
        Method ele = (Method) meta[0];
        Object invoke = meta[1];
        
        String data = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        ByteArrayInputStream bais = new ByteArrayInputStream(Base64.getDecoder().decode(data));

        ObjectInputStream ois = new ObjectInputStream(bais);

        List<Object> os = new ArrayList<>();

        Object o;

        try {
            while (true) {
                o = ois.readObject();
                os.add(o);
            }
        } catch (ClassNotFoundException e1) {

            e1.printStackTrace();
        } catch (IOException iow) {

        }

        Object[] params = os.toArray(new Object[os.size()]);
        Class<?>[] ts = new Class<?>[os.size()];

        for (int i = 0; i < params.length; i++) {
            ts[i] = params[i].getClass();
        }

        Object re;
        try {
            // Method m2 = cc.getClass().getDeclaredMethod("calc", ts);
            re = ele.invoke(invoke, params);
            // m.invoke(o, params);
        } catch (IllegalArgumentException | SecurityException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return;
        }

        String payload = Base64.getEncoder().encodeToString(serialize(re));
        
        baseRequest.setHandled(true);
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        
        try (DataOutputStream wr = new DataOutputStream(response.getOutputStream());) {
            wr.writeBytes(payload);
            wr.flush();
        }        
    }

    private byte[] serialize(Object obj) {

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try (final ObjectOutputStream out = new ObjectOutputStream(baos);) {

            out.writeObject(obj);

            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return baos.toByteArray();
    }

}
