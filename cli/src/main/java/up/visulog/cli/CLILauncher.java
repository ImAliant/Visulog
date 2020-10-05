package up.visulog.cli;

import up.visulog.analyzer.Analyzer;
import up.visulog.config.Configuration;
import up.visulog.config.PluginConfig;

import java.nio.file.FileSystems;
import java.util.HashMap;
import java.util.Optional;

/*
    Le fichier CLILauncher.java possèdent deux méthodes : makeConfigFromCommandLineArgs(String[] args) et displayHelpAndExit().
        La méthode makeConfigFromCommandLineArgs(String[] args) permet à l'utilisateur de choisir une des commandes disponibles 
            ( les commandes sont expliquées avec des commentaires dans la méthode ).
        La méthode displayHelpAndExit() permet à l'utilisateur de connnaître les commandes disponibles.
*/

public class CLILauncher {

    public static void main(String[] args) {
        var config = makeConfigFromCommandLineArgs(args);
        if (config.isPresent()) {
            var analyzer = new Analyzer(config.get());
            var results = analyzer.computeResults();
            System.out.println(results.toHTML());
        } else displayHelpAndExit();
    }

    static Optional<Configuration> makeConfigFromCommandLineArgs(String[] args) {
        var gitPath = FileSystems.getDefault().getPath(".");
        var plugins = new HashMap<String, PluginConfig>();
        for (var arg : args) {
            if (arg.startsWith("--")) {
                String[] parts = arg.split("=");
                if (parts.length != 2) return Optional.empty();
                else {
                    String pName = parts[0];
                    String pValue = parts[1];
                    switch (pName) {
                        /*
                            La commande "--addPlugin" (...)
                            La commande "--loadConfigFile" charge un fichier et utilise sa configuration.La commande "--loadConfigFile" charge un fichier et utilise sa configuration.
                            La commande "--justSaveConfigFile" sauvegarde un fichier de configuration.
                        */
                        case "--addPlugin":
                            // TODO: parse argument and make an instance of PluginConfig

                            // Let's just trivially do this, before the TODO is fixed:

                            if (pValue.equals("countCommits")) plugins.put("countCommits", new PluginConfig() {
                            });

                            break;
                        case "--loadConfigFile":
                            // TODO (load options from a file)
                            break;
                        case "--justSaveConfigFile":
                            // TODO (save command line options to a file instead of running the analysis)
                            break;
                        default:
                            return Optional.empty();
                    }
                }
            } else {
                gitPath = FileSystems.getDefault().getPath(arg);
            }
        }
        return Optional.of(new Configuration(gitPath, plugins));
    }

    private static void displayHelpAndExit() {
        System.out.println("Wrong command...");
        System.out.println("Here are the available commands :");
        System.out.println("  --addPlugin");
        System.out.println("  --loadConfigFile");
        System.out.println("  --justSaveConfigFile");
        System.exit(0);
    }
}
