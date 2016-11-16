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
import krajetum.LTB.configs.BotConfig;
import krajetum.LTB.utils.TelegramAssistanceUtil;
import pro.zackpollard.telegrambot.api.TelegramBot;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssistanceCore {

    TelegramBot telegramBot;

    private static final String APPLICATION_NAME = "LTB";

    private static final java.io.File DATA_STORE_DIR = new java.io.File(System.getProperty("user.home"), ".credentials/LTB_Credentials");

    private static FileDataStoreFactory DATA_STORE_FACTORY;

    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    private static HttpTransport HTTP_TRANSPORT;

    /** Global instance of the scopes required by this quickstart.
     *
     * If modifying these scopes, delete your previously saved credentials
     * at ~/.credentials/gmail-java-quickstart
     */
    private static final List<String> SCOPES = Arrays.asList(GmailScopes.GMAIL_READONLY, GmailScopes.GMAIL_MODIFY, CalendarScopes.CALENDAR);
    public AssistanceCore(TelegramBot bot){
        this.telegramBot = bot;
    }

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
             DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Creates an authorized Credential object.
     * @return an authorized Credential object.
     * @throws IOException
     */
    public static Credential authorize() throws IOException {
        // Load client secrets.
        InputStream in = AssistanceCore.class.getResourceAsStream("/client_id.json");
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(DATA_STORE_FACTORY)
                .setAccessType("offline")
                .build();
        Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
        //System.out.println("Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
        return credential;
    }

    /**
     * Build and return an authorized Gmail client service.
     * @return an authorized Gmail client service
     * @throws IOException
     */
    public static Gmail getGmailService() throws IOException {
        Credential credential = authorize();
        return new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME).build();
    }

    /**
     * Build and return an authorized Calendar client service.
     * @return an authorized Calendar client service
     * @throws IOException
     */
    public static Calendar getCalendarService() throws IOException {
        Credential credential = authorize();
        return new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME).build();
    }

    public void checkMail() throws IOException {
        // Build a new authorized API client service.
        Gmail service = getGmailService();
        Calendar calendar = getCalendarService();

        
        // Print the labels in the user's account.
        //String user = "me";
        ListMessagesResponse response = service.users().messages().list("me").setIncludeSpamTrash(false).setQ("in:unread").execute();

        if(response.getResultSizeEstimate()>0)
            for(Message message : response.getMessages()){

                Message op = service.users().messages().get("me", message.getId()).setFormat("full").execute();
                //System.out.println("----- --- ---- - -- - -- - -- - Message ID:"+op.getId()+" -- ----- ---- ---- - -- - --");

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
                    util.setBody(StringUtils.newStringUtf8(Base64.decodeBase64(part.getBody().getData())));
                    telegramBot.sendMessage(telegramBot.getChat(BotConfig.BOT_LUG_GROUP_ID), util.toTelegramMessage());
                    ModifyMessageRequest request = new ModifyMessageRequest();
                    request.setRemoveLabelIds(Arrays.asList("UNREAD"));
                    service.users().messages().modify("me", op.getId(), request).execute();
                }
                map.clear();
                //String body = StringUtils.newStringUtf8(Base64.decodeBase64(part.getBody().getData()));
                //System.out.println(body);
            }

    }
    public synchronized void runDaemon(){
        //noinspection InfiniteLoopStatement
        while(true) {
            try {
                checkMail();
                //60000 1 min
                //600000 10 min
                //636000 15 min
                /*TODO: DA DECIDERE IL REFRESH RATE */
                wait(60000);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
