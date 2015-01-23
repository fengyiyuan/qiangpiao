import java.io.File;
import java.security.MessageDigest;

import org.junit.Test;
import org.springframework.util.FileCopyUtils;

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
        byte[] digest = md.digest();
        for (byte b : digest) {
            System.out.print(Integer.toHexString(b&0XFF).toUpperCase());
        }
        
    }
}
