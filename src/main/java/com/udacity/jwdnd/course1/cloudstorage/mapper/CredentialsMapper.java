package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Credentials;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CredentialsMapper {
    //crud

    //create
    @Insert("INSERT INTO CREDENTIALS(url,key,username,password,userid) VALUES(#{url},#{key},#{username},#{password},#{userid})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialid")
    Integer create(Credentials credentials);

    @Select("SELECT * from CREDENTIALS where userid = #{userid}")
    List<Credentials> readAll(Integer userid);

    //update
    @Update("UPDATE credentials SET url = #{url}, username = #{username}, key = #{key}, password = #{password} WHERE credentialid = #{credentialid}")
    Integer update(Credentials credentials);

    //delete
    @Delete("DELETE from CREDENTIALS WHERE credentialid = #{credentialid} AND userid = #{userid}")
    Integer delete(Integer credentialid, Integer userid);
}