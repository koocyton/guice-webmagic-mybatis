package com.doopp.gauss.server.dao;

import com.doopp.gauss.server.entity.Movie;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface MovieDao {

    @Select("SELECT * FROM `movie_data` WHERE name=#{name,jdbcType=VARCHAR} LIMIT 1")
    List<Movie> fetchListByName(String name);

    @Select("SELECT * FROM `movie_data` WHERE id=#{id,jdbcType=BIGINT} LIMIT 1")
    Movie fetchById(long id);

    @Select("SELECT count(*) FROM `movie_data` LIMIT 1")
    Long count();

    @Insert("INSERT INTO `movie_data` (`id`, `name`, `cover`, `type`, `intro`, `from_url`, `download_links`) VALUES (${id}, #{name}, #{cover}, #{type}, #{intro}, #{from_url}, #{download_links})")
    void create(Movie movie);

    // @Delete("DELETE FROM `user` WHERE id=${id,jdbcType=BIGINT}")
    // void delete(int id);

    // @Update("UPDATE `user` SET `account`=#{account}, `password`=#{password}, `password_salt`=${password_salt} WHERE `id`=#{id,jdbcType=BIGINT}")
    // void update(page user);
}
