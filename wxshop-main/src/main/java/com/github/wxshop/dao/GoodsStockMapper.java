package com.github.wxshop.dao;

import com.github.api.data.GoodsInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GoodsStockMapper {
  int deductStock(GoodsInfo goodsInfo);
}
