package com.tempos21.mymarket.data.client.mapper;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseVOMapper<V, M> implements VOMapper<V, M> {

  @Override
  public List<M> toModel(List<V> voList) {
    List<M> modelList = new ArrayList<>(voList.size());
    for (V vo : voList) {
      modelList.add(toModel(vo));
    }
    return modelList;
  }

  @Override
  public List<V> toVO(List<M> modelList) {
    List<V> voList = new ArrayList<>(modelList.size());
    for (M model : modelList) {
      voList.add(toVO(model));
    }
    return voList;
  }
}
