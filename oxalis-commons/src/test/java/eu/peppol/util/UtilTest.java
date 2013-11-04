/* Created by steinar on 18.05.12 at 13:55 */
package eu.peppol.util;

import org.testng.annotations.Guice;
import org.testng.annotations.Test;
import sun.misc.BASE64Encoder;

import javax.mail.util.SharedByteArrayInputStream;
import java.io.InputStream;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * @author Steinar Overbeck Cook steinar@sendregning.no
 */
public class UtilTest {

    @Test
    public void testCalculateMD5() throws Exception {
        String hash = Util.calculateMD5("9908:810017902");

        assertEquals(hash, "ddc207601e442e1b751e5655d39371cd");
    }


    /** Experiments with byte arrays in order to verify that our understanding of the API is correct */
    @Test
    public void duplicateInputStream() throws Exception {

        InputStream inputStream = UtilTest.class.getClassLoader().getResourceAsStream("peppol-bis-invoice-sbdh.xml");
        assertNotNull(inputStream);

        byte[] bytes = Util.intoBuffer(inputStream);
        String s = new String(bytes);
        assertTrue(s.contains("</StandardBusinessDocument>"));


        SharedByteArrayInputStream sharedByteArrayInputStream = new SharedByteArrayInputStream(bytes);
        InputStream inputStream1 = sharedByteArrayInputStream.newStream(0, -1);

        byte[] b2 = Util.intoBuffer(inputStream1);
        assertEquals(bytes.length, b2.length);
    }
}
