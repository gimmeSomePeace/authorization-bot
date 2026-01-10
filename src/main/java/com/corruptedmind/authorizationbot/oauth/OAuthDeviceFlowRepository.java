package com.corruptedmind.authorizationbot.oauth;

import com.corruptedmind.authorizationbot.oauth.dto.*;
import com.corruptedmind.authorizationbot.oauth.exception.OAuthError;
import com.nimbusds.oauth2.sdk.*;
import com.nimbusds.oauth2.sdk.device.*;
import com.nimbusds.oauth2.sdk.http.HTTPRequest;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import com.nimbusds.oauth2.sdk.id.ClientID;
import com.nimbusds.oauth2.sdk.token.AccessToken;
import com.nimbusds.oauth2.sdk.token.Tokens;

import java.io.IOException;


public class OAuthDeviceFlowRepository {
    private final OAuthConf conf;

    public OAuthDeviceFlowRepository(OAuthConf conf) {
        this.conf = conf;
    }

    public DeviceIdResponse requestDeviceCode() {
        ClientID clientID = new ClientID(conf.clientId());
        DeviceAuthorizationRequest request = new DeviceAuthorizationRequest(conf.deviceEndpoint(), clientID);

        HTTPRequest httpRequest = request.toHTTPRequest();
        httpRequest.setAccept("application/json");
        httpRequest.setConnectTimeout(10_000);
        httpRequest.setReadTimeout(10_000);

        try {
            HTTPResponse httpResponse = httpRequest.send();
            DeviceAuthorizationResponse response = DeviceAuthorizationResponse.parse(httpResponse);
            if (!response.indicatesSuccess()) {
                DeviceAuthorizationErrorResponse error = response.toErrorResponse();
                throw new IllegalStateException("Device authorization error: " + error.getErrorObject());
            }

            DeviceAuthorizationSuccessResponse success = response.toSuccessResponse();
            return new DeviceIdResponse(
                    success.getDeviceCode().getValue(),
                    success.getUserCode().getValue(),
                    success.getVerificationURI(),
                    success.getLifetime(),
                    success.getInterval()
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public TokenResult pollForToken(String deviceCode) {
        AuthorizationGrant grant = new DeviceCodeGrant(new DeviceCode(deviceCode));
        com.nimbusds.oauth2.sdk.TokenRequest tokenRequest = new com.nimbusds.oauth2.sdk.TokenRequest(
                conf.tokenEndpoint(),
                new ClientID(conf.clientId()),
                grant
        );
        HTTPRequest httpRequest = tokenRequest.toHTTPRequest();
        httpRequest.setAccept("application/json");
        try {
            HTTPResponse httpResponse = httpRequest.send();
            TokenResponse tokenResponse = TokenResponse.parse(httpResponse.getBodyAsJSONObject());
            if (tokenResponse.indicatesSuccess()) {
                Tokens tokens = tokenResponse.toSuccessResponse().getTokens();
                AccessToken accessToken = tokens.getAccessToken();
                com.corruptedmind.authorizationbot.oauth.dto.AccessToken token = new com.corruptedmind.authorizationbot.oauth.dto.AccessToken(
                        accessToken.getValue(),
                        accessToken.getType().getValue(),
                        accessToken.getScope() != null
                                        ? accessToken.getScope().toString()
                                        : null
                );

                return new TokenResult.Success(token);
            }

            TokenErrorResponse tokenErrorResponse = tokenResponse.toErrorResponse();
            ErrorObject errorObject = tokenErrorResponse.getErrorObject();

            return new TokenResult.Error(
                    new OAuthError(errorObject.getCode(), errorObject.getDescription())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
