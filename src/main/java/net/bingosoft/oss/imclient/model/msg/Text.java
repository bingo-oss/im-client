package net.bingosoft.oss.imclient.model.msg;

/**
 * @author kael.
 */
public class Text extends Content {
    protected String text;

    public Text() {
    }

    public Text(String text) {
        this.text = text;
    }

    @Override
    public String toContentString() {
        return text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
