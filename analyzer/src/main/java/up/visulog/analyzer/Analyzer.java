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

    public AnalyzerResult computeResults() {
        List<AnalyzerPlugin> plugins = new ArrayList<>(); // Creation
        for (var pluginConfigEntry: config.getPluginConfigs().entrySet()) { 
        
            var plugin = makePlugin(pluginConfigEntry.getKey(), pluginConfigEntry.getValue());
            plugin.ifPresent(plugins::add); 
        }
        // TODO: try running them in parallel
        for (var plugin: plugins) plugin.run();

        return new AnalyzerResult(plugins.stream().map(AnalyzerPlugin::getResult).collect(Collectors.toList()));
    }
    
    // TODO: find a way so that the list of plugins is not hardcoded in this factory
    @SuppressWarnings("unchecked")
    private Optional<AnalyzerPlugin> makePlugin(String pluginName, PluginConfig pluginConfig) {
        switch (pluginName) {
            case "countCommits" : return Optional.of(new CountCommitsPerAuthorPlugin(config));
            case "countMergesCommits" : return Optional.of(new CountMergeCommitsPerAuthorPlugin(config));
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
