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
        String requestData = "Tv1PQ2466rXPCtrwd0dl7PFsEtvMX05+6oEmYT6Tc7UuZwNQrXlAc2QxfiRXmYhWqKldVVU3WKMRMh08FW3rNvHN6hlYAG5xJA2yss1T+jX9tXztNVRFpUAEyEqgUhqhtc5k4n1zdB3c1fb9wo+wuwjMtqQlHzoEHsWDdnlX4z/ylZtdDsqe0Ls6OMRP8DpxRT+5XHBZDOV+8yy7H8+Ft0ow3kMIfPBytUf6e5Ueu4bw5R3vXlFiHuYIBUm6fAUcqMnYY0ZucRxOZTbldXpQiNuUmiyP08ZBQKW4BRcxze2W+E9FjQLbAcaIBWpEtdR3RBtmn3/KWl/l7TnNiZmhiw==";
        String encryptDataPEM = rsaService.RSADecryptDataPEM(requestData, RsaKeys.getServerPrvKeyPkcs8());
        System.out.println(encryptDataPEM);

    }
}
