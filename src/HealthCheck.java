/**
 * Project: HealthCheck
 *
 * @author Dmitry Istomin aka Jollykai
 * https://github.com/Jollykai
 */
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class HealthCheck extends TimerTask {

    private static URL url;

    public static void main(String[] args) {
        // Обработка передаваемых аргументов, передается интервал и заданный url
        int interval = Integer.parseInt(args[0]) * 1000; // Получаем значение в мс
        String checkUrl = args[1];

        // Проверяем валидность адреса
        try {
            url = isValidURL(checkUrl);
        } catch (MalformedURLException e) {
            System.out.println("URL parsing error");
            System.exit(1);
        }

        // Устанавливаем таймер и запускаем выполенние
        TimerTask timerTask = new HealthCheck();
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(timerTask, 0, interval);
    }

    static URL isValidURL(String inputUrl) throws MalformedURLException {
        String regex = "^https?://([\\w-]+\\.)+[\\w-]+(/[\\w-./?%&=]*)?$";
        if (!inputUrl.matches(regex)) {
            throw new MalformedURLException();
        }
        return new URL(inputUrl);
    }

    @Override
    public void run() {
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("Checking '" + url + "'. Result: OK(200)");
            } else {
                System.out.println("Checking '" + url + "'. Result: ERR(" + responseCode + ")");
            }
        } catch (IOException e) {
            System.out.println("Checking '" + url + "'. Result: ERR(" + e.getMessage() + ")");
        }
    }
}
