package eu.peppol.as2;

import eu.peppol.security.KeystoreManager;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
import javax.mail.internet.MimeMessage;

import static org.testng.Assert.assertNotNull;

/**
 * @author steinar
 *         Date: 22.10.13
 *         Time: 16:13
 */
public class SMimeMessageInspectorTest {

    private MimeMessage signedMimeMessage;

    @BeforeMethod
    public void setUp() throws MimeTypeParseException {
        KeystoreManager keystoreManager = KeystoreManager.getInstance();
        SMimeMessageFactory SMimeMessageFactory = new SMimeMessageFactory(keystoreManager.getOurPrivateKey(), keystoreManager.getOurCertificate());

        signedMimeMessage = SMimeMessageFactory.createSignedMimeMessage("Arne Barne Busemann", new MimeType("text", "plain"));
    }

    @Test
    public void testCalculateMic() throws Exception {

        SMimeMessageInspector SMimeMessageInspector = new SMimeMessageInspector(signedMimeMessage);
        Mic sha1 = SMimeMessageInspector.calculateMic("sha1");

        assertNotNull(sha1);

    }
}
