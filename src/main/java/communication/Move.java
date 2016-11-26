package communication;

import bean.CommandType;

/**
 * Created by qqcs on 25/11/16.
 */
public class Move {
    private CommandType commandType;
    private int parameter = 0;

    public Move(CommandType commandType, int parameter) {
        this.commandType = commandType;
        this.parameter = parameter;
    }

    public CommandType getCommandType() {
        return commandType;
    }

    public void setCommandType(CommandType commandType) {
        this.commandType = commandType;
    }

    public int getParameter() {
        return parameter;
    }

    public void setParameter(int parameter) {
        this.parameter = parameter;
    }

    @Override
    public String toString() {
        return "Move{" +
                "commandType=" + commandType +
                ", parameter=" + parameter +
                '}';
    }
}
