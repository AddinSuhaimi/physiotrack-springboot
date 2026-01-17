package com.physiotrack.summary.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "summary_report")
public class SummaryReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long patientId;
    private int month; // 1-12
    private int year;

    @Column(length = 4000)
    private String summaryData; // JSON or simple text

    public Long getId() { return id; }
    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }
    public int getMonth() { return month; }
    public void setMonth(int month) { this.month = month; }
    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
    public String getSummaryData() { return summaryData; }
    public void setSummaryData(String summaryData) { this.summaryData = summaryData; }
}
