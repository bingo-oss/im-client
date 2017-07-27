package net.bingosoft.oss.imclient.spi;

import net.bingosoft.oss.imclient.model.AccessToken;

/**
 * 用于管理{@link AccessToken}的对象，包含获取代表应用身份的{@link AccessToken}和刷新{@link AccessToken}的功能
 * @author kael.
 */
public interface AccessTokenProvider {

    /**
     * 通过<code>clientId</code>和<code>clientSecret</code>获取{@link AccessToken}
     * 这里clientId和clientSecret如何获取由实现类决定
     * @return 返回只代表应用身份的{@link AccessToken}
     */
    AccessToken obtainAccessTokenByClientCredentials();

    /**
     * 将已经过期的{@link AccessToken}刷新为新的{@link AccessToken}
     * @return 返回一个全新的{@link AccessToken}
     */
    AccessToken refreshAccessToken(AccessToken accessToken);
}
