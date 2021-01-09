package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.UserFile;
import com.udacity.jwdnd.course1.cloudstorage.model.UserFileMetadata;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserFilesMapper {
    @Select("SELECT * from FILES WHERE userid = #{userid}")
    List<UserFile> getFiles(Integer userId);

    @Select("SELECT fileId,filename,contenttype,filesize,userid from FILES WHERE userid = #{userid}")
    List<UserFileMetadata> getFilesMetadata(Integer userId);

    @Insert("INSERT INTO FILES (filename, contenttype, filesize, userid, filedata) VALUES(#{filename}, #{contenttype}, #{filesize},#{userid}, #{filedata})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int insert(UserFile file);

    @Select("SELECT *  from FILES WHERE userid = #{userid} AND fileId = #{fileId}")
    UserFile getFileByFileIdAndUserId(Integer userid, Integer fileId);

    @Delete("DELETE from FILES WHERE userid = #{userid} AND fileId = #{fileId}")
    boolean deleteFileByFileIdAndUserId(Integer userid, Integer fileId);

    @Select("SELECT count(*) FROM files WHERE filename = #{fileName} AND userid = #{userId}")
    int doSameFileNameExists(String fileName, Integer userId);
}