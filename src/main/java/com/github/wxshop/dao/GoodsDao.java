package com.github.wxshop.dao;

import com.github.wxshop.entity.DataStatus;
import com.github.wxshop.generate.Goods;
import com.github.wxshop.generate.GoodsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GoodsDao {

    //注入mapper的方法
    private final GoodsMapper goodsMapper;

    @Autowired
    public GoodsDao(GoodsMapper goodsMapper) {
        this.goodsMapper = goodsMapper;
    }


    public Goods insertGoods(Goods goods) {
        //需要找到这个mapper所对应的mybatis所对应的sql来验证
        //就是找这个insert所对应的sql
        //这里的insert方法是goodsMapper所对应的insert方法
        long id = goodsMapper.insert(goods);
        goods.setId(id);
        return goods;
    }

    public Goods deleteGoodsById(Long goodsId) {
        //这里不执行物理删除
        Goods goods = goodsMapper.selectByPrimaryKey(goodsId);
        if (goods == null) {
            //商品不存在,返回404
            throw new ResourceNotFoundException("商品未找到");
        }
        goods.setStatus(DataStatus.DELETE_STATUS);
        goodsMapper.updateByPrimaryKey(goods);
        return goods;
    }

    public static class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(String message) {
            super(message);
        }
    }
}
