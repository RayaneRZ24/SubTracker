package com.emsi.subtracker.services;

import com.emsi.subtracker.models.Abonnement;

import java.util.List;

/**
 * Service Métier - Intermédiaire entre la vue et la couche de données.
 * Contient la logique de calcul et de validation.
 */
public class SubscriptionService {

    private final CsvService csvService;

    public SubscriptionService() {
        this.csvService = new CsvService();
    }

    /**
     * Récupère tous les abonnements.
     */
    public List<Abonnement> getAll() {
        return csvService.chargerTout();
    }

    /**
     * Ajoute un abonnement et sauvegarde la liste mise à jour.
     */
    public void add(Abonnement abonnement) {
        List<Abonnement> list = getAll();
        list.add(abonnement);
        csvService.sauvegarderTout(list);
    }

    /**
     * Supprime un abonnement par son ID et sauvegarde.
     */
    public void remove(int id) {
        List<Abonnement> list = getAll();

        // Suppression via l'ID
        boolean removed = list.removeIf(a -> a.getId() == id);

        if (removed) {
            csvService.sauvegarderTout(list);
            System.out.println("Abonnement ID " + id + " supprimé.");
        } else {
            System.err.println("Aucun abonnement trouvé avec l'ID " + id);
        }
    }

    /**
     * Calcule le coût mensuel total de tous les abonnements.
     * Pour un abonnement annuel, le prix est divisé par 12.
     * Arrondi à 2 chiffres après la virgule.
     */
    public double calculerTotalMensuel() {
        return getAll().stream()
                .mapToDouble(a -> {
                    if ("Annuel".equalsIgnoreCase(a.getFrequence())) {
                        return a.getPrix() / 12.0;
                    }
                    return a.getPrix();
                })
                .sum();
    }
}
