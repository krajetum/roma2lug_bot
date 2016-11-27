package krajetum.LTB;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import com.google.api.client.repackaged.org.apache.commons.codec.binary.StringUtils;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.*;

import java.io.*;

import krajetum.LTB.configs.BotConfig;
import krajetum.LTB.configs.MIMEType;
import krajetum.LTB.utils.GoogleServices;
import krajetum.LTB.utils.MailHTMLParser;
import krajetum.LTB.utils.TelegramAssistanceUtil;
import org.apache.commons.io.FileUtils;
import org.mortbay.log.Log;
import pro.zackpollard.telegrambot.api.TelegramBot;
import pro.zackpollard.telegrambot.api.chat.message.send.InputFile;
import pro.zackpollard.telegrambot.api.chat.message.send.ParseMode;
import pro.zackpollard.telegrambot.api.chat.message.send.SendableStickerMessage;
import pro.zackpollard.telegrambot.api.keyboards.InlineKeyboardButton;
import pro.zackpollard.telegrambot.api.keyboards.InlineKeyboardMarkup;
import pro.zackpollard.telegrambot.api.keyboards.KeyboardButton;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class AssistanceCore {

    TelegramBot telegramBot;

    AssistanceCore(TelegramBot telegramBot){
        this.telegramBot = telegramBot;
    }

    private void birthday(){

    }


    private void checkMail() throws IOException{
        // Build a new authorized API client service.
        Gmail service = GoogleServices.getGmailService();
        Calendar calendar = GoogleServices.getCalendarService();


        // Print the labels in the user's account.
        //String user = "me";
        ListMessagesResponse response = service.users().messages().list("me").setIncludeSpamTrash(false).setQ("in:unread").execute();

        if(response.getResultSizeEstimate()>0)
            for(Message message : response.getMessages()){
                Message op = service.users().messages().get("me", message.getId()).setFormat("full").execute();


                MessagePart part = op.getPayload();
                List<MessagePartHeader> headers =  part.getHeaders();

                Map<String, String> map = new HashMap<>();

                for (MessagePartHeader head: headers){
                    if(head.getName().equals("Subject")){
                        map.put(head.getName(), head.getValue());

                    }if(head.getName().equals("From")){
                        map.put(head.getName(), head.getValue());

                    }if(head.getName().equals("Date")){
                        map.put(head.getName(), head.getValue().substring(0,head.getValue().length()-5));

                    }
                }

                if(map.get("Subject").startsWith("Assistenza")){
                    TelegramAssistanceUtil util = new TelegramAssistanceUtil();
                    util.setSubject(map.get("Subject"));
                    util.setFrom(map.get("From"));
                    util.setDate(map.get("Date"));

                    StringBuilder builder = new StringBuilder();
                    List<String> tmpStrings = new ArrayList<>();
                    System.out.println(part.getMimeType());
                    if(part.getMimeType().equals(MIMEType.MULTIPART_ALTERNATIVE)) {
                        for (MessagePart parts : part.getParts()) {
                            if (parts.getMimeType().equals(MIMEType.HTML))
                                builder.append(MailHTMLParser.PolishMAIL(StringUtils.newStringUtf8(Base64.decodeBase64(parts.getBody().getData()))));
                        }
                    }else if(part.getMimeType().equals(MIMEType.PLAIN)){
                        builder.append(StringUtils.newStringUtf8(Base64.decodeBase64(part.getBody().getData())));
                    }else if(part.getMimeType().equals(MIMEType.MULTIPART_RELATED)){
                        for (MessagePart parts : part.getParts()) {
                            if (parts.getMimeType().equals(MIMEType.MULTIPART_ALTERNATIVE)){
                                for (MessagePart nested : parts.getParts()) {
                                    if (nested.getMimeType().equals(MIMEType.HTML))
                                        builder.append(MailHTMLParser.PolishMAIL(StringUtils.newStringUtf8(Base64.decodeBase64(nested.getBody().getData()))));
                                }
                            }
                            if (parts.getMimeType().equals(MIMEType.JPEG)){
                                System.out.println(parts.getFilename());
                                builder.append("image: ").append(parts.getFilename().replaceAll("_","-"));
                                MessagePartBody attachPart = service.users().messages().attachments().get("me", message.getId(), parts.getBody().getAttachmentId()).execute();
                                byte[] fileByteArray = Base64.decodeBase64(attachPart.getData());
                                String string = System.getProperty("user.dir")+"/tmp/"+UUID.randomUUID();
                                tmpStrings.add(string);
                                FileUtils.writeByteArrayToFile(new File(string),fileByteArray);
                            }
                        }
                    }
                    util.setBody(builder.toString());


                    telegramBot.sendMessage(telegramBot.getChat(BotConfig.BOT_LUG_GROUP_ID), util.toTelegramMessage(ParseMode.MARKDOWN));
                    //Logger.getLogger(AssistanceCore.class.getName()).log(Level.INFO, part.toPrettyString());
                    if(tmpStrings.size()>0){
                        for(int i =0; i<tmpStrings.size();i++) {
                            SendableStickerMessage sendableStickerMessage = SendableStickerMessage.builder().sticker(new InputFile(new File(tmpStrings.get(i)))).build();
                            telegramBot.sendMessage(telegramBot.getChat(BotConfig.BOT_LUG_GROUP_ID), sendableStickerMessage);
                            File file = new File(tmpStrings.get(i));
                            file.delete();
                        }
                    }
                    
                    ModifyMessageRequest request = new ModifyMessageRequest();
                    request.setRemoveLabelIds(Collections.singletonList("UNREAD"));
                    service.users().messages().modify("me", op.getId(), request).execute();
                }
                map.clear();
                //String body = StringUtils.newStringUtf8(Base64.decodeBase64(part.getBody().getData()));
                //System.out.println(body);
            }

    }
    public synchronized void runDaemon() {
        
        boolean once = false;
        //noinspection InfiniteLoopStatement
        while(true) {
            try {
                checkMail();
                //60000 1 min
                //600000 10 min
                //636000 15 min
                if(!once){
                    Log.info("Daemon Initialization finished");
                    Log.info("Bot Init ended");
                    once = true;
                }
                wait(60000);
            } catch (InterruptedException e){
                e.printStackTrace();
            }catch (UnknownHostException e ) {
                Logger.getLogger(AssistanceCore.class.getName()).log(Level.SEVERE, "Host non raggiungible");
            }catch (SocketTimeoutException e){
                Logger.getLogger(AssistanceCore.class.getName()).log(Level.SEVERE, "Connection Timeout");
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("File client_id.json not found");
                System.out.println("See the wiki of the project to get some clue");
                System.exit(-1);
            }


        }
    }
}
