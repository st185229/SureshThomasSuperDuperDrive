package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NotesMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Notes;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotesService {

    private final NotesMapper notesMapper;
    private final UserService userService;

    public NotesService(NotesMapper notesMapper, UserService userService) {
        this.notesMapper = notesMapper;
        this.userService = userService;
    }

    public List<Notes> getNotes() {

        return notesMapper.get(userService.getLoggedInUserId());

    }

    public Integer addNotes(Notes note) {
        note.setUserid(userService.getLoggedInUserId());
        return notesMapper.create(note);
    }

    public boolean deleteNotes(Integer noteid) {
        int result = notesMapper.delete(userService.getLoggedInUserId(), noteid);
        return result > 0;
    }

    public boolean updateNotes(Notes note) {

        note.setUserid(userService.getLoggedInUserId());
        if (note.getNoteid() > 0) {
            int result = notesMapper.update(note);
            return result > 0;
        } else return false;
    }
}
