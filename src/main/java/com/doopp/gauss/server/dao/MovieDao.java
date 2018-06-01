package com.doopp.gauss.server.dao;

import com.doopp.gauss.server.entity.Movie;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface MovieDao {

    @Select("SELECT * FROM `data_film` WHERE name=#{name,jdbcType=VARCHAR} LIMIT 1")
    List<Movie> fetchListByName(String name);

    @Select("SELECT * FROM `data_film` WHERE id=#{id,jdbcType=BIGINT} LIMIT 1")
    Movie fetchById(long id);

    @Select("SELECT count(*) FROM `movie_data` LIMIT 1")
    Long count();

    @Insert("INSERT INTO `data_film` " +
            "(`id`, `name`, `cover`, `type`, `intro`, `from_url`, `resources`) " +
            "VALUES " +
            "(${id}, #{name}, #{cover}, #{type}, #{intro}, #{from_url}, #{resources})")
    void create(Movie movie);

    // @Delete("DELETE FROM `user` WHERE id=${id,jdbcType=BIGINT}")
    // void delete(int id);

    // @Update("UPDATE `user` SET `account`=#{account}, `password`=#{password}, `password_salt`=${password_salt} WHERE `id`=#{id,jdbcType=BIGINT}")
    // void update(page user);
}
