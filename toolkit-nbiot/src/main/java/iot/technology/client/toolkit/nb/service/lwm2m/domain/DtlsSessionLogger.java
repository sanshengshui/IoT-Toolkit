
package iot.technology.client.toolkit.nb.service.lwm2m.domain;

import org.eclipse.californium.scandium.dtls.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DtlsSessionLogger extends SessionAdapter {
    private static final Logger LOG = LoggerFactory.getLogger(DtlsSessionLogger.class);

    private SessionId sessionIdentifier = null;

    @Override
    public void handshakeStarted(Handshaker handshaker) throws HandshakeException {
        if (handshaker instanceof ResumingServerHandshaker) {
            LOG.info("DTLS abbreviated Handshake initiated by server : STARTED ...");
        } else if (handshaker instanceof ServerHandshaker) {
            LOG.info("DTLS Full Handshake initiated by server : STARTED ...");
        } else if (handshaker instanceof ResumingClientHandshaker) {
            sessionIdentifier = handshaker.getSession().getSessionIdentifier();
            LOG.info("DTLS abbreviated Handshake initiated by client : STARTED ...");
        } else if (handshaker instanceof ClientHandshaker) {
            LOG.info("DTLS Full Handshake initiated by client : STARTED ...");
        }
    }

    @Override
    public void contextEstablished(Handshaker handshaker, DTLSContext establishedContext) throws HandshakeException {
        if (handshaker instanceof ResumingServerHandshaker) {
            LOG.info("DTLS abbreviated Handshake initiated by server : SUCCEED");
        } else if (handshaker instanceof ServerHandshaker) {
            LOG.info("DTLS Full Handshake initiated by server : SUCCEED");
        } else if (handshaker instanceof ResumingClientHandshaker) {
            if (sessionIdentifier != null && sessionIdentifier.equals(handshaker.getSession().getSessionIdentifier())) {
                LOG.info("DTLS abbreviated Handshake initiated by client : SUCCEED");
            } else {
                LOG.info("DTLS abbreviated turns into Full Handshake initiated by client : SUCCEED");
            }
        } else if (handshaker instanceof ClientHandshaker) {
            LOG.info("DTLS Full Handshake initiated by client : SUCCEED");
        }
    }

    @Override
    public void handshakeFailed(Handshaker handshaker, Throwable error) {
        // get cause
        String cause;
        if (error != null) {
            if (error.getMessage() != null) {
                cause = error.getMessage();
            } else {
                cause = error.getClass().getName();
            }
        } else {
            cause = "unknown cause";
        }

        if (handshaker instanceof ResumingServerHandshaker) {
            LOG.info("DTLS abbreviated Handshake initiated by server : FAILED ({})", cause);
        } else if (handshaker instanceof ServerHandshaker) {
            LOG.info("DTLS Full Handshake initiated by server : FAILED ({})", cause);
        } else if (handshaker instanceof ResumingClientHandshaker) {
            LOG.info("DTLS abbreviated Handshake initiated by client : FAILED ({})", cause);
        } else if (handshaker instanceof ClientHandshaker) {
            LOG.info("DTLS Full Handshake initiated by client : FAILED ({})", cause);
        }
    }
}
