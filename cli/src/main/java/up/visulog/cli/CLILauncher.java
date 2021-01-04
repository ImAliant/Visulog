package up.visulog.cli;

import up.visulog.analyzer.Analyzer;
import up.visulog.config.Configuration;
import up.visulog.config.PluginConfig;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
/*
La classe FileSystems définit des méthodes pour créer des systèmes de fichiers qui fournissent l'accès à d'autres types de systèmes de fichiers (personnalisés). 
Un système de fichiers est la fabrique de plusieurs types d'objets: La méthode getPath convertit une chaîne de chemin dépendante du système, renvoyant un objet Path qui peut 
être utilisé pour localiser et accéder à un fichier.La classe FileSystems définit des méthodes pour créer des systèmes de fichiers qui fournissent l'accès à d'autres types de 
systèmes de fichiers (personnalisés). Un système de fichiers est la fabrique de plusieurs types d'objets: La méthode getPath convertit une chaîne de chemin dépendante du système, 
renvoyant un objet Path qui peut être utilisé pour localiser et accéder à un fichier.
*/
import java.nio.file.FileSystems;
import java.util.ArrayList;
/*
HashMap est une classe de collection basée sur Map qui est utilisée pour stocker des paires clé et valeur, elle est désignée par HashMap <Key, Value> ou HashMap <K, V>. 
Cette classe ne donne aucune garantie quant à l'ordre de la carte. Elle est similaire à la classe Hashtable sauf qu'elle n'est pas synchronisée et autorise les valeurs nulles 
(valeurs nulles et clé nulle).
*/
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/*
Optional est un objet conteneur utilisé pour contenir des objets non nuls. L'objet Optional est utilisé pour représenter null avec une valeur absente. 
Cette classe a diverses méthodes utilitaires pour faciliter le code pour gérer les valeurs comme «disponibles» ou «non disponibles» au lieu de vérifier les valeurs nulles.
*/
import java.util.Optional;
import java.util.Scanner;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

public class CLILauncher {

    static String prefix = "(WVS) ";
	
    public static void main(String[] args) {
    	if(args.length > 0) {
    		if(args[0].equalsIgnoreCase("help")) {
    			displayHelpAndExit();
    			return;
    		}
	        var config = makeConfigFromCommandLineArgs(args);
		    if(config == null) return;
	        if (config.isPresent()) {
	        	TraitementPlugin(config.get());
	            System.out.println("Vous avez gener� " + config.get().getPluginConfigs().keySet().toString());	
	        } else displayHelpAndExit();	
	}else {
		displayErreur();
	}
    }
    
    /**
     * Cette fonction factorise la fonction main pour plus de lisibilit�
     * @param configuartion qui prend une configuration en parametre
     * @return void 
     * #CommenterParWilliamBenakli
     */
    static void TraitementPlugin(Configuration configuartion) {
        var analyzer = new Analyzer(configuartion);
        var results = analyzer.computeResults();
        for(int i = 0; i < results.getSubResults().size(); i++)
            results.createPageHtml((String) configuartion.getPluginConfigs().keySet().toArray()[i], (String) configuartion.getPluginConfigs().keySet().toArray()[i], i);
}

    
    /**
     * Repond a la demande de l'utilisateur et cr�er les plugins en foncitons de l'entree  
     * @param args Les arguments entr�e depuis une console 
     * @return Optional<Configuration> Une configuration (un object avec une Map qui contient des plugins); 
     * #CommenterParWilliamBenakli
     */
    static Optional<Configuration> makeConfigFromCommandLineArgs(String[] args) {
        var gitPath = FileSystems.getDefault().getPath(".");
        var plugins = new HashMap<String, PluginConfig>();
       
        for (var arg : args) {
            if (arg.startsWith("--")) {
                String[] parts = arg.split("=");
                if (parts.length == 0) return Optional.empty();
                else if(parts.length == 1){
                    String pName = parts[0];
                	switch(pName.toLowerCase()) {
	                	case "--allplugin":
	                		System.out.println(prefix + "G�n�ration de tous les plugins..."); 
	                		plugins.put("countCommits", new PluginConfig() {});
	                		plugins.put("countMergesCommits", new PluginConfig() {}); 
	                		plugins.put("countDescriptionAndMergedCommits", new PluginConfig() {});
	                		plugins.put("countDescriptionCommits", new PluginConfig() {});
	                		plugins.put("countRemoveLine", new PluginConfig() {});
	                		plugins.put("countLineAdd", new PluginConfig() {});
	                		plugins.put("countAllModifyLine", new PluginConfig() {});
	                		break;
	            		case "--listplugin":
	            			listPlugin();
                			return Optional.empty();
	            		default:
                    	return Optional.empty();
                	}
                }else { 
                    String pName = parts[0];
                    String pValue = parts[1];
                    switch (pName.toLowerCase()) { //to Lower case = rendre en miniscule
	                    case "--addplugin":
	                        if (pValue.equalsIgnoreCase("countCommits"))plugins.put("countCommits", new PluginConfig() {});
	                        else if (pValue.equalsIgnoreCase("countMergesCommits")) plugins.put("countMergesCommits", new PluginConfig() {});
	                        else if (pValue.equalsIgnoreCase("countDescriptionAndMergedCommits")) plugins.put("countDescriptionAndMergedCommits", new PluginConfig() {});
	                        else if (pValue.equalsIgnoreCase("countDescriptionCommits")) plugins.put("countDescriptionCommits", new PluginConfig() {});
	                        else if (pValue.equalsIgnoreCase("countRemoveLine")) plugins.put("countRemoveLine", new PluginConfig() {});
	                        else if (pValue.equalsIgnoreCase("countAllModifyLine")) plugins.put("countAllModifyLine", new PluginConfig() {});
	                        else if (pValue.equalsIgnoreCase("countLineAdd")) plugins.put("countLineAdd", new PluginConfig() {});
	                        else if (pValue.equalsIgnoreCase("countCommitPerDate")) {plugins.put("countCommitPerDate", new PluginConfig(parts[2]) {});           }
	                        else if (pValue.equalsIgnoreCase("countCommitPerTwoDate")) {plugins.put("countCommitPerTwoDate", new PluginConfig(parts[2], parts[3]) {});}
	                        else return Optional.empty();
	                        break;
	                    case "--loadconfigfile":
	                    	if(pValue.length() <= 0) {
	                    		System.out.println(prefix + "Aucun chemin entre en parametre ");
	                    		break;
	                    	}
	                    	File file = new File(pValue);
	                    	if(!file.exists()) {
	                    		System.out.println(prefix + "Chemin du fichier n'est pas valide");
	                    		break;
	                    	}
	                    	Scanner scnr = null;
							try {
								scnr = new Scanner(file);
							} catch (FileNotFoundException e1) {
								e1.printStackTrace();
							}
	                    	 String line = "";
	                         while(scnr.hasNextLine())line += scnr.nextLine();
	                         String[] pluginsNext = line.split("-");
	                    	  for(String plugin :pluginsNext)   plugins.put(plugin.replace(" ", ""), new PluginConfig() {});
	                    	  
	                    	  scnr.close();
	                        break;
	                    case "--justsaveconfigfile":
	                    	DumperOptions options = new DumperOptions();
                            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
                            options.setPrettyFlow(true);
                            Yaml save=  new Yaml(options);
                            String [] nameFile= pValue.split("/");
                            String commandes = nameFile[1];
                            String [] saveConfig= commandes.replace(" ", "$").split(":");
                            if(saveConfig.length>0) {
                                List <String> val = new ArrayList<String>();
                                for(int i=0 ; i<saveConfig.length ; i++) val.add(saveConfig[i].replace("$", " "));   
                                
                                FileWriter writer;
                                try {
                                    writer = new FileWriter(nameFile[0] + ".yml");
                                     save.dump(val, writer);
                                     System.out.println("Votre fichier a bien ete enrengistree voir /" + nameFile[0] + ".yml");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                  
                            }else {
                                System.out.println("erreur fait: help");
                            }
                    			return null;
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

    /**
     * Affiche un panel d'aide
     *  #CommenterParWilliamBenakli
     */
    private static void displayHelpAndExit() {
        System.out.println("(WTS) Commande d'aide");
        System.out.println("# Commande presente  #");
        System.out.println("  --addPlugin=[countCommits,countMerges,countAllModifyLine]");
        System.out.println("  --listPlugin");
        System.out.println("  --loadConfigFile");
        System.out.println("  --justSaveConfigFile");
        System.out.println("  --allPlugin");
        System.out.println("# Commande presente  #");
        System.exit(0);
    }

    /**
     * Affiche un message d'erreur
     *  #CommenterParWilliamBenakli
     */
    private static void displayErreur() {
		System.out.println(prefix + "Erreur aucune entree sasie");
		System.out.println(prefix + "Essayez avec 'help' comme entree pour plus d'info");
    }
    
    /**
     * Affiche la liste des pugins
     */
    private static void listPlugin() {
        System.out.println("(WTS) Commande d'aide");
        System.out.println("# Plugins propos�s  #");
        System.out.println(" > countCommits");
        System.out.println(" > countMergesCommits");
        System.out.println(" > countDescriptionAndMergedCommits");
        System.out.println(" > countDescriptionCommits");
        System.out.println(" > countLineAdd");
        System.out.println(" > countRemoveLine");
        System.out.println(" > countAllModifyLine");
        System.out.println(" > countCommitPerDate=10/10/2000");
        System.out.println(" > countCommitPerTwoDate=10/10/2000=10/11/2000");
        System.out.println("# Commande presente  #");
        System.exit(0);
    }

}
