package com.tempos21.mymarket.data.client.mapper;

import java.util.List;

public interface VOMapper<V, M> {

    V toVO(M model);

    M toModel(V vo);

    List<V> toVO(List<M> modelList);

    List<M> toModel(List<V> voList);

}

