package powerprogress.powerprogress;

/**
 * Created by K on 05-10-2016.
 */

public class CommentDTO {

    private String author;
    private String comment;
    private String timestamp;
    private int votes;

    CommentDTO() {}

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }
}
