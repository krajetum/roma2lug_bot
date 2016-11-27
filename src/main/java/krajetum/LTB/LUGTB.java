package krajetum.LTB;


import krajetum.LTB.configs.BotConfig;
import org.mortbay.log.Log;
import pro.zackpollard.telegrambot.api.TelegramBot;
import pro.zackpollard.telegrambot.api.chat.message.Message;
import pro.zackpollard.telegrambot.api.chat.message.send.SendableTextMessage;
import pro.zackpollard.telegrambot.api.conversations.Conversation;
import pro.zackpollard.telegrambot.api.conversations.ConversationPrompt;
import pro.zackpollard.telegrambot.api.internal.managers.FileManager;
import pro.zackpollard.telegrambot.api.menu.InlineMenu;

import java.lang.management.BufferPoolMXBean;

public class LUGTB {

    public static void main(String argv[]){




        TelegramBot telegramBot = TelegramBot.login(BotConfig.BOT_TOKEN);



        Log.info("Telegram Bot Token Exchange Ended");

        //The API key was invalid, an error will have also been printed into the console.
        if(telegramBot == null) System.exit(-1);
        Log.info("Starting Core class loading");

        AssistanceCore assistanceCore = new AssistanceCore(telegramBot);
        telegramBot.getEventsManager().register(new Roma2LugCore(telegramBot));
        telegramBot.startUpdates(false);
        Log.info("Core Initialized");
        Log.info("Starting Assistance Daemon");

        assistanceCore.runDaemon();
        
        
        //noinspection InfiniteLoopStatement
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}
