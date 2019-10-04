import com.google.inject.AbstractModule;
import com.typesafe.config.Config;
import play.Environment;
import utils.Constant;

/**
 * This class is a Guice module that tells Guice how to bind several
 * different types. This Guice module is created when the Play
 * application starts.
 *
 * Play will automatically use any class called `Module` that is in
 * the root package. You can create modules in other locations by
 * adding `play.modules.enabled` settings to the `application.conf`
 * configuration file.
 */
public class Module extends AbstractModule {
    private final Environment environment;
    private final Config config;

    public Module(Environment environment, Config config) {
        this.environment = environment;
        this.config = config;
    }

    @Override
    public void configure() {
        System.out.println("application started! ================================="+environment.rootPath());
        Constant.applicationPath = environment.rootPath().toString();
        // application starts.
    }

}
