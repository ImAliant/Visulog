package up.visulog.config;


public class PluginConfig {

	//Nous avons decid� de mettre un tableau d'arguement #CommenteParWilliamBenakli
	String[] args;
	public PluginConfig(){}
	public PluginConfig(String ... arg){
		this.args = arg;
	}
	public String[] getArg() {
		return args;
	}
}
