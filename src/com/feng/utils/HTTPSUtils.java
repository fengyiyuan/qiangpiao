/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.feng.utils;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * @author v_wuyunfeng
 *
 */
public class HTTPSUtils  implements X509TrustManager {
    
    private static SSLSocketFactory ssf;
    
    public static SSLSocketFactory getSSF(){
        if(ssf == null){
            try{
                TrustManager[] tm = { new HTTPSUtils() }; 
                SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE"); 
                sslContext.init(null, tm, new java.security.SecureRandom()); 
                // 从上述SSLContext对象中得到SSLSocketFactory对象 
                ssf = sslContext.getSocketFactory(); 
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return ssf;
    }
    
    X509TrustManager sunJSSEX509TrustManager; 
    public HTTPSUtils() throws Exception { 
        // create a "default" JSSE X509TrustManager. 
        KeyStore ks = KeyStore.getInstance("JKS"); 
        ks.load(new FileInputStream(PropUtils.getProp("keyStore.path")), 
                    PropUtils.getProp("keyStore.password").toCharArray()); 
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
