import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit; //pour ne pas afficher les millisecondes
import java.util.Random;
import java.util.ArrayList;


public class Main
{
	//---- Ces variables seront fournies par les autres parties ----
	static String nomEmploye = "Marie";
	static ArrayList<String> codes = new ArrayList<String>();
	static ArrayList<String> achats = new ArrayList<String>();
	static ArrayList<Double> prix = new ArrayList<Double>();
	//---- ----
	
	static int LARGEUR_FACTURE = 35; //ajustez cette valeur pour avoir une facture plus ou moins large
	
	
	static double tps (double montant)
	//Renvoie la TPS à payer selon le montant fourni.
	{return Math.round(montant * 5) / 100.0;}
	
	
	static double tvq (double montant)
	//Renvoie la TVQ à payer selon le montant fourni.
	{return Math.round(montant * 9.975) / 100.0;}
	
	
	static double calculerRabais (double montant)
	//Renvoie le montant du rabais mystère ou 0 s'il n'est pas appliqué.
	{
		if (new Random().nextBoolean())
		{return Math.round(montant * 25) / 100.0;}
		return 0;
	}
	
	
	static String compenserADroite(String texte, int longueur)
	//Ajoute des espaces à la fin du texte de manière à ce que la String renvoyée ait la bonne longueur.
	{
		String padding = "";
		for (int i = texte.length(); i < longueur; i++)
		{padding += " ";}
		return texte + padding;
	}
	
	
	static void afficherLigneFacture (String texte)
	//Affiche le texte passé en paramètre de manière à ce qu'il soit formatté en facture.
	{System.out.println("| " + compenserADroite(texte, LARGEUR_FACTURE - 4) + " |");}
	
	
	static void afficherSeparateurFacture (char caractere)
	//Affiche un séparateur dans la facture.
	//Ce séparateur est composé d'une répétition du caractère passé en paramètre.
	//Exemple: |===================|
	{
		System.out.print('|');
		for (int i = 0; i < LARGEUR_FACTURE - 2; i++)
		{System.out.print(caractere);}
		System.out.println('|');
	}
	
	
	static void genererFacture ()
	//Calcule le total à payer et affiche la facture à l'écran.
	{
		//Calcul du sous-total initial:
		double sousTotal = 0;
		for (double prixIndividuel:prix)
		{sousTotal += prixIndividuel;}
		
		//Calcul du rabais et ajustement du sous-total:
		double rabaisMystere = calculerRabais(sousTotal);
		sousTotal = Math.round((sousTotal - rabaisMystere) * 100) / 100.0;
		
		//Calcul du total:
		double total = Math.round((sousTotal + tps(sousTotal) + tvq(sousTotal)) * 100) / 100.0;
		
		//Centrage du mot "FACTURE":
		String titre = "";
		for (int i = 0; i < (LARGEUR_FACTURE - 11) / 2; i++) //le 11 vient de "FACTURE".length + 4 (les bordures)
		{titre += ' ';}
		titre += "FACTURE";
		
		//Affichage de l'entête de la facture:
		afficherSeparateurFacture('=');
		afficherLigneFacture(titre);
		afficherSeparateurFacture('-');
		afficherSeparateurFacture(' ');
		
		//Affichage des items achetés:
		for (int i = 0; i < achats.size(); i++)
		{afficherLigneFacture(codes.get(i) + " " + achats.get(i) + ":  " + prix.get(i) + "$");}
		if (rabaisMystere > 0)
		{afficherLigneFacture("Rabais mystère: -" + String.valueOf(rabaisMystere) + "$");}
		afficherSeparateurFacture(' ');
		
		//Affichage du sous-total, des taxes et du total:
		afficherLigneFacture("Sous-Total: " + String.valueOf(sousTotal) + "$");
		afficherLigneFacture("  TPS: " + String.valueOf(tps(sousTotal)) + "$");
		afficherLigneFacture("  TVQ: " + String.valueOf(tvq(sousTotal)) + "$");
		afficherLigneFacture("Total: " + String.valueOf(total) + "$");
		afficherSeparateurFacture(' ');
		afficherSeparateurFacture('-');
		
		//Affichage du "pied de page" de la facture:
		afficherLigneFacture("Vous avez été servi par " + nomEmploye + ".");
		afficherLigneFacture("Date: " + LocalDate.now().toString());
		afficherLigneFacture("Heure: " + LocalTime.now().truncatedTo(ChronoUnit.SECONDS).toString());
		afficherSeparateurFacture('=');
		System.out.println("");
	}
	
	
	//---- Cette fonction et son contenu va disparaître lorsqu'on mergera les parties ----
	public static void main (String[] args)
	{
		double sousTotal = 0;
		
		codes.add("L1");
		achats.add("Laptop ASUS");
		prix.add(600.89);
		
		codes.add("L2");
		achats.add("Laptop Dell");
		prix.add(499.75);
		
		genererFacture();
	}
	//---- ----
}