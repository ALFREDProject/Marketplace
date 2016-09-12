package com.tempos21.mymarket.data.database.mapper;

import java.util.List;

public interface EntityMapper<E, M> {

  E toEntity(M model);

  M toModel(E entity);

  List<E> toEntity(List<M> modelList);

  List<M> toModel(List<E> entityList);
}

