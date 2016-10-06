package powerprogress.powerprogress;

import java.util.List;

public class ProfileDTO
{
    private String email;
    private String name;
    private String age;
    private String description;
    private List<String> options;
    private List<String> uploads;

    public ProfileDTO()
    {
    }

    public ProfileDTO(String email, String name, String age)
    {
        this.email = email;
        this.name  = name;
        this.age   = age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setEmail(String email) {
        this.email = escapeEmail(email);
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public void setUploads(List<String> uploads) {
        this.uploads = uploads;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAge() {
        return age;
    }

    public String getEmail() {
        return escapeEmail(email);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getOptions() {
        return options;
    }

    public List<String> getUploads() {
        return uploads;
    }

    public String escapeEmail(String email) {
        if (email.contains("."))
        {
            return email.replace('.', ',');
        }
        else
        {
            //if getting the data from the database the , is corverted back into a .
            return email.replace(',','.');
        }
    }
}



