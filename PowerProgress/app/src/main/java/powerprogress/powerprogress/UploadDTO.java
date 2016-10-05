package powerprogress.powerprogress;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class UploadDTO{

    private String name;
    private String titel;
    private String date;
    private String description;
    private List<String> options;
    private List<CommentDTO> comments;

    UploadDTO() {
      this.date = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public List<CommentDTO> getComments() {
        return comments;
    }

    public void setComments(List<CommentDTO> comments) {
        this.comments = comments;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }
}
