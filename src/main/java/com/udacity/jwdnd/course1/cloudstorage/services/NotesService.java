package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NotesMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Notes;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotesService {

    private  final NotesMapper notesMapper;
    private  final UserService userService;

    public NotesService(NotesMapper notesMapper, UserService userService) {
        this.notesMapper = notesMapper;
        this.userService = userService;
    }

    public List<Notes> getNotes(){

        return notesMapper.get(userService.getLoggedInUserId());

    }
    public Integer addNotes(Notes note){
        note.setUserId(userService.getLoggedInUserId());
        return notesMapper.create(note);
    }

    public Integer deleteNotes(Integer noteid){
        return notesMapper.delete(userService.getLoggedInUserId(),noteid);
    }

    public boolean saveEditedNote(Notes note) {
        int result = notesMapper.update(note.getNoteTitle(), note.getNoteDescription(), note.getNoteId());
        if (result > 0) {
            return true;
        }
        return false;
    }
}
