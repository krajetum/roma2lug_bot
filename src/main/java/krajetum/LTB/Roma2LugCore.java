package krajetum.LTB;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import krajetum.LTB.utils.LUGMember;
import pro.zackpollard.telegrambot.api.TelegramBot;
import pro.zackpollard.telegrambot.api.chat.message.send.*;

import pro.zackpollard.telegrambot.api.event.Listener;
import pro.zackpollard.telegrambot.api.event.chat.ParticipantJoinGroupChatEvent;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;



public class Roma2LugCore implements Listener {


    private final TelegramBot telegramBot;

    public Roma2LugCore(TelegramBot telegramBot) {

        this.telegramBot = telegramBot;
    }

    @Override
    public void onParticipantJoinGroupChat(ParticipantJoinGroupChatEvent event) {

        SendableTextMessage welcomeMessage = SendableTextMessage.builder()
                                            .message("Diamo il benvenuto a "+event.getParticipant().getFullName()+"!")
                                            .parseMode(ParseMode.MARKDOWN)
                                            .build();

        event.getChat().sendMessage(welcomeMessage);
        /**TODO: cercare di inviare un cavolo di sticker*/
        
        SendableStickerMessage sendableStickerMessage = SendableStickerMessage.builder().sticker(new InputFile(new File(System.getProperty("user.dir")+"/stickers/linux_inside.png"))).build();
        event.getChat().sendMessage(sendableStickerMessage);
        

    }

    @Override
    public void onCommandMessageReceived(CommandMessageReceivedEvent event) {
        if(event.getCommand().equals("whoami")){
            SendableTextMessage sendableTextMessage = SendableTextMessage.builder().message("Name: *" + event.getMessage().getSender().getFullName() + "*").parseMode(ParseMode.MARKDOWN).build();
            event.getChat().sendMessage(sendableTextMessage);
        }else if(event.getCommand().equals("k")){
            SendableTextMessage sendableTextMessage = SendableTextMessage.builder().message("K").parseMode(ParseMode.MARKDOWN).build();
            event.getChat().sendMessage(sendableTextMessage);
        }else if(event.getCommand().equals("kappa")){
            SendableStickerMessage stickerMessage = SendableStickerMessage.builder().sticker(new InputFile(new File(System.getProperty("user.dir")+"/stickers/kappa.png"))).build();
            event.getChat().sendMessage(stickerMessage);
        }else if(event.getCommand().equals("blaster")){
            SendableStickerMessage stickerMessage = SendableStickerMessage.builder().sticker(new InputFile(new File(System.getProperty("user.dir")+"/stickers/blaster.webp"))).build();
            event.getChat().sendMessage(stickerMessage);
        }else if(event.getCommand().equals("fuckyou")){
            SendableStickerMessage stickerMessage = SendableStickerMessage.builder().sticker(new InputFile(new File(System.getProperty("user.dir") + "/stickers/fuckyou.jpg"))).build();
            event.getChat().sendMessage(stickerMessage);
        }else if(event.getCommand().equals("pogchamp")){
            SendableStickerMessage stickerMessage = SendableStickerMessage.builder().sticker(new InputFile(new File(System.getProperty("user.dir") + "/stickers/pogchamp.jpg"))).build();
            event.getChat().sendMessage(stickerMessage);
        }else if(event.getCommand().equals("gengiskhan")) {
            SendableStickerMessage stickerMessage = SendableStickerMessage.builder().sticker(new InputFile(new File(System.getProperty("user.dir") + "/stickers/genghis-khan.jpg"))).build();
            event.getChat().sendMessage(stickerMessage);
        }else if(event.getCommand().equals("feelsbadman")) {
            SendableStickerMessage stickerMessage = SendableStickerMessage.builder().sticker(new InputFile(new File(System.getProperty("user.dir") + "/stickers/feelsadman.jpg"))).build();
            event.getChat().sendMessage(stickerMessage);
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

            if(event.getArgs().length == 0){
            } else if(event.getArgs()[0].equals("spam")){
                StringBuilder builder = new StringBuilder();
                builder.append("k - ").append("Ovviamente K").append("\n");
                builder.append("kappa - ").append("Kappa").append("\n");
                builder.append("blaster - ").append("Boom, get out the way!").append("\n");
                builder.append("pogchamp - ").append("Pogchamp ").append("\n");
                builder.append("gengiskhan - ").append("Gengiskan (more info @Twiiiiin)").append("\n");
                builder.append("feelsbadman - ").append("feelsbadman").append("\n");
                builder.append("quarantennetriste - ").append("40s lottery").append("\n");
                SendableTextMessage sendableTextMessage = SendableTextMessage.builder().message(builder.toString()).build();
                event.getChat().sendMessage(sendableTextMessage);
            }
        }else if(event.getCommand().equals("cetriolotime")){
            Gson gson = new Gson();

            try {
                File file = new File(System.getProperty("user.dir")+"/data/lug_members.json");
                Type listType = new TypeToken<ArrayList<LUGMember>>(){}.getType();
                List<LUGMember> memeMembers= gson.fromJson(new JsonReader(new FileReader(file)), listType);
                int rand = ThreadLocalRandom.current().nextInt(0, memeMembers.size());
                System.out.println("rand num " + rand);

                SendableTextMessage textMessage = SendableTextMessage.builder().message("Cetriolo a: " + memeMembers.get(rand).getName() + " ! Congratulazioni!").build();
                event.getChat().sendMessage(textMessage);


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


        }
    }
}