package com.juzipi.Test;

import com.juzipi.demo.EncryptApplication;
import com.juzipi.demo.rsa.RsaKeys;
import com.juzipi.demo.service.RsaService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = EncryptApplication.class)
public class EncryptTest {

    @Autowired
    private RsaService rsaService;

    @Before
    public void before() throws Exception{
    }

    @After
    public void after() throws Exception {
    }

    @Test
    public void genEncryptDataByPubKey() {
        String data = "{\"columnid\":\"31\"}";

        try {

            String encData = rsaService.RSAEncryptDataPEM(data, RsaKeys.getServerPubKey());

            System.out.println("data: " + data);
            System.out.println("encData: " + encData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test() throws Exception {
        String requestData = "p68Cke76QsLngWc8F4E8rco+yNjEZM0DoUKVj3H2mT7UZwtbgunWCKtx7vc4WYMvyngLK7mH8UNEgTP/IAcskOYaV6B9mHzIDiO2Dbg7yUssC6QZCgId7DGHvyOb5g0VB6HXaWkoYjLePz2/cagT8Q5tjHh64ZRCkgdFEDQXSe4tKaFRZY7md1cEIDITLQptUXgGujunYtYMRcrHTkhpqCZtyyqnqnXNqxw7lH7k7pNxSiI69paxTE5uS2hIraL78Xh8yFdKFAykN8Tr4cBuiV+Qcdmvszb4k29NBHnBD2cf/5R5kwiSgINDxZSt8i8Kawc6sGzzYeSXej8jrBN91w==";
        String encryptDataPEM = rsaService.RSADecryptDataPEM(requestData, RsaKeys.getServerPrvKeyPkcs8());
        System.out.println(encryptDataPEM);

    }
}
