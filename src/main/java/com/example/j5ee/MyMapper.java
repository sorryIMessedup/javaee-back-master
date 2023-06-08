package com.example.j5ee;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/***
 * @author Urmeas
 * @date 2022/10/1 12:14 上午
 */
public interface MyMapper<T> extends Mapper<T>, MySqlMapper<T> {
}
