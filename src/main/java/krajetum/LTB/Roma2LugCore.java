package krajetum.LTB;


import java.io.File;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import pro.zackpollard.telegrambot.api.TelegramBot;
import pro.zackpollard.telegrambot.api.chat.message.send.InputFile;
import pro.zackpollard.telegrambot.api.chat.message.send.ParseMode;

import pro.zackpollard.telegrambot.api.chat.message.send.SendableStickerMessage;
import pro.zackpollard.telegrambot.api.chat.message.send.SendableTextMessage;
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
                    SendableTextMessage sendableTextMessage = SendableTextMessage.builder().message("K").build();
                    for(int i =0; i<ripeti;i++){
                        event.getChat().sendMessage(sendableTextMessage);
                    }
                }
            }catch (NumberFormatException e){
                System.out.println("Questo è flavio che fa lo stronzo xD");
            }

        }
    }
}