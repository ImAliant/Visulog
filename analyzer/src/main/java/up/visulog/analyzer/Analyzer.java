package up.visulog.analyzer;

import up.visulog.config.Configuration;
import up.visulog.config.PluginConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

public class Analyzer {
	
	
    private final Configuration config; // Map de plugin et un path (chemin d'acces
    private AnalyzerResult result; // recup ce qu'il y'a 

    public Analyzer(Configuration config) {
        this.config = config;
    }
    // Construit un nouvel analyseur contenant une nouvelle configuration.

    public AnalyzerResult computeResults() {
        List<AnalyzerPlugin> plugins = new ArrayList<>(); // Création d'une liste de plugins.
        for (var pluginConfigEntry: config.getPluginConfigs().entrySet()) { // tranformation //CommitPerAuthor PluginConfig
        // Pour chaque plugin de config appelé en entrée : 
            var plugin = makePlugin(pluginConfigEntry.getKey(), pluginConfigEntry.getValue()); // Création d'un nouveau plugin avec ce nom et cette valeur.
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
    @SuppressWarnings("unchecked")
	private Optional<AnalyzerPlugin> makePlugin(String pluginName, PluginConfig pluginConfig) {
    	var map = pluginConfig.getMap();
    	if(pluginName.equals(((Entry<String, PluginConfig>) map).getKey())) {
    		return Optional.of(new CountCommitsPerAuthorPlugin(config));
    	}
    	 return Optional.empty();
    }

	public AnalyzerResult getResult() {
		return result;
	}

	public void setResult(AnalyzerResult result) {
		this.result = result;
	}   
}
