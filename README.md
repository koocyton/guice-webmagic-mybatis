# spider movie

用于 webmagic 技术测试，

从豆瓣上抓取电影介绍和评论

从 piaohua 上获取电影的下载地址

```sql
CREATE TABLE `film_comment` (
  `comment_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '电影 ID',
  `film_id` bigint(20) NOT NULL COMMENT '电影主键',
  `film_name` varchar(100) NOT NULL COMMENT '电影名',
  `vote` int(10) NOT NULL DEFAULT '0' COMMENT '评论得票数',
  `user_name` varchar(100) NOT NULL COMMENT '评论人',
  `date_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '评论时间',
  `content` text NOT NULL COMMENT '评论时间',
  PRIMARY KEY (`comment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='电影评论信息';

CREATE TABLE `film_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `film_name` varchar(100) NOT NULL COMMENT '电影名',
  `film_cover` varchar(100) NOT NULL DEFAULT '' COMMENT '封面',
  `directors` varchar(100) NOT NULL DEFAULT '' COMMENT '导演',
  `screenwriters` varchar(100) NOT NULL DEFAULT '' COMMENT '编剧',
  `actors` text NOT NULL COMMENT '主演',
  `genres` varchar(100) NOT NULL DEFAULT '' COMMENT '分类',
  `region` varchar(100) NOT NULL DEFAULT '' COMMENT '地区',
  `language` varchar(100) NOT NULL DEFAULT '' COMMENT '语言',
  `release_date` varchar(100) NOT NULL DEFAULT '' COMMENT '上映时间(年月日)',
  `film_duration` int(10) NOT NULL COMMENT '片长(分钟)',
  `alias` varchar(200) NOT NULL DEFAULT '' COMMENT '别名',
  `douban_rating` decimal(2,1) NOT NULL COMMENT '评分',
  `douban_id` int(8) NOT NULL COMMENT '豆瓣 ID',
  `imdb_link` text NOT NULL COMMENT 'iMDB Link',
  `imdb_rating` decimal(2,1) NOT NULL COMMENT '评分',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='电影基本信息';


CREATE TABLE `film_source` (
  `source_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '电影 ID',
  `film_id` bigint(20) NOT NULL COMMENT '电影主键',
  `film_name` varchar(100) NOT NULL COMMENT '电影名',
  `source_site` enum('piaohua','dytt8','ed2000','unknown') NOT NULL DEFAULT 'unknown' COMMENT '数据来源的网站标记',
  `source_url` text NOT NULL COMMENT '数据来源的地址',
  `source_data` text NOT NULL COMMENT '电影天堂的下载地址',
  PRIMARY KEY (`source_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='电影下载资源信息';
```


