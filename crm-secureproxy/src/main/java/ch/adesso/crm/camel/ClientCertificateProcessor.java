package ch.adesso.crm.camel;

import java.security.cert.X509Certificate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class ClientCertificateProcessor implements Processor {

	private static final Pattern commonName = Pattern.compile("CN=(.*?),", Pattern.CASE_INSENSITIVE);

	public String getX509CommonName(Exchange exchange) {

		HttpServletRequest req = exchange.getIn().getBody(HttpServletRequest.class);
		X509Certificate[] certs = (X509Certificate[]) req.getAttribute("javax.servlet.request.X509Certificate");

		String name = null;

		if (certs != null && certs.length > 0) {

			Matcher m = commonName.matcher(certs[0].getSubjectX500Principal().getName());

			if (m.find() && m.groupCount() == 1) {

				name = m.group(1);
			}
		}

		return name;
	}

	@Override
	public void process(Exchange exchange) throws Exception {

		String commonName = getX509CommonName(exchange);
		
		if ( commonName != null ) {
			
			exchange.setProperty("X509CLientCertificateCommonName", commonName);
		}
	}

}
