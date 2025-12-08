package com.emsi.subtracker.services;

import com.emsi.subtracker.models.Abonnement;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Service gérant la persistance des abonnements dans un fichier CSV.
 */
public class CsvService {
    private static final String FILE_NAME = "data_abonnements.csv";

    /**
     * Charge tous les abonnements depuis le fichier CSV.
     * Si le fichier n'existe pas, retourne une liste vide.
     */
    public List<Abonnement> chargerTout() {
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            System.out.println("Fichier de données introuvable (" + FILE_NAME + "). Démarrage avec une liste vide.");
            return new ArrayList<>();
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            return br.lines()
                    .map(String::trim)
                    .filter(line -> !line.isEmpty())
                    .map(Abonnement::fromCsvString)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture du fichier : " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Sauvegarde la liste complète des abonnements dans le fichier CSV.
     * Écrase le contenu précédent.
     */
    public void sauvegarderTout(List<Abonnement> abonnements) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Abonnement sub : abonnements) {
                String line = Abonnement.toCsvString(sub);
                bw.write(line);
                bw.newLine();
            }
            System.out.println("Sauvegarde effectuée avec succès (" + abonnements.size() + " items).");
        } catch (IOException e) {
            System.err.println("Erreur lors de l'écriture du fichier : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
