package krajetum.LTB;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import krajetum.LTB.objects.LUGMember;
import krajetum.LTB.objects.SpamCommand;
import org.mortbay.log.Log;
import pro.zackpollard.telegrambot.api.TelegramBot;
import pro.zackpollard.telegrambot.api.chat.message.send.*;

import pro.zackpollard.telegrambot.api.event.Listener;
import pro.zackpollard.telegrambot.api.event.chat.ParticipantJoinGroupChatEvent;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;



public class Roma2LugCore implements Listener {

    private HashMap<String, String> spam_commands;
    private List<SpamCommand> commandList;
    private final TelegramBot telegramBot;

    public Roma2LugCore(TelegramBot telegramBot) {

        this.telegramBot = telegramBot;
        spam_commands = new HashMap<>();

        File file = new File(System.getProperty("user.dir")+"/data/spam_commands.json");
        Gson gson = new Gson();
        try {
            Type type = new TypeToken<List<SpamCommand>>(){}.getType();
            commandList = gson.fromJson(new FileReader(file), type);
            Log.info("INIT SPAM COMMANDS");
            for(SpamCommand command:commandList){
                Log.info("Command "+command.getCommand()+" initialized");
                spam_commands.put(command.getCommand(), System.getProperty("user.dir") +"/stickers/"+ command.getFilepath());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onParticipantJoinGroupChat(ParticipantJoinGroupChatEvent event) {

        SendableTextMessage welcomeMessage = SendableTextMessage.builder()
                                            .message("Diamo il benvenuto a "+event.getParticipant().getFullName()+"!")
                                            .parseMode(ParseMode.MARKDOWN)
                                            .build();

        event.getChat().sendMessage(welcomeMessage);
        
        SendableStickerMessage sendableStickerMessage = SendableStickerMessage.builder().sticker(new InputFile(new File(System.getProperty("user.dir")+"/stickers/linux_inside.png"))).build();
        event.getChat().sendMessage(sendableStickerMessage);

    }

    @Override
    public void onCommandMessageReceived(CommandMessageReceivedEvent event) {
        if(spam_commands.containsKey(event.getCommand())){

            SendableStickerMessage message = SendableStickerMessage.builder().sticker(new InputFile(new File(spam_commands.get(event.getCommand())))).build();
            event.getChat().sendMessage(message);

        }else if(event.getCommand().equals("whoami")){
            SendableTextMessage sendableTextMessage = SendableTextMessage.builder().message("Name: *" + event.getMessage().getSender().getFullName() + "*").parseMode(ParseMode.MARKDOWN).build();
            event.getChat().sendMessage(sendableTextMessage);
        }else if(event.getCommand().equals("k")){
            SendableTextMessage sendableTextMessage = SendableTextMessage.builder().message("K").parseMode(ParseMode.MARKDOWN).build();
            event.getChat().sendMessage(sendableTextMessage);
        }else if(event.getCommand().equals("quarantennetriste")){
            File file = new File(System.getProperty("user.dir")+"/stickers/quarantennitristi");
            //noinspection ConstantConditions
            int rand = ThreadLocalRandom.current().nextInt(0, file.listFiles().length);
            SendableStickerMessage stickerMessage = SendableStickerMessage.builder().sticker(new InputFile(file.listFiles()[rand])).build();
            event.getChat().sendMessage(stickerMessage);
        }else if(event.getCommand().equals("chatid")){
            SendableTextMessage sendableTextMessage = SendableTextMessage.builder().message(event.getChat().getId()).build();
            event.getChat().sendMessage(sendableTextMessage);
        }else if(event.getCommand().equals("kloop")){
            try{
                String string = event.getArgsString();
                int ripeti= Integer.parseInt(string);
                if(ripeti>5){
                    SendableTextMessage sendableTextMessage = SendableTextMessage.builder().message("No spam pls").build();
                    event.getChat().sendMessage(sendableTextMessage);
                }else{
                    StringBuilder builder = new StringBuilder();
                    for(int i =0; i<ripeti;i++){
                       builder.append("k \n");
                    }
                    SendableTextMessage sendableTextMessage = SendableTextMessage.builder().message(builder.toString()).build();
                    event.getChat().sendMessage(sendableTextMessage);
                }
            }catch (NumberFormatException e){
                System.out.println("Questo è flavio che fa lo stronzo xD");
            }
        }else if(event.getCommand().equals("list")){
            StringBuilder builder = new StringBuilder();
            builder.append("______SPAM______").append("\n");
            for(SpamCommand command : commandList){
                builder.append(command.getCommand() +" - "+command.getDescription()).append("\n");
            }

            builder.append("k - ").append("Ovviamente K").append("\n");
            builder.append("quarantennetriste - ").append("40s lottery").append("\n");
            builder.append("cetriolotime - ").append("cetriolo per i membri attivi").append("\n");
            if(event.getArgs().length == 0){
                builder.append("____UTILITY_____").append("\n");
                builder.append("about - ").append("Scopri di piu sul bot").append("\n");
                builder.append("whoami - ").append("Chi sei? Goku non lo sai... ma cosi lo scoprirai").append("\n");
            }

            SendableTextMessage sendableTextMessage = SendableTextMessage.builder().message(builder.toString()).build();
            event.getChat().sendMessage(sendableTextMessage);
        }else if(event.getCommand().equals("cetriolotime")){
            Gson gson = new Gson();

            try {
                File file = new File(System.getProperty("user.dir")+"/data/lug_members.json");
                Type listType = new TypeToken<ArrayList<LUGMember>>(){}.getType();
                List<LUGMember> memeMembers= gson.fromJson(new JsonReader(new FileReader(file)), listType);
                int rand = ThreadLocalRandom.current().nextInt(0, memeMembers.size());

                SendableTextMessage textMessage = SendableTextMessage.builder().message("Cetriolo a: *" + memeMembers.get(rand).getName() + "* ! Congratulazioni!").parseMode(ParseMode.MARKDOWN).build();
                event.getChat().sendMessage(textMessage);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }else if(event.getCommand().equals("about")){
            SendableTextMessage message = SendableTextMessage.builder().message("Get this project on: https://krajetum.github.io/roma2lug_bot/").parseMode(ParseMode.HTML).build();
            event.getChat().sendMessage(message);
        }
    }
}