package up.visulog.analyzer;

import up.visulog.config.Configuration;
import up.visulog.config.PluginConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

public class Analyzer {
	
	
    private final Configuration config;
    private AnalyzerResult result;

    public Analyzer(Configuration config) {
        this.config = config;
    }
    

    /**
     * A partir d'une configuration elle créer les plugins et les creer un object AnalyzerResult avec 
     * une la liste de plugin dans config;
     * 
     * @return AnalyzeResult 
     */
    public AnalyzerResult computeResults() {
        List<AnalyzerPlugin> plugins = new ArrayList<>(); // Creation
        for (var pluginConfigEntry: config.getPluginConfigs().entrySet()) { 
        
            var plugin = makePlugin(pluginConfigEntry.getKey(), pluginConfigEntry.getValue());
            plugin.ifPresent(plugins::add); 
        }
        for (var plugin: plugins) {
            Thread t = new Thread() {
                public void run() {
                	plugin.run();
                }
              };
              t.start();
        }
        return new AnalyzerResult(plugins.stream().map(AnalyzerPlugin::getResult).collect(Collectors.toList()));
    }
    
    // TODO: find a way so that the list of plugins is not hardcoded in this factory
    @SuppressWarnings("unchecked")
    private Optional<AnalyzerPlugin> makePlugin(String pluginName, PluginConfig pluginConfig) {
        switch (pluginName) {
            case "countCommits" : return Optional.of(new CountCommitsPerAuthorPlugin(config));
            case "countMergesCommits" : return Optional.of(new CountMergeCommitsPerAuthorPlugin(config));
            case "countDescriptionAndMergedCommits" : return Optional.of(new CountCommitsWithDescriptionAndMergedPlugin(config));
            case "countDescriptionCommits" : return Optional.of(new CountCommitsWithDescriptionPerAuthorsPlugin(config));
            case "countRemoveLine" : return Optional.of(new CountRemoveLinePerAutor(config));
            case "countAllModifyLine" : return Optional.of(new CountModifyLinePerAuthors(config));
            case "countLineAdd" : return Optional.of(new CountLineAddPerAuthor(config));
            case "countCommitPerDate" : return Optional.of(new CountCommitsPerDate(config, pluginConfig.getArg()));
            case "countCommitPerTwoDate" : return Optional.of(new CountCommitsPerTwoDate(config, pluginConfig.getArg()));
            default : return Optional.empty();
        }
    } 

	public AnalyzerResult getResult() {
		return result;
	}

	public void setResult(AnalyzerResult result) {
		this.result = result;
	}   
}
