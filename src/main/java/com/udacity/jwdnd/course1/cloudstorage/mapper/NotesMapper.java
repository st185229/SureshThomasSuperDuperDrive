package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Notes;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NotesMapper {

    @Insert("INSERT INTO NOTES(notetitle,notedescription,userid) VALUES(#{noteTitle},#{noteDescription},#{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "noteid")
    Integer create(Notes note);



    @Delete("DELETE from NOTES WHERE userid = #{userid} AND noteid = #{noteId}")
    Integer delete(Integer userid, Integer noteId );

    @Select("SELECT * from NOTES where userid = #{userid}")
    List<Notes> get(Integer userid);

    @Update("UPDATE notes SET notetitle = #{noteTitle}, notedescription = #{noteDescription} WHERE noteid = #{noteId}")
    int update(String noteTitle, String noteDescription, Integer noteId);
}
