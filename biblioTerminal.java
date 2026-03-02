import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit; //pour ne pas afficher les millisecondes
import java.util.Random;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.InputMismatchException;


public class Main
{
	static int LARGEUR_FACTURE = 40; //ajustez cette valeur pour avoir une facture plus ou moins large

	static String[] codes = {"A1", "A2", "B1", "B2", "B3", "L1", "L2", "L3"}; //liste des codes d'article disponibles
	static String[] noms  = {"Crayons", "Cahier Canada", "Table pliante", "Fauteuil en cuir", "Bureau d'etudiant", "Laptop ASUS", "Laptop HP", "Laptop Acer"}; //liste des noms d'articles disponibles
	static double[] prix  = {3.99, 1.59, 66.99, 199.99, 118.99, 600.89, 700.89, 250.99}; //liste des prix

	static ArrayList<String> panier = new ArrayList<String>(); //contient les codes des articles achetés
	static String nomEmploye; //nom de l'employé actuellement connectés


	//|==========|
	//| Partie 4 |
	//|==========|

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
		for (String article:panier)
		{
			for (int i = 0; i < codes.length; i++)
			{
				if (article.equals(codes[i]))
				{sousTotal += prix[i];}
			}
		}

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
		System.out.println("");
		afficherSeparateurFacture('=');
		afficherLigneFacture(titre);
		afficherSeparateurFacture('-');
		afficherSeparateurFacture(' ');

		//Affichage des items achetés:
		for (int j = 0; j < codes.length; j++)
		{
			for (int i = 0; i < panier.size(); i++)
			{
				if (codes[j].equals(panier.get(i)))
				{afficherLigneFacture(panier.get(i) + " " + noms[j] + ":  " + prix[j] + "$");}
			}
		}
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


	//|==========|
	//| PARTIE 3 |
	//|==========|

    static void ajouterArticle(Scanner sc)
    //Permet à l'utilisateur d'ajouter un article à son panier.
    {
        while (true)
        {
		    System.out.println("\nAJOUT ARTICLE");

		    System.out.println("Codes dispo: ");
		    for (int i = 0; i < codes.length; i++)
		    {System.out.println(codes[i] + ": " + noms[i] + " - " + prix[i] + "$");}

		    System.out.print("\nCode: ");
		    String code = sc.nextLine().trim();

		    boolean existe = false;
		    for (int i = 0; i < codes.length; i++)
		    {
		        if (codes[i].equalsIgnoreCase(code))
		        {
		            existe = true;
		            break;
		        }
		    }

		    if (existe)
		    {
		        panier.add(code.toUpperCase());
		        System.out.println("Ajouté au panier.");
		        return;
		    }
		    else
		    {System.out.println("Code invalide.");}
		}
    }


    static void supprimerArticle(Scanner sc)
    //Permet à l'utilisateur de supprimer un article de son panier.
    {
        System.out.println("\nSUPPRIMER ARTICLE");

        afficherPanier();
        System.out.print("Code de l'article à supprimer: ");
        String code = sc.nextLine().trim();
        boolean retire = false;

        for (int i = 0; i < panier.size(); i++)
        {
            if (panier.get(i).equalsIgnoreCase(code))
            {
                panier.remove(i);
                retire = true;
                break;
            }
        }

        if (retire)
        {System.out.println("Article supprimé.");}
        else
        {System.out.println("Cet article n'est pas dans le panier.");}
    }


    static void afficherPanier()
    //Affiche le contenu du panier.
    {
        System.out.println("\nAFFICHER PANIER");
        if (panier.isEmpty())
        {
            System.out.println("Panier vide.");
            return;
        }

        for (int p = 0; p < panier.size(); p++)
        {
            String codePanier = panier.get(p);
            for (int i = 0; i < codes.length; i++)
            {
                if (codes[i].equalsIgnoreCase(codePanier))
                {
                    System.out.println(codes[i] + ": " + noms[i] + " - " + prix[i] + "$");
                    break;
                }
            }
        }
    }


	//|==========|
	//| PARTIE 2 |
	//|==========|
	
	static void menu(Scanner sc)
	//Demande à l'utilisateur quelle action il veut accomplir.
	//Quitte lorsque l'utilisateur choisit de payer.
	{
		while (true)
		{
			System.out.println("\n--- Menu principal ---");
			System.out.println("1. Ajouter un article");
			System.out.println("2. Supprimer un article");
			System.out.println("3. Afficher le panier");
			System.out.println("4. Payer");
			System.out.print("Votre choix: ");

			if (!sc.hasNextInt())
			{
				System.out.println("Veuillez entrer un nombre valide.");
				sc.next();
				continue;
			}
			int choix = sc.nextInt();
			sc.nextLine();

			if (choix >= 1 && choix <= 4)
			{
				switch (choix)
				{
				    case 1:
				        ajouterArticle(sc);
				        break;

				    case 2:
				        supprimerArticle(sc);
				        break;

				    case 3:
				        afficherPanier();
				        break;

				    case 4:
				        return;
				}

			}
			else
			{System.out.println("veuillez entrer un nombre entre 1 et 4.");}
		}
	}


	//|==========|
	//| PARTIE 1 |
	//|==========|

	static void sIdentifier (Scanner sc)
	//Permet à un usager de s'identifier auprès du programme.
	//Lorsqu'un code d'usager correct est identifié, nomEmploye est mis à jour en conséquence.
	{
		int[] codes = {001, 002, 003, 004, 005};
		String[] noms = {"Samir", "Kamel", "Nadia", "Jean-Gabriel", "Eve"};
		boolean trouve = false;

		while (!trouve)
		{
			try
			{
				System.out.print("Veuillez vous identifier: ");
				int codeEntre = sc.nextInt();
				for (int i = 0; i < codes.length; i++)
				{
					if (codeEntre == codes[i])
					{
						System.out.println("Bonjour, " + noms[i]);
						trouve = true;
						nomEmploye = noms[i];
						break;
					}
				}
				if (!trouve)
				{System.out.println("ERREUR: Numéro d'employé invalide");}
			}
			catch (InputMismatchException e)
			{
				System.out.println("Entrée invalide! (Entrez un nombre entier)");
				sc.next(); //vider l'entrée invalide
			}
		}
	}


	public static void main (String[] args)
	//Fonction appelée pour débuter le programme.
	{
		Scanner sc = new Scanner(System.in);

		sIdentifier(sc); //partie 1
		menu(sc); //parties 2 et 3
		genererFacture(); //partie 4

		sc.close();
	}
}
