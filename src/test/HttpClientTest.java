package test; 
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.junit.Test;
import org.springframework.util.FileCopyUtils;

import com.alibaba.fastjson.JSONArray;
public class HttpClientTest implements X509TrustManager { 
    
    @Test
    public void connTest() throws Exception{
        String sitUrl = "https://kyfw.12306.cn";
     // 创建SSLContext对象，并使用我们指定的信任管理器初始化 
        TrustManager[] tm = { new HttpClientTest() }; 
        SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE"); 
        sslContext.init(null, tm, new java.security.SecureRandom()); 
        // 从上述SSLContext对象中得到SSLSocketFactory对象 
        SSLSocketFactory ssf = sslContext.getSocketFactory(); 
        // 创建URL对象 
        URL myURL = new URL(sitUrl + "/otn/login/init"); 
        // 创建HttpsURLConnection对象，并设置其SSLSocketFactory对象 
        HttpsURLConnection httpsConn = (HttpsURLConnection) myURL.openConnection(); 
        httpsConn.setSSLSocketFactory(ssf); 
        // 取得该连接的输入流，以读取响应内容 
        Map<String, List<String>> headerFields = httpsConn.getHeaderFields();
        List<String> list = headerFields.get("Set-Cookie");
        String cookiesStr = JSONArray.toJSONString(list);
        String BIGipServerotn =cookiesStr.substring(cookiesStr.indexOf("BIGipServerotn=") + "BIGipServerotn=".length(), cookiesStr.indexOf(";",cookiesStr.indexOf("BIGipServerotn=") + "BIGipServerotn=".length()));
        String JSESSIONID =cookiesStr.substring(cookiesStr.indexOf("JSESSIONID=") + "JSESSIONID=".length(), cookiesStr.indexOf(";",cookiesStr.indexOf("JSESSIONID=") + "JSESSIONID=".length()));
        BufferedReader br = new BufferedReader(new InputStreamReader(httpsConn.getInputStream())); 
        // 读取服务器的响应内容并显示 
        String line = null;
        String dynamicJsUrl = null;
       while((line = br.readLine()) != null){
           if(line.contains("dynamicJs")){
               dynamicJsUrl =sitUrl +  line.substring(line.indexOf("\"") + 1,line.indexOf("\"",line.indexOf("\"") + 1));
               System.out.println(dynamicJsUrl);
           }
       }
        System.out.println("---------------------------------------------------------------------");
        System.out.println("BIGipServerotn:" + BIGipServerotn);
        System.out.println("JSESSIONID:" + JSESSIONID);
        System.out.println("---------------------------------------------------------------------");
        /****************************/
        // 创建URL对象 
        URL jsURL = new URL(dynamicJsUrl); 
        // 创建HttpsURLConnection对象，并设置其SSLSocketFactory对象 
        HttpsURLConnection jsConn = (HttpsURLConnection) jsURL.openConnection(); 
        jsConn.setRequestProperty("Cookie", "JSESSIONID=" + JSESSIONID + ";BIGipServerotn="+ BIGipServerotn);
        jsConn.setSSLSocketFactory(ssf); 
        BufferedReader br2 = new BufferedReader(new InputStreamReader(jsConn.getInputStream())); 
        String dynamicJs = "";
        while((line = br2.readLine()) != null){
            dynamicJs += line;
       }
        String key = dynamicJs.substring(dynamicJs.indexOf("gc(){var key='") + "gc(){var key='".length(), dynamicJs.indexOf("'",dynamicJs.indexOf("gc(){var key='") + "gc(){var key='".length()));
        byte[] array = FileCopyUtils.copyToByteArray(new File("D:/12306.js"));
        String jsStr = new String(array);
        String ret = runSecretKeyValueMethod(key,jsStr);
        System.out.println(ret);
    }
    
    public static String runSecretKeyValueMethod(String mark,String jsStr) throws Exception {
        ScriptEngineManager sem = new ScriptEngineManager();
        ScriptEngine se = sem.getEngineByExtension("js");
        se.eval(jsStr);
        String value = (String) se.eval("eval(\"encode32(bin216(Base32.encrypt('1111','"+mark+"')))\")");
        System.out.println("secret value = " + value);
        return value;
    }
    
    /*
     * The default X509TrustManager returned by SunX509.  We'll delegate
     * decisions to it, and fall back to the logic in this class if the
     * default X509TrustManager doesn't trust it.
     */ 
    X509TrustManager sunJSSEX509TrustManager; 
    public HttpClientTest() throws Exception { 
        // create a "default" JSSE X509TrustManager. 
        KeyStore ks = KeyStore.getInstance("JKS"); 
        ks.load(new FileInputStream("d:/dest_cer_store"), 
            "wyfeng".toCharArray()); 
        TrustManagerFactory tmf = 
        TrustManagerFactory.getInstance("SunX509", "SunJSSE"); 
        tmf.init(ks); 
        TrustManager tms [] = tmf.getTrustManagers(); 
        /*
         * Iterate over the returned trustmanagers, look
         * for an instance of X509TrustManager.  If found,
         * use that as our "default" trust manager.
         */ 
        for (int i = 0; i < tms.length; i++) { 
            if (tms[i] instanceof X509TrustManager) { 
                sunJSSEX509TrustManager = (X509TrustManager) tms[i]; 
                return; 
            } 
        } 
        /*
         * Find some other way to initialize, or else we have to fail the
         * constructor.
         */ 
        throw new Exception("Couldn't initialize"); 
    } 
    /*
     * Delegate to the default trust manager.
     */ 
    public void checkClientTrusted(X509Certificate[] chain, String authType) 
                throws CertificateException { 
        try { 
            sunJSSEX509TrustManager.checkClientTrusted(chain, authType); 
        } catch (CertificateException excep) { 
            // do any special handling here, or rethrow exception. 
        } 
    } 
    /*
     * Delegate to the default trust manager.
     */ 
    public void checkServerTrusted(X509Certificate[] chain, String authType) 
                throws CertificateException { 
        try { 
            sunJSSEX509TrustManager.checkServerTrusted(chain, authType); 
        } catch (CertificateException excep) { 
            /*
             * Possibly pop up a dialog box asking whether to trust the
             * cert chain.
             */ 
        } 
    } 
    /*
     * Merely pass this through.
     */ 
    public X509Certificate[] getAcceptedIssuers() { 
        return sunJSSEX509TrustManager.getAcceptedIssuers(); 
    } 
} 
