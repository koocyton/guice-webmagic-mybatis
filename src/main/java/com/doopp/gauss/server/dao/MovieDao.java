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
            "(`id`, `name`, `type`, `first_category`, `second_category`, `third_category`, `cover`, `publish_date`, `translate_name`, `film_name`, `release_age`, `origin_place`, `category`, `language`, `subtitle`, `release_date`, `video_format`, `video_size`, `file_size`, `time_length`, `director`, `actors`, `download_links`, `from_url`, `score`, `like`, `download_sum`, `browse_sum`) " +
            "VALUES " +
            "(${id}, #{name}, #{type}, #{first_category}, #{second_category}, #{third_category}, #{cover}, #{publish_date}, #{translate_name}, #{film_name}, #{release_age}, #{origin_place}, #{category}, #{language}, #{subtitle}, #{release_date}, #{video_format}, #{video_size}, #{file_size}, #{time_length}, #{director}, #{actors}, #{download_links}, #{from_url}, 0, 0, 0, 0)")
    void create(Movie movie);

    // @Delete("DELETE FROM `user` WHERE id=${id,jdbcType=BIGINT}")
    // void delete(int id);

    // @Update("UPDATE `user` SET `account`=#{account}, `password`=#{password}, `password_salt`=${password_salt} WHERE `id`=#{id,jdbcType=BIGINT}")
    // void update(page user);
}
