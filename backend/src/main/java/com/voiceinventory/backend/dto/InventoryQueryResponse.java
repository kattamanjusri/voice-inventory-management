package com.voiceinventory.backend.dto;

import java.util.List;

public class InventoryQueryResponse {

    private String question;
    private String answer;
    private List<String> items;

    public InventoryQueryResponse() {
    }

    public InventoryQueryResponse(String question, String answer, List<String> items) {
        this.question = question;
        this.answer = answer;
        this.items = items;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }
}
