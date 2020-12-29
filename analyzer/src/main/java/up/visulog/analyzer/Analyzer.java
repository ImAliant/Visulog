package up.visulog.analyzer;

import up.visulog.config.Configuration;
import up.visulog.config.PluginConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Analyzer {
    private final Configuration config;

    private AnalyzerResult result;

    public Analyzer(Configuration config) {
        this.config = config;
    }
    // Construit un nouvel analyseur contenant une nouvelle configuration.

    public AnalyzerResult computeResults() {
        List<AnalyzerPlugin> plugins = new ArrayList<>(); // Création d'une liste de plugins.
        for (var pluginConfigEntry: config.getPluginConfigs().entrySet()) { 
        // Pour chaque plugin de config appelé en entrée : 
            var pluginName = pluginConfigEntry.getKey(); // On stocke sa clé dans la variable "pluginName".
            var pluginConfig = pluginConfigEntry.getValue(); // Et sa valeur dans la variable "pluginConfig".
            var plugin = makePlugin(pluginName, pluginConfig); // Création d'un nouveau plugin avec ce nom et cette valeur.
            plugin.ifPresent(plugins::add); // Si ce plugin est bien présent, l'ajouter dans la liste de plugins.
        }
        // run all the plugins
        // TODO: try running them in parallel
        for (var plugin: plugins) plugin.run();

        // store the results together in an AnalyzerResult instance and return it
        return new AnalyzerResult(plugins.stream().map(AnalyzerPlugin::getResult).collect(Collectors.toList()));
    }
    // Retourne cette liste de plugins si son analyse s'est bien passée durant le test.
    
    // TODO: find a way so that the list of plugins is not hardcoded in this factory
    private Optional<AnalyzerPlugin> makePlugin(String pluginName, PluginConfig pluginConfig) {
        switch (pluginName) {
            case "countCommits" : return Optional.of(new CountCommitsPerAuthorPlugin(config));
            // Dans le cas où le nom du plugin est "countCommits", la fonction retourne le nombre de commits par auteur pour la config.
            default : return Optional.empty();
            // Sinon, elle retourne si le nombre de commits ajoutés est vide.
        }
    }   
}
