package com.tempos21.mymarket.data.client.mapper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tempos21.mymarket.data.client.response.ClientResponse;
import com.tempos21.mymarket.data.client.response.push.TokenVO;
import com.tempos21.mymarket.sdk.model.Token;

import java.lang.reflect.Type;

public class TokenVOMapper extends BaseVOMapper<TokenVO, Token> {

    public static ClientResponse<TokenVO> mapResponse(String json) {
        Type type = new TypeToken<ClientResponse<TokenVO>>() {
        }.getType();
        return new Gson().fromJson(json, type);
    }

    @Override
    public TokenVO toVO(Token model) {
        return null;
    }

    @Override
    public Token toModel(TokenVO vo) {
        Token token = new Token();
        token.uuid = vo.uuid;
        token.platforms = new PlatformVOMapper().toModel(vo.platforms);
        token.users = new UserVOMapper().toModel(vo.users);
        token.version = vo.version;
        token.id = vo.id;

        return token;
    }
}
