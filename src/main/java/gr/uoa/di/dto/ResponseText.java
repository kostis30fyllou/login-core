package gr.uoa.di.dto;

public class ResponseText {
    private String text;

    public ResponseText() {}

    public ResponseText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
