package com.dallmeier.evidencer.listener.EventBus;

public class SearchInput {
    private String textInput;

    public SearchInput(String textInput) {
        this.textInput = textInput;
    }

    public String getTextInput() {
        return textInput;
    }

    public void setTextInput(String textInput) {
        this.textInput = textInput;
    }
}
