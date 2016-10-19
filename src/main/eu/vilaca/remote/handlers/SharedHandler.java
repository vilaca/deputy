
package eu.vilaca.remote.handlers;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;

public class SharedHandler implements InvocationHandler {

    private String classname;

    public SharedHandler(Class<? extends Object> class1) {
        this.classname = class1.getCanonicalName();
    }

    @Override
    public Object invoke(Object nu, Method m, Object[] args) throws Throwable {

        final byte[] data = serialize(args);

        final String sdata = Base64.getEncoder().encodeToString(data);

        return sendPost(classname + "." + m.getName(), sdata);
    }

    private Object sendPost(String mname, String sdata) throws IOException {

        URLConnection con;
        try {
            URL url = new URL("http://localhost:8089/" + mname);
            con = url.openConnection();
            HttpURLConnection http = (HttpURLConnection) con;
            http.setRequestMethod("POST"); // PUT is another valid option
            http.setDoOutput(true);

        } catch (MalformedURLException | ProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

        String urlParameters = sdata;

        // Send post request
        con.setDoOutput(true);
        try (DataOutputStream wr = new DataOutputStream(con.getOutputStream());) {
            wr.writeBytes(urlParameters);
            wr.flush();
        }

        // int responseCode = con.getResponseCode();

        BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));

        String data = br.readLine();
        ByteArrayInputStream baos = new ByteArrayInputStream(Base64.getDecoder().decode(data));

        try (final ObjectInputStream ois = new ObjectInputStream(baos);) {
            Object o = ois.readObject();
            return o;
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    private byte[] serialize(Object[] params) {

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try (final ObjectOutputStream out = new ObjectOutputStream(baos);) {

            for (Object p : params) {
                out.writeObject(p);
            }

            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return baos.toByteArray();
    }
}
