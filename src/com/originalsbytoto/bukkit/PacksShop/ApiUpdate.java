package com.originalsbytoto.bukkit.packsshop;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class ApiUpdate {
	   int projectId = 96517;
	   String apiKey;
	 
	   // Cl� des valeurs JSON:
	   private static final String API_NAME_VALUE = "name", 
	      API_LINK_VALUE = "downloadUrl", 
	      API_RELEASE_TYPE_VALUE = "releaseType", 
	      API_FILE_NAME_VALUE = "fileName",
	      API_GAME_VERSION_VALUE = "gameVersion";
	 
	   //Adresse pour obtenir la liste des versions du projet:
	   private static final String API_QUERY_URL = "https://api.curseforge.com/servermods/files?projectIds=";
	 
	   //Informations récupérées à propos de la dernière version:
	   private String latestName, latestLink, latestType, latestFileName, latestGameVersion;
	 
	   /**
	   * Cr�e une nouvelle instance de ApiUpdate pour utiliser de fa�on anonyme (sans cl�) l'api ServerMods.
	   * @param projectId l'id du plugin
	   */
	   
	   public ApiUpdate(int pId) {
		   this(pId, null);
	   }
	   
	   /**
	   * Crée une nouvelle instance de ApiUpdate pour utiliser avec une clé d'api ServerMods.
	   * @param projectId l'id du plugin
	   * @param apiKey la clé de l'api
	   */
	   public ApiUpdate(int projectId, String apiKey) {
	      this.projectId = projectId;
	      this.apiKey = apiKey;
	   }
	 
	   

	/**
	   * Se connecte à internet et envoie une requête d'api pour obtenir les informations sur le projet.
	   */
	   public void query() {
	      URL url = null;
	 
	      try {
	         url = new URL(API_QUERY_URL + projectId);//On ajoute l'id dans l'url
	      } catch (MalformedURLException e) {
		// Erreur dans la création de l'url
		e.printStackTrace();
		return;
	      }
	 
	      try {
	         // Ouvre une connexion:
		 URLConnection conn = url.openConnection();
	 
		 if (apiKey != null) {
		    // Si on a l'api key, on l'ajoute dans le header X-API-Key de la requête:
		    conn.addRequestProperty("X-API-Key", apiKey);
		 }
	 
		 // On ajoute le header User-Agent qui sert � identifier votre plugin. Remplacer le 2è paramêtre par le nom de votre plugin, 
	         // sa version et son auteur
		 conn.addRequestProperty("User-Agent", "ServerModsAPI-Example v1.0 (wiki bukkit fr)");
	 
		 // On lit la réponse:
		 final BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		 String response = reader.readLine();
	 
		 // On extrait les informations JSON de la réponse:
		 JSONArray array = (JSONArray) JSONValue.parse(response);
	 
		 if (array.size() > 0) {
	            //On ne prend que la dernière version:
		    JSONObject latest = (JSONObject) array.get(array.size() - 1);
	 
		    latestName = (String) latest.get(API_NAME_VALUE);
	            latestLink = (String) latest.get(API_LINK_VALUE);
	            latestType = (String) latest.get(API_RELEASE_TYPE_VALUE);
	            latestFileName = (String) latest.get(API_FILE_NAME_VALUE);
	            latestGameVersion = (String) latest.get(API_GAME_VERSION_VALUE);
	 
		  }
	       } catch (IOException e) {
		  e.printStackTrace();
		  return;
	       }
	   }
	 
	   public String getLatestName() {
	      return latestName;
	   }
	 
	   public String getLatestLink() {
	      return latestLink;
	   }
	 
	   public String getLatestType() {
	      return latestType;
	   }
	 
	   public String getLatestFileName() {
	      return latestFileName;
	   }  
	 
	   public String getLatestGameVersion() {
	      return latestGameVersion;
	   }  
	}
