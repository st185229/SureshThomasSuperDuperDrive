package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Notes;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NotesMapper {
    @Insert("INSERT INTO NOTES(notetitle,notedescription,userid) VALUES(#{notetitle},#{notedescription},#{userid})")
    @Options(useGeneratedKeys = true, keyProperty = "noteid")
    Integer create(Notes note);

    @Delete("DELETE from NOTES WHERE userid = #{userid} AND noteid = #{noteid}")
    Integer delete(Integer userid, Integer noteid);

    @Select("SELECT * from NOTES where userid = #{userid}")
    List<Notes> get(Integer userid);

    @Update("UPDATE notes SET notetitle = #{notetitle}, notedescription = #{notedescription} WHERE noteid = #{noteid} and userid = #{userid} ")
    int update(Notes note);
}
