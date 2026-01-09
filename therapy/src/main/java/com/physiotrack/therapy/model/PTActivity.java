package com.physiotrack.therapy.model;

import jakarta.persistence.*;

@Entity
@Table(name = "pt_activity")
public class PTActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private boolean completed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pt_program_id")
    private PTProgram program;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void setProgram(PTProgram program) {
        this.program = program;
    }

    public PTProgram getProgram() {
        return program;
    }
}
