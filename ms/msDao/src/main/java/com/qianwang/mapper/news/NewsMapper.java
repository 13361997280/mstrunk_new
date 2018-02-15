package com.qianwang.mapper.news;

import com.qianwang.dao.domain.news.News;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface NewsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(News record);

    int insertSelective(News record);

    News selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(News record);

    int updateByPrimaryKeyWithBLOBs(News record);

    int updateByPrimaryKey(News record);

    List<News> selectNews(@Param("orderBy") String orderBy, @Param("start") Integer start, @Param("length") Integer length);

    int selectNewsCount();

    int updateNews(@Param("newsId") Integer newsId, @Param("isRelease") Boolean isRelease);

    int selectReleaseNews();

    List<News> selectOutNews(@Param("size") Integer size);
}