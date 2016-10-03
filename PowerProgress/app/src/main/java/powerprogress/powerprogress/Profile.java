package powerprogress.powerprogress;

import java.util.List;

public class Profile
{
    private String email;
    private String name;
    private String age;
    private List<String> options;
    private List<String> uploads;

    public Profile(String email, String name, String age)
    {
        this.email = email;
        this.name  = name;
        this.age   = age;
        this.options = options;
        this.uploads = uploads;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public void setUploads(List<String> uploads) {
        this.uploads = uploads;
    }

    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }

    public String getEmail() {
        return email;
    }

    public List<String> getOptions() {
        return options;
    }

    public List<String> getUploads() {
        return uploads;
    }

    public String escapeEmail() {
        if (email.contains("."))
        {
            return email.replace('.', ',');
        }
        return email;
    }


}
