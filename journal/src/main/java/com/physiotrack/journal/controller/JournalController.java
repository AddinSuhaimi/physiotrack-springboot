package com.physiotrack.journal.controller;

import com.physiotrack.journal.model.JournalEntry;
import com.physiotrack.journal.service.JournalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/journal")
public class JournalController {

    @Autowired
    private JournalService journalService;

    @PostMapping
    public ResponseEntity<JournalEntry> create(@RequestBody JournalEntry entry) {
        return ResponseEntity.ok(journalService.createEntry(entry));
    }

    @GetMapping("/{id}")
    public ResponseEntity<JournalEntry> get(@PathVariable Long id, @RequestParam Long authorId) {
        return ResponseEntity.ok(journalService.getEntry(id, authorId));
    }

    @GetMapping("/user/{authorId}")
    public ResponseEntity<Page<JournalEntry>> list(@PathVariable Long authorId,
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int size) {
        Pageable p = PageRequest.of(page, size);
        return ResponseEntity.ok(journalService.listEntries(authorId, p));
    }

    @PutMapping("/{id}")
    public ResponseEntity<JournalEntry> update(@PathVariable Long id, @RequestParam Long authorId, @RequestBody JournalEntry updates) {
        return ResponseEntity.ok(journalService.updateEntry(id, authorId, updates));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @RequestParam Long authorId) {
        journalService.deleteEntry(id, authorId);
        return ResponseEntity.noContent().build();
    }
}
