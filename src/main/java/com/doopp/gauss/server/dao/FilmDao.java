package com.doopp.gauss.server.dao;

import com.doopp.gauss.server.entity.FilmComment;
import com.doopp.gauss.server.entity.FilmInfo;
import com.doopp.gauss.server.entity.FilmSource;
import org.apache.ibatis.annotations.*;

public interface FilmDao {

//    @Select("SELECT * FROM `data_film2` WHERE name=#{name,jdbcType=VARCHAR} LIMIT 1")
//    List<FilmInfo> fetchListByName(String name);
//
//    @Select("SELECT * FROM `data_film2` WHERE id=#{id,jdbcType=BIGINT} LIMIT 1")
//    FilmInfo fetchById(long id);
//
//    @Select("SELECT count(*) FROM `movie_data2` LIMIT 1")
//    Long count();

    @Insert("INSERT INTO `film_info` " +
            "(`id`, `film_name`, `film_cover`, `directors`, `screenwriters`, `actors`, `genres`, `region`, `language`, `release_date`, `film_duration`, `alias`, `douban_id`, `douban_rating`, `imdb_link`, `imdb_rating`) " +
            "VALUES " +
            "(${id}, #{film_name}, #{film_cover}, #{directors}, #{screenwriters}, #{actors}, #{genres}, #{region}, #{language}, #{release_date}, ${film_duration}, #{alias}, ${douban_id}, ${douban_rating}, #{imdb_link}, ${imdb_rating})")
    void saveInfo(FilmInfo filmInfo);

    @Insert("INSERT INTO `film_source` " +
            "(`source_id`, `film_id`, `film_name`, `source_site`, `source_url`, `source_data`) " +
            "VALUES " +
            "(${source_id}, ${film_id}, #{film_name}, #{source_site}, #{source_url}, #{source_data})")
    void saveSource(FilmSource filmSource);


    @Insert("INSERT INTO `film_comment` " +
            "(`comment_id`, `film_id`, `film_name`, `vote`, `user_name`, `date_time`, `content`) " +
            "VALUES " +
            "(${comment_id}, ${film_id}, #{film_name}, ${vote}, #{user_name}, #{date_time}, #{content})")
    void saveComment(FilmComment filmComment);

    // @Delete("DELETE FROM `user` WHERE id=${id,jdbcType=BIGINT}")
    // void delete(int id);

    // @Update("UPDATE `user` SET `account`=#{account}, `password`=#{password}, `password_salt`=${password_salt} WHERE `id`=#{id,jdbcType=BIGINT}")
    // void update(page user);
}
