package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Notes;
import com.udacity.jwdnd.course1.cloudstorage.services.NotesService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
public class NotesController {
    private final NotesService notesService;

    public NotesController(NotesService notesService) {
        this.notesService = notesService;
    }

    @PostMapping("/notes")
    public String addNotes(@ModelAttribute Notes notes, RedirectAttributes redirectAttributes) throws IOException {
        if (notes.getNoteId() != null) {
            return this.editNote(notes);
        }

        Integer noteId = notesService.addNotes(notes);
        if (noteId == null) {
            redirectAttributes.addFlashAttribute("message",
                    "Invalid Note id " + notes.getNoteId() + "!");
            return "redirect:/";
        } else {
            redirectAttributes.addFlashAttribute("message",
                    "Note id: " + notes.getNoteId() + " Successfully added!");
        }
        return "redirect:/#nav-notes";
    }

    private String editNote(Notes note) {
        String actionError = null;

        boolean result;
        Integer noteId = note.getNoteId();
        result = this.notesService.saveEditedNote(note);

        return "redirect:/#nav-notes";
    }

    @GetMapping("/note/delete/{noteId}")
    public String deleteNote(@PathVariable("noteId") Integer noteId, RedirectAttributes redirectAttributes) {
        notesService.deleteNotes(noteId);
        redirectAttributes.addFlashAttribute("message",
                "Note : Id " + noteId + "Successfully deleted");
        return "redirect:/#nav-notes";
    }
}
