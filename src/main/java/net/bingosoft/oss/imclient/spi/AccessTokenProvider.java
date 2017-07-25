package net.bingosoft.oss.imclient.spi;

import net.bingosoft.oss.imclient.model.AccessToken;

/**
 * @author kael.
 */
public interface AccessTokenProvider {
    
    AccessToken obtainAccessTokenByClientCredentials();
    
    AccessToken refreshAccessToken(AccessToken accessToken);
}
