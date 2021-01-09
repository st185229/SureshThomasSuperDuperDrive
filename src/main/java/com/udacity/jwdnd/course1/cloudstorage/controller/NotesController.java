package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Notes;
import com.udacity.jwdnd.course1.cloudstorage.services.NotesService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class NotesController {
    private final NotesService notesService;

    public NotesController(NotesService notesService) {
        this.notesService = notesService;
    }

    @PostMapping("/notes")
    public String addNotes(@ModelAttribute Notes notes, RedirectAttributes redirectAttributes) {

        if (notes.getNoteid() != null) {
            if (updateNotes(notes)) {
                redirectAttributes.addFlashAttribute("message",
                        "Updated  # " + notes + "!");
            }
            return "redirect:/";
        }
        Integer noteId = notesService.addNotes(notes);
        if (noteId == null) {
            redirectAttributes.addFlashAttribute("message",
                    "Invalid Note id " + notes + "!");
            return "redirect:/";
        } else redirectAttributes.addFlashAttribute("message",
                "Note id: " + notes + " Successfully added!");


        return "redirect:/";
    }

    private boolean updateNotes(Notes note) {
        return notesService.updateNotes(note);

    }

    @GetMapping("/note/delete/{noteid}")
    public String deleteNote(@PathVariable("noteid") Integer noteid, RedirectAttributes redirectAttributes) {
        if (notesService.deleteNotes(noteid)) redirectAttributes.addFlashAttribute("message",
                "Note : Id " + noteid + "Successfully deleted");
        return "redirect:/#nav-notes";
    }
}
