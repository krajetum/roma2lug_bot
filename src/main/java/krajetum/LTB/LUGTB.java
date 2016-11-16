package krajetum.LTB;


import krajetum.LTB.configs.BotConfig;
import pro.zackpollard.telegrambot.api.TelegramBot;

public class LUGTB {

    public static void main(String argv[]){

        TelegramBot telegramBot = TelegramBot.login(BotConfig.BOT_TOKEN);

        //The API key was invalid, an error will have also been printed into the console.
        if(telegramBot == null) System.exit(-1);

        AssistanceCore assistanceCore = new AssistanceCore(telegramBot);
        telegramBot.getEventsManager().register(new Roma2LugCore(telegramBot));
        telegramBot.startUpdates(false);

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
