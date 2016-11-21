package krajetum.LTB.objects;

/**
 * Created by Lorenzo on 21/11/2016.
 */
public class SpamCommand {

    private String command;
    private String filepath;
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }
}
