import java.io.File;
import java.security.MessageDigest;

import org.junit.Test;
import org.springframework.util.FileCopyUtils;

import sun.misc.BASE64Encoder;

/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */

/**
 * @author v_wuyunfeng
 *
 */
public class FengTest {
    
    @Test
    public void test1() throws Exception{
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(FileCopyUtils.copyToByteArray(new File("D:/sb.sql")));
        //BASE64Encoder encoder = new BASE64Encoder();
        byte[] digest = md.digest();
        for (byte b : digest) {
            System.out.print(Integer.toHexString(b&0XFF).toUpperCase());
        }
        
    }
}
