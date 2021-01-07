package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.exception.UserNotLoggedInException;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserFilesMapper;


import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.UserFile;
import com.udacity.jwdnd.course1.cloudstorage.model.UserFileMetadata;
import org.apache.ibatis.annotations.Delete;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserFileService {
    private final UserFilesMapper userFilesMapper;

    private final UserMapper userMapper;

    public UserFileService(UserFilesMapper userFilesMapper, UserMapper userMapper) {
        this.userFilesMapper = userFilesMapper;
        this.userMapper = userMapper;
    }

    public int addUserFile(UserFile userFile) {

      return userFilesMapper.insert
              (new UserFile
                      (null,
                              userFile.getFilename(),
                              userFile.getContenttype(),
                              userFile.getFilesize(),
                              userFile.getUserid(),
                              userFile.getFiledata()));


    }


    public List<UserFile> getUserFiles(Integer userId) {

        return userFilesMapper.getFiles(userId);

    }

    public List<UserFile> getCurrentUserFiles(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            return getUserFiles(userMapper.getUserID(currentUserName));
        }
        throw new UserNotLoggedInException("User not logged-in");
    }

    public List<UserFileMetadata> getCurrentUserFilesMetaData(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();

            Integer userid = userMapper.getUserID(currentUserName);

            return userFilesMapper.getFilesMetadata(userid);
        }
        throw new UserNotLoggedInException("User not logged-in");
    }

    public Integer getFileOwnerUserId() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            return userMapper.getUserID(currentUserName);
        }
        throw new UserNotLoggedInException("User not logged-in");

    }

  //  @Delete("DELETE from FILES WHERE userid = #{userid} AND fileId = #{fileId}")
 //   byte[] deleteFileByFileIdAndUserId(Integer userid, Integer fileId );

    public boolean deleteFileByFileIdAndUserId( Integer fileId ){
       return  userFilesMapper.deleteFileByFileIdAndUserId(getFileOwnerUserId(),fileId);
    }

    //Make sure that the user is not requesting for other users
    public byte[] getUserFileId(Integer fileId){
        return userFilesMapper.getFileByFileIdAndUserId(getFileOwnerUserId(),fileId);

    }
}
