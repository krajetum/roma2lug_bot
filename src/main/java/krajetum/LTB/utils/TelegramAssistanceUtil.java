package krajetum.LTB.utils;

import pro.zackpollard.telegrambot.api.chat.message.send.ParseMode;
import pro.zackpollard.telegrambot.api.chat.message.send.SendableTextMessage;

/**
 * Created by Lorenzo on 16/11/2016.
 */
public class TelegramAssistanceUtil {

    private String subject;
    private String from;
    private String date;
    private String body;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public SendableTextMessage toTelegramMessage(ParseMode parseMode){
        StringBuilder builder = new StringBuilder();
        builder.append("*Data:* ").append(getDate()).append("\n");
        builder.append("*From:* ").append(getFrom()).append("\n");
        builder.append("*Subject:* ").append(getSubject()).append("\n");
        builder.append("*BODY:* \n").append(getBody());

        return SendableTextMessage.builder().message(builder.toString()).parseMode(parseMode).build();
    }

}
