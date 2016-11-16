package krajetum.LTB;


import krajetum.LTB.configs.BotConfig;
import pro.zackpollard.telegrambot.api.TelegramBot;
import pro.zackpollard.telegrambot.api.chat.message.send.ParseMode;
import pro.zackpollard.telegrambot.api.chat.message.send.SendableChatAction;
import pro.zackpollard.telegrambot.api.chat.message.send.SendableTextMessage;
import pro.zackpollard.telegrambot.api.event.Listener;
import pro.zackpollard.telegrambot.api.event.chat.ParticipantJoinGroupChatEvent;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;
import pro.zackpollard.telegrambot.api.menu.InlineMenu;


public class Roma2LugCore implements Listener {


    private final TelegramBot telegramBot;

    public Roma2LugCore(TelegramBot telegramBot) {

        this.telegramBot = telegramBot;
    }

    public void onParticipantJoinGroupChat(ParticipantJoinGroupChatEvent event) {

        SendableTextMessage welcomeMessage = SendableTextMessage.builder()
                                            .message("Diamo il benvenuto a "+event.getParticipant().getFullName()+"!")
                                            .parseMode(ParseMode.MARKDOWN)
                                            .build();

        event.getChat().sendMessage(welcomeMessage);
        /**TODO: cercare di inviare un cavolo di sticker*/
      /*  try {
            SendableStickerMessage sendableStickerMessage = SendableStickerMessage.builder().sticker(new InputFile(Roma2LugCore.class.getResource("/linux_inside.png").toURI().toURL())).build();
            event.getChat().sendMessage(sendableStickerMessage);
        } catch (URISyntaxException | MalformedURLException e) {
            e.printStackTrace();
        }*/

    }

    @Override
    public void onCommandMessageReceived(CommandMessageReceivedEvent event) {
        if(event.getCommand().equals("whoami")){
            SendableTextMessage sendableTextMessage = SendableTextMessage.builder().message(event.getMessage().getSender().getId()+""+event.getMessage().getSender().getFullName() ).build();
            event.getChat().sendMessage(sendableTextMessage);
        }

        if(event.getCommand().equals("commandtest")){
            InlineMenu.builder(telegramBot).forWhom(telegramBot.getChat(BotConfig.BOT_LUG_GROUP_ID));
        }
    }
}
