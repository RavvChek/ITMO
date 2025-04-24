public interface Management {
    void run() throws Exception;

    void stop();

    void waitCommand() throws Exception;
}
