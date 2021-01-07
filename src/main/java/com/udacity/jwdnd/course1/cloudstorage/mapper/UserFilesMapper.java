package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.UserFile;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserFilesMapper {

    @Select("SELECT * from FILES WHERE fileId = #{fileId}")
    UserFile getFile(Integer fileId);

    @Select("SELECT * from FILES WHERE userid = #{userid}")
    List<UserFile> getFiles(Integer userId);

    @Insert("INSERT INTO FILES (filename, contenttype, filesize, filedata) VALUES(#{filename}, #{contenttype}, #{filesize}, #{filedata}")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int insert(UserFile file);
}