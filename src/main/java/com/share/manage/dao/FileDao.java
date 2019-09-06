package com.share.manage.dao;

import com.share.manage.model.VedioFile;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface FileDao {

    @Insert("insert into video_share(url,personName,upTime) values(#{url},#{personName},now())")
    int addFile(VedioFile file);

    @Select("select * from video_share where id = #{id}")
    VedioFile showFile(int id);
}
