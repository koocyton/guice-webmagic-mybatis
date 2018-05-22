package com.doopp.gauss.server.dao;

import com.doopp.gauss.server.entity.page;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface PageDao {

    @Select("SELECT * FROM `page` WHERE name=#{name,jdbcType=VARCHAR} LIMIT 1")
    List<page> fetchListByName(String name);

    @Select("SELECT * FROM `page` WHERE id=#{id,jdbcType=BIGINT} LIMIT 1")
    page fetchById(long id);

    @Select("SELECT count(*) FROM `page` LIMIT 1")
    Long count();

    // @Insert("INSERT INTO `user` (`id`, `account`, `password`, `password_salt`, `create_at`) VALUES (${id}, #{account}, #{password}, #{password_salt}, ${created_at})")
    // void create(page user);

    // @Delete("DELETE FROM `user` WHERE id=${id,jdbcType=BIGINT}")
    // void delete(int id);

    // @Update("UPDATE `user` SET `account`=#{account}, `password`=#{password}, `password_salt`=${password_salt} WHERE `id`=#{id,jdbcType=BIGINT}")
    // void update(page user);
}
