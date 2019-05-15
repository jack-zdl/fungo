package com.game.common.ts.mq.config;

import com.alibaba.mq.amqp.utils.UserUtils;
import com.rabbitmq.client.impl.CredentialsProvider;
import org.apache.commons.lang3.StringUtils;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class AliyunCredentialsProvider implements CredentialsProvider {

    /**
     * Access Key ID.
     */
    private String accessKeyId;

    /**
     * Access Key Secret.
     */
    private String accessKeySecret;

    /**
     * (Optional) Security temp token.
     */
    private final String securityToken;

    /**
     * 资源 Owner 账号（主账号）
     */
    private long resourceOwnerId;

    public AliyunCredentialsProvider(final String accessKeyId, final String accessKeySecret,
                                     final long resourceOwnerId) {
        this(accessKeyId, accessKeySecret, null, resourceOwnerId);
    }

    public AliyunCredentialsProvider(final String accessKeyId, final String accessKeySecret,
                                     final String securityToken, final long resourceOwnerId) {
        this.accessKeyId = accessKeyId;
        this.accessKeySecret = accessKeySecret;
        this.securityToken = securityToken;
        this.resourceOwnerId = resourceOwnerId;
    }

    @Override
    public String getUsername() {
        if (StringUtils.isNotEmpty(securityToken)) {
            return UserUtils.getUserName(accessKeyId, resourceOwnerId, securityToken);
        } else {
            return UserUtils.getUserName(accessKeyId, resourceOwnerId);
        }
    }

    @Override
    public String getPassword() {
        try {
            return UserUtils.getPassord(accessKeySecret);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
