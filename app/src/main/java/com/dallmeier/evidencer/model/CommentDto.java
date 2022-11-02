package com.dallmeier.evidencer.model;

public class CommentDto {
    private String content;
    private long incidentId;

    public CommentDto(String content, long incidentId) {
        this.content = content;
        this.incidentId = incidentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getIncidentId() {
        return incidentId;
    }

    public void setIncidentId(int incidentId) {
        this.incidentId = incidentId;
    }
}
