package com.emsi.subtracker;

import com.emsi.subtracker.models.Abonnement;
import com.emsi.subtracker.services.SubscriptionService;

import java.time.LocalDate;
import java.util.List;

/**
 * Classe de test console mise à jour pour tester la logique métier.
 */
public class TestConsole {

    public static void main(String[] args) {
        System.out.println("=== DEBUT DU TEST LOGIQUE METIER ===");

        SubscriptionService service = new SubscriptionService();

        // 1. Nettoyage (optionnel, pour partir sur une base saine si on voulait, mais
        // ici on charge l'existant)
        // Pour le test, on va juste lister ce qui existe déjà
        List<Abonnement> initialList = service.getAll();
        System.out.println("Données initiales : " + initialList.size() + " abonnements.");

        // 2. Calcul du total actuel
        double totalAvant = service.calculerTotalMensuel();
        System.out.println("Total mensuel actuel : " + totalAvant + " DH");

        // 3. Ajout d'un abonnement ANNUEL
        // Exemple : "Adobe CC" à 1200 DH / an => 100 DH / mois
        System.out.println("\nAjout d'un abonnement Annuel (1200 DH/an)...");
        // On génère un ID unique simpliste pour le test
        int newId = (int) (System.currentTimeMillis() % 10000);
        Abonnement nouveau = new Abonnement(newId, "Adobe CC", 1200.00, LocalDate.now(), "Annuel", "Travail");
        service.add(nouveau);

        // 4. Vérification du nouveau total
        double totalApres = service.calculerTotalMensuel();
        System.out.println("Nouveau total mensuel : " + totalApres + " DH");

        // Verification mathématique
        double difference = totalApres - totalAvant;
        // On s'attend à +100.0 (avec une petite marge d'erreur flottante)
        if (Math.abs(difference - 100.0) < 0.01) {
            System.out.println("SUCCÈS : Le total a bien augmenté de 100 DH (1200/12).");
        } else {
            System.err.println("ÉCHEC : Augmentation inattendue de " + difference);
        }

        // 5. Test suppression
        System.out.println("\nSuppression de l'abonnement ajouté...");
        service.remove(newId);

        double totalFinal = service.calculerTotalMensuel();
        System.out.println("Total après suppression : " + totalFinal + " DH");

        if (Math.abs(totalFinal - totalAvant) < 0.01) {
            System.out.println("SUCCÈS : Retour au total initial.");
        } else {
            System.err.println("ÉCHEC : Le total n'est pas revenu à la normale.");
        }

        System.out.println("\n=== FIN DU TEST ===");
    }
}
