package iot.technology.client.toolkit.coap.service;

import org.eclipse.californium.core.CoapClient;

import java.net.URI;

/**
 * @author mushuwei
 */
public interface CoapClientService {

	/**
	 * Converts content type text into CoAP's media type code.
	 * If the input is a number assumes it is already a CoAP media type code.
	 *
	 * @param contentType - Content type number or text
	 * @return CoAP media type code
	 */
	int coapContentType(String contentType);

	/**
	 * a new CoapClient that sends request to the specified URI.
	 *
	 * @param uri the specified URI
	 * @return new CoapClient
	 */
	CoapClient getCoapClient(URI uri);
}
